package com.example.instagramclone.di

import com.example.instagramclone.App
import com.example.instagramclone.activities.createUser.EmailFragment
import com.example.instagramclone.activities.createUser.NamePassFragment
import com.example.instagramclone.activities.createUser.RegisterActivity
import com.example.instagramclone.activities.login.LoginActivity
import com.example.instagramclone.activities.profile.EditProfileActivity
import com.example.instagramclone.activities.profile.ProfileActivity
import dagger.Component
import dagger.Provides
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

//FireBaseModule::class,
@Component(modules = arrayOf( AppModule::class, RepositoryModule::class, ViewModelModule::class))


@Singleton
interface AppComponent {

    fun viewModelFactory(): ViewModelFactory

    fun inject(activity: LoginActivity)
    fun inject(activity: EditProfileActivity)

    fun inject(fragment: EmailFragment)
    fun inject(fragment: NamePassFragment)


}