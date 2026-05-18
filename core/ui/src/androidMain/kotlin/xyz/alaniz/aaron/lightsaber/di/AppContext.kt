package xyz.alaniz.aaron.lightsaber.di

import android.content.Context

@JvmInline
value class AppContext(val value: Context) {
    init {
        require(value == value.applicationContext) {
            "context must be an application context"
        }
    }
}