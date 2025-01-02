package com.example.vociapp.data.remote

import android.util.Log
import com.example.vociapp.data.local.database.Homeless
import com.example.vociapp.data.local.database.Request
import com.example.vociapp.data.local.database.Update
import com.example.vociapp.data.local.database.Volunteer
import com.example.vociapp.data.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

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

    suspend fun getHomeless(homelessID: String): Homeless? {
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
                homeless // Return the Homeless object if found
            } else {
                null // Return null if homeless not found
            }
        } catch (e: Exception) {
            // Handle exception, e.g., log the error
            // and return null or throw an exception
            Log.e("FirestoreDataSource", "Error fetching homeless: ${e.message}")
            null
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

    fun deleteHomeless(id: Any) {
        TODO()//if needed
    }
    //----------------------------------TODO()-------------------------------
    // ------------------------------- Volunteer Functions ----------------------------------

    suspend fun addVolunteer(volunteer: Volunteer): Resource<String> {
        return try {
            val uid = FirebaseAuth.getInstance().currentUser?.uid
            val volunteerWithId = volunteer.copy(id = uid ?: "")

            val documentReference = uid?.let {
                firestore.collection("volunteers")
                    .document(it)
                    .set(volunteerWithId)
                    .await()
            }

            Resource.Success(documentReference.toString())
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

    suspend fun getVolunteerById(id: String): Volunteer? {
        val volunteerDoc = firestore.collection("volunteers")
            .whereEqualTo("id", id)
            .get()
            .await()
            .documents
            .firstOrNull()

        return volunteerDoc?.toObject(Volunteer::class.java)
    }

    suspend fun getVolunteerByNickname(nickname: String): Volunteer? {
        Log.d("Firestore", "Eseguo la query per il nickname: $nickname") // Aggiungi log per il nickname
        val volunteerNickname = firestore.collection("volunteers")
            .whereEqualTo("nickname", nickname)
            .get()
            .await()
        return volunteerNickname.documents
            .firstOrNull()?.toObject(Volunteer::class.java)
    }

    suspend fun getVolunteerByEmail(email: String): Volunteer? {
        val volunteerNickname = firestore.collection("volunteers")
            .whereEqualTo("email", email)
            .get()
            .await()
            .documents
            .firstOrNull()

        return volunteerNickname?.toObject(Volunteer::class.java)
    }

    suspend fun getVolunteerIdByEmail(email: String): String? {
        return try {
            val querySnapshot = firestore.collection("volunteers")
                .whereEqualTo("email", email)
                .get()
                .await()

            if (querySnapshot.documents.isNotEmpty()) {
                querySnapshot.documents[0].getString("id")
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("Firestore", "Error getting volunteer ID", e)
            null
        }
    }

    suspend fun updateVolunteer(volunteer: Volunteer): Resource<Unit> {
        return try {
            val querySnapshot = firestore.collection("volunteers")
                .whereEqualTo("nickname", oldVolunteer.nickname) //Preleva il volontario già esistente tramite nickname
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

    // ------------------------------- Preferences Functions ----------------------------------

    suspend fun getUserPreferences(userId: String): Resource<List<String>> {
        try {
            val volunteerQuery = firestore.collection("volunteers").whereEqualTo("id", userId).get().await()
            if (volunteerQuery.documents.isNotEmpty()) {
                val volunteerDocId = volunteerQuery.documents[0].id
                val documentSnapshot = firestore
                    .collection("volunteers")
                    .document(volunteerDocId)
                    .get()
                    .await()
                if (documentSnapshot.exists()) {
                    val preferredHomelessIds = documentSnapshot.get("preferredHomelessIds") as? List<String> ?: emptyList()
                    return Resource.Success(preferredHomelessIds)
                } else {
                    return Resource.Success(emptyList())
                }
            } else {
                return Resource.Error("Volunteer not found")
            }
        } catch (e: Exception) {
            return Resource.Error(e.message ?: "An unknown error occurred")
        }
    }

    suspend fun updateUserPreferences(userId: String, preferredHomelessIds: List<String>): Resource<Unit> {
        try {
            val volunteerQuery = firestore.collection("volunteers").whereEqualTo("id", userId).get().await()
            if (volunteerQuery.documents.isNotEmpty()) {
                val volunteerDocId = volunteerQuery.documents[0].id
                firestore
                    .collection("volunteers")
                    .document(volunteerDocId)
                    .update(mapOf("preferredHomelessIds" to preferredHomelessIds))
                    .await()
                return Resource.Success(Unit)
            } else {
                return Resource.Error("Volunteer not found1")
            }
        } catch (e: Exception) {
            return Resource.Error(e.message ?: "An unknown error occurred")
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

    fun updateUpdate(data: Update?) {
        TODO() //if needed
    }

    fun deleteUpdate(id: String) {
        TODO() //if needed
    }

}