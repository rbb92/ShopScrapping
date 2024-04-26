package com.example.shopscrapping.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.shopscrapping.R
import java.net.URL


//TODO Notificacion de prueba, borrar!
fun notificationTest(context: Context, title: String, message: String,url: String) {
    // Crear un id único para la notificación
    val notificationId = (0..100000).random()
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    val pendingIntent = PendingIntent.getActivity(context, 0, intent, if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    } else {
        PendingIntent.FLAG_UPDATE_CURRENT
    })


    // Crear un canal de notificación para dispositivos Android Oreo (API 26) y superiores
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channelId = "mi_canal_id"
        val channelName = "Mi Canal"
        val channelDescription = "Descripción del canal"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, channelName, importance).apply {
            description = channelDescription
        }
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }


    val notificationLayout = RemoteViews(context.packageName, R.layout.small_notification)
//    notificationLayout.setTextViewText(R.id.notification_title,title)
    val notificationLayoutExpanded = RemoteViews(context.packageName, R.layout.expanded_notification)
    val bitmap = BitmapFactory.decodeStream(URL("https://m.media-amazon.com/images/I/619chxJU3kL.__AC_SX300_SY300_QL70_ML2_.jpg").openStream())
    notificationLayoutExpanded.setImageViewBitmap(R.id.product_image,bitmap)
    notificationLayout.setImageViewBitmap(R.id.product_image,bitmap)


    // Crear la notificación
    val builder = NotificationCompat.Builder(context, "mi_canal_id")
        .setSmallIcon(android.R.drawable.ic_dialog_info)
        .setStyle(NotificationCompat.DecoratedCustomViewStyle())
        .setCustomContentView(notificationLayout)
        .setCustomBigContentView(notificationLayoutExpanded)
        .setPriority(NotificationCompat.PRIORITY_MAX)
        .addAction(android.R.drawable.btn_star_big_on , "\uD83D\uDED2 Comprar", pendingIntent)



    // Mostrar la notificación
    with(NotificationManagerCompat.from(context)) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            // ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            // public fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
            //                                        grantResults: IntArray)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            return@with
        }
        // notificationId is a unique int for each notification that you must define.
        notify(notificationId, builder.build())
    }
}

fun notificationProductAdded(context: Context, message: String) {
    // Crear un id único para la notificación
    val notificationId = (0..100000).random()


    // Crear un canal de notificación para dispositivos Android Oreo (API 26) y superiores
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channelId = "productAdded"
        val channelName = "productAdded"
        val channelDescription = "New product for scrapping added to the channel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, channelName, importance).apply {
            description = channelDescription
        }
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    val title = "✅ Nuevo producto añadido"
    // Crear la notificación
    val builder = NotificationCompat.Builder(context, "mi_canal_id")
        .setSmallIcon(android.R.drawable.ic_dialog_info)
        .setContentTitle(title)
        .setContentText(message)
        .setPriority(NotificationCompat.PRIORITY_MAX)

    // Mostrar la notificación
    with(NotificationManagerCompat.from(context)) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            // ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            // public fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
            //                                        grantResults: IntArray)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            return@with
        }
        // notificationId is a unique int for each notification that you must define.
        notify(notificationId, builder.build())
    }
}

fun notificationGoal(context: Context, message: String, title_header: String, url: String, url_img: String,price:String) {
    // Crear un id único para la notificación
    val notificationId = (0..1000000).random()

    // Crear una intención para abrir la URL en un navegador
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    val pendingIntent = PendingIntent.getActivity(context, 0, intent, if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    } else {
        PendingIntent.FLAG_UPDATE_CURRENT
    })
    // Crear un canal de notificación para dispositivos Android Oreo (API 26) y superiores
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channelId = "mainGoal"
        val channelName = "mainGoal"
        val channelDescription = "New goal reached"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(channelId, channelName, importance).apply {
            description = channelDescription
        }
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    val title = "\uD83C\uDF89 HURRAY! ${title_header}"
    val notificationLayout = RemoteViews(context.packageName, R.layout.small_notification)
    notificationLayout.setTextViewText(R.id.notification_title,title)
    val notificationLayoutExpanded = RemoteViews(context.packageName, R.layout.expanded_notification)
    val bitmap = BitmapFactory.decodeStream(URL(url_img).openStream())
    notificationLayoutExpanded.setImageViewBitmap(R.id.product_image,bitmap)
    notificationLayout.setImageViewBitmap(R.id.product_image,bitmap)
    notificationLayoutExpanded.setTextViewText(R.id.notification_title,title)
    notificationLayoutExpanded.setTextViewText(R.id.product_title,message)
    notificationLayoutExpanded.setTextViewText(R.id.product_price,price)
    notificationLayoutExpanded.setOnClickPendingIntent(R.id.button,pendingIntent)


    // Crear la notificación
    val builder = NotificationCompat.Builder(context, "mainGoal")
        .setSmallIcon(android.R.drawable.btn_star_big_on)
//        .setContentTitle(title)
        .addAction(android.R.drawable.btn_star_big_on , "\uD83D\uDED2 Comprar", pendingIntent)
        .setCustomContentView(notificationLayout)
        .setCustomBigContentView(notificationLayoutExpanded)
        .setPriority(NotificationCompat.PRIORITY_MAX)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)

    // Mostrar la notificación
    with(NotificationManagerCompat.from(context)) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            // ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            // public fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
            //                                        grantResults: IntArray)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            return@with
        }
        // notificationId is a unique int for each notification that you must define.
        notify(notificationId, builder.build())
    }
}

//fun notificationSecondaryGoal(context: Context, message: String, title_header: String, url: String, url_img: String,price:String) {
//    // Crear un id único para la notificación
//    val notificationId = (0..1000000).random()
//
//    // Crear una intención para abrir la URL en un navegador
//    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
//    val pendingIntent = PendingIntent.getActivity(context, 0, intent, if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//        PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
//    } else {
//        PendingIntent.FLAG_UPDATE_CURRENT
//    })    // Crear un canal de notificación para dispositivos Android Oreo (API 26) y superiores
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//        val channelId = "mainGoal"
//        val channelName = "mainGoal"
//        val channelDescription = "New goal reached"
//        val importance = NotificationManager.IMPORTANCE_DEFAULT
//        val channel = NotificationChannel(channelId, channelName, importance).apply {
//            description = channelDescription
//        }
//        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        notificationManager.createNotificationChannel(channel)
//    }
//
//    val title = "\uD83C\uDF89 Novedad! ${title_header}"
//    // Crear la notificación
//    val builder = NotificationCompat.Builder(context, "mainGoal")
//        .setSmallIcon(android.R.drawable.ic_dialog_info)
//        .setContentTitle(title)
//        .setContentText(message)
//        .setPriority(NotificationCompat.PRIORITY_MAX)
//        .addAction(android.R.drawable.btn_star_big_on , "\uD83D\uDED2 Comprar", pendingIntent)
//
//    // Mostrar la notificación
//    with(NotificationManagerCompat.from(context)) {
//        if (ActivityCompat.checkSelfPermission(
//                context,
//                Manifest.permission.POST_NOTIFICATIONS
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            // TODO: Consider calling
//            // ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            // public fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
//            //                                        grantResults: IntArray)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//
//            return@with
//        }
//        // notificationId is a unique int for each notification that you must define.
//        notify(notificationId, builder.build())
//    }
//}