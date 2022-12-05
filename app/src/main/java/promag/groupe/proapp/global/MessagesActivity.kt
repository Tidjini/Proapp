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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import promag.groupe.proapp.BaseApplication
import promag.groupe.proapp.DISCUSSION_EXTRA
import promag.groupe.proapp.global.message.MessageFooter
import promag.groupe.proapp.global.message.MessageHeader
import promag.groupe.proapp.global.ui.theme.ProappTheme
import promag.groupe.proapp.global.ui.theme.Success80
import promag.groupe.proapp.models.Discussion
import promag.groupe.proapp.models.Message
import promag.groupe.proapp.models.User


open class BaseCompActivity : ComponentActivity() {
    lateinit var mApplication: BaseApplication
    lateinit var user: User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mApplication = applicationContext as BaseApplication
        user = mApplication.user
    }
}

class MessagesActivity : BaseCompActivity() {

    var discussion: Discussion? = null
    var messages: List<Message> = ArrayList<Message>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        discussion = intent.getSerializableExtra(DISCUSSION_EXTRA) as Discussion?

        val vm = MessagesViewModel(mApplication, discussion!!.id)
        setContent {
            ProappTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background
                ) {
                    MyApp(discussion, vm)
                }
            }
        }
    }


    override fun onResume() {
        super.onResume()


    }


}

class MessagesViewModel(val app: BaseApplication, val discussionId: Int) : ViewModel() {
    private val mMessages = mutableStateListOf<Message>()
    var errorMessage: String by mutableStateOf("")
    val messages: List<Message>
        get() = mMessages

    fun getMessages() {
        viewModelScope.launch {
            try {
                mMessages.clear()
                val result =
                    app.quotesApi.getMessages("token ${app.user.token}", discussionId) ?: return@launch

                mMessages.addAll(result.body()!!.results)
            } catch (e: Exception) {
                errorMessage = e.message.toString()
            }
        }
    }

    fun postMessage(){
        mMessages.addAll(result.body()!!.results)
    }
}

@Composable
fun MyApp(discussion: Discussion?, vm: MessagesViewModel) {
    Scaffold(


        content = {

            BarkHomeContent(discussion, vm)
        })
}


@Composable
fun BarkHomeContent(discussion: Discussion?, vm: MessagesViewModel) {

    //DONE get discussion
    //DONE get user from discussion
    //todo get messages list
    //todo send message

    val user = discussion?.other ?: User(username = "John", name = "John Doe")
    LaunchedEffect(Unit, block = {
        vm.getMessages()
    })



    Column(Modifier.fillMaxSize()) {
        MessageHeader(user = user)


        Divider()


        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
        ) {
            items(items = vm.messages, itemContent = {
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