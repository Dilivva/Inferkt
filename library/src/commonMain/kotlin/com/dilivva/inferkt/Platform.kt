package com.dilivva.inferkt

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform