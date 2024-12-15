package com.vdzon.java.common

import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.SetOptions

class FirebaseProducer(
    private val dbFirestore: Firestore?,
    private val collection: String,
    private val document: String,
) {
    private val lastData: String = ""
    private val lastStatus: String = ""
    private var nrUpdates =0

    fun setTime(time: String) {
        if (time==lastData){
            return
        }
        println("Write to firebase: $time")
        val documentRef = dbFirestore?.collection(collection)?.document(document)
        documentRef?.set(mapOf("klok" to time), SetOptions.merge())
        nrUpdates++
        documentRef?.set(mapOf("updatecount" to nrUpdates), SetOptions.merge())
    }

    fun setStatus(status: String) {
        if (status==lastStatus){
            return
        }
        println("Write to firebase: $status")
        val documentRef = dbFirestore?.collection(collection)?.document(document)
        documentRef?.set(mapOf("status" to status), SetOptions.merge())
        nrUpdates++
        documentRef?.set(mapOf("updatecount" to nrUpdates), SetOptions.merge())
    }


}