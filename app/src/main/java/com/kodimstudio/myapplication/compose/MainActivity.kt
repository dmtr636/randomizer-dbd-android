package com.kodimstudio.myapplication.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentRecomposeScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kodimstudio.myapplication.R
import com.kodimstudio.myapplication.compose.ui.theme.RandomizerDBDTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RandomizerDBDTheme {
                Layout()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Layout() {
    RandomizerDBDTheme {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.main_bg),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                AppBar()
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Links()
                }
            }
        }
    }
}

@Composable
fun Links() {
    IconButton(
        modifier = Modifier.fillMaxWidth(),
        onClick = {},
    ) {
        Image(
            painter = painterResource(id = R.drawable.link_randomizer),
            modifier = Modifier.fillMaxWidth(),
            contentDescription = null,
            contentScale = ContentScale.FillBounds
        )
        Text(
            text = stringResource(id = R.string.randomizer),
            style = MaterialTheme.typography.h1,
            modifier = Modifier.padding(start = 10.dp)
        )
    }
    IconButton(
        modifier = Modifier.fillMaxWidth(),
        onClick = {}
    ) {
        Image(
            painter = painterResource(id = R.drawable.link_randomizer),
            modifier = Modifier.fillMaxWidth(),
            contentDescription = null
        )
        Text(
            text = stringResource(id = R.string.randomizer),
            style = MaterialTheme.typography.h1,
            modifier = Modifier.padding(start = 10.dp)
        )
    }
    IconButton(
        modifier = Modifier.fillMaxWidth(),
        onClick = {}
    ) {
        Image(
            painter = painterResource(id = R.drawable.link_randomizer),
            contentDescription = null
        )
        Text(
            text = stringResource(id = R.string.randomizer),
            style = MaterialTheme.typography.h1,
            modifier = Modifier.padding(start = 10.dp)
        )
    }
    IconButton(
        modifier = Modifier.fillMaxWidth(),
        onClick = {}
    ) {
        Image(
            painter = painterResource(id = R.drawable.link_randomizer),
            contentDescription = null
        )
        Text(
            text = stringResource(id = R.string.randomizer),
            style = MaterialTheme.typography.h1,
            modifier = Modifier.padding(start = 10.dp)
        )
    }
    IconButton(
        modifier = Modifier.fillMaxWidth(),
        onClick = {}
    ) {
        Image(
            painter = painterResource(id = R.drawable.link_randomizer),
            contentDescription = null
        )
        Text(
            text = stringResource(id = R.string.randomizer),
            style = MaterialTheme.typography.h1,
            modifier = Modifier.padding(start = 10.dp)
        )
    }
}

@Composable
fun AppBar() {
    Box(
        modifier = Modifier
            .height(69.dp)
            .fillMaxWidth()
    ) {
        Image(
            painter = painterResource(R.drawable.top_bar_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 25.dp, top = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = null,
                Modifier
                    .width(30.dp)
                    .height(30.dp)
            )
            Text(
                text = stringResource(id = R.string.menu),
                style = MaterialTheme.typography.h1,
                modifier = Modifier.padding(start = 10.dp)
            )
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}