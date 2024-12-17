package com.tekskills.st_tekskills.presentation.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.tekskills.st_tekskills.R
import com.tekskills.st_tekskills.databinding.ActivityLocationPickerBinding
import com.tekskills.st_tekskills.utils.MapUtility
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.IOException
import java.util.Locale
import java.util.regex.Pattern
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class LocationPickerActivity : AppCompatActivity(), OnMapReadyCallback {

    lateinit var binding: ActivityLocationPickerBinding
    private val TAG: String = LocationPickerActivity::class.java.simpleName
    private var userAddress: String? = ""
    private var userAddressName: String? = ""
    private var mLatitude = 0.0
    private var mLongitude = 0.0
    private var mMap: GoogleMap? = null
    private var mLocationPermissionGranted = false
    var PLACE_AUTOCOMPLETE_REQUEST_CODE: Int = 1

    //initial zoom
    private var isZooming = false

    //Declaration of FusedLocationProviderClient
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null

    private val filterTaskList: MutableList<Job> = ArrayList()

    var regex: String = "^(-?\\d+(\\.\\d+)?),\\s*(-?\\d+(\\.\\d+)?)$"
    var latLongPattern: Pattern = Pattern.compile(regex)
    private var doAfterPermissionProvided = 0
    private var doAfterLocationSwitchedOn = 1
    private var currentLatitude = 0.0
    private var currentLongitude = 0.0
    private var locationRequest: LocationRequest? = null
    private var locationCallback: LocationCallback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_location_picker)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        )
        if (supportActionBar != null) supportActionBar!!.hide()

        //initialization of FusedLocationProviderClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        //Prepare for Request for current location
        getLocationRequest()

        //define callback of location request
        locationCallback = object : LocationCallback() {
            override fun onLocationAvailability(locationAvailability: LocationAvailability) {
                Timber.tag(TAG).d(
                    "onLocationAvailability: isLocationAvailable =  %s",
                    locationAvailability.isLocationAvailable
                )
            }

            override fun onLocationResult(locationResult: LocationResult) {
                Timber.tag(TAG).d("onLocationResult: $locationResult")
                if (locationResult == null) {
                    return
                }

                when (doAfterLocationSwitchedOn) {
                    1 -> startParsingAddressToShow()
                    2 -> showCurrentLocationOnMap(false)//on Click on imgCurrent
                    3 -> showCurrentLocationOnMap(true)//on Click on Direction Tool
                }
                fusedLocationProviderClient!!.removeLocationUpdates(locationCallback!!)
            }
        }

        // Try to obtain the map from the SupportMapFragment.
        val mapFragment = fragmentManager.findFragmentById(R.id.map) as MapFragment
        mapFragment.getMapAsync(this)
        //if you want to open the location on the LocationPickerActivity through intent
        val i = intent
        if (i != null) {
            val extras = i.extras
            if (extras != null) {
                userAddress = extras.getString(MapUtility.ADDRESS)
                userAddressName = extras.getString(MapUtility.NAME)
                mLatitude = intent.getDoubleExtra(MapUtility.LATITUDE, 0.0)
                mLongitude = intent.getDoubleExtra(MapUtility.LONGITUDE, 0.0)
            }
        }

        if (savedInstanceState != null) {
            mLatitude = savedInstanceState.getDouble("latitude")
            mLongitude = savedInstanceState.getDouble("longitude")
            userAddress = savedInstanceState.getString("userAddress")
            userAddressName = savedInstanceState.getString("userAddressName")
            currentLatitude = savedInstanceState.getDouble("currentLatitude")
            currentLongitude = savedInstanceState.getDouble("currentLongitude")
        }

        if (!MapUtility.isNetworkAvailable(this)) {
            MapUtility.showToast(this, "Please Connect to Internet")
        }

        binding.imgSearch.setOnClickListener(View.OnClickListener {
            if (!Places.isInitialized()) {
                Places.initialize(
                    this@LocationPickerActivity.applicationContext,
                    getString(R.string.google_maps_key)
                )
            }
            // Set the fields to specify which types of place data to return.
            val fields = listOf(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.ADDRESS,
                Place.Field.LAT_LNG,
            )

            // Start the autocomplete intent.
            val intent = Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields
            )
//                .setCountry(userCountryISOCode)
                .build(this@LocationPickerActivity)
            this@LocationPickerActivity.startActivityForResult(
                intent,
                PLACE_AUTOCOMPLETE_REQUEST_CODE
            )
        })

        binding.fabSelectLocation.setOnClickListener {
            val intent = Intent()
            // add data into intent and send back to Parent Activity
            intent.putExtra(MapUtility.ADDRESS, userAddress!!.trim { it <= ' ' })
            intent.putExtra(MapUtility.NAME, userAddressName)
            intent.putExtra(MapUtility.LATITUDE, mLatitude)
            intent.putExtra(MapUtility.LONGITUDE, mLongitude)
            this@LocationPickerActivity.setResult(RESULT_OK, intent)
            this@LocationPickerActivity.finish()
        }

        binding.imgCurrentloc.setOnClickListener {
            this@LocationPickerActivity.showCurrentLocationOnMap(false)
            doAfterPermissionProvided = 2
            doAfterLocationSwitchedOn = 2
        }

        // google maps tools
        binding.directionTool.setOnClickListener {
            this@LocationPickerActivity.showCurrentLocationOnMap(true)
            doAfterPermissionProvided = 3
            doAfterLocationSwitchedOn = 3
        }

        binding.googleMapsTool.setOnClickListener { // Default google map
            val intent = Intent(
                Intent.ACTION_VIEW, Uri.parse(
                    "http://maps.google.com/maps?q=loc:$mLatitude, $mLongitude"
                )
            )
            this@LocationPickerActivity.startActivity(intent)
        }
    }

    override fun onPause() {
        super.onPause()
        fusedLocationProviderClient!!.removeLocationUpdates(locationCallback!!)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK && data != null) {
                val place = Autocomplete.getPlaceFromIntent(data)
                binding.imgSearch.text = place.name
                userAddressName = place.name
                mLatitude = place.latLng?.latitude ?: 0.0
                mLongitude = place.latLng?.longitude ?: 0.0
//                place_id = place.id ?: ""
//                place_url = place.websiteUri?.toString() ?: ""

                if (mLatitude != 0.0 && mLongitude != 0.0) {
                    if (MapUtility.popupWindow != null && MapUtility.popupWindow!!.isShowing) {
                        MapUtility.hideProgress()
                    }
                    Timber.tag(TAG).d("getAddressByGeoCodingLatLng: START")
                    this@LocationPickerActivity.userAddress = place.address
                    this@LocationPickerActivity.userAddressName = place.name
                    MapUtility.hideProgress()
                    addMarker()
//                    updateLocationDetails(place.latLng)
                }

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                val status = Autocomplete.getStatusFromIntent(data!!)
                Timber.tag(TAG).i(status.statusMessage ?: "Error")
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        } else if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode != RESULT_OK) {
                Toast.makeText(this, "Location Not Available..", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Fetching Location...", Toast.LENGTH_LONG).show()
                startLocationUpdates()
            }
        }
    }

    private fun checkAndRequestPermissions(): Boolean {
        val locationPermission =
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        val coarsePermission =
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        val listPermissionsNeeded: MutableList<String> = ArrayList()

        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        if (coarsePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        }

        if (listPermissionsNeeded.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                listPermissionsNeeded.toTypedArray<String>(),
                REQUEST_ID_MULTIPLE_PERMISSIONS
            )
            return false
        }

        //getSettingsLocation();
        return true
    }

    private fun showCurrentLocationOnMap(isDirectionClicked: Boolean) {
        if (checkAndRequestPermissions()) {
            @SuppressLint("MissingPermission") val lastLocation =
                fusedLocationProviderClient!!.lastLocation
            lastLocation.addOnSuccessListener(this) { location ->
                if (location != null) {
                    mMap!!.clear()
                    if (isDirectionClicked) {
                        currentLatitude = location.latitude
                        currentLongitude = location.longitude
                        //Go to Map for Directions
                        val intent = Intent(
                            Intent.ACTION_VIEW, Uri.parse(
                                "http://maps.google.com/maps?saddr=$currentLatitude, $currentLongitude&daddr=$mLatitude, $mLongitude"
                            )
                        )
                        this@LocationPickerActivity.startActivity(intent)
                    } else {
                        //Go to Current Location
                        mLatitude = location.latitude
                        mLongitude = location.longitude
                        this@LocationPickerActivity.addressByGeoCodingLatLng
                    }
                } else {
                    //Gps not enabled if loc is null
                    this@LocationPickerActivity.settingsLocation
                    Toast.makeText(
                        this@LocationPickerActivity,
                        "Location not Available", Toast.LENGTH_SHORT
                    ).show()
                }
            }
            lastLocation.addOnFailureListener { //If perm provided then gps not enabled
//                getSettingsLocation();
                Toast.makeText(
                    this@LocationPickerActivity,
                    "Location Not Available", Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun resizeMapIcons(iconName: String?, width: Int, height: Int): Bitmap {
        val imageBitmap = BitmapFactory.decodeResource(
            resources,
            resources.getIdentifier(iconName, "drawable", packageName)
        )
        val resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false)
        return resizedBitmap
    }

    private fun addMarker() {
        val cameraUpdate: CameraUpdate
        val coordinate = LatLng(mLatitude, mLongitude)
        if (mMap != null) {
            val markerOptions: MarkerOptions
            try {
                mMap!!.clear()
                binding.imgSearch.text = userAddress
                markerOptions = MarkerOptions().position(coordinate).title(userAddress).icon(
                    BitmapDescriptorFactory.fromBitmap(
                        resizeMapIcons("ic_pointer", 100, 100)
                    )
                )
                cameraUpdate = if (isZooming) {
                    CameraUpdateFactory.newLatLngZoom(coordinate, mMap!!.cameraPosition.zoom)
                } else {
                    CameraUpdateFactory.newLatLngZoom(coordinate, 18f)
                }

                mMap!!.animateCamera(cameraUpdate)
                mMap!!.mapType = GoogleMap.MAP_TYPE_NORMAL

                val marker = mMap!!.addMarker(markerOptions)
                //marker.showInfoWindow();
            } catch (ex: Exception) {
                Timber.tag(TAG).d("address error init $ex")
            }
        }

        try {
            binding.addressline2.text = userAddress
//            binding.citydetails.text =
//                userCity + SPACE + userPostalCode + SPACE + userState + SPACE + userCountry
        } catch (ex: Exception) {
            Timber.tag(TAG).d("address error text $ex")
        }
    }

    @SuppressLint("PotentialBehaviorOverride")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap!!.clear()
        mMap!!.mapType = GoogleMap.MAP_TYPE_NORMAL
        mMap!!.uiSettings.isMapToolbarEnabled = false
        if (mMap!!.isIndoorEnabled) {
            mMap!!.setIndoorEnabled(false)
        }

        mMap!!.setInfoWindowAdapter(object : InfoWindowAdapter {
            override fun getInfoWindow(arg0: Marker): View? {
                return null
            }

            override fun getInfoContents(arg0: Marker): View? {
                val v = layoutInflater.inflate(R.layout.info_window_layout, null)

                // Getting the position from the marker
                val latLng = arg0.position
                mLatitude = latLng.latitude
                mLongitude = latLng.longitude
                val tvLat = v.findViewById<TextView>(R.id.address)
                tvLat.text = userAddress
                return v
            }
        })

        mMap!!.uiSettings.isZoomControlsEnabled = true

        // Setting a click event handler for the map
        mMap!!.setOnMapClickListener { latLng ->
            mMap!!.clear()
            mLatitude = latLng.latitude
            mLongitude = latLng.longitude
            Timber.tag("latlng").e("%s", latLng.toString())
            isZooming = true
            this@LocationPickerActivity.addMarker()
            if (!MapUtility.isNetworkAvailable(this@LocationPickerActivity)) {
                MapUtility.showToast(this@LocationPickerActivity, "Please Connect to Internet")
            }
            this@LocationPickerActivity.addressByGeoCodingLatLng
        }

        if (checkAndRequestPermissions()) {
            startParsingAddressToShow()
        } else {
            doAfterPermissionProvided = 1
        }
    }

    private val settingsLocation: Unit
        get() {
            val builder = LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest!!)

            val result =
                LocationServices.getSettingsClient(this).checkLocationSettings(builder.build())

            result.addOnCompleteListener { task ->
                try {
                    val response = task.getResult(
                        ApiException::class.java
                    )
                    // All location settings are satisfied. The client can initialize location
                    // requests here.
                    //...
                    if (response != null) {
                        val locationSettingsStates = response.locationSettingsStates
                        Timber.tag(TAG).d("getSettingsLocation: %s", locationSettingsStates)
                        this@LocationPickerActivity.startLocationUpdates()
                    }
                } catch (exception: ApiException) {
                    Timber.tag(TAG).e("getSettingsLocation: $exception")
                    when (exception.statusCode) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->                             // Location settings are not satisfied. But could be fixed by showing the
                            // user a dialog.
                            try {
                                // Cast to a resolvable exception.
                                val resolvable = exception as ResolvableApiException
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                resolvable.startResolutionForResult(
                                    this@LocationPickerActivity,
                                    REQUEST_CHECK_SETTINGS
                                )
                            } catch (e: SendIntentException) {
                                Timber.tag(TAG).e("SendIntentException: $exception")

                            } catch (e: ClassCastException) {
                                Timber.tag(TAG).e("ClassCastException: $exception")
                            }

                        LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {}
                    }
                }
            }
        }

    /**
     * Show location from intent
     */
    private fun startParsingAddressToShow() {
        //get address from intent to show on map
        if (userAddress == null || userAddress!!.isEmpty()) {
            showCurrentLocationOnMap(false)
        } else  //check if address contains lat long, then extract
        //format will be lat,lng i.e 19.23234,72.65465
            if (latLongPattern.matcher(userAddress).matches()) {
                val p = Pattern.compile("(-?\\d+(\\.\\d+)?)") // the pattern to search for
                val m = p.matcher(userAddress)

                // if we find a match, get the group
                var i = 0
                while (m.find()) {
                    // we're only looking for 2s group, so get it
                    if (i == 0) mLatitude = m.group().toDouble()
                    if (i == 1) mLongitude = m.group().toDouble()
                    i++
                }
                //show on map
                addressByGeoCodingLatLng
                addMarker()
            } else {
                //get  latlong from String address via reverse geo coding
                //Since lat long not present in db
                if (mLatitude == 0.0 && mLongitude == 0.0) {
                    latLngByRevGeoCodeFromAdd
                } else {
                    // Latlng is more accurate to get exact point on map ,
                    // String address might not be sufficient (i.e Mumbai, Mah..etc)
                    addMarker()
                }
            }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putDouble("latitude", mLatitude)
        outState.putDouble("longitude", mLongitude)
        outState.putString("userAddress", userAddress)
        outState.putString("userAddressName", userAddressName)
        outState.putDouble("currentLatitude", currentLatitude)
        outState.putDouble("currentLongitude", currentLongitude)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                this.finish()
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        finish()
    }

    private val addressByGeoCodingLatLng: Unit
        get() {
            if (mLatitude != 0.0 && mLongitude != 0.0) {
                if (MapUtility.popupWindow != null && MapUtility.popupWindow!!.isShowing) {
                    MapUtility.hideProgress()
                }
                Timber.tag(TAG).d("getAddressByGeoCodingLatLng: START")
                updateLocationDetails(mLatitude, mLongitude)
            }
        }

    private val latLngByRevGeoCodeFromAdd: Unit
        get() {
            if (mLatitude == 0.0 && mLongitude == 0.0) {
                if (MapUtility.popupWindow != null && MapUtility.popupWindow!!.isShowing) {
                    MapUtility.hideProgress()
                }

                Timber.tag(TAG).d("getLatLngByRevGeoCodeFromAdd: START")
                userAddress?.let {
                    getLatLngFromAddress(userAddress) { latLng ->
                        if (latLng != null) {
                            mLatitude = latLng.latitude
                            mLongitude = latLng.longitude
                        }
                    }
                }
            }
        }

    // Function to cancel previous tasks and launch the new one
    private fun updateLocationDetails(mLatitude: Double, mLongitude: Double) {
        // Cancel all previous tasks
        for (prevTask in filterTaskList) {
            prevTask.cancel()
        }
        filterTaskList.clear()

        // Launch the new coroutine task
        val job = CoroutineScope(Dispatchers.Main).launch {
            getAddressFromLatLng(mLatitude, mLongitude) { userAddress ->
                userAddress?.let {
                    this@LocationPickerActivity.userAddress = it.getString("fulladdress")
                    this@LocationPickerActivity.userAddressName = it.getString("name")
                }
                MapUtility.hideProgress()
                addMarker()
            }
        }
        filterTaskList.add(job)
    }

    private suspend fun getAddressFromLatLng(
        latitude: Double,
        longitude: Double,
        callback: (Bundle?) -> Unit
    ) {
        withContext(Dispatchers.Main) {
            MapUtility.showProgress(this@LocationPickerActivity)

            val addressBundle = withContext(Dispatchers.IO) {
                try {
                    val geocoder = Geocoder(this@LocationPickerActivity, Locale.getDefault())
                    val addresses: List<Address>?
                    val sb = StringBuilder()
                    addresses = geocoder.getFromLocation(latitude, longitude, 1)

                    if (addresses != null && addresses.isNotEmpty()) {
                        val address = addresses[0]
                        val bundle = Bundle()

                        address.featureName?.let {
                            bundle.putString("name", it)
                            sb.append(it).append(" ")
                        }

                        address.getAddressLine(0)?.let {
                            bundle.putString("addressline2", it)
                            sb.append(it).append(" ")
                        }

                        address.locality?.let {
                            bundle.putString("city", it)
                            sb.append(it).append(" ")
                        }

                        address.adminArea?.let {
                            bundle.putString("state", it)
                            sb.append(it).append(" ")
                        }

                        address.countryName?.let {
                            bundle.putString("country", it)
                            sb.append(it).append(" ")
                        }

                        address.postalCode?.let {
                            bundle.putString("postalcode", it)
                            sb.append(it).append(" ")
                        }

                        bundle.putString("fulladdress", sb.toString())
                        bundle
                    } else {
                        null
                    }
                } catch (e: IOException) {
                    Timber.tag(TAG).e("Exception occur at ${e.message}")
                    val errorBundle = Bundle()
                    errorBundle.putBoolean("error", true)
                    errorBundle
                }
            }

            callback(addressBundle)
        }
    }

