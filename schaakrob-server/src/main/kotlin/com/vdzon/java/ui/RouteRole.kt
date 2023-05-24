package com.vdzon.java.ui

import io.javalin.core.security.Role

enum class RouteRole : Role { SPECTATOR, PLAYER, ADMIN }