package promag.groupe.proapp.tasks.viewmodels

import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import promag.groupe.proapp.BaseApplication
import promag.groupe.proapp.PRODUCT_EXTRA
import promag.groupe.proapp.TIER_EXTRA
import promag.groupe.proapp.comercial.models.Payment
import promag.groupe.proapp.comercial.views.PaymentView
import promag.groupe.proapp.models.User
import promag.groupe.proapp.tasks.models.Task


class TaskViewModel(val app: BaseApplication) : ViewModel() {

    private val mTasks = mutableStateListOf<Task>()
    private val mTask = mutableStateOf(Task())

    var errorMessage: String by mutableStateOf("")

    val tasks: List<Task>
        get() {
            return mTasks
        }

    val task: Task
        get() {
            return mTask.value
        }

    fun getTasks(type: Int) {
        viewModelScope.launch {
            try {
                mTasks.clear()
                val result = app.tasksAPI.getTasks("token ${app.user.token}", type)
                    ?: return@launch


                mTasks.addAll(result.body()!!.results)
            } catch (e: Exception) {
                errorMessage = e.message.toString()
            }
        }
    }



    fun save(label: String, description: String, statue: String, receiver: User, task: Task) {

        try {
            task.label = label
            task.description = description
            task.statue = statue
            task.receiver = receiver.id


            if (task.id == null || task.id == 0) {
                return createTask(task)
            }
            return updateTask(task)

        } catch (e: Exception) {
            //todo display error message
        }
    }



    fun createTask(task: Task) {
        viewModelScope.launch {
            try {

                app.tasksAPI.createTask("token ${app.user.token}", task)
                    ?: return@launch

                gotoTaskCollectionView()
            } catch (e: Exception) {
                errorMessage = e.message.toString()
            }
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            try {

                app.tasksAPI.updateTask("token ${app.user.token}", task.id!!, task)
                    ?: return@launch

                gotoTaskCollectionView()
            } catch (e: Exception) {
                errorMessage = e.message.toString()
            }
        }
    }


    private fun gotoTaskCollectionView() {
        val i = Intent(app, TaskCollectionView::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
        app.startActivity(i)
    }

    fun createTaskView() {

        val intent = Intent(app, TaskView::class.java)
        intent.putExtra(PRODUCT_EXTRA, Task())
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS

        app.startActivity(intent)
    }

    fun editTaskView(task: Task) {
        mTask.value = task
        val intent = Intent(app, TaskView::class.java)
        intent.putExtra(PRODUCT_EXTRA, task)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
        app.startActivity(intent)
    }




}