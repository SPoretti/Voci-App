package com.voci.app.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.voci.app.data.local.database.Homeless
import com.voci.app.data.local.database.Preference
import com.voci.app.data.local.database.Request
import com.voci.app.data.local.database.Update
import com.voci.app.data.local.database.Volunteer
import com.voci.app.data.util.Resource
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

//Remote data source
class FirestoreDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    // ------------------------------- Request Functions ----------------------------------

    suspend fun addRequest(request: Request): Resource<String> {
        return try {
            val documentReference = firestore.collection("requests").add(request).await()
            Resource.Success(documentReference.id)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unknown error occurred")
        }
    }

    suspend fun getRequests(): Resource<List<Request>> {
        return try {
            val requests = firestore.collection("requests").get().await()
                .toObjects(Request::class.java)
            Resource.Success(requests)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error fetching requests from remote data")
        }
    }

    suspend fun updateRequest(request: Request): Resource<Unit> {
        return try {
            val querySnapshot = firestore.collection("requests")
                .whereEqualTo("id", request.id) // Query by "id" field
                .get()
                .await()

            if (querySnapshot.documents.isNotEmpty()) {
                val documentId = querySnapshot.documents[0].id // Get the document ID
                firestore.collection("requests")
                    .document(documentId) // Use the document ID for update
                    .set(request, SetOptions.merge())
                    .await()
                Resource.Success(Unit)
            } else {
                Resource.Error("Request not found") // Handle case where request is not found
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unknown error occurred")
        }
    }

    suspend fun deleteRequest(request: Request): Resource<Unit> {
        return try {
            val querySnapshot = firestore.collection("requests")
                .whereEqualTo("id", request.id) // Query by "id" field
                .get()
                .await()

            if (querySnapshot.documents.isNotEmpty()) {
                val documentId = querySnapshot.documents[0].id // Get the document ID
                firestore.collection("requests")
                    .document(documentId) // Use the document ID for deletion
                    .delete()
                    .await()
                Resource.Success(Unit)
            } else {
                Resource.Error("Request not found") // Handle case where request is not found
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unknown error occurred")
        }
    }

    suspend fun getRequestById(requestId: String): Resource<Request> {
        return try {
            val querySnapshot = firestore.collection("requests")
                .whereEqualTo("id", requestId)
                .get()
                .await()

            if (querySnapshot.documents.isNotEmpty()) {
                val request = querySnapshot.documents[0].toObject(Request::class.java)!!
                Resource.Success(request)
            } else {
                Resource.Error("Request not found")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unknown error occurred")
        }
    }

    suspend fun getRequestsByHomelessId(homelessId: String): Resource<List<Request>> {
        return try {
            val querySnapshot = firestore.collection("requests")
                .whereEqualTo("homelessID", homelessId)
                .get()
                .await()

            if (querySnapshot.documents.isNotEmpty()) {
                val requests = querySnapshot.documents.mapNotNull { it.toObject(Request::class.java) }
                Resource.Success(requests)
            } else {
                Resource.Error("Requests not found")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unknown error occurred")
        }
    }

    suspend fun deleteRequestsByHomelessId(homelessId: String): Resource<Unit> {
        return try {
            // Query by "homelessId" field
            val querySnapshot = firestore.collection("requests")
                .whereEqualTo("homelessID", homelessId)
                .get()
                .await()

            if (querySnapshot.documents.isNotEmpty()) {
                // Iterate through all documents and delete them
                val deleteResults = querySnapshot.documents.map { document ->
                    firestore.collection("requests")
                        .document(document.id) // Use the document ID for deletion
                        .delete()
                }
                // Wait for all delete operations to complete
                deleteResults.forEach { it.await() }
                Resource.Success(Unit)
            } else {
                Resource.Error("Requests not found") // Handle case where requests are not found
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unknown error occurred")
        }
    }

    // ------------------------------- Homeless Functions ----------------------------------

    suspend fun addHomeless(homeless: Homeless): Resource<String> {
        return try {
            val documentReference = firestore.collection("homelesses").add(homeless).await()
            Resource.Success(documentReference.id)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unknown error occurred")
        }
    }

    suspend fun getHomelesses(): Resource<List<Homeless>> {
        return try {
            val homelesses = firestore.collection("homelesses").get().await()
                .toObjects(Homeless::class.java)
            Resource.Success(homelesses)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unknown error occurred")
        }
    }

    suspend fun getHomelessById(homelessID: String): Resource<Homeless> {
        return try {
            val querySnapshot = firestore.collection("homelesses")
                .whereEqualTo("id", homelessID)
                .get()
                .await()

            if (querySnapshot.documents.isNotEmpty()) {
                val documentId = querySnapshot.documents[0].id
                val homeless = firestore.collection("homelesses")
                    .document(documentId)
                    .get()
                    .await()
                    .toObject(Homeless::class.java)
                Resource.Success(homeless!!) // Return Resource.Success with the Homeless object
            } else {
                Resource.Error("Homeless not found") // Return Resource.Error if homeless not found
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unknown error occurred") // Return Resource.Error for other exceptions
        }
    }

    suspend fun updateHomeless(homeless: Homeless): Resource<Unit> {
        return try {
            val querySnapshot = firestore.collection("homelesses")
                .whereEqualTo("id", homeless.id)
                .get()
                .await()

            if (querySnapshot.documents.isNotEmpty()) {
                val documentId = querySnapshot.documents[0].id
                firestore.collection("homelesses")
                    .document(documentId)
                    .set(homeless, SetOptions.merge())
                    .await()
                Resource.Success(Unit)
            } else {
                Resource.Error("Homeless not found")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unknown error occurred")
        }
    }

    // Delete a homeless person and its related requests etc from firestore
    suspend fun deleteHomelessById(homelessId: String): Resource<Unit> {
        return try {
            // Delete the Homeless document
            val querySnapshot = firestore.collection("homelesses")
                .whereEqualTo("id", homelessId) // Query by "id" field
                .get()
                .await()

            if (querySnapshot.documents.isNotEmpty()) {
                val documentId = querySnapshot.documents[0].id // Get the document ID
                firestore.collection("homelesses")
                    .document(documentId) // Use the document ID for deletion
                    .delete()
                    .await()

                // Delete related preferences
                deletePreferencesByHomelessId(homelessId)

                // Delete related requests
                deleteRequestsByHomelessId(homelessId)

                // Delete related updates
                deleteUpdatesByHomelessId(homelessId)
            }

            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unknown error occurred")
        }
    }

    // ------------------------------- Volunteer Functions ----------------------------------

    suspend fun addVolunteer(volunteer: Volunteer): Resource<String> {
        return try {
            val documentReference = firestore.collection("volunteers").add(volunteer).await()
            Resource.Success(documentReference.id)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unknown error occurred")
        }
    }

    suspend fun getVolunteers(): Resource<List<Volunteer>>{
        return try {
            val volunteers = firestore.collection("volunteers").get().await()
                .toObjects(Volunteer::class.java)
            Resource.Success(volunteers)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unknown error occurred")
        }
    }

    suspend fun getVolunteerById(volunteerId: String): Resource<Volunteer> {
        return try {
            val querySnapshot = firestore.collection("volunteers")
                .whereEqualTo("id", volunteerId)
                .get()
                .await()

            if (querySnapshot.documents.isNotEmpty()) {
                val volunteer = querySnapshot.documents[0].toObject(Volunteer::class.java)!!
                Resource.Success(volunteer)
            } else {
                Resource.Error("Request not found")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unknown error occurred")
        }
    }

    suspend fun getVolunteerByNickname(nickname: String): Resource<Volunteer> {
        return try {
            val querySnapshot = firestore.collection("volunteers")
                .whereEqualTo("nickname", nickname)
                .get()
                .await()

            if (querySnapshot.documents.isNotEmpty()) {
                val volunteer = querySnapshot.documents[0].toObject(Volunteer::class.java)!!
                Resource.Success(volunteer)
            } else {
                Resource.Error("Volunteer not found")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unknown error occurred")
        }
    }

    suspend fun getVolunteerByEmail(email: String): Resource<Volunteer> {
        return try {
            val querySnapshot = firestore.collection("volunteers")
                .whereEqualTo("email", email)
                .get()
                .await()

            if (querySnapshot.documents.isNotEmpty()) {
                val volunteer = querySnapshot.documents[0].toObject(Volunteer::class.java)!!
                Resource.Success(volunteer)
            } else {
                Resource.Error("Volunteer not found")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unknown error occurred")
        }
    }
    
    suspend fun updateVolunteer(volunteer: Volunteer): Resource<Unit> {
        return try {
            val querySnapshot = firestore.collection("volunteers")
                .whereEqualTo("id", volunteer.id)
                .get()
                .await()

            if (querySnapshot.documents.isNotEmpty()) {
                val documentId = querySnapshot.documents[0].id // Get the document ID

                firestore.collection("volunteers")
                    .document(documentId) // Use the document ID for update
                    .set(volunteer, SetOptions.merge())
                    .await()
                Resource.Success(Unit)
            } else {
                Resource.Error("Volunteer not found") // Handle case where request is not found
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unknown error occurred")
        }
    }

    // ------------------------------- Updates Functions ----------------------------------

    suspend fun addUpdate(update: Update): Resource<String> {
        return try {
            val documentReference = firestore.collection("updates").add(update).await()
            Resource.Success(documentReference.id)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unknown error occurred")
        }
    }

    suspend fun getUpdates(): Resource<List<Update>> {
        return try {
            val requests = firestore.collection("updates").get().await()
                .toObjects(Update::class.java)
            Resource.Success(requests)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unknown error occurred")
        }
    }

    fun updateUpdate(data: Update) {
        TODO() //if needed
    }

    suspend fun deleteUpdateById(id: String): Resource<Unit> {
        return try {
            // Query by "id" field
            val querySnapshot = firestore.collection("updates")
                .whereEqualTo("id", id)
                .get()
                .await()

            if (querySnapshot.documents.isNotEmpty()) {
                val documentId = querySnapshot.documents[0].id // Get the document ID
                firestore.collection("updates")
                    .document(documentId) // Use the document ID for deletion
                    .delete()
                    .await()
                Resource.Success(Unit)
            } else {
                Resource.Error("Update not found") // Handle case where request is not found
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unknown error occurred")
        }
    }

    suspend fun deleteUpdatesByHomelessId(homelessId: String): Resource<Unit> {
        return try {
            // Query by "homelessId" field
            val querySnapshot = firestore.collection("updates")
                .whereEqualTo("homelessID", homelessId)
                .get()
                .await()

            if (querySnapshot.documents.isNotEmpty()) {
                // Iterate through all documents and delete them
                val deleteResults = querySnapshot.documents.map { document ->
                    firestore.collection("updates")
                        .document(document.id) // Use the document ID for deletion
                        .delete()
                }
                // Wait for all delete operations to complete
                deleteResults.forEach { it.await() }
                Resource.Success(Unit)
            } else {
                Resource.Error("Update not found") // Handle case where request is not found
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unknown error occurred")
        }
    }

    // ------------------------------- Updates Functions ----------------------------------

    suspend fun addPreference(preference: Preference): Resource<String> {
        return try {
//            val preferenceMap = mapOf(
//                "volunteerId" to preference.volunteerId,
//                "homelessId" to preference.homelessId
//            )
            firestore.collection("preferences")
                .document("${preference.volunteerId}_${preference.homelessId}")
                .set(preference)
                .await()
            Resource.Success("${preference.volunteerId}_${preference.homelessId}")
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unknown error occurred")
        }
    }

    suspend fun deletePreference(preference: Preference): Resource<Unit> {
        return try {
            firestore.collection("preferences")
                .document("${preference.volunteerId}_${preference.homelessId}") // Use composite key as document ID
                .delete()
                .await()
            Resource.Success(Unit) // Return Unit on success
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unknown error occurred while deleting preference")
        }
    }

    suspend fun getPreferencesForVolunteer(volunteerId: String): Resource<List<Preference>> {
        return try {
            val querySnapshot = firestore.collection("preferences")
                .whereEqualTo("volunteerId", volunteerId)
                .get()
                .await()

            val preferences = querySnapshot.toObjects(Preference::class.java)
            Resource.Success(preferences)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error fetching preferences for volunteer")
        }
    }

    suspend fun deletePreferencesByHomelessId(homelessId: String): Resource<Unit> {
        return try {
            // Query by "homelessId" field
            val querySnapshot = firestore.collection("preferences")
                .whereEqualTo("homelessId", homelessId)
                .get()
                .await()

            if (querySnapshot.documents.isNotEmpty()) {
                // Iterate through all documents and delete them
                val deleteResults = querySnapshot.documents.map { document ->
                    firestore.collection("preferences")
                        .document(document.id) // Use the document ID for deletion
                        .delete()
                }
                // Wait for all delete operations to complete
                deleteResults.forEach { it.await() }
                Resource.Success(Unit)
            } else {
                Resource.Error("Preferences not found") // Handle case where preferences are not found
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unknown error occurred")
        }
    }
}