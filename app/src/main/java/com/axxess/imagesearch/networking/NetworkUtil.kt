package com.axxess.imagesearch.networking

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log


class NetworkUtil(val context: Context) {
    val TAG = javaClass.name
    val RETRY_TIMES = 3

    fun isConnected(): Boolean {
        return isConnectedToWifi() || isConnectedToMobile()
    }

    fun isConnectedToMobile() = isConnected(ConnectionType.MOBILE)

    fun isConnectedToWifi() = isConnected(ConnectionType.WIFI)

    private fun isConnected(type: ConnectionType): Boolean {
        try {
            val connectionType = getConnectionType()
            val isConnected = getConnectedState()
            return (connectionType == type && isConnected)
        } catch (ex: Exception) {
            // getting null pointer exception on some devices while trying to getActiveNetworkInfo();
            // happening from NetworkBroadcastReceiver
            Log.e(TAG, ex.toString())
        }

        return false
    }

    @Suppress("DEPRECATION")
    private fun getConnectionType(): ConnectionType {
        var result = ConnectionType.NONE
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            cm?.run {
                getNetworkCapabilities(activeNetwork)?.run {
                    if (hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        result = ConnectionType.WIFI
                    } else if (hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        result = ConnectionType.MOBILE
                    }
                }
            }
        } else {
            cm?.run {
                activeNetworkInfo?.run {
                    if (type == ConnectivityManager.TYPE_WIFI) {
                        result = ConnectionType.WIFI
                    } else if (type == ConnectivityManager.TYPE_MOBILE) {
                        result = ConnectionType.MOBILE
                    }
                }
            }
        }
        return result
    }

    private fun getConnectedState() =
        (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo.isConnected

    internal enum class ConnectionType {
        NONE, MOBILE, WIFI
    }
}