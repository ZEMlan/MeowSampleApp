package ru.zemlyanaya.meowsampleapp

import android.content.Context
import meow.MeowApp
import meow.MeowController
import meow.bindMeow
import meow.ktx.isNightModeFromSettings
import meow.ktx.logD
import meow.meowModule
import org.kodein.di.Kodein
import org.kodein.di.android.x.BuildConfig
import org.kodein.di.android.x.androidXModule
import org.kodein.di.direct
import org.kodein.di.erased.bind
import org.kodein.di.erased.singleton


class App : MeowApp() {

    // Layout Direction would be set automatically by Android System.
    // Default language is Russian
    override fun getLanguage(context: Context?) = "rus"
    // App's theme is set by Android System Light/Dark (Day/Night) mode.
    override fun getTheme(context: Context?) =
        if (context.isNightModeFromSettings()) MeowController.Theme.NIGHT else  MeowController.Theme.DAY

    override fun onCreate() {
        super.onCreate()
        bindMeow {
            it.isDebugMode = BuildConfig.DEBUG // Now logs are only allowed in Debug Mode
            it.onException = { exception -> // Only non-fatal error
                logD(m = exception.message.toString())
            }
        }
    }

    // Create a kodein module.
    val appModule = Kodein.Module("App Module", false) {
        // Provide object of SomeOfClass(such as View Models) in Kodein with bind() function.
        // bind() from singleton { SomeOfClass(instance()) }
    }

    // Source is `KodeinAware` interface.
    override val kodein = Kodein.lazy {
        bind() from singleton { kodein.direct }
        bind() from singleton { this@App }
        import(androidXModule(this@App))
        import(meowModule) // Important
        import(appModule)
    }
}