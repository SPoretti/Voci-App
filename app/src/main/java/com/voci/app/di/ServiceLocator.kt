package com.voci.app.di

import android.content.Context
import com.voci.app.data.local.RoomDataSource
import com.voci.app.data.local.database.VociAppRoomDatabase
import com.voci.app.data.remote.FirestoreDataSource
import com.voci.app.data.repository.HomelessRepository
import com.voci.app.data.repository.MapboxRepository
import com.voci.app.data.repository.RequestRepository
import com.voci.app.data.repository.UpdatesRepository
import com.voci.app.data.repository.VolunteerRepository
import com.voci.app.data.util.NetworkManager
import com.voci.app.ui.viewmodels.AuthViewModel
import com.voci.app.ui.viewmodels.HomelessViewModel
import com.voci.app.ui.viewmodels.MapboxViewModel
import com.voci.app.ui.viewmodels.RequestViewModel
import com.voci.app.ui.viewmodels.UpdatesViewModel
import com.voci.app.ui.viewmodels.VolunteerViewModel
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

    private val roomDataSource: RoomDataSource = RoomDataSource(
        homelessDao = roomDatabase.homelessDao(),
        volunteerDao = roomDatabase.volunteerDao(),
        preferenceDao = roomDatabase.PreferenceDao(),
        requestDao = roomDatabase.requestDao(),
        updateDao = roomDatabase.updateDao(),
        syncQueueDao = roomDatabase.syncQueueDao()
    )

    private val networkManager: NetworkManager = NetworkManager(
        context = context
    )

    // Repositories and ViewModels

    private val mapboxRepository: MapboxRepository = MapboxRepository()
    private val mapboxViewModel: MapboxViewModel by lazy {
        MapboxViewModel(mapboxRepository)
    }

    private val homelessRepository: HomelessRepository = HomelessRepository(FirestoreDataSource(firestore), roomDataSource, networkManager)
    private val homelessViewModel: HomelessViewModel = HomelessViewModel(homelessRepository, mapboxViewModel)

    private val volunteerRepository: VolunteerRepository = VolunteerRepository(FirestoreDataSource(firestore), roomDataSource, networkManager)
    private val volunteerViewModel: VolunteerViewModel = VolunteerViewModel(volunteerRepository)

    private val requestRepository: RequestRepository = RequestRepository(FirestoreDataSource(firestore), roomDataSource, networkManager)
    private val requestViewModel: RequestViewModel by lazy {
        RequestViewModel(requestRepository)
    }
    private val updatesRepository: UpdatesRepository = UpdatesRepository(FirestoreDataSource(firestore), roomDataSource, networkManager)
    private val updatesViewModel: UpdatesViewModel by lazy{
        UpdatesViewModel(updatesRepository)
    }

    // Getters for repositories and view models
    fun obtainMapboxRepository(): MapboxRepository = instance.mapboxRepository
    fun obtainMapboxViewModel(): MapboxViewModel = instance.mapboxViewModel

    fun obtainRequestRepository(): RequestRepository = instance.requestRepository
    fun obtainRequestViewModel(): RequestViewModel = instance.requestViewModel

    fun obtainUpdatesRepository(): UpdatesRepository = instance.updatesRepository
    fun obtainUpdatesViewModel(): UpdatesViewModel = instance.updatesViewModel

    fun obtainHomelessRepository(): HomelessRepository = instance.homelessRepository
    fun obtainHomelessViewModel(): HomelessViewModel = instance.homelessViewModel

    fun obtainVolunteerRepository(): VolunteerRepository = instance.volunteerRepository
    fun obtainVolunteerViewModel(): VolunteerViewModel = instance.volunteerViewModel

    fun obtainAuthViewModel(): AuthViewModel = authViewModel

    // Network manager
    fun obtainNetworkManager(): NetworkManager = instance.networkManager

    fun fetchAllData(){
        homelessViewModel.fetchHomelesses()
        volunteerViewModel.fetchVolunteers()
        requestViewModel.fetchRequests()
        updatesViewModel.fetchUpdates()
    }
}


