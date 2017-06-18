package info.metadude.android.battery.api

import info.metadude.android.battery.map.models.ScootersResponse
import retrofit2.http.GET
import rx.Observable

interface ScootersService {

    @GET("api/v1/scooters.json")
    fun getScooters(): Observable<ScootersResponse>

}
