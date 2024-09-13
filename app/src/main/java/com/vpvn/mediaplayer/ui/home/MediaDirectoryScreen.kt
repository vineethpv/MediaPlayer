package com.vpvn.mediaplayer.ui.home

import android.util.Log
import androidx.compose.foundation.Image
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vpvn.mediaplayer.R

data class MediaDirectory(
    val folderName: String,
    var videoCount: Int,
    val path: String
)

@Composable
fun HomeScreen(onItemClick: (String, String) -> Unit) {
    val viewModel: HomeViewModel = hiltViewModel()
    val directoryList = viewModel.mediaDirectoriesLiveData.observeAsState().value
    directoryList?.let { MediaDirectoriesList(directories = it, onItemClick) }
}

@Composable
fun MediaDirectoriesList(
    directories: List<MediaDirectory>,
    onItemClick: (String, String) -> Unit
) {
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
        items(directories) { directory ->
            DirectoryCardItem(
                name = directory.folderName,
                path = directory.path,
                directory.videoCount,
                onItemClick = { name, path ->
                    Log.d("vineeth", "$name clicked")
                    onItemClick(name, path)
                })
        }
    }
}


@Composable
fun DirectoryCardItem(
    name: String,
    path: String,
    count: Int,
    onItemClick: (String, String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .clickable { onItemClick(name, path) }
            .wrapContentHeight(),
        shape = RoundedCornerShape(5.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        RowContent(name, "$count videos")
    }
}

@Composable
fun RowContent(title: String, subTitle: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.icon_folder_192),
            contentDescription = null,
            alpha = 0.4F,
            modifier = Modifier
                .padding(start = 8.dp, top = 6.dp, bottom = 6.dp)
                .size(56.dp)
        )
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                text = title,
                color = Color.Black,
                style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Normal)
            )
            Text(
                text = subTitle,
                color = Color.Gray,
                style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal)
            )
        }
    }
}
