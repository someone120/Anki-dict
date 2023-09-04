package org.someone120.anki_dict.anki

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.ichi2.anki.FlashCardsContract.READ_WRITE_PERMISSION
import com.ichi2.anki.api.AddContentApi
import org.someone120.anki_dict.dicts.Definition
import org.someone120.anki_dict.dicts.ExampleSentence


class AnkiApi(context: Context) {

    private val DECK_REF_DB = "com.ichi2.anki.api.decks"
    private val MODEL_REF_DB = "com.ichi2.anki.api.models"

    private var mApi: AddContentApi? = null
    private var mContext: Context? = null

    init {
        mContext = context.applicationContext
        mApi = AddContentApi(mContext)
    }

    fun getApi(): AddContentApi? {
        return mApi
    }

    /**
     * Whether or not the API is available to use.
     * The API could be unavailable if AnkiDroid is not installed or the user explicitly disabled the API
     * @return true if the API is available to use
     */
    fun isApiAvailable(context: Context?): Boolean {
        return AddContentApi.getAnkiDroidPackageName(context) != null
    }


    /**
     * Whether or not we should request full access to the AnkiDroid API
     */
    fun shouldRequestPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            mContext!!,
            READ_WRITE_PERMISSION
        ) != PackageManager.PERMISSION_GRANTED
    }

    /**
     * Request permission from the user to access the AnkiDroid API (for SDK 23+)
     * @param callbackActivity An Activity which implements onRequestPermissionsResult()
     * @param callbackCode The callback code to be used in onRequestPermissionsResult()
     */
    fun requestPermission(callbackActivity: Activity?, callbackCode: Int) {
        ActivityCompat.requestPermissions(
            callbackActivity!!,
            arrayOf<String>(READ_WRITE_PERMISSION),
            callbackCode
        )
    }

    fun addCard(origin: String,definition:Definition,example: ExampleSentence?):Boolean{
        TODO()
    }
}