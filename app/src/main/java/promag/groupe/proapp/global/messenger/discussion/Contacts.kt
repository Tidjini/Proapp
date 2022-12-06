package promag.groupe.proapp.global.messenger.discussion

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Message
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import promag.groupe.proapp.BaseComponentActivity
import promag.groupe.proapp.global.ui.theme.*
import promag.groupe.proapp.models.User

class Contacts : BaseComponentActivity() {

    var vm: DiscussionViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm = DiscussionViewModel(mApplication)

        setContent {
            ProappTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background
                ) {
                    Scaffold(

                        content = {
                            MainContent(vm!!, mApplication.user, this)
                        })
                }
            }
        }
    }


}


@Composable
fun MainContent(vm: DiscussionViewModel, user: User, activity: Contacts) {


    val listState = rememberLazyListState()


    LaunchedEffect(Unit, block = {
        vm.getContacts()
    })



    Column(Modifier.fillMaxSize()) {
        CreateDiscussionHeader(activity)
        Divider()
        LazyColumn(
            state = listState,
            modifier = Modifier.weight(1f),
        ) {
            items(items = vm.contacts, itemContent = {
                ContactItem(user = it, currentUser = user, vm)
                Divider()
            })
        }

    }

}


@Composable
fun CreateDiscussionHeader(activity: Contacts) {

    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 14.dp)
    ) {

        IconButton(onClick = { activity.onBackPressed() }) {
            Icon(
                Icons.Outlined.ArrowBack,
                contentDescription = "Favorite",
                modifier = Modifier.size(28.dp),
                tint = Success

            )
        }

        Text(
            text = "Utilisateurs",
            style = MaterialTheme.typography.h6,
            fontSize = 18.sp,
            color = Independence,
            modifier = Modifier
                .padding(start = 14.dp)
                .align(Alignment.CenterVertically)
                .weight(1f)
        )

        Row(
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            Icon(
                Icons.Outlined.Message,
                contentDescription = "Favorite",
                modifier = Modifier.size(28.dp),
                tint = Success

            )
            Icon(
                Icons.Outlined.Settings,
                contentDescription = "Favorite",
                modifier = Modifier
                    .padding(start = 14.dp)
                    .size(28.dp),
                tint = Success

            )
        }

    }
}


@Composable
fun ContactItem(user: User, currentUser: User, viewModel: DiscussionViewModel) {

    if (user.id == currentUser.id) return

    Row(
        Modifier
            .fillMaxWidth()
            .padding(14.dp)
    ) {
        Box(
            modifier = Modifier
                .width(48.dp)
                .height(48.dp)
                .clip(shape = RoundedCornerShape(28.dp))
                .background(Success)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(2.dp)
                    .clip(shape = RoundedCornerShape(28.dp))
                    .background(FlatWhite)

            ) {
                Text(
                    text = user.caption, modifier = Modifier.align(Alignment.Center)

                )
            }


        }
        Column(
            modifier = Modifier
                .padding(start = 14.dp)
                .align(Alignment.CenterVertically)
                .weight(1f)
        ) {
            Text(
                text = user.name,
                style = MaterialTheme.typography.h6,
                fontSize = 14.sp,
                color = Independence
            )
            Text(
                text = "online", style = MaterialTheme.typography.caption, color = Independence50
            )

        }

        IconButton(onClick = { viewModel.addDiscussion(user.id) }) {
            Icon(
                Icons.Outlined.Add,
                contentDescription = "Add Contact",
                modifier = Modifier.size(28.dp),
                tint = Success
            )
        }


    }
}