package promag.groupe.proapp.global.message

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Videocam
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import promag.groupe.proapp.global.MessagesViewModel
import promag.groupe.proapp.global.ui.theme.FlatWhite
import promag.groupe.proapp.global.ui.theme.Independence
import promag.groupe.proapp.global.ui.theme.Independence50
import promag.groupe.proapp.global.ui.theme.Success
import promag.groupe.proapp.models.User

class SampleUserProvider : PreviewParameterProvider<User> {
    override val values = sequenceOf(User(username = "John", name = "John Doe"))
}


@Composable
fun MessageHeader(user: User?) {
//    LaunchedEffect(Unit, block = {
//        vm.getDiscussion(discussionId)
//    })
    val user = user ?: User(username = "John", name = "John Doe")
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 14.dp)
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

        Row(
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            Icon(
                Icons.Outlined.Phone,
                contentDescription = "Favorite",
                modifier = Modifier.size(28.dp),
                tint = Success

            )
            Icon(
                Icons.Outlined.Videocam,
                contentDescription = "Favorite",
                modifier = Modifier
                    .padding(start = 14.dp)
                    .size(28.dp),
                tint = Success

            )
        }

    }
}

