package com.example.shopscrapping.work

import android.content.Context
import android.content.SharedPreferences
import com.example.shopscrapping.bbdd.PreferencesManager
import com.example.shopscrapping.notifications.notificationMainGoal
import com.example.shopscrapping.notifications.notificationSecondaryGoal


//TODO esta clase recibe un context y un RequirementsForWork y toma la decision correspondiente
class WorkCore(val context: Context, val inputData: RequirementsForWork) {
    var notificationGenerated = false
    fun makeDecision():Boolean {
        //Si se cumplen los objetivos que se buscan, se notifica al usuario
        if(mainGoal())
            return notificationGenerated
        //Si no se cumplen los objetivos que se buscan, analizamos objetivos secundarios
        secondaryGoal()
        return notificationGenerated

    }

    private fun secondaryGoal() {
        //Minimo 3 dias desde publicacion hasta empezar a emitir notificaciones secundarias.
        // Convertir milisegundos a
        val diasDesdePublicaion = inputData.currentDate - inputData.initialDate / (1000 * 60 * 60 * 24)
        val diasDesdeUltimaNotificacion = inputData.currentDate - inputData.latestNotificationDate / (1000 * 60 * 60 * 24)
        if (diasDesdePublicaion >= 3)
        {
            if(diasDesdeUltimaNotificacion >= 1)
            {
                //Si se han generado mas de 6 notificaciones para ese producto, parar con notificaciones secundarias Ligeras
                if(inputData.numberNotifications < 5)
                {
                    //NOTIFICACIONES Ligeras
                    //Si precio inicial a bajado
                    if(inputData.initialPrice > inputData.currentPrice)
                    {
                        secondaryGoalNotification(inputData.name,"Bajada de precio!", inputData.urlReferido)
                        return
                    }
                    if(inputData.isStock and (inputData.currentMinPrice>0.0f))
                    {

                        secondaryGoalNotification(inputData.name,"Disponible de segunda mano" ,inputData.urlReferido)
                        return
                    }
                }
                else
                {
                    //NOTIFICACIONES Medias
                    if(!inputData.isAllPrice and (inputData.currentMinPrice <= inputData.alertPrice))
                    {
                        //objetivo cumplido pero para productos de ocasion
                        secondaryGoalNotification(inputData.name,"Bajada de precio en ocasión" ,inputData.urlReferido)
                    }
                    if (diasDesdeUltimaNotificacion >= 5)
                    {
                        if(inputData.initialPrice > inputData.currentPrice)
                        {
                            secondaryGoalNotification(inputData.name,"Bajada de precio" ,inputData.urlReferido)
                            return
                        }
                    }

                }
            }

        }
    }

    private fun mainGoal(): Boolean
    {
        //si buscaba stock
        if(inputData.isStock)
        {
            if(!inputData.isAllPrice)
            {
                if(inputData.currentPrice > 0.0f)
                {
                    //GENERAMOS NOTIFICACION!!
                    mainGoalNotification(inputData.name, "Producto disponible!",inputData.urlReferido)
                    return true
                }
            }
            else
            {
                if (inputData.currentMinPrice > 0.0f || inputData.currentPrice > 0.0f)
                {
                    //GENERAMOS NOTIFICACION!!
                    mainGoalNotification(inputData.name, "Producto disponible!",inputData.urlReferido)
                    return true
                }
            }
        }
        //si buscaba bajada de precio
        else
        {
            if(!inputData.isAllPrice)
            {
                if(inputData.currentPrice <= inputData.alertPrice)
                {
                    //GENERAMOS NOTIFICACION!!
                    mainGoalNotification(inputData.name, "Tu producto ha bajado del limite!",inputData.urlReferido)
                    return true
                }
            }
            else
            {
                if (inputData.currentMinPrice <= inputData.alertPrice || inputData.currentPrice <= inputData.alertPrice)
                {
                    //GENERAMOS NOTIFICACION!!
                    mainGoalNotification(inputData.name, "Tu producto ha bajado del limite!",inputData.urlReferido)
                    return true
                }
            }
        }
        return false
    }

    private fun mainGoalNotification(message: String, header:String, url:String)
    {
        notificationMainGoal(context, message, header, url)
        notificationGenerated = true
        PreferencesManager(context).addBugdeListStar()
    }
    private fun secondaryGoalNotification(message: String, header:String, url:String)
    {
        notificationSecondaryGoal(context, message, header, url)
        notificationGenerated = true
        PreferencesManager(context).addBugdeListPoint()
    }

}