//    @SuppressLint("MissingPermission")
//    fun getPlaceDetailsByResult(latitude: LatLng?, longitude: Double, callback: (Place?) -> Unit) {
//        try {
//            val placesClient: PlacesClient = Places.createClient(this)
//
//            val placeFields = listOf(
//                Place.Field.ID,
//                Place.Field.NAME,
//                Place.Field.ADDRESS,
//                Place.Field.LAT_LNG,
//            )
//            val request = FindCurrentPlaceRequest.newInstance(placeFields)
//
//            placesClient.findCurrentPlace(request).addOnSuccessListener { response ->
//                for (placeLikelihood: PlaceLikelihood in response.placeLikelihoods) {
//                    val place = placeLikelihood.place
//                    if (place.latLng?.latitude == latitude && place.latLng?.longitude == longitude) {
//                        callback(place)
//                        return@addOnSuccessListener
//                    }
//                }
//                callback(null)
//            }.addOnFailureListener { exception ->
//                Timber.tag(TAG).e("Exception occur at ${exception.message}")
//                callback(null)
//                val errorBundle = Bundle()
//                errorBundle.putBoolean("error", true)
//                errorBundle
//            }
//        } catch (e: Exception) {
//            Timber.tag(TAG).e("Exception occur at ${e.message}")
//            val errorBundle = Bundle()
//            errorBundle.putBoolean("error", true)
//            errorBundle
//        }
//    }

