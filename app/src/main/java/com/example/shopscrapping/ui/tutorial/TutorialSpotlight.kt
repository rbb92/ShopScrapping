package com.example.shopscrapping.ui.tutorial

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.compose.runtime.Composable
import com.example.shopscrapping.R
import com.takusemba.spotlight.OnTargetListener
import com.takusemba.spotlight.Target
import com.takusemba.spotlight.effet.FlickerEffect
import com.takusemba.spotlight.shape.Circle
import com.takusemba.spotlight.shape.RoundedRectangle


fun PresentationTarget(intro: View): Target = Target.Builder()
        .setOverlay(intro)
        .setOnTargetListener(object : OnTargetListener {
            override fun onStarted() {
                intro.findViewById<TextView>(R.id.Spotlight_text).setText("Bienvenidos a Price Watcher!! \uD83D\uDE00 . En el siguiente tutorial explicaremos como funciona la aplicacion")
            }

            override fun onEnded() {
                intro.findViewById<TextView>(R.id.Spotlight_text).setText("")
            }
        })
        .build()


fun SearchUrlTarget(searchTip: View, screenWidth:Int, screenHeight:Int) = Target.Builder()
    .setAnchor((screenWidth/2).toFloat(),(screenHeight/2).toFloat()-220f)
    .setShape(RoundedRectangle(550f, screenWidth.toFloat()-120f,20f))
    .setOverlay(searchTip)
//        .setEffect(RippleEffect(100f, 200f, argb(30, 124, 255, 90)))
    .setEffect(FlickerEffect(45f, Color.argb(30, 124, 255, 90),500L, DecelerateInterpolator(1f),5))
    .setOnTargetListener(object : OnTargetListener {
        override fun onStarted() {
            searchTip.findViewById<TextView>(R.id.Spotlight_text).setText("En este campo introduce la url del producto el cual quieres ser notificado si baja de precio")
            searchTip.findViewById<TextView>(R.id.Spotlight_text).setTextSize(18f)
        }

        override fun onEnded() {
            searchTip.findViewById<TextView>(R.id.Spotlight_text).setText("")
        }
    })
    .build()


fun SelectStoreTarget(searchTip: View, screenWidth:Int, screenHeight:Int) = Target.Builder()
    .setAnchor((screenWidth/2).toFloat(),(screenHeight/2).toFloat()+260)
    .setShape(RoundedRectangle(450f, screenWidth.toFloat()-120f,20f))
    .setOverlay(searchTip)
//        .setEffect(RippleEffect(100f, 200f, argb(30, 124, 255, 90)))
    .setEffect(FlickerEffect(45f, Color.argb(30, 124, 255, 90),500L,DecelerateInterpolator(1f),5))
    .setOnTargetListener(object : OnTargetListener {
        override fun onStarted() {
            searchTip.findViewById<TextView>(R.id.Spotlight_text).setText("Actualmente soportamos Aliexpress y Amazon, para Aliexpress puedes configurar la región para que los precios se adapten")
            searchTip.findViewById<TextView>(R.id.Spotlight_text).setTextSize(18f)
        }

        override fun onEnded() {
            searchTip.findViewById<TextView>(R.id.Spotlight_text).setText("")
        }
    })
    .build()

fun SearchStoreTarget(searchTip: View, screenWidth:Int, screenHeight:Int) = Target.Builder()
    .setAnchor((screenWidth/2).toFloat(),(screenHeight/2).toFloat()-100)
    .setShape(Circle(180f))
    .setOverlay(searchTip)
//        .setEffect(RippleEffect(100f, 200f, argb(30, 124, 255, 90)))
    .setEffect(FlickerEffect(45f, Color.argb(30, 124, 255, 90),500L,DecelerateInterpolator(1f),5))
    .setOnTargetListener(object : OnTargetListener {
        override fun onStarted() {
            searchTip.findViewById<TextView>(R.id.Spotlight_text).setText("Una vez introducido la url, pulsa sobre el boton de la lupa")
            searchTip.findViewById<TextView>(R.id.Spotlight_text).setTextSize(18f)
        }

        override fun onEnded() {
            searchTip.findViewById<TextView>(R.id.Spotlight_text).setText("")
        }
    })
    .build()

