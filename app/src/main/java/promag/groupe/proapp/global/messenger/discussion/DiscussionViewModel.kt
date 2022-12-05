package promag.groupe.proapp.global.messenger.discussion

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import promag.groupe.proapp.BaseApplication
import promag.groupe.proapp.models.Discussion
import promag.groupe.proapp.models.User

class DiscussionViewModel(val app: BaseApplication) : ViewModel() {

    private val mContacts = mutableStateListOf<User>()
    private val mDiscussions = mutableStateListOf<Discussion>()

    var errorMessage: String by mutableStateOf("")

    val contacts: List<User>
        get() {
            return mContacts
        }

    val discussions: List<Discussion>
        get() {
            return mDiscussions
        }

    fun getDiscussions() {
        viewModelScope.launch {
            try {
                mDiscussions.clear()
                val result = app.quotesApi.getDiscussions("token ${app.user.token}")
                    ?: return@launch

                mDiscussions.addAll(result.body()!!.results)
            } catch (e: Exception) {
                errorMessage = e.message.toString()
            }
        }
    }

    fun addDiscussion(userId: Int){

    }


}