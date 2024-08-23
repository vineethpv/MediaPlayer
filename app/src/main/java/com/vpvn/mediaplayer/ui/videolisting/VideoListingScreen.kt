package com.vpvn.mediaplayer.ui.videolisting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoFilesScreen(onBackClick: () -> Unit) {
    val videoFilesViewModel: VideoFilesViewModel = hiltViewModel()
    val directoryName = videoFilesViewModel.directoryNameLiveData.observeAsState().value ?: ""
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
        Text(
            "Click the back button",
            modifier = Modifier.padding(innerPadding),
        )
    }
}

@Composable
fun VideoFileList() {
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
                    text = "Media",
                    color = Color.Black,
                    style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
                )
            }

        }
//        items(directories) { directory ->
//            DirectoryCard(name = directory.folderName, onItemClick = { name ->
//                Log.d("vineeth", "$name clicked")
//            })
//        }
    }
}