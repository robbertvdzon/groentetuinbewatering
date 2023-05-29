package com.vdzon.java

import io.javalin.Javalin
import io.javalin.http.Context

class RestEndpoints {

    fun initRestEndpoints(app: Javalin) {
        app["/api/game/load", { ctx: Context? -> ctx?.json(GroentetuinStatus("klep status", "veel regen")) }]
        app["/api/game/plus5", { ctx: Context? -> ctx?.json(GroentetuinStatus("klep status", "plus 5")) }]
        app["/api/game/min5", { ctx: Context? -> ctx?.json(GroentetuinStatus("klep status", "min 5")) }]
    }

}


