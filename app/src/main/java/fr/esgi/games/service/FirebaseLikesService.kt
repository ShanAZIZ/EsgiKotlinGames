package fr.esgi.games.service

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirebaseLikesService {

    private val documentName: String = "users"
    private val db: FirebaseFirestore = Firebase.firestore

    fun getLikes(userId: String): Task<ArrayList<Int>> {
        return db.collection(documentName).document(userId)
            .get()
            .continueWith { task ->
                if (task.isSuccessful) {
                    val documentSnapshot = task.result
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        convertArrayList(documentSnapshot.get("liked") as ArrayList<*>)
                    } else {
                        throw Exception("Document does not exist")
                    }
                } else {
                    throw task.exception!!
                }
            }
    }

    fun isInLikes(userId: String, item: Int, callback: (Boolean) -> Unit) {
        db.collection(documentName).document(userId)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                val intList = convertArrayList(documentSnapshot.get("liked") as ArrayList<*>)
                callback(intList.contains(item))
            }
    }

    private fun convertArrayList(arrayList: ArrayList<*>): ArrayList<Int> {
        val intArrayList = ArrayList<Int>()
        for (item in arrayList) {
            val i = item as Long
            intArrayList.add(i.toInt())
        }
        return intArrayList
    }
}