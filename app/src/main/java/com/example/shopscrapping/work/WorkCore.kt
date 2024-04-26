package com.example.shopscrapping.work

import android.content.Context
import android.content.SharedPreferences
import com.example.shopscrapping.bbdd.PreferencesManager
import com.example.shopscrapping.notifications.notificationGoal


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
        val diasDesdePublicaion = (inputData.currentDate - inputData.initialDate) / (1000 * 60 * 60 * 24)
        val diasDesdeUltimaNotificacion = (inputData.currentDate - inputData.latestNotificationDate) / (1000 * 60 * 60 * 24)
        if (diasDesdePublicaion >= 3)
        {
            if(diasDesdeUltimaNotificacion >= 1)
            {
                //Si se han generado mas de 6 notificaciones para ese producto, parar con notificaciones secundarias Ligeras
                if(inputData.numberNotifications < 5)
                {
                    //NOTIFICACIONES Ligeras
                    //Si precio inicial a bajado
                    if((inputData.initialPrice > inputData.currentPrice)
                        and (inputData.currentPrice>0.0f)
                        and ((inputData.currentPrice/inputData.initialPrice)<=0.9f)) //bajada del 10% del precio
                    {
                        secondaryGoalNotification(inputData.name,"Bajada de precio!",
                            inputData.urlReferido, inputData.imgSrc, inputData.currentPrice)
                        return
                    }
                    if(inputData.isStock and (inputData.currentMinPrice>0.0f))
                    {

                        secondaryGoalNotification(inputData.name,"Disponible de segunda mano" ,
                            inputData.urlReferido, inputData.imgSrc, inputData.currentMinPrice)
                        return
                    }
                }
                else
                {
                    //NOTIFICACIONES Medias
                    if((!inputData.isAllPrice) and (inputData.currentMinPrice <= inputData.alertPrice) and (inputData.currentMinPrice>0.0f))
                    {
                        //objetivo cumplido pero para productos de ocasion
                        secondaryGoalNotification(inputData.name,"Bajada de precio en ocasión",
                            inputData.urlReferido, inputData.imgSrc, inputData.currentMinPrice)
                        return
                    }
                    if (diasDesdeUltimaNotificacion >= 5)
                    {
                        if((inputData.initialPrice > inputData.currentPrice)
                            and (inputData.currentPrice>0.0f)
                            and ((inputData.currentPrice/inputData.initialPrice)<=0.9f)) //bajada del 10% del precio
                        {
                            secondaryGoalNotification(inputData.name,"Bajada de precio" ,
                                inputData.urlReferido, inputData.imgSrc, inputData.currentPrice)
                            return
                        }
                    }

                }

            }

        }
        if ((!inputData.isAllPrice) and (inputData.latestPrice == 0.0f) and (inputData.currentPrice > 0.0f))
        {
            secondaryGoalNotification(inputData.name,"Vuelve a estar disponible!" ,
                inputData.urlReferido, inputData.imgSrc, inputData.currentPrice)
            return
        }
        if ((inputData.isAllPrice) and (((inputData.latestPrice == 0.0f) and  (inputData.currentPrice > 0.0f)) or
                    ((inputData.latestMinPrice == 0.0f) and  (inputData.currentMinPrice > 0.0f))))
        {
            secondaryGoalNotification(inputData.name,"Vuelve a estar disponible!" ,
                inputData.urlReferido, inputData.imgSrc, inputData.currentMinPrice)
            return
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
                    mainGoalNotification(inputData.name, "Producto disponible!",
                        inputData.urlReferido, inputData.imgSrc, inputData.currentPrice)
                    return true
                }
            }
            else
            {
                if (inputData.currentMinPrice > 0.0f || inputData.currentPrice > 0.0f)
                {
                    //GENERAMOS NOTIFICACION!!
                    mainGoalNotification(inputData.name, "Producto disponible!",
                        inputData.urlReferido, inputData.imgSrc, inputData.currentMinPrice)
                    return true
                }
            }
        }
        //si buscaba bajada de precio
        else
        {
            if(!inputData.isAllPrice)
            {
                if((inputData.currentPrice <= inputData.alertPrice) and (inputData.currentPrice > 0.0f))
                {
                    //GENERAMOS NOTIFICACION!!
                    mainGoalNotification(inputData.name, "Tu producto ha bajado del limite!",
                        inputData.urlReferido, inputData.imgSrc, inputData.currentPrice)
                    return true
                }
            }
            else
            {
                if ((inputData.currentMinPrice <= inputData.alertPrice) and (inputData.currentMinPrice > 0.0f) ||
                    (inputData.currentPrice <= inputData.alertPrice) and (inputData.currentPrice > 0.0f))
                {
                    //GENERAMOS NOTIFICACION!!
                    mainGoalNotification(inputData.name, "Tu producto ha bajado del limite!",
                        inputData.urlReferido, inputData.imgSrc, inputData.currentMinPrice)
                    return true
                }
            }
        }
        return false
    }

    private fun mainGoalNotification(message: String, header:String, url:String, url_img: String, price:Float)
    {
        //TODO price.toString(), llamar a metodo que traduzca float a string de precio
        notificationGoal(context, message, "\uD83C\uDF89 HURRAY! ${header}", url, url_img, price.toString())
        notificationGenerated = true
        PreferencesManager(context).addBugdeListStar()
    }
    private fun secondaryGoalNotification(message: String, header:String, url:String, url_img: String, price:Float)
    {
        //TODO price.toString(), llamar a metodo que traduzca float a string de precio
        if(!PreferencesManager(context).isSecundaryNotificationsDisabled())
        {
            notificationGoal(context, message, "🎉 Novedad! ${header}", url, url_img, price.toString())
            notificationGenerated = true
            PreferencesManager(context).addBugdeListPoint()
        }
    }

}