package com.vdzon.java

import io.javalin.Javalin
import io.javalin.http.Context

class RestEndpoints(val controller: Controller) {

    fun initRestEndpoints(app: Javalin) {
        app["/api/game/load", { ctx: Context? ->
            load(ctx)
        }]
        app["/api/game/plus5", { ctx: Context? ->
            plus5(ctx)
        }]
        app["/api/game/min5", { ctx: Context? ->
            min5(ctx)
        }]
    }

    private fun min5(ctx: Context?) {
        controller.klepClose()
        ctx?.json(GroentetuinStatus("klep status", "min 5"))
    }

    private fun plus5(ctx: Context?) {
        controller.klepOpen()
        ctx?.json(GroentetuinStatus("klep status", "plus 5"))
    }

    private fun load(ctx: Context?) {
        ctx?.json(GroentetuinStatus("klep status", "veel regen"))
    }

}


