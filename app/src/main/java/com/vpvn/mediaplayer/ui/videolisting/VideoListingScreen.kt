package com.vpvn.mediaplayer.ui.videolisting

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.vpvn.mediaplayer.R


@Composable
fun VideoFilesScreen(
    onBackClick: () -> Unit,
    onItemClick: (String) -> Unit,
    viewModel: VideoFilesViewModel = hiltViewModel()
) {
    val directoryName by viewModel.directoryNameState.collectAsStateWithLifecycle()
    val videoFilesState by viewModel.videoFilesUiState.collectAsStateWithLifecycle()

    VideoFilesScreen(
        onBackClick = onBackClick,
        onItemClick = onItemClick,
        directoryName = directoryName,
        videoFilesState = videoFilesState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoFilesScreen(
    onBackClick: () -> Unit,
    onItemClick: (String) -> Unit,
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
                        style = TextStyle(color = Color.Red)
                    )
                }

                is VideoFilesUiState.Success -> {
                    VideoFileList(
                        videoList = videoFilesState.videoFiles,
                        onItemClick = onItemClick,
                        innerPadding = innerPadding
                    )
                }
            }
        }
    }
}

@Composable
fun VideoFileList(
    videoList: List<VideoFile>,
    onItemClick: (String) -> Unit,
    innerPadding: PaddingValues) {
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
                onItemClick = { url ->
                    onItemClick(url)
                })
        }
    }
}

@Composable
fun VideoCardItem(
    videoFile: VideoFile,
    onItemClick: (String) -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(align = Alignment.CenterVertically)
            .padding(8.dp)
            .clickable { onItemClick(videoFile.url) },
        shape = RoundedCornerShape(2.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Thumbnail(
                url = videoFile.url,
                duration = videoFile.duration
            )
            TextColumn(
                name = videoFile.displayName,
                date = videoFile.date
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
    url: String = "",
    duration: String = "09:06"
) {
    Box(contentAlignment = Alignment.BottomEnd) {
        AsyncImage(
            model = url,
            contentScale = ContentScale.Crop,
            contentDescription = null,
            alignment = Alignment.Center,
            modifier = Modifier
                .size(95.dp, 70.dp)
                .clip(RoundedCornerShape(topStart = 2.dp, bottomStart = 2.dp))
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