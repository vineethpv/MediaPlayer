package com.vpvn.mediaplayer.ui.musiclisting

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.vpvn.mediaplayer.R

typealias OnAudioItemClick = (AudioFile) -> Unit

@Composable
fun AudioFilesScreen(
    onBackClick: () -> Unit,
    onItemClick: OnAudioItemClick,
    viewModel: AudioFilesViewModel = hiltViewModel()
) {
    val directoryName by viewModel.directoryNameState.collectAsStateWithLifecycle()
    val audioFilesState by viewModel.audioFilesUiState.collectAsStateWithLifecycle()

    AudioFilesScreen(
        onBackClick = onBackClick,
        onItemClick = onItemClick,
        directoryName = directoryName,
        audioFilesState = audioFilesState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AudioFilesScreen(
    onBackClick: () -> Unit,
    onItemClick: OnAudioItemClick,
    directoryName: String,
    audioFilesState: AudioFilesUiState
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(directoryName)
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "description"
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when (audioFilesState) {
                AudioFilesUiState.Loading -> {
                    Text(
                        text = stringResource(id = R.string.loading),
                        style = TextStyle(color = Color.Red)
                    )
                }

                is AudioFilesUiState.Success -> {
                    AudioFileList(
                        audioList = audioFilesState.videoFiles,
                        onItemClick = onItemClick,
                        innerPadding = innerPadding
                    )
                }
            }
        }
    }
}

@Composable
fun AudioFileList(
    audioList: List<AudioFile>,
    onItemClick: OnAudioItemClick,
    innerPadding: PaddingValues
) {
    val context = LocalContext.current
    val lifeCycleOwner = LocalLifecycleOwner.current

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            playWhenReady = true
            repeatMode = Player.REPEAT_MODE_ONE
        }
    }
    LaunchedEffect(exoPlayer) {
        lifeCycleOwner.lifecycle.addObserver(getLifeCycleObserver(exoPlayer))
    }

    var showAudioPlayer by remember {
        mutableStateOf(false)
    }
//    var audioUrl by remember {
//        mutableStateOf("")
//    }
    var audioFileItem by remember { mutableStateOf(audioList[0]) }

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentPadding = PaddingValues(16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(vertical = 25.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Audios",
                        color = Color.Black,
                        style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    )
                }

            }
            items(audioList) { audioFile ->
                AudioCardItem(
                    audioFile = audioFile,
                    onItemClick = { item ->
                        audioFileItem = item
                        showAudioPlayer = true
                        //onItemClick(url)
                    })
            }
        }
        if (showAudioPlayer) {
            println("vineeth - audioUrl ::-> ${audioFileItem.url}")
            exoPlayer.setMediaItem(MediaItem.fromUri(audioFileItem.url))
            exoPlayer.prepare()
            exoPlayer.play()
            AudioPlayer(exoPlayer = exoPlayer, audioFile = audioFileItem)
        }
    }
}

@Composable
fun AudioCardItem(
    audioFile: AudioFile,
    onItemClick: OnAudioItemClick
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(align = Alignment.CenterVertically)
            .padding(8.dp)
            .clickable {
                onItemClick(audioFile)
            },
        shape = RoundedCornerShape(5.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Thumbnail(
                duration = audioFile.duration
            )
            TextColumn(
                name = audioFile.displayName,
                date = audioFile.date
            )
        }

    }
}

@Preview
@Composable
fun TextColumn(
    name: String = "VID-20190727-WA0019",
    date: String = "11/08/2021"
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = name,
            color = Color.Black,
            style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal)
        )
        Text(
            modifier = Modifier
                .padding(top = 8.dp)
                .background(color = Color.LightGray),
            text = date,
            color = Color.Black,
            style = TextStyle(fontSize = 10.sp, fontWeight = FontWeight.Normal)
        )
    }
}

@Preview
@Composable
fun Thumbnail(
    duration: String = "09:06"
) {
    Box(contentAlignment = Alignment.BottomEnd) {
        Image(
            painter = painterResource(id = R.drawable.baseline_music_video_24),
            contentDescription = "music",
            alpha = 0.4F,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.tertiary),
            modifier = Modifier
                .padding(start = 8.dp)
                .size(56.dp)
        )
        Text(
            text = duration,
            modifier = Modifier
                .padding(2.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(color = Color.Black)
                .padding(start = 2.dp, end = 2.dp, top = 1.dp, bottom = 1.dp),
            style = TextStyle(
                fontSize = 8.sp,
                color = Color.White
            )
        )
    }
}