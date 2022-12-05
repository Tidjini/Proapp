package promag.groupe.proapp.global.message

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import io.socket.emitter.Emitter
import kotlinx.coroutines.launch
import promag.groupe.proapp.BaseApplication
import promag.groupe.proapp.models.Discussion
import promag.groupe.proapp.models.Message
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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