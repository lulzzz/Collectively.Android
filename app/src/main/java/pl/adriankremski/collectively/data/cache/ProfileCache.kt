package pl.adriankremski.collectively.data.cache

import android.content.SharedPreferences
import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.Observable
import pl.adriankremski.collectively.Constants
import pl.adriankremski.collectively.data.model.Profile
import java.util.concurrent.TimeUnit

class ProfileCache(val sharedPreferences: SharedPreferences, val gson: Gson) : Cache<Profile> {

    private val CACHE_EXPIRATION_TIME = TimeUnit.DAYS.toMillis(7)
    private val typeToken = object : TypeToken<Profile>() {}.type

    override fun isExpired(): Boolean {
        var cacheTime = sharedPreferences.getLong(Constants.PreferencesKey.PROFILE_CACHE_TIME, 0)
        var currentTime = System.currentTimeMillis()

        return TextUtils.isEmpty(profileInJson()) || currentTime - CACHE_EXPIRATION_TIME >= cacheTime
    }

    private fun profileInJson() : String = sharedPreferences.getString(Constants.PreferencesKey.PROFILE, "")

    override fun putData(profile: Profile) {
        val profileInJson = gson.toJson(profile, typeToken)
        sharedPreferences.edit().putString(Constants.PreferencesKey.PROFILE, profileInJson)
        sharedPreferences.edit().putLong(Constants.PreferencesKey.PROFILE_CACHE_TIME, System.currentTimeMillis())
        sharedPreferences.edit().commit()
    }

    override fun getData(): Observable<Profile> {
        return Observable.just(gson.fromJson(profileInJson(), typeToken))
    }

    override fun clear() {
        sharedPreferences.edit().clear()
        sharedPreferences.edit().commit()
    }
}

