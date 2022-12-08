package promag.groupe.proapp.tasks.views

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Photo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import promag.groupe.proapp.BaseComponentActivity
import promag.groupe.proapp.TASK_EXTRA
import promag.groupe.proapp.comercial.AppViewModelFactory
import promag.groupe.proapp.global.ui.theme.*
import promag.groupe.proapp.tasks.models.Task
import promag.groupe.proapp.tasks.viewmodels.TaskViewModel
import promag.groupe.proapp.views.CustomTextField

class TaskView : BaseComponentActivity() {

    lateinit var vm: TaskViewModel
    lateinit var task: Task

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        vm =
            ViewModelProvider(this, AppViewModelFactory(mApplication))[TaskViewModel::class.java]

        task = intent.getSerializableExtra(TASK_EXTRA) as Task

        setContent {
            ProappTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background
                ) {
                    Scaffold(

                        content = {
                            TaskViewContent(vm, this, task)
                        })
                }
            }
        }
    }

    override fun onBackPressed() {
        val i = Intent(this, TaskCollectionView::class.java)
        startActivity(i)
        this.finish()
    }


}


@Composable
fun TaskViewContent(vm: TaskViewModel, activity: TaskView, task: Task) {


    Column(Modifier.fillMaxSize()) {
        TaskViewHeader(task, activity)
        Divider()
        TaskEditor(vm, task)

    }

}

//todo use translate between frensh and english

@Composable
fun TaskViewHeader(task: Task, activity: TaskView) {

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
            text = task.label,
            style = MaterialTheme.typography.h6,
            fontSize = 18.sp,
            color = Independence,
            modifier = Modifier
                .padding(start = 14.dp)
                .align(Alignment.CenterVertically)
                .weight(1f)
        )

        IconButton(onClick = { /*TODO*/ }, modifier = Modifier.align(Alignment.CenterVertically)) {
            Icon(
                Icons.Outlined.Photo,
                contentDescription = "Menu",
                modifier = Modifier.size(28.dp),
                tint = Success

            )
        }


    }
}

//
//fun getValidatedNumber(text: String): String {
//    // Start by filtering out unwanted characters like commas and multiple decimals
//    val filteredChars = text.filterIndexed { index, c ->
//        c in "0123456789" ||                      // Take all digits
//                (c == '.' && text.indexOf('.') == index)  // Take only the first decimal
//    }
//    // Now we need to remove extra digits from the input
//    return if (filteredChars.contains('.')) {
//        val beforeDecimal = filteredChars.substringBefore('.')
//        val afterDecimal = filteredChars.substringAfter('.')
//        beforeDecimal.take(3) + "." + afterDecimal.take(2)    // If decimal is present, take first 3 digits before decimal and first 2 digits after decimal
//    } else {
//        filteredChars.take(3)                     // If there is no decimal, just take the first 3 digits
//    }
//}

@Composable
fun TaskEditor(vm: TaskViewModel, task: Task) {

    var label = rememberSaveable { mutableStateOf(task.label) }
    var description = rememberSaveable { mutableStateOf(task.description) }
    var receiver = rememberSaveable { mutableStateOf(task.receiverItem!!.name) }
    var statue = rememberSaveable { mutableStateOf(task.statue) }

    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {


        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Personnel: ${receiver.value}"
            )
            Text(
                text = "Intitule"
            )
            CustomTextField(
                trailingIcon = null,
                modifier = Modifier
                    .background(
                        Independence10, RoundedCornerShape(percent = 5)
                    )
                    .height(56.dp),
                fontSize = 14.sp,
                text = label,
                placeholderText = "Intitule"
            )
            Text(
                text = "Description"
            )
            CustomTextField(
                trailingIcon = null,
                modifier = Modifier
                    .background(
                        Independence10, RoundedCornerShape(percent = 5)
                    )
                    .height(56.dp),
                fontSize = 14.sp,
                text = description,
                placeholderText = "description",
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { /*todo*/ }
                )
            )

//            Row(horizontalArrangement = Arrangement.SpaceEvenly) {
//                Button(onClick = {
//                    statue.value = task.changeState()
//
//                }, modifier = Modifier.background(Success)) {
//                    Text(text = "Etat ${task.statVerbose(statue.value)}")
//                }
//                Button(onClick = {
//                    statue.value = task.changeState()
//
//                }, modifier = Modifier.background(Red)) {
//                    Text(text = "Annuler ${task.statVerbose(statue.value)}")
//                }
//            }
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.padding(top = 14.dp)
            ) {
                Button(
                    onClick = {
                        statue.value = task.changeState(statue.value)
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Success,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .height(48.dp)
                        .padding()
                        .weight(1f)
                ) {
                    Text(text = "Etat ${task.statVerbose(statue.value)}", fontSize = 11.sp)
                }
                Button(
                    onClick = {
                        statue.value = task.cancel()

                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Red,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .height(48.dp)
                        .padding(start = 14.dp)
                        .weight(1f)
                ) {
                    Text(text = "Annuler", fontSize = 11.sp)
                }
            }

            Button(onClick = {

                vm.save(
                    label = label.value,
                    description = description.value,
                    statue = statue.value,
                    receiver = task.receiver,
                    task = task
                )
            }) {
                Text(text = "Sauvgarder")
            }
        }


    }
}