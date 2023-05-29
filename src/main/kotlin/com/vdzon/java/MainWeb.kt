package com.vdzon.java

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
    fun start() {
        log.info("Starting backend..")
        app = Javalin.create { config: JavalinConfig ->
            config.enableWebjars()
            config.addStaticFiles("/html")
            config.accessManager(this::accessManager)
        }
        app!!.get("/", VueComponent("<play></play>"))
        app!!.get("/play", VueComponent("<play></play>"), setOf(RouteRole.SPECTATOR))
        app!!.get("/login", VueComponent("<login></login>"), setOf(RouteRole.SPECTATOR))
        app!!.post("/api/login", { ctx: Context ->  login(ctx)})
        app!!.get("/api/logout", { ctx: Context ->  logout(ctx)})
        app!!.get("/api/userdata", { ctx: Context ->  ctx?.json(getUserData(ctx))})

        RestEndpoints().initRestEndpoints(app!!)
        log.info("Starting server")
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
        "1961" to RouteRole.ADMIN
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

