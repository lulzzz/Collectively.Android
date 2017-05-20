package pl.adriankremski.collectively.data.datasource

import io.reactivex.Observable
import pl.adriankremski.collectively.data.model.Settings
import pl.adriankremski.collectively.data.net.Api
import retrofit2.Response

class SettingsDataSourceImpl(val api: Api) : SettingsDataSource{
    override fun settings(): Observable<Settings> = api.settings()
    override fun saveSettings(settings: Settings): Observable<Response<Void>> = api.saveSettings(settings)
}

