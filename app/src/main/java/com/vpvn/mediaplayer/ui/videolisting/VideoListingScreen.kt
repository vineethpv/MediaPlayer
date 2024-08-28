package com.vpvn.mediaplayer.ui.videolisting

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.VideoFrameDecoder
import com.vpvn.mediaplayer.R


@Composable
fun VideoFilesScreen(
    onBackClick: () -> Unit,
    viewModel: VideoFilesViewModel = hiltViewModel()
) {
    val directoryName by viewModel.directoryNameState.collectAsStateWithLifecycle()
    val videoFilesState by viewModel.videoFilesUiState.collectAsStateWithLifecycle()

    VideoFilesScreen(
        onBackClick = onBackClick,
        directoryName = directoryName,
        videoFilesState = videoFilesState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoFilesScreen(
    onBackClick: () -> Unit,
    directoryName: String,
    videoFilesState: VideoFilesUiState
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
            when (videoFilesState) {
                VideoFilesUiState.Loading -> {
                    Text(
                        text = stringResource(id = R.string.loading),
                    )
                }

                is VideoFilesUiState.Success -> {
                    VideoFileList(
                        videoList = videoFilesState.videoFiles,
                        innerPadding = innerPadding
                    )
                }
            }
        }
    }
}

@Composable
fun VideoFileList(videoList: List<VideoFile>, innerPadding: PaddingValues) {
    val imageLoader = ImageLoader.Builder(LocalContext.current)
        .components { add(VideoFrameDecoder.Factory()) }
        .build()
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
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
                    text = "Videos",
                    color = Color.Black,
                    style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
                )
            }

        }
        items(videoList) { videoFile ->
            VideoCardItem(
                videoFile = videoFile,
                imageLoader = imageLoader,
                onItemClick = { name ->
                    //Log.d("vineeth", "$name clicked")
                })
        }
    }
}

@Composable
fun VideoCardItem(
    videoFile: VideoFile,
    imageLoader: ImageLoader,
    onItemClick: (String) -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(align = Alignment.CenterVertically)
            .padding(8.dp)
            .clickable { onItemClick(videoFile.displayName) },
        shape = RoundedCornerShape(2.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                model = videoFile.url,
                imageLoader = imageLoader,
                contentScale = ContentScale.Crop,
                contentDescription = null,
                alignment = Alignment.Center,
                modifier = Modifier
                    .size(80.dp, 60.dp)
                    .clip(RoundedCornerShape(3.dp))
            )
            Column(modifier = Modifier.padding(5.dp)) {
                Text(
                    text = videoFile.displayName,
                    color = Color.Black,
                    style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Normal)
                )
                Text(
                    text = videoFile.duration,
                    color = Color.Gray,
                    style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal)
                )
            }
        }

    }
}