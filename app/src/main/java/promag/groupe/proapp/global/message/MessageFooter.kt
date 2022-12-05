package promag.groupe.proapp.global.message

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Icon
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material.icons.outlined.PhotoCameraFront
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import promag.groupe.proapp.global.ui.theme.Independence
import promag.groupe.proapp.global.ui.theme.Independence10
import promag.groupe.proapp.global.ui.theme.Independence20
import promag.groupe.proapp.global.ui.theme.Independence50


@Preview
@Composable
fun MessageFooter() {

    var message by remember {
        mutableStateOf(
            TextFieldValue("")
        )
    }
    val lightBlue = Color(0xffd8e6ff)
    val blue = Color(0xff76a9ff)

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
                .border(width = 1.dp, color = Independence20, shape = RoundedCornerShape(24.dp))


        ) {
            Icon(
                Icons.Outlined.PhotoCameraFront,
                contentDescription = "Favorite",
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.Center),
                tint = Independence50

            )
        }

        CustomTextField(

            trailingIcon = null, modifier = Modifier
                .weight(1f)
                .background(
                    Independence10, RoundedCornerShape(percent = 25)
                )
                .height(42.dp), fontSize = 14.sp, placeholderText = "Message"
        )

        Box(
            modifier = Modifier
                .width(36.dp)
                .height(36.dp)
                .background(Color.Transparent)
                .border(width = 1.dp, color = Independence20, shape = RoundedCornerShape(24.dp))


        ) {
            Icon(
                Icons.Outlined.Mic,
                contentDescription = "Favorite",
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.Center),
                tint = Independence50

            )
        }
        Box(
            modifier = Modifier
                .width(36.dp)
                .height(36.dp)
                .background(Color.Transparent)
                .border(width = 1.dp, color = Independence20, shape = RoundedCornerShape(24.dp))


        ) {
            Icon(
                Icons.Outlined.Explore,
                contentDescription = "Favorite",
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.Center),
                tint = Independence50

            )
        }


    }
}

@Composable
private fun CustomTextField(
    modifier: Modifier = Modifier,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    placeholderText: String = "Placeholder",
    fontSize: TextUnit = MaterialTheme.typography.body2.fontSize
) {
    var text by rememberSaveable { mutableStateOf("") }
    BasicTextField(modifier = modifier

        .fillMaxWidth(),
        value = text,
        onValueChange = {
            text = it
        },
        singleLine = true,
        cursorBrush = SolidColor(MaterialTheme.colors.primary),
        textStyle = LocalTextStyle.current.copy(
            color = MaterialTheme.colors.onSurface, fontSize = fontSize
        ),
        decorationBox = { innerTextField ->
            Row(
                modifier, verticalAlignment = Alignment.CenterVertically
            ) {
                if (leadingIcon != null) leadingIcon()
                Box(
                    Modifier
                        .weight(1f)
                        .padding(start = 14.dp)
                ) {
                    if (text.isEmpty()) Text(
                        placeholderText, style = LocalTextStyle.current.copy(
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.3f),
                            fontSize = fontSize
                        )
                    )
                    innerTextField()
                }
                if (trailingIcon != null) trailingIcon()
            }
        })
}