package promag.groupe.proapp.tasks.views

import android.content.Intent
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
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import promag.groupe.proapp.BaseComponentActivity
import promag.groupe.proapp.MainActivity
import promag.groupe.proapp.comercial.AppViewModelFactory
import promag.groupe.proapp.global.ui.theme.*
import promag.groupe.proapp.models.User
import promag.groupe.proapp.tasks.models.Task
import promag.groupe.proapp.tasks.viewmodels.TaskViewModel


class TaskCollectionView : BaseComponentActivity() {

    lateinit var vm: TaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vm = ViewModelProvider(
            this, AppViewModelFactory(mApplication)
        )[TaskViewModel::class.java]

        setContent {
            ProappTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background
                ) {
                    Scaffold(

                        content = {
                            TaskCollectionContent(vm!!, mApplication.user, this)
                        })
                }
            }
        }
    }

    override fun onBackPressed() {
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
        this.finish()
    }


}


@Composable
fun TaskCollectionContent(vm: TaskViewModel, user: User, activity: TaskCollectionView) {


    val listState = rememberLazyListState()

    var type = rememberSaveable { mutableStateOf(1) }

    LaunchedEffect(Unit, block = {
        vm.getTasks(type = type.value)
    })



    Column(Modifier.fillMaxSize()) {
        TasksViewHeader(activity)
        Divider()
        Row(horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier.padding(top = 14.dp)) {
            Button(
                onClick = {
                    type.value = 0
                    vm.getTasks(type = type.value)

                }, colors = ButtonDefaults.buttonColors(backgroundColor = Red, contentColor = Color.White),
                modifier = Modifier.height(48.dp).padding(start = 14.dp).weight(1f)
            ) {
                Text(text = "Tâches Créés")
            }
            Button(
                onClick = {
                    type.value = 1
                    vm.getTasks(type = type.value)

                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Success, contentColor = Color.White),
                modifier = Modifier.height(48.dp).padding(horizontal = 14.dp).weight(1f)
            ) {
                Text(text = "Tâches Affectées")
            }
        }


        LazyColumn(
            state = listState,
            modifier = Modifier.weight(1f),
        ) {
            items(items = vm.tasks, itemContent = {
                TaskItem(item = it, vm)
                Divider()
            })
        }

    }

}

//todo use translate between frensh and english

@Composable
fun TasksViewHeader(activity: TaskCollectionView) {

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
            text = "Tâches",
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

            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    Icons.Outlined.FilterList,
                    contentDescription = "Filter",
                    modifier = Modifier.size(28.dp),
                    tint = Success

                )
            }
            IconButton(onClick = {
//                todo activity.vm.createTaskView()
            }) {
                Icon(
                    Icons.Outlined.AddCircle,
                    contentDescription = "Add Tasks",
                    modifier = Modifier.size(28.dp),
                    tint = Success

                )
            }

        }

    }
}


//Todo add symbole or shortcut
@Composable
fun TaskItem(item: Task, viewModel: TaskViewModel) {


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
                    text = item.caption, modifier = Modifier.align(Alignment.Center)

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
                text = item.label,
                style = MaterialTheme.typography.h6,
                fontSize = 14.sp,
                color = Independence
            )
            Text(
                text = "${item.shortDescription}",
                style = MaterialTheme.typography.caption,
                color = Independence50
            )

            Text(
                text = "${item.statueVerbose}",
                style = MaterialTheme.typography.h6,
                fontSize = 10.sp,
                color = Independence
            )


        }

        Row() {

            IconButton(onClick = { viewModel.editTaskView(item) }) {
                Icon(
                    Icons.Outlined.Edit,
                    contentDescription = "Add Contact",
                    modifier = Modifier.size(28.dp),
                    tint = Success
                )
            }

        }


    }
}