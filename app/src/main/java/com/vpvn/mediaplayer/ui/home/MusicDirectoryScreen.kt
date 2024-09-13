package com.vpvn.mediaplayer.ui.home

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vpvn.mediaplayer.R

@Composable
fun MusicScreen(onItemClick: (String, String) -> Unit) {
    val viewModel: MusicDirectoryViewModel = hiltViewModel()
    val directoryList = viewModel.musicDirectoriesLiveData.observeAsState().value
    directoryList?.let { MusicDirectoriesList(directories = it, onItemClick) }
}

@Composable
fun MusicDirectoriesList(
    directories: List<MusicDirectory>,
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
                    text = stringResource(id = R.string.media),
                    color = Color.Black,
                    style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
                )
            }

        }
        items(directories) { directory ->
            MusicDirectoryCardItem(
                name = directory.folderName,
                path = directory.path,
                directory.count,
                onItemClick = { name, path ->
                    Log.d("vineeth", "$name clicked")
                    onItemClick(name, path)
                })
        }
    }
}


@Composable
fun MusicDirectoryCardItem(
    name: String,
    path: String,
    count: Int,
    onItemClick: (String, String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .clickable { onItemClick(name, path) }
            .wrapContentHeight(),
        shape = RoundedCornerShape(5.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        RowContent(title = name, subTitle = stringResource(id = R.string.audios, count))
    }
}
