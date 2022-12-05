package promag.groupe.proapp.global

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import com.google.gson.Gson
import io.socket.emitter.Emitter
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@Suppress("DEPRECATION")
open class BaseCompActivity : ComponentActivity() {
    lateinit var mApplication: BaseApplication
    lateinit var user: User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        mApplication = applicationContext as BaseApplication
        user = mApplication.user
    }
}

//

class MessagesActivity : BaseCompActivity() {

    var discussion: Discussion? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        discussion = intent.getSerializableExtra(DISCUSSION_EXTRA) as Discussion


        val vm = MessagesViewModel(mApplication, discussion!!.id)
        vm.listen()

        setContent {
            ProappTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background
                ) {
                    MyApp(vm, mApplication.user, discussion!!)
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (mApplication.mSocket == null) return
        mApplication.listenNotifications(mApplication.user.token)
    }


}

class MessagesViewModel(val app: BaseApplication, private val discussionId: Int) : ViewModel() {
    private val mMessages = mutableStateListOf<Message>()
    private val mDiscussion = mutableStateOf(Discussion())
    var errorMessage: String by mutableStateOf("")
    val messages: List<Message>
        get() = mMessages

    val discussion: Discussion
        get() = mDiscussion.value


    fun listen() {
        if (app.mSocket == null) return
        app.clearSocketListening(app.user.token)
        app.mSocket!!.on(app.user.token, onNewMessage)
    }

    private val onNewMessage =
        Emitter.Listener { args ->
            val data = args[0]
            val message = Gson().fromJson(data.toString(), Message::class.java)
            if (message.discussion == discussionId)
                mMessages.add(0, message)
        }

    fun getDiscussion(id: Int) {
        val token = app.user.token
        if (token.isNullOrEmpty()) return
        viewModelScope.launch {
            try {
                mMessages.clear()
                val result = app.quotesApi.getDiscussion("token ${app.user.token}", discussionId)
                    ?: return@launch

                mDiscussion.value = result.body()!!
            } catch (e: Exception) {
                errorMessage = e.message.toString()
            }
        }
    }

    fun getMessages() {
        viewModelScope.launch {
            try {
                mMessages.clear()
                val result = app.quotesApi.getMessages("token ${app.user.token}", discussionId)
                    ?: return@launch

                mMessages.addAll(result.body()!!.results)
            } catch (e: Exception) {
                errorMessage = e.message.toString()
            }
        }
    }

    fun postMessage(message: String) {

        val result = app.quotesApi.sendMessage(
            "token ${app.user.token}",
            Message(
                message = message,
                discussion = discussionId
            )
        ) ?: return
        result.enqueue(object : Callback<Message?> {
            override fun onResponse(call: Call<Message?>, response: Response<Message?>) {
                if (response.errorBody() != null || response.body()!!.id == 0) {
                    //displayConnexionFailure("BODY ERROR| " + response.errorBody())
                    return
                }
                val msg: Message = response.body()
                    ?: //displayConnexionFailure("BODY ERROR| " + response.errorBody())
                    return

                mMessages.add(0, msg)

            }

            override fun onFailure(call: Call<Message?>, t: Throwable) {
                //displayConnexionFailure("ON FAILURE| " + t.toString())
            }

        })
    }
}

@Composable
fun MyApp(vm: MessagesViewModel, user: User?, discussion: Discussion) {
    Scaffold(

        content = {
            BarkHomeContent(vm, user, discussion)
        })
}


@Composable
fun BarkHomeContent(vm: MessagesViewModel, appUser: User?, discussion: Discussion) {


//    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val user = discussion.other


    LaunchedEffect(Unit, block = {
        vm.getMessages()
    })



    Column(Modifier.fillMaxSize()) {
        MessageHeader(user)


        Divider()


        LazyColumn(
            state = listState,
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp),
            reverseLayout = true
        ) {
            items(items = vm.messages, itemContent = {
                MessageListItem(message = it, appUser)
            })
        }
        Divider()
        MessageFooter(vm)

    }

}


@Composable
fun MessageListItem(message: Message, user: User?) {
    val mUser = user!!
    val sender = message.sendTo!!
    val bottomEnd = if (sender.id == mUser.id) 0.dp else 32.dp
    val bottomStart = if (sender.id != mUser.id) 0.dp else 32.dp
    val paddingStart = if (sender.id != mUser.id) 8.dp else 32.dp
    val paddingEnd = if (sender.id == mUser.id) 8.dp else 32.dp
    val cardAlignment = if (sender.id == mUser.id) Alignment.End else Alignment.Start

    // Declaring 4 Colors
//    val colorBlack = Color.Black
//    val colorMagenta = Color.Magenta

    // Creating a Radial Gradient Color
    val gradientRadial = Brush.linearGradient(
        listOf(
            MaterialTheme.colors.background, Success80
        )
    )
    val senderBackground =
        if (sender.id == mUser.id) Modifier.background(gradientRadial) else Modifier.background(
            Color.Transparent
        )

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Card(
            modifier = Modifier
                .padding(
                    top = 4.dp, start = paddingStart, end = paddingEnd
                )
                .wrapContentWidth()
                .align(cardAlignment),
            elevation = 1.dp,
            shape = RoundedCornerShape(
                topStart = 32.dp, topEnd = 32.dp, bottomStart = bottomStart, bottomEnd = bottomEnd
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