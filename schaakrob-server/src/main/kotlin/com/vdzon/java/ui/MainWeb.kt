package com.vdzon.java.ui

import com.vdzon.java.RestEndpoints
import com.vdzon.java.robitapi.RobotAansturing
import com.vdzon.java.robotclient.RobotAansturingClient
import com.vdzon.java.robotimpl.RobotAansturingImpl
import com.vdzon.java.schaakspel.Schaakspel
import io.javalin.Javalin
import io.javalin.core.JavalinConfig
import io.javalin.core.security.Role
import io.javalin.plugin.rendering.vue.VueComponent
import org.slf4j.LoggerFactory
import io.javalin.http.Context

import io.javalin.http.Handler

class MainWeb {
    private val log = LoggerFactory.getLogger(MainWeb::class.java)

    var app: Javalin? = null
    fun start(schaakbord: Boolean) {
        log.info("Starting backend..")
        app = Javalin.create { config: JavalinConfig ->
            config.enableWebjars()
            config.addStaticFiles("/html")
            config.accessManager(this::accessManager)
        }
        app!!.get("/", VueComponent("<play></play>"), setOf(RouteRole.SPECTATOR))
        app!!.get("/demo", VueComponent("<demo></demo>"), setOf(RouteRole.ADMIN))
        app!!.get("/play", VueComponent("<play></play>"), setOf(RouteRole.SPECTATOR))
        app!!.get("/manual", VueComponent("<manual></manual>"), setOf(RouteRole.ADMIN))
        app!!.get("/rebuild", VueComponent("<rebuild></rebuild>"), setOf(RouteRole.ADMIN))
        app!!.get("/login", VueComponent("<login></login>"), setOf(RouteRole.SPECTATOR))
        app!!.post("/api/login", { ctx: Context ->  login(ctx)})
        app!!.get("/api/logout", { ctx: Context ->  logout(ctx)})
        app!!.get("/api/userdata", { ctx: Context ->  ctx?.json(getUserData(ctx))})

        var robotAansturing: RobotAansturing? = null
        robotAansturing = if (schaakbord) {
            RobotAansturingImpl()
        } else {
            RobotAansturingClient("http://192.168.178.88:8080")
        }
        val schaakspel = Schaakspel(robotAansturing)

        if (robotAansturing is RobotAansturingImpl){
            robotAansturing.setSchaakspel(schaakspel) // deze dependency op betere manier oplossen
        }

        RestEndpoints().initRestEndpoints(app, robotAansturing, schaakspel)
        log.info("Starting server")
        robotAansturing.bootsound()
        app!!.start(8080)
    }

    private fun getUserData(ctx: Context): User {
        return User(ctx.userRole.name)


    }

    private fun login(ctx: Context) {
        val accessCode: String = ctx.body()
        ctx.cookieStore("auth",accessCode)
    }

    private fun logout(ctx: Context) {
        ctx.cookieStore("auth","")
        ctx.clearCookieStore()

    }

    fun stop() {
        app!!.stop()
    }

    fun accessManager(handler: Handler, ctx: Context, permittedRoles: Set<Role>) {
        val roles = ctx.userRole
        when {
            permittedRoles.contains(RouteRole.SPECTATOR) || permittedRoles.isEmpty()->
                handler.handle(ctx)
            ctx.userRole in permittedRoles  ->
                handler.handle(ctx)
            else ->
                ctx.status(401).json("Unauthorized")
        }
    }
    private val Context.userRole: RouteRole
        get() = this.getAuth()?.let { accesscode ->
            userRoleMap[accesscode] ?: RouteRole.SPECTATOR
        } ?: RouteRole.SPECTATOR

    private val userRoleMap = hashMapOf(
        "1961" to RouteRole.ADMIN,
        "1976" to RouteRole.PLAYER
    )


    private fun Context.getAuth(): String? {
        try {
            val accessCode = this.cookieStore<String>("auth")
            return accessCode
        }
        catch (e: Exception){
            println("Error loading auth:"+e.message)
            return null
        }
    }

    data class User(val role: String)

}

