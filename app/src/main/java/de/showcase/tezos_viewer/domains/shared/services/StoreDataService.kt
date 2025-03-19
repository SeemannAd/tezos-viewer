package de.showcase.tezos_viewer.domains.shared.services

import android.content.Context
import de.showcase.tezos_viewer.domains.shared.extensions.read
import de.showcase.tezos_viewer.domains.shared.extensions.write
import timber.log.Timber

class StoreDataService(
   private val context: Context
) {

    fun write(key: String, value: String)  {
        if(context.write(key, value)) {
            Timber.d("write(): successful")
        } else {
            Timber.e("write(): failed")
        }

        verify(key = key)
    }

    fun read(key: String) : String {
        val value =  context.read(key)

        if(value.isNotEmpty()) {
            Timber.d("read(): successful")
        } else {
            Timber.e("read(): failed")
        }

        return value
    }

   private fun verify(key: String) : Boolean {
        return if(context.read(key).isNotEmpty()) {
            Timber.d("read(): successful")
            true
        } else {
            Timber.e("read(): failed")
            false
        }
    }
}