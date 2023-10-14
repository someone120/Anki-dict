package org.someone120.anki_dict.anki

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
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


    /**
     * Try to find the given model by name, accounting for renaming of the model:
     * If there's a model with this modelName that is known to have previously been created (by this app)
     * and the corresponding model ID exists and has the required number of fields
     * then return that ID (even though it may have since been renamed)
     * If there's a model from #getModelList with modelName and required number of fields then return its ID
     * Otherwise return null
     * @param modelName the name of the model to find
     * @param numFields the minimum number of fields the model is required to have
     * @return the model ID or null if something went wrong
     */
    fun findModelIdByName(modelName: String, numFields: Int): Long? {
        val modelsDb = mContext!!.getSharedPreferences(MODEL_REF_DB, Context.MODE_PRIVATE)
        val prefsModelId = modelsDb.getLong(modelName, -1L)
        // if we have a reference saved to modelName and it exists and has at least numFields then return it
        if (prefsModelId != -1L && mApi!!.getModelName(prefsModelId) != null && mApi!!.getFieldList(
                prefsModelId
            ) != null && mApi!!.getFieldList(prefsModelId).size >= numFields
        ) { // could potentially have been renamed
            return prefsModelId
        }
        val modelList = mApi!!.getModelList(numFields)
        if (modelList != null) {
            for ((key, value) in modelList) {
                if (value == modelName) {
                    return key // first model wins
                }
            }
        }
        // model no longer exists (by name nor old id), the number of fields was reduced, or API error
        return null
    }


    /**
     * Try to find the given deck by name, accounting for potential renaming of the deck by the user as follows:
     * If there's a deck with deckName then return it's ID
     * If there's no deck with deckName, but a ref to deckName is stored in SharedPreferences, and that deck exist in
     * AnkiDroid (i.e. it was renamed), then use that deck.Note: this deck will not be found if your app is re-installed
     * If there's no reference to deckName anywhere then return null
     * @param deckName the name of the deck to find
     * @return the did of the deck in Anki
     */
    fun findDeckIdByName(deckName: String): Long? {
        val decksDb = mContext!!.getSharedPreferences(DECK_REF_DB, Context.MODE_PRIVATE)
        // Look for deckName in the deck list
        var did = getDeckId(deckName)
        return if (did != null) {
            // If the deck was found then return it's id
            did
        } else {
            // Otherwise try to check if we have a reference to a deck that was renamed and return that
            did = decksDb.getLong(deckName, -1)
            if (did != -1L && mApi!!.getDeckName(did) != null) {
                did
            } else {
                // If the deck really doesn't exist then return null
                null
            }
        }
    }

    /**
     * Get the ID of the deck which matches the name
     * @param deckName Exact name of deck (note: deck names are unique in Anki)
     * @return the ID of the deck that has given name, or null if no deck was found or API error
     */
    private fun getDeckId(deckName: String): Long? {
        val deckList = mApi!!.deckList
        if (deckList != null) {
            for ((key, value) in deckList) {
                if (value.equals(deckName, ignoreCase = true)) {
                    return key
                }
            }
        }
        return null
    }
}