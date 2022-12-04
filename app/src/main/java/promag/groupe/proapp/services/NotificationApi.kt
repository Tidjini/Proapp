package promag.groupe.proapp.services

import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import promag.groupe.proapp.NOTIFICATION_END
import java.net.URISyntaxException

class NotificationApi {

    companion object{
        private var mSocket: Socket? = null

        fun build(endpoint: String = NOTIFICATION_END, event:String="event", listener: Emitter.Listener? = null) : Socket?{
            try{
                mSocket = IO.socket(endpoint)
                mSocket!!.connect()
                Log.d("socket", "connected to server")
                if(listener != null)
                    on(event, listener)
            }catch (e: URISyntaxException) {
                Log.e("socket", "Factory issue problem due to: $e")
                mSocket = null
            }
            return mSocket
        }

        fun on(event: String, listener: Emitter.Listener){
            if(mSocket != null)
                mSocket!!.on(event, listener)
        }

    }
}