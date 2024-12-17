package com.tekskills.st_tekskills.presentation.activities

import android.Manifest
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.View.GONE
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.google.android.gms.location.LocationResult
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.tekskills.st_tekskills.R
import com.tekskills.st_tekskills.databinding.ActivityMainBinding
import com.tekskills.st_tekskills.data.model.AddLocationCoordinates
import com.tekskills.st_tekskills.data.model.LocationResponse
import com.tekskills.st_tekskills.presentation.viewmodel.MainActivityViewModel
import com.tekskills.st_tekskills.utils.NetworkObserver
import com.tekskills.st_tekskills.utils.RestApiStatus
import com.tekskills.st_tekskills.utils.SmartDialog
import com.tekskills.st_tekskills.utils.SmartDialogBuilder
import com.tekskills.st_tekskills.utils.SmartDialogClickListener
import com.tekskills.st_tekskills.utils.SuccessResource
import com.tekskills.st_tekskills.utils.location.executeUnderLocationPermission
import com.tekskills.st_tekskills.utils.location.workers.LocationWorker
import com.tekskills.st_tekskills.utils.recording.BackgroundRecordingService
import com.tekskills.st_tekskills.utils.recording.FileUploadWorker
import com.tekskills.st_tekskills.utils.recording.FileUploadWorker.Companion.sendNotification
import com.tekskills.geolocator.geofencer.Geofencer
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    val viewModel: MainActivityViewModel by viewModels()
    private var hasNotificationPermissionGranted = false
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private val PERMISSION_REQUEST_CODE = 562

    private val permissions = arrayOf(
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.MODIFY_AUDIO_SETTINGS,
        Manifest.permission.READ_PHONE_STATE,
    )

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        navViewInit()
        Geofencer(this).removeAll {}
        viewModel.getEmployeeMe()
        observerData()
        updateWorkManager()
        setupPermissions()
