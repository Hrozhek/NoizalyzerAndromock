package com.github.hrozhek.noizalyzerandromock

import java.util.*

class AppContext private constructor() {

    var server: String? = null
    var port = 0
    var endpoint: String? = null
    var ws: String? = null
    var connectionClient: ConnectionClient? = null
    var readTimeout: Timeout? = null
    var writeTimeout: Timeout? = null
    var id: UUID? = null

    companion object {
        val instance = AppContext()
    }
}