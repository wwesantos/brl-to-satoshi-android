package co.javacafe.satoshirealconverter

import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.NotificationCompat

class AppStartupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val detailsIntent = Intent(this, MainActivity::class.java)

        val pendingIntent: PendingIntent? = TaskStackBuilder.create(this)
                .addNextIntentWithParentStack(detailsIntent)
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)

        NotificationCompat.Builder(this).setContentIntent(pendingIntent)
    }
}