//    @SuppressLint("MissingPermission")
//    fun getPlaceDetails(latitude: Double, longitude: Double, callback: (Place?) -> Unit) {
//        try {
//            val placesClient: PlacesClient = Places.createClient(this)
//
//            val placeFields = listOf(
//                Place.Field.ID,
//                Place.Field.NAME,
//                Place.Field.ADDRESS,
//                Place.Field.LAT_LNG
//            )
//            val request = FindCurrentPlaceRequest.newInstance(placeFields)
//
//            placesClient.findCurrentPlace(request).addOnSuccessListener { response ->
//                var nearestPlace: Place? = null
//                var minDistance = Double.MAX_VALUE
//
//                for (placeLikelihood: PlaceLikelihood in response.placeLikelihoods) {
//                    val place = placeLikelihood.place
//                    val placeLatLng = place.latLng
//                    if (placeLatLng != null) {
//                        val distance = haversineDistance(latitude, longitude, placeLatLng.latitude, placeLatLng.longitude)
//                        if (distance < minDistance) {
//                            minDistance = distance
//                            nearestPlace = place
//                        }
//                    }
//                }
//                callback(nearestPlace)
//            }.addOnFailureListener { exception ->
//                Timber.tag(TAG).e("Exception occur at ${exception.message}")
//                callback(null)
//                val errorBundle = Bundle()
//                errorBundle.putBoolean("error", true)
//                errorBundle
//            }
//        } catch (e: Exception) {
//            Timber.tag(TAG).e("Exception occur at ${e.message}")
//            val errorBundle = Bundle()
//            errorBundle.putBoolean("error", true)
//            errorBundle
//        }
//    }


    // Function to calculate the Haversine distance between two points on the Earth
    fun haversineDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val R = 6371e3 // Earth's radius in meters
        val phi1 = lat1 * Math.PI / 180
        val phi2 = lat2 * Math.PI / 180
        val deltaPhi = (lat2 - lat1) * Math.PI / 180
        val deltaLambda = (lon2 - lon1) * Math.PI / 180

        val a = sin(deltaPhi / 2) * sin(deltaPhi / 2) +
                cos(phi1) * cos(phi2) *
                sin(deltaLambda / 2) * sin(deltaLambda / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return R * c // in meters
    }

    private fun getLatLngFromAddress(userAddress: String?, callback: (LatLng?) -> Unit) {
        // Cancel previous tasks
        for (prevJob in filterTaskList) {
            prevJob.cancel()
        }
        filterTaskList.clear()

        val job = CoroutineScope(Dispatchers.Main).launch {
            MapUtility.showProgress(this@LocationPickerActivity)

            val latLng = withContext(Dispatchers.IO) {
                var latLngResult: LatLng? = null
                try {
                    val geocoder = Geocoder(this@LocationPickerActivity, Locale.getDefault())
                    val addresses: List<Address> = geocoder.getFromLocationName(userAddress!!, 1)!!

                    if (addresses.isNotEmpty()) {
                        val address = addresses[0]
                        latLngResult = LatLng(address.latitude, address.longitude)
                    }
                } catch (ignored: Exception) {
                }
                latLngResult
            }

            callback(latLng)
            MapUtility.hideProgress()
        }
        filterTaskList.add(job)
    }


