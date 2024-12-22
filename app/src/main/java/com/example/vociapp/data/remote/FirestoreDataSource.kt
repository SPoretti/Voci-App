package com.example.vociapp.data.remote

import android.util.Log
import com.example.vociapp.data.types.Homeless
import com.example.vociapp.data.types.Request
import com.example.vociapp.data.types.Update
import com.example.vociapp.data.types.Volunteer
import com.example.vociapp.data.util.Resource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {

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
            Resource.Error(e.message ?: "An unknown error occurred")
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

    //Query volontari
    suspend fun addVolunteer(volunteer: Volunteer): Resource<String> {
        return try {
            val documentReference = firestore.collection("volunteers").add(volunteer).await()
            Resource.Success(documentReference.id)
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

        Log.d("Firestore", "Risultati query: ${volunteerNickname.size()} documenti trovati") // Log per il numero di documenti trovati

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
                .whereEqualTo("id", volunteer.id) // Query by "id" field
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

//    suspend fun getUserPreferences(userId: String): Resource<List<String>> {
//        try {
//            val volunteerQuery = firestore.collection("volunteers").whereEqualTo("id", userId).get().await()
//            if (volunteerQuery.documents.isNotEmpty()) {
//                val volunteerDocId = volunteerQuery.documents[0].id
//                val documentSnapshot = firestore
//                    .collection("volunteers")
//                    .document(volunteerDocId)
//                    .collection("userPreferences")
//                    .document("preferences")
//                    .get()
//                    .await()
//                if (documentSnapshot.exists()) {
//                    return Resource.Success(documentSnapshot.toObject(UserPreferences::class.java)!!)
//                } else {
//                    return Resource.Success(UserPreferences(userId = userId)) // Return empty preferences if not found
//                }
//            } else {
//                return Resource.Error("Volunteer not found12$userId" + "3")
//            }
//        } catch (e: Exception) {
//            return Resource.Error(e.message ?: "An unknown error occurred")
//        }
//    }

    suspend fun getUserPreferences(userId: String): Resource<List<String>> {
        try {
            val volunteerQuery = firestore.collection("volunteers").whereEqualTo("id", userId).get().await()
            if (volunteerQuery.documents.isNotEmpty()) {
                val volunteerDocId = volunteerQuery.documents[0].id
                val documentSnapshot = firestore
                    .collection("volunteers")
                    .document(volunteerDocId)
                    .collection("userPreferences")
                    .document("preferences")
                    .get()
                    .await()
                if (documentSnapshot.exists()) {
                    val preferredItemIds = documentSnapshot.get("preferredItemIds") as? List<String> ?: emptyList()
                    return Resource.Success(preferredItemIds)
                } else {
                    return Resource.Success(emptyList()) // Return empty list if preferences not found
                }
            } else {
                return Resource.Error("Volunteer not found")
            }
        } catch (e: Exception) {
            return Resource.Error(e.message ?: "An unknown error occurred")
        }
    }

    suspend fun updateUserPreferences(userId: String, preferredItemIds: List<String>): Resource<Unit> {
        try {
            val volunteerQuery = firestore.collection("volunteers").whereEqualTo("id", userId).get().await()
            if (volunteerQuery.documents.isNotEmpty()) {
                val volunteerDocId = volunteerQuery.documents[0].id
                firestore
                    .collection("volunteers")
                    .document(volunteerDocId)
                    .collection("userPreferences")
                    .document("preferences")
                    .set(mapOf("preferredItemIds" to preferredItemIds))
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

}