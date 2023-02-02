package fr.esgi.games.service

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import fr.esgi.games.utils.Utils.Companion.convertArrayList

class firebaseWishlistService {

    private val documentName: String = "users"
    private val db: FirebaseFirestore = Firebase.firestore

    fun getWish(userId: String): Task<ArrayList<Int>> {
        return db.collection(documentName).document(userId)
            .get()
            .continueWith { task ->
                if (task.isSuccessful) {
                    val documentSnapshot = task.result
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        convertArrayList(documentSnapshot.get("wished") as ArrayList<*>)
                    } else {
                        throw Exception("Document does not exist")
                    }
                } else {
                    throw task.exception!!
                }
            }
    }
}