//    fun roundAvoid(value: Double): Double {
//        val scale: Double = 10.pow(6.0)
//        return Math.round(value * scale) / scale
//    }

    override fun onDestroy() {
        super.onDestroy()
        for (task in filterTaskList) {
            task.cancel()
        }
        filterTaskList.clear()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        mLocationPermissionGranted = false
        when (requestCode) {
            REQUEST_ID_MULTIPLE_PERMISSIONS -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    mLocationPermissionGranted = true
                }
            }

        }
    }

    override fun onResume() {
        super.onResume()
        //Do tasks for which permission was granted by user in onRequestPermission()
        if (!isFinishing && mLocationPermissionGranted) {
            // perform action required b4 asking permission
            mLocationPermissionGranted = false
            when (doAfterPermissionProvided) {
                1 -> startParsingAddressToShow()
                2 -> showCurrentLocationOnMap(false)
                3 -> showCurrentLocationOnMap(true)
            }
        }
    }

    private fun startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(
                this@LocationPickerActivity, "Location not Available", Toast.LENGTH_SHORT
            ).show()
            return
        }

        fusedLocationProviderClient!!.requestLocationUpdates(
            locationRequest!!,
            locationCallback!!,
            null /* Looper */
        )
            .addOnSuccessListener { Timber.tag(TAG).d("startLocationUpdates: onSuccess: ") }
            .addOnFailureListener { e ->
                if (e is ApiException) {
                    Timber.tag(TAG).d("startLocationUpdates: %s", e.message)
                } else {
                    Timber.tag(TAG).d("startLocationUpdates: %s", e.message)
                }
            }
    }

    private fun getLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest!!.setInterval(10000)
        locationRequest!!.setFastestInterval(3000)
        locationRequest!!.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
    }

    companion object {
        private const val REQUEST_CHECK_SETTINGS = 2
        private const val REQUEST_ID_MULTIPLE_PERMISSIONS = 2
    }
}