//        checkAndRequestPermissions()
    }

    private fun checkAndRequestPermissions() {
        val notGrantedPermissions = ArrayList<String>()

        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED
            ) {
                notGrantedPermissions.add(permission)
            }
        }

        if (notGrantedPermissions.isNotEmpty()) {
            // Request permissions
            ActivityCompat.requestPermissions(
                this,
                notGrantedPermissions.toTypedArray(),
                PERMISSION_REQUEST_CODE
            )
        } else {
            // All permissions granted, proceed with audio recording
//            sendNotification("Permission Granted", "Permission Granted Successfully")
            startRecording()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                    // All permissions granted, proceed with audio recording
                    startRecording()
                } else {
                    // Permission denied, inform the user and ask to enable from settings
                    showPermissionDialog()
                }
            }
        }
    }

    private fun showPermissionDialog() {
        MaterialAlertDialogBuilder(
            this,
            com.google.android.material.R.style.MaterialAlertDialog_Material3
        ).setTitle("Permissions Required")
            .setMessage("Please enable the required permissions from the app settings.")
            .setPositiveButton("Go to Settings") { _, _ ->
                openAppSettings()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(false)
            .show()
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivityForResult(intent, PERMISSION_REQUEST_CODE)
    }

    fun upload(view: View) {
        val context = view.context
        val filePath =
            context.getExternalFilesDir(null)?.absolutePath + "/recorded_call.mp3"

        val uploadWorkRequest =
            OneTimeWorkRequest.Builder(FileUploadWorker::class.java)
                .setInputData(workDataOf(FileUploadWorker.KEY_FILE_PATH to filePath))
                .build()
        context.sendNotification("File Enque", "Enque.")
        WorkManager.getInstance(context).enqueue(uploadWorkRequest)
    }

    private fun startRecording() {
        Timber.d("startRecording => ")


        try {
            // Start the BackgroundRecordingService
            val serviceIntent = Intent(this, BackgroundRecordingService::class.java)

            // Use startForegroundService for Android 8.0 (Oreo) or higher
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent)
//        }
//        else {
//            startService(serviceIntent)
//        }
        }catch (e:Exception)
        {
            Timber.e("startRecording MainActivity => ${e.toString()} ")
        }
    }

    private fun updateWorkManager() {
        executeUnderLocationPermission(this) {
            createWorkManager(context = this)
        }
    }

    private fun setupPermissions() {
        // Sets up permissions request launcher.
        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                if (!it) {
                    Snackbar.make(
                        findViewById<View>(android.R.id.content).rootView,
                        "Please grant Notification permission from App Settings",
                        Snackbar.LENGTH_LONG
                    ).show()
//                    showSettingDialog()
                }
            }
        //        if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
        //            showNotification()
        //        }
        if (Build.VERSION.SDK_INT >= 33) {
            notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        } else {
            hasNotificationPermissionGranted = true
        }
    }

    private fun observerData() {
        LocationLiveData.locationData.observe(this) { locationResult ->
            // Handle the location data here
            locationResult?.let { data ->

                val userCoordinates = AddLocationCoordinates(
                    longitude = data.lastLocation?.longitude.toString(),
                    lattitude = data.lastLocation?.latitude.toString()
                )

                Timber.d("onViewCreated: print response $userCoordinates")

                viewModel.addUserCoordinates(userCoordinates)
            }
        }

        viewModel.resEmployeeMe.observe(this) {
            when (it.status) {
                RestApiStatus.SUCCESS -> {
                    if (it.data != null)
                        it.data.let { res ->
                            viewModel.saveUserEmployeeID(res.securityUser.employeeMaster.id.toString())
                            viewModel.saveUserRoleID(res.securityUser.employeeMaster.roleId.toString())
                            val headerView: View? = binding.navView.getHeaderView(0)
                            headerView?.let { view ->
                                val headerTextView =
                                    view.findViewById<TextView>(R.id.tv_nav_header_title)
                                val headerEmail =
                                    view.findViewById<TextView>(R.id.tv_nav_header_email)
                                val headerRole =
                                    view.findViewById<TextView>(R.id.tv_nav_header_role)

                                val userName = res.securityUser.employeeMaster.userName
                                val userEmail = res.securityUser.employeeMaster.email

                                viewModel.saveUserName(userName ?: getString(R.string.app_name))
                                headerTextView?.text = userName ?: getString(R.string.app_name)
                                headerEmail?.text = userEmail ?: ""
                            }

//                            var geofence = Geofence()
//
//                            geofence.title = "Reached Home"
////                            geofence.message = res.userAddress.lineOne
//                            res.userAddress?.let { address ->
//                                geofence.message = with(address) {
//                                    "$lineOne, $lineTwo, $city, $state, $country, $zpiCode"
//                                }
//                                val coordinates = address.coOrdinates.split(",")
//                                geofence.latitude = coordinates[0].toDouble()
//                                geofence.longitude = coordinates[1].toDouble()
//                                geofence.radius = 30.0
//                                geofence.transitionType =
//                                    GEOFENCE_TRANSITION_ENTER
//                                geofence.locType = "User"
////                                requestLocationPermission {
////                                    if (it.granted) {
//                                Geofencer(this)
//                                    .addGeofenceWorker(geofence, NotificationWorker::class.java) {
////                                            binding?.container?.isGone = true
////                                            showGeofences()
//                                    }
////                                    }
////                                }
//                            }
                        }
                    else {
                        Snackbar.make(
                            binding.root,
                            "Login Failed",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }

                RestApiStatus.LOADING -> {
//                        binding.progress.visibility = View.VISIBLE
                }

                RestApiStatus.ERROR -> {
//                        binding.progress.visibility = View.GONE
                    SmartDialogBuilder(this@MainActivity)
                        .setTitle("Log Out")
                        .setSubTitle("Session Expired")
                        .setCancalable(false)
                        .setCustomIcon(R.drawable.icon2)
                        .setTitleColor(ContextCompat.getColor(this, R.color.black))
                        .setSubTitleColor(ContextCompat.getColor(this, R.color.black))
                        .setNegativeButtonHide(true)
                        .useNeutralButton(true)
                        .setPositiveButton("Okay", object : SmartDialogClickListener {
                            override fun onClick(smartDialog: SmartDialog?) {
                                if (viewModel.clearSharedPreference()) {
                                    val intent =
                                        Intent(this@MainActivity, SplashActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                                smartDialog!!.dismiss()
                            }
                        })
                        .setNegativeButton("Cancel", object : SmartDialogClickListener {
                            override fun onClick(smartDialog: SmartDialog?) {
                                if (viewModel.clearSharedPreference()) {
                                    val intent =
                                        Intent(this@MainActivity, SplashActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                                smartDialog!!.dismiss()
                            }
                        })
                        .setNeutralButton("Cancel", object : SmartDialogClickListener {
                            override fun onClick(smartDialog: SmartDialog?) {
                                if (viewModel.clearSharedPreference()) {
                                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                                    val intent =
                                        Intent(this@MainActivity, SplashActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                                smartDialog!!.dismiss()
                            }
                        })
                        .build().show()

                    Snackbar.make(binding.root, "Log Out", Snackbar.LENGTH_SHORT)
                        .show()
                }
//                else -> {
////                        binding.progress.visibility = View.GONE
//                    Snackbar.make(binding.root, "Log Out", Snackbar.LENGTH_SHORT)
//                        .show()
//                }
            }
        }

        viewModel.networkLiveData.observe(this) {
            observeUserNetworkConnection(it)
        }

        NetworkObserver.getNetworkLiveData(applicationContext)
            .observe(this) { isConnected ->
                viewModel._networkLiveData.value = isConnected
            }
    }

    private fun navViewInit() {
        setSupportActionBar(binding.appBarMain.toolbar)

        val navController = findNavController(R.id.nav_fragment)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.home_fragment,
//                R.id.view_opportunity_fragment,
                R.id.new_purpose_meeting,
                R.id.view_meetings,
                R.id.settings_fragment,
                R.id.map_fragment,
                R.id.logout
            ), binding.drawerLayout
        )

        binding.navView.menu.apply {
            findItem(R.id.logout)
                .setOnMenuItemClickListener {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    logOutDevice()
//                    if (viewModel.clearSharedPreference()) {
//                        val intent = Intent(this@MainActivity, SplashActivity::class.java)
//                        startActivity(intent)
//                        finish()
//                    }
                    true
                }

            findItem(R.id.home_fragment)
                .setOnMenuItemClickListener {
                    val intent = Intent(this@MainActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                    true
                }

            findItem(R.id.view_meetings)
                .setOnMenuItemClickListener {
                    val bundle = Bundle().apply {
                        putString("opportunityID", "")
                    }
                    findNavController(R.id.nav_fragment).navigate(R.id.view_meetings, bundle)
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }

            findItem(R.id.new_purpose_meeting)
                .setOnMenuItemClickListener {
                    findNavController(R.id.nav_fragment).navigate(R.id.new_purpose_meeting)
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
        }

        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)
    }

    private fun observeUserNetworkConnection(isConnected: Boolean) {

        if (!isConnected) {
            binding.appBarMain.includeContainMain.textViewNetworkStatus.text =
                getString(R.string.no_internet)
            binding.appBarMain.includeContainMain.networkStatusLayout.apply {
                binding.appBarMain.includeContainMain.networkStatusLayout.visibility = View.VISIBLE
                setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        android.R.color.holo_red_light
                    )
                )
            }
        } else {
            binding.appBarMain.includeContainMain.textViewNetworkStatus.text =
                getString(R.string.back_online)

            binding.appBarMain.includeContainMain.networkStatusLayout.apply {
                animate()
                    .alpha(1f)
                    .setStartDelay(1000)
                    .setDuration(1000)
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            binding.appBarMain.includeContainMain.networkStatusLayout.visibility =
                                View.GONE
                        }
                    }).start()
                setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        android.R.color.holo_green_dark
                    )
                )
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun createWorkManager(context: Context) {
        val updateInterval = 15L

        val locationRequest: PeriodicWorkRequest = PeriodicWorkRequest.Builder(
            LocationWorker::class.java,
            updateInterval,
            TimeUnit.MINUTES
        ).setInitialDelay(0, TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "LocationUpdates",
            ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
            locationRequest
        )
    }

    private val notificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            hasNotificationPermissionGranted = isGranted
            if (!isGranted) {
                if (Build.VERSION.SDK_INT >= 33) {
                    if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {
                        showNotificationPermissionRationale()
                    } else {
                        showSettingDialog()
                    }
                }
            } else {
                Timber.tag("TAG").d("notification permission granted")
//                Toast.makeText(applicationContext, "notification permission granted", Toast.LENGTH_SHORT)
//                    .show()
            }
        }

    private fun showSettingDialog() {
        MaterialAlertDialogBuilder(
            this,
            com.google.android.material.R.style.MaterialAlertDialog_Material3
        )
            .setTitle("Notification Permission")
            .setMessage("Notification permission is required, Please allow notification permission from setting")
            .setPositiveButton("Ok") { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.parse("package:$packageName")
                startActivity(intent)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showNotificationPermissionRationale() {
        MaterialAlertDialogBuilder(
            this,
            com.google.android.material.R.style.MaterialAlertDialog_Material3
        )
            .setTitle("Alert")
            .setMessage("Notification permission is required, to show notification")
            .setPositiveButton("Ok") { _, _ ->
                if (Build.VERSION.SDK_INT >= 33) {
                    notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun logOutDevice() {
        SmartDialogBuilder(this@MainActivity)
            .setTitle("LogOut")
            .setSubTitle("Press Okay To Logout")
            .setCancalable(false)
            .setCustomIcon(R.drawable.icon2)
            .setTitleColor(resources.getColor(R.color.black))
            .setSubTitleColor(resources.getColor(R.color.black))
            .setNegativeButtonHide(true)
            .useNeutralButton(false)
            .setPositiveButton("Okay", object : SmartDialogClickListener {
                override fun onClick(smartDialog: SmartDialog?) {
                    logOutClearData()
                    smartDialog!!.dismiss()
                }
            })
            .setNegativeButton("Cancel", object : SmartDialogClickListener {
                override fun onClick(smartDialog: SmartDialog?) {
                    viewModel?._loading?.value = GONE
                    smartDialog!!.dismiss()
                }
            })
            .setNeutralButton("Cancel", object : SmartDialogClickListener {
                override fun onClick(smartDialog: SmartDialog?) {
                    viewModel?._loading?.value = GONE
                    smartDialog!!.dismiss()
                }
            })
            .build().show()
    }

    fun logOutClearData()
    {
        if (viewModel.clearSharedPreference()) {
            val intent = Intent(this@MainActivity, SplashActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


}

object LocationLiveData {
    private val _locationData = MutableLiveData<LocationResult>()
    val locationData: LiveData<LocationResult>
        get() = _locationData

    fun setLocationData(locationResult: LocationResult) {
        _locationData.value = locationResult
    }

    val _resAddUserCoordinates =
        MutableLiveData<SuccessResource<LocationResponse>>()

    val resAddUserCoordinates: LiveData<SuccessResource<LocationResponse>>
        get() = _resAddUserCoordinates

}