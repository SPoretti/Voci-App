package com.example.vociapp.di

import android.content.Context
import com.example.vociapp.data.local.RoomDataSource
import com.example.vociapp.data.local.database.VociAppRoomDatabase
import com.example.vociapp.data.remote.FirestoreDataSource
import com.example.vociapp.data.repository.HomelessRepository
import com.example.vociapp.data.repository.RequestRepository
import com.example.vociapp.data.repository.UpdatesRepository
import com.example.vociapp.data.repository.VolunteerRepository
import com.example.vociapp.data.util.NetworkManager
import com.example.vociapp.ui.viewmodels.AuthViewModel
import com.example.vociapp.ui.viewmodels.HomelessViewModel
import com.example.vociapp.ui.viewmodels.RequestViewModel
import com.example.vociapp.ui.viewmodels.UpdatesViewModel
import com.example.vociapp.ui.viewmodels.VolunteerViewModel
import com.google.firebase.firestore.FirebaseFirestore

class ServiceLocator(context: Context, firestore: FirebaseFirestore) {
    companion object {
        private lateinit var instance: ServiceLocator
        private lateinit var authViewModel: AuthViewModel

        fun initialize(context: Context, firestore: FirebaseFirestore) {
            instance = ServiceLocator(context, firestore)
            authViewModel = AuthViewModel()
        }

        fun getInstance(): ServiceLocator {
            if (!::instance.isInitialized) {
                throw IllegalStateException("ServiceLocator must be initialized first")
            }
            return instance
        }
    }

    private val roomDatabase: VociAppRoomDatabase by lazy {
        VociAppRoomDatabase.getDatabase(context)
    }

    private val syncQueueDao = roomDatabase.syncQueueDao()

    private val roomDataSource: RoomDataSource = RoomDataSource(
        homelessDao = roomDatabase.homelessDao(),
        volunteerDao = roomDatabase.volunteerDao(),
        requestDao = roomDatabase.requestDao(),
        updateDao = roomDatabase.updateDao(),
        syncQueueDao = syncQueueDao
    )

    private val networkManager: NetworkManager = NetworkManager(
        context = context
    )

    // Repositories and ViewModels
    private val homelessRepository: HomelessRepository = HomelessRepository(
        firestoreDataSource = FirestoreDataSource(firestore),
        roomDataSource = roomDataSource,
        networkManager = networkManager,
        syncQueueDao = syncQueueDao
    )

    private val homelessViewModel: HomelessViewModel = HomelessViewModel(homelessRepository)

    private val volunteerRepository: VolunteerRepository = VolunteerRepository(FirestoreDataSource(firestore), roomDataSource, networkManager, syncQueueDao)
    private val volunteerViewModel: VolunteerViewModel = VolunteerViewModel(volunteerRepository)

    private val requestRepository: RequestRepository = RequestRepository(FirestoreDataSource(firestore), roomDataSource, networkManager, syncQueueDao)
    private val requestViewModel: RequestViewModel by lazy {
        RequestViewModel(requestRepository)
    }
    private val updatesRepository: UpdatesRepository = UpdatesRepository(FirestoreDataSource(firestore), roomDataSource, networkManager, syncQueueDao)
    private val updatesViewModel: UpdatesViewModel by lazy{
        UpdatesViewModel(updatesRepository)
    }

    // Getters for repositories and view models
    fun obtainRequestRepository(): RequestRepository = instance.requestRepository
    fun obtainRequestViewModel(): RequestViewModel = instance.requestViewModel

    fun obtainUpdatesRepository(): UpdatesRepository = instance.updatesRepository
    fun obtainUpdatesViewModel(): UpdatesViewModel = instance.updatesViewModel

    fun obtainHomelessRepository(): HomelessRepository = instance.homelessRepository
    fun obtainHomelessViewModel(): HomelessViewModel = instance.homelessViewModel

    fun obtainVolunteerRepository(): VolunteerRepository = instance.volunteerRepository
    fun obtainVolunteerViewModel(): VolunteerViewModel = instance.volunteerViewModel

    fun obtainAuthViewModel(): AuthViewModel = authViewModel

    fun fetchAllData(){
        homelessViewModel.fetchHomelesses()
        volunteerViewModel.fetchVolunteers()
        requestViewModel.fetchRequests()
        updatesViewModel.fetchUpdates()
    }
}


