package com.example.instagramclone.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.instagramclone.activities.createUser.EmailViewModel
import com.example.instagramclone.activities.createUser.NamePassViewModel
import com.example.instagramclone.activities.login.LoginViewModel
import com.example.instagramclone.activities.profile.EditProfViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    internal abstract fun loginViewModel(viewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EditProfViewModel::class)
    internal abstract fun editProfViewModel(viewModel: EditProfViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EmailViewModel::class)
    internal abstract fun emailViewModel(viewModel: EmailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NamePassViewModel::class)
    internal abstract fun namePassViewModel(viewModel: NamePassViewModel): ViewModel

}