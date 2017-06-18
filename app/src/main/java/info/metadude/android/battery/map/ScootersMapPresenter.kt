package info.metadude.android.battery.map

import rx.Subscription

class ScootersMapPresenter(
        val model: ScootersMap.Model,
        val view: ScootersMap.View
) : ScootersMap.Presenter {

    private lateinit var modelSubscription: Subscription

    override fun onViewCreated() {
        modelSubscription = model.stateObservable().subscribe(this::renderState)
    }

    override fun onViewDestroy() {
        if (!modelSubscription.isUnsubscribed) {
            modelSubscription.unsubscribe()
        }
    }

    override fun loadData() {
        model.getScooters()
    }

    private fun renderState(state: ScootersMapState) {
        view.displayScooters(state.items)
        when (state.type) {
            ScootersMapState.Type.IDLE -> view.hideProgress()
            ScootersMapState.Type.LOADING -> view.showProgress()
            ScootersMapState.Type.ERROR -> renderErrorState()
            ScootersMapState.Type.DONE -> renderDoneState(state.items.size)
        }
    }

    private fun renderErrorState() {
        view.showErrorMessage()
        view.hideProgress()
    }

    private fun renderDoneState(scootersCount: Int) {
        view.displayScootersCount(scootersCount)
        view.hideProgress()
    }
}
