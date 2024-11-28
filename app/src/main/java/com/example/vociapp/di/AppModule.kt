package com.example.vociapp.di

import com.example.vociapp.data.remote.FirestoreDataSource
import com.example.vociapp.data.repository.RequestRepositoryImpl
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirestoreDataSource(firestore: FirebaseFirestore): FirestoreDataSource {
        return FirestoreDataSource(firestore)
    }

    @Provides
    @Singleton
    fun provideRequestRepository(firestoreDataSource: FirestoreDataSource): RequestRepositoryImpl {
        return RequestRepositoryImpl(firestoreDataSource)
    }
}