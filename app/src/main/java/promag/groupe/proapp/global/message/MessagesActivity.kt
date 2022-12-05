package promag.groupe.proapp.global.message

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import promag.groupe.proapp.BaseComponentActivity
import promag.groupe.proapp.DISCUSSION_EXTRA
import promag.groupe.proapp.global.ui.theme.ProappTheme
import promag.groupe.proapp.global.ui.theme.Success80
import promag.groupe.proapp.models.Discussion
import promag.groupe.proapp.models.Message
import promag.groupe.proapp.models.User


class MessagesActivity : BaseComponentActivity() {

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
                    Messages(vm, mApplication.user, discussion!!)
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


@Composable
fun Messages(vm: MessagesViewModel, user: User?, discussion: Discussion) {
    Scaffold(

        content = {
            MainContent(vm, user, discussion)
        })
}


@Composable
fun MainContent(vm: MessagesViewModel, appUser: User?, discussion: Discussion) {


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