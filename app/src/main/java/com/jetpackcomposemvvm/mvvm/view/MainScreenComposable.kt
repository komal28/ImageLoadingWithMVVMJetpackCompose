package com.jetpackcomposemvvm.mvvm.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jetpackcomposemvvm.mvvm.model.data.ImageData
import com.jetpackcomposemvvm.mvvm.model.data.Thumbnail
import com.jetpackcomposemvvm.R
import com.jetpackcomposemvvm.mvvm.viewmodel.ImageDataViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.URL

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ImageMainScreen(viewModel: ImageDataViewModel) {
    LaunchedEffect(Unit) {
        viewModel.fetchMediaCoverages()
    }
    val creditCards by viewModel.imageData.observeAsState(emptyList())
    Log.e("List creditCards : ", creditCards.toString())
    val thumbnailList = mutableListOf<Thumbnail>()

    Column {
        Header()
        Row {
            if (creditCards.isEmpty()) {
                // Show loading indicator or placeholder
                Text(text = "Loading...")
            } else {

                creditCards.let { list ->
                    list.forEach { mediaCoverage ->
                        FetchData(mediaCoverage, thumbnailList)
                    }
                }
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 128.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    items(thumbnailList) { item ->
                        Card(
                            modifier = Modifier.padding(5.dp),
                            elevation = CardDefaults.cardElevation(5.dp)
                        ) {
                            Column {
                                GetImages(item)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Header() {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .background(Color.LightGray)
    ) {

        Button(
            onClick = {
                exitApp(context)
            }, colors = ButtonDefaults.buttonColors(
                containerColor = Color.LightGray,
            )
        ) {
            Image(
                painter = painterResource(id = R.drawable.baseline_arrow_back_ios_24),
                contentDescription = "Dummy",
                colorFilter = ColorFilter.tint(Color.Black)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.05f)
                .background(Color.LightGray)
        ) {
            Text(
                text = "Acharya Prashant Foundation",
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            )

        }
    }
}

fun exitApp(context: Context) {
    // Finish the current activity to exit the app
    (context as? MainActivity)?.finish()
}


@Composable
private fun FetchData(
    mediaCoverage: ImageData,
    thumbnailList: MutableList<Thumbnail>
) {
    val thumbnail = mediaCoverage.thumbnail
    thumbnailList.add(
        Thumbnail(
            aspectRatio = thumbnail.aspectRatio,
            basePath = thumbnail.basePath,
            domain = thumbnail.domain,
            id = thumbnail.id,
            key = thumbnail.key,
            qualities = thumbnail.qualities,
            version = thumbnail.version
        )
    )
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
@OptIn(DelicateCoroutinesApi::class)
private fun GetImages(item: Thumbnail) {
    var cachedImages by remember { mutableStateOf<Map<String, ImageBitmap?>>(emptyMap()) }

    val imageURL = item.domain + "/" + item.basePath + "/0/" + item.key
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }

    if (imageURL !in cachedImages) {
        Log.e("DisplayImages : ", "Load image from URL")

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val inputStream = URL(imageURL).openStream()
                val downloadedBitmap = BitmapFactory.decodeStream(inputStream)
                bitmap.value = downloadedBitmap
                val imageBitmap = bitmap.value?.asImageBitmap()
                cachedImages = cachedImages + (imageURL to imageBitmap)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        ShowImage(bitmap)
    } else {
        Log.e("DisplayImages : ", "Display image if cached")
        cachedImages[imageURL]?.let { imageBitmap ->

            Image(
                bitmap = imageBitmap,
                contentDescription = null,
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.FillWidth,
            )
        }
    }
}


@Composable
private fun ShowImage(bitmap: MutableState<Bitmap?>) {
    val imageBitmap = bitmap.value?.asImageBitmap()
    if (imageBitmap != null) {
        Image(
            bitmap = imageBitmap,
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.FillWidth,
        )
    } else {
        Image(
            painter = painterResource(id = R.drawable.baseline_broken_image_24), // Placeholder drawable resource
            contentDescription = "Placeholder Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds // Adjust content scale as needed
        )
    }
}

@Composable
fun ConnectivityChecker() {
    Column {
        Header()
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(Color.LightGray)
        ) {
            Text(
                text = "ERR_INTERNET_DISCONNECTED \n It seems you are not connected to Internet. \n Please Check Your Internet Connection.",
                fontSize = 24.sp,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            )
        }

    }
}