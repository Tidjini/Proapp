package promag.groupe.proapp.global.message

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Expand
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material.icons.outlined.PhotoCameraFront
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import promag.groupe.proapp.global.ui.theme.Independence


@Preview
@Composable
fun MessageFooter() {

    var message by remember {
        mutableStateOf(
            TextFieldValue("")
        )
    }

    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {

        Box(
            modifier = Modifier
                .width(36.dp)
                .height(36.dp)
                .background(Color.Transparent)
                .border(width = 1.dp, color = Independence, shape = RoundedCornerShape(24.dp))


        ) {
            Icon(
                Icons.Outlined.PhotoCameraFront,
                contentDescription = "Favorite",
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.Center),
                tint = Independence

            )
        }
        TextField(
            value = message, onValueChange = { newText ->
                message = newText
            },
            Modifier
                .background(Color.Transparent)
                .weight(1f)
        )
        Box(
            modifier = Modifier
                .width(36.dp)
                .height(36.dp)
                .background(Color.Transparent)
                .border(width = 1.dp, color = Independence, shape = RoundedCornerShape(24.dp))


        ) {
            Icon(
                Icons.Outlined.Mic,
                contentDescription = "Favorite",
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.Center),
                tint = Independence

            )
        }
        Box(
            modifier = Modifier
                .width(36.dp)
                .height(36.dp)
                .background(Color.Transparent)
                .border(width = 1.dp, color = Independence, shape = RoundedCornerShape(24.dp))


        ) {
            Icon(
                Icons.Outlined.Expand,
                contentDescription = "Favorite",
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.Center),
                tint = Independence

            )
        }


    }
}


@Composable
fun AppTextF() {

    Column {
        var textState by remember { mutableStateOf("") }
        val maxLength = 110
        val lightBlue = Color(0xffd8e6ff)
        val blue = Color(0xff76a9ff)
        Text(
            text = "Caption",
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp),
            textAlign = TextAlign.Start,
            color = blue
        )
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = textState,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = lightBlue,
                cursorColor = Color.Black,
                disabledLabelColor = lightBlue,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            onValueChange = {
                if (it.length <= maxLength) textState = it
            },
            shape = RoundedCornerShape(8.dp),
            singleLine = true,
            trailingIcon = {
                if (textState.isNotEmpty()) {
                    IconButton(onClick = { textState = "" }) {
                        Icon(
                            imageVector = Icons.Outlined.Close,
                            contentDescription = null
                        )
                    }
                }
            }
        )
        Text(
            text = "${textState.length} / $maxLength",
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            textAlign = TextAlign.End,
            color = blue
        )
    }
}