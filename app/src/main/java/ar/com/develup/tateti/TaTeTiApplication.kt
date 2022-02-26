package ar.com.develup.tateti

import android.annotation.SuppressLint
import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

class TaTeTiApplication : Application() {
    @SuppressLint("StringFormatInvalid")
    override fun onCreate() {
        super.onCreate()
        // TODO-07-NOTIFICATION

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }


            val token = task.result


            val msg = this.getString(R.string.msg_token_fmt, token)
            Log.d(TAG, msg)
            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
        })
    }
}