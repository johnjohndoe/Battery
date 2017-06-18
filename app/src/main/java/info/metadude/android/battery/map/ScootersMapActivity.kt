package info.metadude.android.battery.map

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import info.metadude.android.battery.BuildConfig
import info.metadude.android.battery.R
import info.metadude.android.battery.api.Api
import info.metadude.android.battery.api.ScootersService
import info.metadude.android.battery.map.models.EnergyLevel
import info.metadude.android.battery.map.models.EnergyLevel.Status.*
import info.metadude.android.battery.map.models.Scooter
import kotlinx.android.synthetic.main.activity_maps.*
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class ScootersMapActivity : AppCompatActivity(), OnMapReadyCallback, ScootersMap.View {

    private val INITIAL_MAP_LOCATION: LatLng = LatLng(52.518611, 13.408333)
    private val INITIAL_MAP_ZOOM: Float = 12f

    private lateinit var presenter: ScootersMap.Presenter
    private lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        setSupportActionBar(toolbar)
        initViews()
        initPresenter()
    }

    private fun initViews() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun initPresenter() {
        val model = ScootersMapModel(ScootersMapState.createDefault(), service)
        presenter = ScootersMapPresenter(model, this)
        presenter.onViewCreated()
    }

    override fun onDestroy() {
        presenter.onViewDestroy()
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.map_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_update) {
            presenter.loadData()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun displayScooters(scooters: List<Scooter>) {
        scooters.forEach {
            val location = LatLng(it.location.latitude, it.location.longitude)
            val color = colorForEnergyStatus(it.energyLevel.status)
            val icon = BitmapDescriptorFactory.defaultMarker(color)
            val snippet = getString(R.string.scooters_map_snippet,
                    it.model, it.energyLevel.numericValue)
            val options = MarkerOptions()
                    .icon(icon)
                    .position(location)
                    .title(it.licensePlate)
                    .snippet(snippet)
            map.addMarker(options)
        }
    }

    fun colorForEnergyStatus(status: EnergyLevel.Status) = when (status) {
        LOW -> BitmapDescriptorFactory.HUE_RED
        MEDIUM -> BitmapDescriptorFactory.HUE_YELLOW
        HIGH -> BitmapDescriptorFactory.HUE_GREEN
        UNDEFINED -> BitmapDescriptorFactory.HUE_CYAN
    }

    override fun displayScootersCount(scootersCount: Int) {
        val scootersText = resources.getQuantityString(
                R.plurals.scooters_map_scooters, scootersCount, scootersCount)
        val subtitle = getString(R.string.scooters_map_scooters_shown, scootersText)
        updateSubtitle(subtitle)
    }

    override fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progressBar.visibility = View.GONE
    }

    override fun showErrorMessage() {
        updateSubtitle(getString(R.string.scooters_map_error_loading_item))
    }

    private fun updateSubtitle(subtitle: String) {
        toolbar.subtitle = subtitle
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        presenter.loadData()
        initMap()
    }

    private fun initMap() {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(INITIAL_MAP_LOCATION, INITIAL_MAP_ZOOM))
    }

    private val service: ScootersService by lazy {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.HEADERS
        val CACHE_SIZE: Long = 10 * 1024 * 1024
        val cache = Cache(application.cacheDir, CACHE_SIZE)
        val okHttpClient = OkHttpClient.Builder()
                .cache(cache)
                .addNetworkInterceptor(interceptor)
                .build()
        Api.provideScootersService(BuildConfig.API_BASE_URL, okHttpClient)
    }

}
