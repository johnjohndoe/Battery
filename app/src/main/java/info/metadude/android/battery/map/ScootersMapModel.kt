package info.metadude.android.battery.map

import info.metadude.android.battery.api.ScootersService
import info.metadude.android.battery.map.ScootersMapState.Type.*
import info.metadude.android.battery.map.models.Scooter
import info.metadude.android.battery.map.models.ScootersResponse
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subjects.BehaviorSubject
import java.util.*

class ScootersMapModel(
        initialState: ScootersMapState,
        val service: ScootersService
) : ScootersMap.Model {

    private val stateSubject = BehaviorSubject.create<ScootersMapState>(initialState)

    override fun getScooters() {
        val loadingState = ScootersMapState(LOADING, stateSubject.value.items)
        stateSubject.onNext(loadingState)
        service.getScooters()
                .map(this::scooters)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSuccess, this::onError)
    }

    private fun onSuccess(scooters: List<Scooter>) {
        val idleState = ScootersMapState(DONE, scooters)
        stateSubject.onNext(idleState)
    }

    private fun onError(throwable: Throwable) {
        val errorState = ScootersMapState(ERROR, Collections.emptyList())
        stateSubject.onNext(errorState)
        throwable.printStackTrace()
    }

    private fun scooters(scootersResponse: ScootersResponse) = scootersResponse.data.scooters

    override fun stateObservable(): Observable<ScootersMapState> = stateSubject

}


