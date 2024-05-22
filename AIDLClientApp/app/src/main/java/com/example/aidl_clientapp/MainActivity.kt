package com.example.aidl_clientapp

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.RemoteException
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import com.example.aidl_clientapp.ui.theme.AIDLClientAppTheme
import com.s4ltf1sh.aidl_motherapp.IExampleInterface
import com.s4ltf1sh.aidl_motherapp.model.MyMessage

class MainActivity : ComponentActivity(), ServiceConnection {
    companion object {
        private const val MOTHER_PACKAGE = "com.s4ltf1sh.aidl_motherapp"
        private const val ACTION_CONNECT = "com.s4ltf1sh.aidl_motherapp.ACTION_CONNECT"
    }

    private var mRemoteService: IExampleInterface? = null
    private var mBound = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AIDLClientAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        Button(onClick = { connect() }) {
                            Text(text = "Connect")
                        }

                        Button(onClick = { getMessageFromServer() }) {
                            Text(text = "Request Hello")
                        }

                        Button(onClick = { sendMessageToServer("Hello Mother") }) {
                            Text(text = "Hello Mother")
                        }

                        Button(onClick = { sendMessageObjToServer("Hello Mother obj") }) {
                            Text(text = "Hello Mother obj")
                        }

                        Button(onClick = { disconnect() }) {
                            Text(text = "Disconnect")
                        }
                    }
                }
            }
        }
    }

    private fun getMessageFromServer() {
        if (mBound.not()) {
            Toast.makeText(this@MainActivity, "Not Connected", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val message = mRemoteService?.messageFromMother
            Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
        } catch (e: RemoteException) {
            e.printStackTrace()
        }
    }

    private fun sendMessageObjToServer(content: String) {
        if (mBound.not()) {
            Toast.makeText(this@MainActivity, "Not Connected", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            mRemoteService?.sendMessageObjToMother(
                MyMessage().apply {
                    this.content = content
                    this.time = System.currentTimeMillis()
                }
            )
        } catch (e: RemoteException) {
            e.printStackTrace()
        }
    }

    private fun sendMessageToServer(content: String) {
        if (mBound.not()) {
            Toast.makeText(this@MainActivity, "Not Connected", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            mRemoteService?.sendMessageToMother(content)
        } catch (e: RemoteException) {
            e.printStackTrace()
        }

    }

    private fun connect() {
        Intent(ACTION_CONNECT).also { intent ->
            intent.`package` = MOTHER_PACKAGE
            applicationContext?.bindService(intent, this, BIND_AUTO_CREATE)
        }
    }

    private fun disconnect() {
        if (mBound) {
            applicationContext.unbindService(this)
            mBound = false
        }
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        mRemoteService = IExampleInterface.Stub.asInterface(service)
        mBound = true
        Toast.makeText(this@MainActivity, "Connected", Toast.LENGTH_SHORT).show()
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        mRemoteService = null
        mBound = false
    }

    override fun onDestroy() {
        disconnect()
        super.onDestroy()
    }
}