package com.example.instagramclone.di

import com.example.instagramclone.data.*
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class RepositoryModule {

    @Singleton
    @Provides
    fun provideLoginRepository() : LoginRepository {
        return LoginRepositoryImpl(firebaseAuth = FirebaseAuth.getInstance())
    }

    @Singleton
    @Provides
    fun provideEmailRepository() : EmailRepository {
        return EmailRepositoryImpl(firebaseAuth = FirebaseAuth.getInstance())
    }

    @Singleton
    @Provides
    fun provideEditProfileRepository() : EditProfileRepository {
        return EditProfileRepositoryImpl(firebaseAuth = FirebaseAuth.getInstance())
    }


    @Singleton
    @Provides
    fun provideNamePassRepository() : NamePassRepository {
        return NamePassRepositoryImpl(FirebaseAuth.getInstance())
    }

}