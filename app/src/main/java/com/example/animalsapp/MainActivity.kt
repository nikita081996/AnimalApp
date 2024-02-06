package com.example.animalsapp

import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.example.animalsapp.domain.model.CatModel
import com.example.animalsapp.ui.theme.AnimalsAppTheme
import kotlinx.coroutines.flow.StateFlow
import com.example.animalsapp.data.Result

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)

        setContent {
            AnimalsAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Render(Modifier, viewModel.animalState)
                }
            }
        }
    }
}

@Composable
fun Render(modifier: Modifier = Modifier, animalsList: StateFlow<Result<List<CatModel>>>) {
    val uiState: Result<List<CatModel>>? by animalsList.collectAsState()
    when (uiState) {
        is Result.Hidden -> {
        }

        is Result.Loading -> {
            Loader()
        }

        is Result.Error -> {
            Toast.makeText(
                BaseApplication.getContext(),
                (uiState as Result.Error).meta,
                Toast.LENGTH_LONG
            ).show()
        }

        is Result.Success -> {
            (uiState as Result.Success<List<CatModel>>).data?.let {
                Column(Modifier.fillMaxSize()) {
                    Animals(
                        modifier,
                        it
                    )
                }
            }
        }

        else -> {
        }
    }
}


@Composable
fun Animals(modifier: Modifier = Modifier, animalsList: List<CatModel>) {
    val listState = rememberLazyListState()
    LazyColumn(
        state = listState
    ) {
        items(
            count = animalsList.size,
            key = { animalsList[it].id.toString() },
            itemContent = { index ->
                AnimalItem(animalModel = animalsList[index], modifier = modifier)
            })
    }
}

@Composable
fun AnimalItem(
    modifier: Modifier = Modifier,
    animalModel: CatModel
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(dimensionResource(id = R.dimen.margin_4dp)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LoadImage(image = animalModel.url, width = animalModel.width, height = animalModel.height)
    }
}

@Composable
fun LoadImage(image: String?, width: Int?, height: Int?) {
    val imageLoader = ImageLoader.Builder(LocalContext.current)
        .components {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()

    Image(
        modifier = Modifier
            .clip(RoundedCornerShape(dimensionResource(id = R.dimen.margin_4dp))),
        contentScale = ContentScale.FillBounds,
        painter = rememberAsyncImagePainter(
            ImageRequest.Builder(LocalContext.current)
                .placeholder(R.drawable.baseline_image_search_24)
                .data(data = image)
                .apply(block = fun ImageRequest.Builder.() {
                    size(width = width ?: 400, height = height ?: 200)
                }).build(),
            imageLoader = imageLoader
        ),
        contentDescription = null,
    )
}

@Composable
fun Loader() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
    }
}

@Preview(showBackground = true)
@Composable
fun AnimalItemPreview() {
    AnimalsAppTheme {
        AnimalItem(
            animalModel = CatModel(
                id = "123",
                url = "https://cdn2.thecatapi.com/images/49f.gif",
                width = 400,
                height = 200
            )
        )
    }
}