fun ScrapTabTarget(tabsTip: View, screenWidth:Int, screenHeight:Int) = Target.Builder()
    .setAnchor((screenWidth/3).toFloat()-200f,(screenHeight).toFloat())
    .setShape(Circle(160f))
    .setOverlay(tabsTip)
//        .setEffect(RippleEffect(100f, 200f, argb(30, 124, 255, 90)))
    .setEffect(FlickerEffect(45f, Color.argb(30, 124, 255, 90),500L,DecelerateInterpolator(1f),5))
    .setOnTargetListener(object : OnTargetListener {
        override fun onStarted() {
            tabsTip.findViewById<TextView>(R.id.Spotlight_text).setText("En esta pestaña podras buscar los productos")
            tabsTip.findViewById<TextView>(R.id.Spotlight_text).setTextSize(18f)
        }

        override fun onEnded() {
            tabsTip.findViewById<TextView>(R.id.Spotlight_text).setText("")
        }
    })
    .build()

fun ListScrapTabTarget(tabsTip: View, screenWidth:Int, screenHeight:Int) = Target.Builder()
    .setAnchor((screenWidth/3).toFloat()*2-200f,(screenHeight).toFloat())
    .setShape(Circle(160f))
    .setOverlay(tabsTip)
//        .setEffect(RippleEffect(100f, 200f, argb(30, 124, 255, 90)))
    .setEffect(FlickerEffect(45f, Color.argb(30, 124, 255, 90),500L,DecelerateInterpolator(1f),5))
    .setOnTargetListener(object : OnTargetListener {
        override fun onStarted() {
            tabsTip.findViewById<TextView>(R.id.Spotlight_text).setText("En esta pestaña podras ver la lista de tus productos")
            tabsTip.findViewById<TextView>(R.id.Spotlight_text).setTextSize(18f)
        }

        override fun onEnded() {
            tabsTip.findViewById<TextView>(R.id.Spotlight_text).setText("")
        }
    })
    .build()

fun SettingTabTarget(tabsTip: View, screenWidth:Int, screenHeight:Int) = Target.Builder()
    .setAnchor((screenWidth).toFloat()-200f,(screenHeight).toFloat())
    .setShape(Circle(160f))
    .setOverlay(tabsTip)
//        .setEffect(RippleEffect(100f, 200f, argb(30, 124, 255, 90)))
    .setEffect(FlickerEffect(45f, Color.argb(30, 124, 255, 90),500L,DecelerateInterpolator(1f),5))
    .setOnTargetListener(object : OnTargetListener {
        override fun onStarted() {
            tabsTip.findViewById<TextView>(R.id.Spotlight_text).setText("En esta pestaña podras cambiar la configuracion ")
            tabsTip.findViewById<TextView>(R.id.Spotlight_text).setTextSize(18f)
        }

        override fun onEnded() {
            tabsTip.findViewById<TextView>(R.id.Spotlight_text).setText("")
        }
    })
    .build()


fun ScrapScreenTarget(intro: View): Target = Target.Builder()
    .setOverlay(intro)
    .setOnTargetListener(object : OnTargetListener {
        override fun onStarted() {
            intro.findViewById<TextView>(R.id.Spotlight_text).setTextSize(18f)
            intro.findViewById<TextView>(R.id.Spotlight_text).setText("Hemos encontrado tu producto!, ahora debes especificar el precio a partir del cual te enviaremos una notificacion cuando el producto" +
                    "baje de dicho o precio, o puedes indicar si deseas ser notificado cuando este disponible. Tambien debes de especificar cada cuanto tiempo quieres que se compruebe el precio o stock del producto")

        }

        override fun onEnded() {
            intro.findViewById<TextView>(R.id.Spotlight_text).setText("")
        }
    })
    .build()
