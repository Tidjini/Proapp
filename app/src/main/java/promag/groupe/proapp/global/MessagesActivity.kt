package promag.groupe.proapp.global

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import promag.groupe.proapp.global.message.MessageFooter
import promag.groupe.proapp.global.message.MessageHeader
import promag.groupe.proapp.global.ui.theme.ProappTheme
import promag.groupe.proapp.global.ui.theme.Success80
import promag.groupe.proapp.models.Message
import promag.groupe.proapp.models.MessageProvider
import promag.groupe.proapp.models.User

class MessagesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProappTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background
                ) {
                    MyApp()
                }
            }
        }
    }
}

@Composable
fun MyApp() {
    Scaffold(


        content = {

            BarkHomeContent()
        })
}


@Composable
fun BarkHomeContent() {

    //todo get discussion
    //todo get user from discussion
    //todo get messages list
    //todo send message









    val messages = remember { MessageProvider.messges }
    Column(Modifier.fillMaxSize()) {
        MessageHeader(user = User(username = "tidjini", name = "Messaoudi Tidjini"))


        Divider()


        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
        ) {
            items(items = messages, itemContent = {
                MessageListItem(message = it)
            })
        }
        Divider()
        MessageFooter()

    }

}


@Composable
fun MessageListItem(message: Message) {
    val bottomEnd = if (message.sender == 1) 0.dp else 32.dp
    val bottomStart = if (message.sender != 1) 0.dp else 32.dp
    val paddingStart = if (message.sender != 1) 8.dp else 32.dp
    val paddingEnd = if (message.sender == 1) 8.dp else 32.dp
    val cardAlignment = if (message.sender == 1) Alignment.End else Alignment.Start

    // Declaring 4 Colors
//    val colorBlack = Color.Black
//    val colorMagenta = Color.Magenta

    // Creating a Radial Gradient Color
    val gradientRadial =
        Brush.linearGradient(
            listOf(
                MaterialTheme.colors.background,
                Success80
            )
        )
    val senderBackground =
        if (message.sender == 1) Modifier.background(gradientRadial) else Modifier.background(
            Color.Transparent
        )

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Card(
            modifier = Modifier
                .padding(
                    top = 8.dp, bottom = 8.dp, start = paddingStart, end = paddingEnd
                )
                .wrapContentWidth()
                .align(cardAlignment),
            elevation = 1.dp,
            shape = RoundedCornerShape(
                topStart = 32.dp,
                topEnd = 32.dp,
                bottomStart = bottomStart,
                bottomEnd = bottomEnd
            ),
            backgroundColor = MaterialTheme.colors.background,

            ) {


            Row(
                modifier = senderBackground
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .wrapContentWidth()
                        .align(Alignment.CenterVertically)
                ) {
                    Text(text = message.message, style = typography.caption)
                }
            }
        }
    }
}