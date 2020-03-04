package com.example.instagramclone.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val app: Application){

    @Provides
    @Singleton
    fun provideContext() : Context = app
}



//import com.example.instagramclone.activities.createUser.EmailViewModel
//import com.example.instagramclone.activities.createUser.NamePassViewModel
//import com.example.instagramclone.activities.login.LoginViewModel
//import com.example.instagramclone.activities.profile.EditProfViewModel
//import com.example.instagramclone.data.*
//import org.koin.android.viewmodel.dsl.viewModel
//import org.koin.dsl.module
//
//
//val repositoryModule = module {
//    single { LoginRepositoryImpl(firebaseAuth = get()) as LoginRepository }
//    single { EmailRepositoryImpl(firebaseAuth = get()) as EmailRepository}
//    single { NamePassRepositoryImpl(firebaseAuth = get()) as NamePassRepository }
//    single { EditProfileRepositoryImpl(firebaseAuth = get()) as EditProfileRepository}
//}
//
//val viewModelModule = module {
//    viewModel { LoginViewModel(loginRepository = get()) }
//    viewModel { EmailViewModel(emailRepository = get()) }
//    viewModel { NamePassViewModel(namePassRepository = get()) }
//    viewModel { EditProfViewModel(editProfRepository = get()) }
//}
//
//val appModules = listOf(repositoryModule, viewModelModule)
//
//
//


