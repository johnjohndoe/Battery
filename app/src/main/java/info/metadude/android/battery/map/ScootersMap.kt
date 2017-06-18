package info.metadude.android.battery.map

import info.metadude.android.battery.map.models.Scooter
import rx.Observable

interface ScootersMap {

    interface Model {

        fun getScooters()

        fun stateObservable(): Observable<ScootersMapState>

    }

    interface View {

        fun displayScooters(scooters: List<Scooter>)

        fun displayScootersCount(scootersCount: Int)

        fun showProgress()

        fun hideProgress()

        fun showErrorMessage()

    }

    interface Presenter {

        fun onViewCreated()

        fun onViewDestroy()

        fun loadData()

    }
}

