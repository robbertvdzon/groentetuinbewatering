//package com.vdzon.java
//
//import io.javalin.Javalin
//import io.javalin.http.Context
//
//class RestEndpoints(val controller: Controller) {
//
//    fun initRestEndpoints(app: Javalin) {
//        app["/api/game/load", { ctx: Context? ->
//            load(ctx)
//        }]
//        app["/api/game/plus1", { ctx: Context? ->
//            plus1(ctx)
//        }]
//        app["/api/game/plus10", { ctx: Context? ->
//            plus10(ctx)
//        }]
//        app["/api/game/min1", { ctx: Context? ->
//            min1(ctx)
//        }]
//        app["/api/game/min10", { ctx: Context? ->
//            min10(ctx)
//        }]
//        app["/api/game/dicht", { ctx: Context? ->
//            dicht(ctx)
//        }]
//        app["/api/game/reload", { ctx: Context? ->
//            reload(ctx)
//        }]
//    }
//
//    private fun reload(ctx: Context?) {
//        val displayData = controller.getDisplayData()
//        ctx?.json(GroentetuinStatus(displayData))
//    }
//
//    private fun min1(ctx: Context?) {
//        controller.encoderDown()
//        val displayData = controller.getDisplayData()
//        ctx?.json(GroentetuinStatus(displayData))
//    }
//
//    private fun min10(ctx: Context?) {
//        controller.encoderDown(10)
//        val displayData = controller.getDisplayData()
//        ctx?.json(GroentetuinStatus(displayData))
//    }
//
//    private fun plus1(ctx: Context?) {
//        controller.encoderUp()
//        val displayData = controller.getDisplayData()
//        ctx?.json(GroentetuinStatus(displayData))
//    }
//
//    private fun plus10(ctx: Context?) {
//        controller.encoderUp(10)
//        val displayData = controller.getDisplayData()
//        ctx?.json(GroentetuinStatus(displayData))
//    }
//
//    private fun dicht(ctx: Context?) {
//        controller.dicht()
//        val displayData = controller.getDisplayData()
//        ctx?.json(GroentetuinStatus(displayData))
//    }
//
//
//    private fun load(ctx: Context?) {
//        val displayData = controller.getDisplayData()
//        ctx?.json(GroentetuinStatus(displayData))
//    }
//
//}
//
//
