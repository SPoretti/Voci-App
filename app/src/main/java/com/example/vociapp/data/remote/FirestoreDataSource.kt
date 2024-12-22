package com.example.vociapp.data.remote

import android.util.Log
import com.example.vociapp.data.types.Homeless
import com.example.vociapp.data.types.Request
import com.example.vociapp.data.types.Volunteer
import com.example.vociapp.data.util.Resource
import com.google.firebase.auth.FirebaseAuth
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

    suspend fun updateVolunteer(oldVolunteer: Volunteer, newVolunteer: Volunteer): Resource<Unit> {
        return try {
            val querySnapshot = firestore.collection("volunteers")
                .whereEqualTo("nickname", oldVolunteer.nickname) //Preleva il volontario già esistente tramite nickname
                .get()
                .await()

            if (querySnapshot.documents.isNotEmpty()) {
                val documentId = querySnapshot.documents[0].id // Get the document ID

                val updatedVolunteer = newVolunteer.copy( //Copia nel volontario aggiornato i dati preesistenti
                    id = oldVolunteer.id,
                    phoneNumber = oldVolunteer.phoneNumber,
                    email = oldVolunteer.email
                )

                firestore.collection("volunteers")
                    .document(documentId) // Use the document ID for update
                    .set(updatedVolunteer, SetOptions.merge())
                    .await()
                Resource.Success(Unit)
            } else {
                Resource.Error("Volunteer not found") // Handle case where request is not found
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unknown error occurred")
        }

    }

    suspend fun completeVolunteerProfile(volunteer: Volunteer): Resource<Unit> {
        return try {
            val querySnapshot = firestore.collection("volunteers")
                .whereEqualTo("email", volunteer.email) // Preleva il volontario tramite email
                .get()
                .await()
            if (querySnapshot.documents.isNotEmpty()) {
                val documentId = querySnapshot.documents[0].id // Get the document ID
                val updatedVolunteer = volunteer.copy(
                    id = documentId,
                    email = volunteer.email
                )
                firestore.collection("volunteers")
                    .document(documentId)
                    .set(updatedVolunteer, SetOptions.merge())
                    .await()
                Resource.Success(Unit)
            } else {
                Resource.Error("Volunteer not found")
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
            null
        }
    }
}