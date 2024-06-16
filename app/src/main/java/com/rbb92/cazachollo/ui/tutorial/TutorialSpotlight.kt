package com.rbb92.cazachollo.ui.tutorial

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.TextView
import com.rbb92.cazachollo.R
import com.takusemba.spotlight.OnTargetListener
import com.takusemba.spotlight.Target
import com.takusemba.spotlight.effet.FlickerEffect
import com.takusemba.spotlight.shape.Circle
import com.takusemba.spotlight.shape.RoundedRectangle


fun PresentationTarget(intro: View, context: Context): Target = Target.Builder()
        .setOverlay(intro)
        .setOnTargetListener(object : OnTargetListener {
            override fun onStarted() {
                intro.findViewById<TextView>(R.id.Spotlight_text).setText(context.getString(R.string.tutorial_presentation_target))
            }

            override fun onEnded() {
                intro.findViewById<TextView>(R.id.Spotlight_text).setText("")
            }
        })
        .build()


fun SearchUrlTarget(searchTip: View, screenWidth:Int, screenHeight:Int, context: Context) = Target.Builder()
    .setAnchor((screenWidth/2).toFloat(),(screenHeight/2).toFloat()-220f)
    .setShape(RoundedRectangle(550f, screenWidth.toFloat()-120f,20f))
    .setOverlay(searchTip)
//        .setEffect(RippleEffect(100f, 200f, argb(30, 124, 255, 90)))
    .setEffect(FlickerEffect(45f, Color.argb(30, 124, 255, 90),500L, DecelerateInterpolator(1f),5))
    .setOnTargetListener(object : OnTargetListener {
        override fun onStarted() {
            searchTip.findViewById<TextView>(R.id.Spotlight_text).setText(context.getString(R.string.tutorial_search_url_target))
            searchTip.findViewById<TextView>(R.id.Spotlight_text).setTextSize(18f)
        }

        override fun onEnded() {
            searchTip.findViewById<TextView>(R.id.Spotlight_text).setText("")
        }
    })
    .build()


fun SelectStoreTarget(searchTip: View, screenWidth:Int, screenHeight:Int, context: Context) = Target.Builder()
    .setAnchor((screenWidth/2).toFloat(),(screenHeight/2).toFloat()+260)
    .setShape(RoundedRectangle(450f, screenWidth.toFloat()-120f,20f))
    .setOverlay(searchTip)
//        .setEffect(RippleEffect(100f, 200f, argb(30, 124, 255, 90)))
    .setEffect(FlickerEffect(45f, Color.argb(30, 124, 255, 90),500L,DecelerateInterpolator(1f),5))
    .setOnTargetListener(object : OnTargetListener {
        override fun onStarted() {
            searchTip.findViewById<TextView>(R.id.Spotlight_text).setText(context.getString(R.string.tutorial_store_select_target))
            searchTip.findViewById<TextView>(R.id.Spotlight_text).setTextSize(18f)
        }

        override fun onEnded() {
            searchTip.findViewById<TextView>(R.id.Spotlight_text).setText("")
        }
    })
    .build()

fun SearchStoreTarget(searchTip: View, screenWidth:Int, screenHeight:Int, context: Context) = Target.Builder()
    .setAnchor((screenWidth/2).toFloat(),(screenHeight/2).toFloat()-100)
    .setShape(Circle(180f))
    .setOverlay(searchTip)
//        .setEffect(RippleEffect(100f, 200f, argb(30, 124, 255, 90)))
    .setEffect(FlickerEffect(45f, Color.argb(30, 124, 255, 90),500L,DecelerateInterpolator(1f),5))
    .setOnTargetListener(object : OnTargetListener {
        override fun onStarted() {
            searchTip.findViewById<TextView>(R.id.Spotlight_text).setText(context.getString(R.string.tutorial_search_store_target))
            searchTip.findViewById<TextView>(R.id.Spotlight_text).setTextSize(18f)
        }

        override fun onEnded() {
            searchTip.findViewById<TextView>(R.id.Spotlight_text).setText("")
        }
    })
    .build()

fun ScrapTabTarget(tabsTip: View, screenWidth:Int, screenHeight:Int,context: Context) = Target.Builder()
    .setAnchor((screenWidth/3).toFloat()-200f,(screenHeight).toFloat())
    .setShape(Circle(160f))
    .setOverlay(tabsTip)
//        .setEffect(RippleEffect(100f, 200f, argb(30, 124, 255, 90)))
    .setEffect(FlickerEffect(45f, Color.argb(30, 124, 255, 90),500L,DecelerateInterpolator(1f),5))
    .setOnTargetListener(object : OnTargetListener {
        override fun onStarted() {
            tabsTip.findViewById<TextView>(R.id.Spotlight_text).setText(context.getString(R.string.tutorial_scrap_tab_target))
            tabsTip.findViewById<TextView>(R.id.Spotlight_text).setTextSize(18f)
        }

        override fun onEnded() {
            tabsTip.findViewById<TextView>(R.id.Spotlight_text).setText("")
        }
    })
    .build()

fun ListScrapTabTarget(tabsTip: View, screenWidth:Int, screenHeight:Int, context: Context) = Target.Builder()
    .setAnchor((screenWidth/3).toFloat()*2-200f,(screenHeight).toFloat())
    .setShape(Circle(160f))
    .setOverlay(tabsTip)
//        .setEffect(RippleEffect(100f, 200f, argb(30, 124, 255, 90)))
    .setEffect(FlickerEffect(45f, Color.argb(30, 124, 255, 90),500L,DecelerateInterpolator(1f),5))
    .setOnTargetListener(object : OnTargetListener {
        override fun onStarted() {
            tabsTip.findViewById<TextView>(R.id.Spotlight_text).setText(context.getString(R.string.tutorial_scrap_list_tab_target))
            tabsTip.findViewById<TextView>(R.id.Spotlight_text).setTextSize(18f)
        }

        override fun onEnded() {
            tabsTip.findViewById<TextView>(R.id.Spotlight_text).setText("")
        }
    })
    .build()

fun SettingTabTarget(tabsTip: View, screenWidth:Int, screenHeight:Int, context: Context) = Target.Builder()
    .setAnchor((screenWidth).toFloat()-200f,(screenHeight).toFloat())
    .setShape(Circle(160f))
    .setOverlay(tabsTip)
//        .setEffect(RippleEffect(100f, 200f, argb(30, 124, 255, 90)))
    .setEffect(FlickerEffect(45f, Color.argb(30, 124, 255, 90),500L,DecelerateInterpolator(1f),5))
    .setOnTargetListener(object : OnTargetListener {
        override fun onStarted() {
            tabsTip.findViewById<TextView>(R.id.Spotlight_text).setText(context.getString(R.string.tutorial_setting_tab_target))
            tabsTip.findViewById<TextView>(R.id.Spotlight_text).setTextSize(18f)
        }

        override fun onEnded() {
            tabsTip.findViewById<TextView>(R.id.Spotlight_text).setText("")
        }
    })
    .build()


fun ScrapScreenTarget(intro: View, context: Context): Target = Target.Builder()
    .setOverlay(intro)
    .setOnTargetListener(object : OnTargetListener {
        override fun onStarted() {
            intro.findViewById<TextView>(R.id.Spotlight_text).setTextSize(18f)
            intro.findViewById<TextView>(R.id.Spotlight_text).setText(context.getString(R.string.tutorial_scrap_screen_target))

        }

        override fun onEnded() {
            intro.findViewById<TextView>(R.id.Spotlight_text).setText("")
        }
    })
    .build()
