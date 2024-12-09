package com.example.vociapp.di

import com.example.vociapp.data.remote.FirestoreDataSource
import com.example.vociapp.data.repository.HomelessRepository
import com.example.vociapp.data.repository.RequestRepository
import com.example.vociapp.data.repository.VolunteerRepository
import com.example.vociapp.ui.viewmodels.AuthViewModel
import com.example.vociapp.ui.viewmodels.HomelessViewModel
import com.example.vociapp.ui.viewmodels.RequestViewModel
import com.example.vociapp.ui.viewmodels.VolunteerViewModel
import com.google.firebase.firestore.FirebaseFirestore

class ServiceLocator(firestore: FirebaseFirestore) {
    companion object {
        private lateinit var instance: ServiceLocator
        private lateinit var authViewModel: AuthViewModel

        fun initialize(firestore: FirebaseFirestore) {
            instance = ServiceLocator(firestore)
            authViewModel = AuthViewModel()
        }

        fun getInstance(): ServiceLocator {
            if (!::instance.isInitialized) {
                throw IllegalStateException("ServiceLocator must be initialized first")
            }
            return instance
        }


    }

    fun getRequestRepository(): RequestRepository = instance.requestRepository
    fun getRequestViewModel(): RequestViewModel = instance.requestViewModel

    fun getHomelessRepository(): HomelessRepository = instance.homelessRepository
    fun getHomelessViewModel(): HomelessViewModel = instance.homelessViewModel

    fun getVolunteerRepository(): VolunteerRepository = instance.volunteerRepository
    fun getVolunteerViewModel(): VolunteerViewModel = instance.volunteerViewModel
    fun getAuthViewModel(): AuthViewModel = authViewModel

    private val volunteerRepository: VolunteerRepository = VolunteerRepository(FirestoreDataSource(firestore))
    private val volunteerViewModel: VolunteerViewModel = VolunteerViewModel(volunteerRepository)

    private val requestRepository: RequestRepository = RequestRepository(FirestoreDataSource(firestore))
    private val requestViewModel: RequestViewModel = RequestViewModel(requestRepository)

    private val homelessRepository: HomelessRepository = HomelessRepository(FirestoreDataSource(firestore))
    private val homelessViewModel: HomelessViewModel = HomelessViewModel(homelessRepository)


}

