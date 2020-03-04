package com.example.instagramclone

import android.app.Activity
import android.app.Application
import com.example.instagramclone.di.AppComponent
import com.example.instagramclone.di.AppModule
import com.example.instagramclone.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.HasActivityInjector


class App : Application(){

    lateinit var appComponent: AppComponent

    private fun initDagger(app: App): AppComponent =
        DaggerAppComponent.builder()
            .appModule(AppModule(app))
            .build()


    override fun onCreate() {
        super.onCreate()
        appComponent = initDagger(this)
    }
}


//import android.app.Application
//import com.example.instagramclone.di.appModules
//import org.koin.android.ext.koin.androidContext
//import org.koin.core.context.startKoin
//
//class App : Application() {
//    override fun onCreate() {
//        super.onCreate()
//        startKoin {
//            androidContext(this@App)
//            modules(appModules)
//        }
//    }
//}