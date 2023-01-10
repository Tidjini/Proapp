package promag.groupe.proapp.permissions

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import promag.groupe.proapp.BaseApplication


class NetworkMonitor : NetworkCallback {


    private var networkRequest: NetworkRequest
    val listener: Listener

    constructor(application: BaseApplication) {
        listener = application
        networkRequest = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
            .build()
        enable(application)
    }

    private fun enable(context: Context) {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.registerNetworkCallback(networkRequest, this)
    }


    override fun onUnavailable() {
        super.onUnavailable()
        listener.onNetworkUnavailable()
    }

    override fun onLost(network: Network) {
        super.onLost(network)
        listener.onNetworkUnavailable()
    }


    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        listener.onNetworkAvailable()
    }


    interface Listener {
        fun onNetworkAvailable()
        fun onNetworkUnavailable()
    }


    companion object {
        fun checkNetworkState(application: BaseApplication): Boolean {
            var result = false
            val connectivityManager =
                application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val networkCapabilities = connectivityManager.activeNetwork ?: return false
                val actNw =
                    connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
                result = when {
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            } else {
                connectivityManager.run {
                    connectivityManager.activeNetworkInfo?.run {
                        result = when (type) {
                            ConnectivityManager.TYPE_WIFI -> true
                            ConnectivityManager.TYPE_MOBILE -> true
                            ConnectivityManager.TYPE_ETHERNET -> true
                            else -> false
                        }

                    }
                }
            }

            return result
        }


    }
}