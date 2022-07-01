@file:Suppress("unused")

package com.ecommpay.msdk.ui.navigation


internal sealed class Route(
    private val route: String,
    private val key: String = ""
) {
    object Init : Route(route = "init")
    object Main : Route(route = "main")
    object Result : Route(route = "result")

    override fun toString(): String {
        return when {
            key.isNotEmpty() -> "$route/{$key}"
            else -> route
        }
    }

    fun getPath() = toString()
}