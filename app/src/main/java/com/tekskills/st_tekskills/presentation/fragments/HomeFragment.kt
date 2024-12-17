package com.tekskills.st_tekskills.presentation.fragments

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.MPPointF
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.tekskills.st_tekskills.R
import com.tekskills.st_tekskills.data.model.AddCheckInRequest
import com.tekskills.st_tekskills.data.model.MeetingPurposeResponseData
import com.tekskills.st_tekskills.data.model.MeetingStatusRequest
import com.tekskills.st_tekskills.data.model.TaskInfo
import com.tekskills.st_tekskills.databinding.FragmentMainBinding
import com.tekskills.st_tekskills.presentation.activities.MainActivity
import com.tekskills.st_tekskills.presentation.adapter.ViewMeetingPurposeAdapter
import com.tekskills.st_tekskills.presentation.viewmodel.MainActivityViewModel
import com.tekskills.st_tekskills.utils.AppUtil
import com.tekskills.st_tekskills.utils.RestApiStatus
import com.tekskills.st_tekskills.utils.SmartDialog
import com.tekskills.st_tekskills.utils.SmartDialogBuilder
import com.tekskills.st_tekskills.utils.SmartDialogClickListener
import com.tekskills.st_tekskills.utils.geoLocation.NotificationWorker
import com.tekskills.geolocator.geofencer.Geofencer
import com.tekskills.geolocator.geofencer.models.Geofence
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class HomeFragment : ParentFragment(), OnChartValueSelectedListener {

    private lateinit var viewModel: MainActivityViewModel
    private lateinit var binding: FragmentMainBinding
    private val REQUEST_CALL_PHONE = 199


    @Inject
    @Named("view_main_meetings")
    lateinit var adapter: ViewMeetingPurposeAdapter

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        setOnClickListenerInit()
        observerData()

        viewModel.getTodayMeetingPurposeByStatus("Today")
        viewModel.getMeetingPurposeStatus()
        initRecyclerViewAdapter()
        createPieChart()
    }

    private fun observerData() {
        viewModel.resMeetingPurposeStatusDetails.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                RestApiStatus.SUCCESS -> {
                    binding.progress.visibility = View.GONE
                    if (it.data != null) {
                        setMeetingStatusData(it.data)
                    } else {
                        val meetingStatusRequest = MeetingStatusRequest(
                            approveCount = 0, pendingCount = 0, rejectCount = 0
                        )
                        setMeetingStatusData(meetingStatusRequest)
                    }
                }

                RestApiStatus.LOADING -> {
                    binding.progress.visibility = View.VISIBLE
                }

                RestApiStatus.ERROR -> {

                    val meetingStatusRequest = MeetingStatusRequest(
                        approveCount = 0, pendingCount = 0, rejectCount = 0
                    )

                    // Call the method with the static data
                    setMeetingStatusData(meetingStatusRequest)
                    binding.progress.visibility = View.GONE
                    Snackbar.make(binding.root, "Something went wrong", Snackbar.LENGTH_SHORT)
                        .show()
                }

                else -> {
                    binding.progress.visibility = View.GONE
                    Snackbar.make(binding.root, "Something went wrong", Snackbar.LENGTH_SHORT)
                        .show()
                }
            }
        })

        viewModel.resTodayMeetingPurposeList.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                RestApiStatus.SUCCESS -> {
                    binding.progress.visibility = View.GONE
                    if (it.data != null) {
                        it.data.let { listData ->
//                            val listData = filterMeetingsTodayTomorrow(list)
                            binding.avMeetings.visibility = if (listData.isEmpty()) View.VISIBLE
                            else View.GONE
                            adapter.differ.submitList(listData)

                            listData.forEach { meetingData ->
                                parseJsonToTaskInfo(meetingData).let {
                                    viewModel.insertOrUpdateTaskInfo(it)
                                }

                                var geofence = Geofence()

//                            geofence.message = res.userAddress.lineOne
                                meetingData.userCordinates.let { coordinates ->

                                    geofence.title =
                                        "Reached the ${meetingData.customerName} Location"

                                    geofence.message =
                                        "Purpose Of Visit ${meetingData.visitPurpose}, Time: ${meetingData.visitTime},"

                                    geofence.latitude =
                                        coordinates.destinationLatitude.toDouble()
                                    geofence.longitude =
                                        coordinates.destinationLongitude.toDouble()

                                    if (coordinates.checkInCordinates == null && coordinates.checkOutCordinates == null) {
                                        geofence.radius = 500.0
                                        geofence.locType = "Check_In"
                                        geofence.transitionType =
                                            com.google.android.gms.location.Geofence.GEOFENCE_TRANSITION_ENTER

                                    } else if (coordinates.checkInCordinates != null && coordinates.checkOutCordinates == null) {
                                        geofence.radius = 1000.0
                                        geofence.locType = "Check_Out"
                                        geofence.transitionType =
                                            com.google.android.gms.location.Geofence.GEOFENCE_TRANSITION_EXIT
                                    } else {
                                        geofence.radius = 500.0
                                        geofence.locType = "Completed"
                                        geofence.transitionType =
                                            com.google.android.gms.location.Geofence.GEOFENCE_TRANSITION_ENTER
                                    }

//                                    requireActivity().requestLocationPermission {
//                                        if (it.granted) {
                                    Geofencer(requireContext())
                                        .addGeofenceWorker(
                                            geofence,
                                            NotificationWorker::class.java
                                        ) {
                                            //                                            binding?.container?.isGone = true
                                            //                                            showGeofences()
                                        }
//                                        }
//                                    }
                                }
                            }


                            startLocationUpdates()
                        }
                    } else {
                        Snackbar.make(
                            binding.root, "No Data Found", Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }

                RestApiStatus.LOADING -> {
                    binding.progress.visibility = View.VISIBLE
                }

                RestApiStatus.ERROR -> {
                    binding.progress.visibility = View.GONE
                    Snackbar.make(binding.root, "Something went wrong", Snackbar.LENGTH_SHORT)
                        .show()
                }

                else -> {
                    binding.progress.visibility = View.GONE
                    Snackbar.make(binding.root, "Something went wrong", Snackbar.LENGTH_SHORT)
                        .show()
                }
            }
        })

        viewModel.resUserMeetingCheckIN.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                RestApiStatus.SUCCESS -> {
                    binding.progress.visibility = View.GONE
                    if (it.message != "success") {
                        viewModel.getMeetingPurpose("")
                    } else {
                        Snackbar.make(
                            binding.root, "Something Went Wrong Check-IN", Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }

                RestApiStatus.LOADING -> {
                    binding.progress.visibility = View.VISIBLE
                }

                RestApiStatus.ERROR -> {
                    binding.progress.visibility = View.GONE
                    Snackbar.make(
                        binding.root,
                        "Something Went Wrong Check-IN",
                        Snackbar.LENGTH_SHORT
                    )
                        .show()
                }

                else -> {
                    binding.progress.visibility = View.GONE
                    Snackbar.make(
                        binding.root,
                        "Something Went Wrong Check-IN",
                        Snackbar.LENGTH_SHORT
                    )
                        .show()
                }
            }
        })

        viewModel.resUserMeetingCheckOUT.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                RestApiStatus.SUCCESS -> {
                    binding.progress.visibility = View.GONE
                    if (it.message != "success") {
                        viewModel.getMeetingPurpose("")
                    } else {
                        Snackbar.make(
                            binding.root,
                            "Something Went Wrong Check-OUT",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }

                RestApiStatus.LOADING -> {
                    binding.progress.visibility = View.VISIBLE
                }

                RestApiStatus.ERROR -> {
                    binding.progress.visibility = View.GONE
                    Snackbar.make(
                        binding.root,
                        "Something Went Wrong Check-OUT",
                        Snackbar.LENGTH_SHORT
                    )
                        .show()
                }

                else -> {
                    binding.progress.visibility = View.GONE
                    Snackbar.make(
                        binding.root,
                        "Something Went Wrong Check-OUT",
                        Snackbar.LENGTH_SHORT
                    )
                        .show()
                }
            }
        })

    }

    private fun parseJsonToTaskInfo(responseData: MeetingPurposeResponseData): TaskInfo {

        val userCoordinates = responseData.userCordinates

        return TaskInfo(
            id = responseData.id,
            purposeOfVisit = responseData.visitPurpose ?: "",
            date = SimpleDateFormat("yyyy-MM-dd",
                Locale.getDefault()).parse(responseData.visitDate),
            priority = 1, // Assign appropriate value
            status = responseData.status == "Completed",
            category = "", // Assign appropriate value
            clientContactPerson = "", // Assign appropriate value
            clientContactPos = "", // Assign appropriate value
            opportunityDesc = "",
            TaskID = responseData.id,
            customerName = responseData.customerName ?: "",
            custmerEmail = responseData.custmerEmail ?: "",
            modeOfTravel = responseData.modeOfTravel ?: "",
            customerContactName = responseData.customerContactName ?: "",
            customerPhone = responseData.customerPhone.toString(),
            visitDate = responseData.visitDate,
            visitTime = responseData.visitTime,
            visitPurpose = responseData.visitPurpose,
            source = userCoordinates.source,
            sourceLatitude = userCoordinates.sourceLatitude ?: "0.00",
            sourceLongitude = userCoordinates.sourceLongitude ?: "0.00",
            destination = userCoordinates.destination ?: "",
            destinationLatitude = userCoordinates.destinationLatitude ?: "0.00",
            destinationLongitude = userCoordinates.destinationLongitude ?: "0.00",
            totalDistance = userCoordinates.totalDistance,
            checkInTime = userCoordinates.checkInTime ?: "",
            checkInCordinates = userCoordinates.checkInCordinates ?: "",
            checkOutTime = userCoordinates.checkOutTime ?: "",
            checkOutCordinates = userCoordinates.checkOutCordinates ?: "",
            mapTime = userCoordinates.mapTime
        )
    }

    private fun setOnClickListenerInit() {
        binding.swiperefresh.setOnRefreshListener {
            binding.swiperefresh.postDelayed({
                viewModel.getMeetingPurpose("")
                viewModel.getMeetingPurposeStatus()
                binding.swiperefresh.isRefreshing = false
            }, 50)
        }

        binding.llMeetings.setOnClickListener {
            val action =
                HomeFragmentDirections.actionHomeFragmentToViewMeetingPurposeFragment("")
            binding.root.findNavController().navigate(action)
        }
    }

    private fun initRecyclerViewAdapter() {
        binding.rvMeetings.adapter = adapter
        binding.rvMeetings.layoutManager = LinearLayoutManager(requireContext())

        adapter.setOnTaskStatusChangedListener {

        }
        adapter.setOnItemClickListener {
            val action =
                HomeFragmentDirections.actionHomeFragmentToViewMeetingDetailsFragment(
                    it.id.toString()
                )
            binding.root.findNavController().navigate(action)
        }
        adapter.setOnEditItemClickListener {

        }
        adapter.setOnCheckINItemClickListener {
            val action =
                HomeFragmentDirections.actionHomeFragmentToNewCheckInFragment(it.id.toString())
            binding.root.findNavController().navigate(action)
        }

        adapter.setOnCheckOUTItemClickListener {
            getCurrentLocation(it.id.toString(), it)
        }

        adapter.setOnAddMOMItemClickListener {
            val action =
                HomeFragmentDirections.actionHomeFragmentToNewAddMOMFragment(it.id.toString())
            binding.root.findNavController().navigate(action)
        }

        adapter.setOnCallContactPersonItemClickListener {
            makePhoneCall(it.customerPhone)
//            val callIntent = Intent(Intent.ACTION_CALL)
//            callIntent.setData(
//                Uri.parse("tel:" + it.customerPhone)
//            )
//            requireActivity().startActivity(callIntent)
        }

        adapter.setOnEmailContactPersonItemClickListener {
            sendEmail(it.custmerEmail)
//            val emailIntent = Intent(
//                Intent.ACTION_SENDTO, Uri.fromParts(
//                    "mailto", it.custmerEmail, null
//                )
//            )
//            requireActivity().startActivity(Intent.createChooser(emailIntent, ""))
        }


    }

    private fun getCurrentLocation(
        purposeID: String,
        meetingDetails: MeetingPurposeResponseData
    ) {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.lastLocation.addOnCompleteListener { task: Task<Location> ->
            if (task.isSuccessful && task.result != null) {
                val location: Location? = task.result
                location?.let {
                    val latitude = it.latitude
                    val longitude = it.longitude

                    val checkin = AddCheckInRequest(
                        latitude = latitude.toString(),
                        longitude = longitude.toString(),
                    )

                    meetingDetails?.let {
                        if (AppUtil.isWithinRange(
                                latitude, longitude, meetingDetails!!.userCordinates.destinationLatitude.toDouble(), meetingDetails!!.userCordinates.destinationLongitude.toDouble(),
                                1000F)) {
                            viewModel.putUserMeetingCheckOUT(purposeID, checkin)
                            Log.d("Location", "Lat: $latitude, Lon: $longitude")
                        } else {
                            SmartDialogBuilder(requireContext())
                                .setTitle("Note")
                                .setSubTitle("Your Not in Location Range")
                                .setCancalable(false)
                                .setCustomIcon(R.drawable.icon2)
                                .setTitleColor(resources.getColor(R.color.black))
                                .setSubTitleColor(resources.getColor(R.color.black))
                                .setNegativeButtonHide(true)
                                .useNeutralButton(true)
                                .setPositiveButton("Okay", object : SmartDialogClickListener {
                                    override fun onClick(smartDialog: SmartDialog?) {
                                        Log.d("TAG", "onViewCreated: okay for alert dialog exceeds"
                                        )
                                        smartDialog!!.dismiss()
                                    }
                                })
                                .setNegativeButton("Cancel", object : SmartDialogClickListener {
                                    override fun onClick(smartDialog: SmartDialog?) {
                                        smartDialog!!.dismiss()
                                    }
                                })
                                .setNeutralButton("Cancel", object : SmartDialogClickListener {
                                    override fun onClick(smartDialog: SmartDialog?) {
                                        smartDialog!!.dismiss()
                                    }
                                })
                                .build().show()
                        }
                    }
//
                }
            } else {
                Log.w("Location", "Failed to get location.")
            }
        }
    }

    private fun startLocationUpdates() {
        val locationRequest = LocationRequest.create().apply {
            interval = 10000 // 10 seconds
            fastestInterval = 5000 // 5 seconds
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                val range = 1000f // Example range in meters
                adapter.observeLocationResult(locationResult, range)
            }
        }

        if (ActivityCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.requestLocationUpdates(
            locationRequest, locationCallback, Looper.getMainLooper()
        )
    }

//    fun filterItemsByDate(items: MeetingPurposeResponse): ArrayList<MeetingPurposeResponseData> {
//        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
//        val today = Calendar.getInstance().time
//        val tomorrow = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 1) }.time
//        val todayString = sdf.format(today)
//        val tomorrowString = sdf.format(tomorrow)
//
//        return items.filter {
////            if (it.visitTime != null) {
////                it.visitTime == todayString || it.visitTime == tomorrowString
////            } else {
//            it.visitDate == todayString || it.visitDate == tomorrowString
////            }
//        }.sortedBy { LocalDate.parse(it.visitDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")) }
//            .toCollection(ArrayList())
//    }

    private fun createPieChart() {
        binding.pieChart.description.isEnabled = false
        binding.pieChart.setExtraOffsets(5F, 10F, 5F, 5F)
        binding.pieChart.setDragDecelerationFrictionCoef(0.95f)
        binding.pieChart.isDrawHoleEnabled = true
        binding.pieChart.setHoleColor(Color.WHITE)
        binding.pieChart.setTransparentCircleColor(Color.WHITE)
        binding.pieChart.setTransparentCircleAlpha(110)
        binding.pieChart.holeRadius = 60f
        binding.pieChart.transparentCircleRadius = 61f
        binding.pieChart.setDrawCenterText(true)

        // enable rotation of the chart by touch
        binding.pieChart.isRotationEnabled = true
        binding.pieChart.isHighlightPerTapEnabled = true
        binding.pieChart.rotationAngle = 20f

        // binding.pieChart.setUnit(" €")
        // binding.pieChart.setDrawUnitsInChart(true)

        // add a selection listener
        binding.pieChart.setOnChartValueSelectedListener(this)
        binding.pieChart.animateY(1400, Easing.EaseInOutQuad)

        // binding.pieChart.spin(2000, 0, 360)
        val l: Legend = binding.pieChart.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        l.orientation = Legend.LegendOrientation.HORIZONTAL
        l.setDrawInside(false)
        l.form = Legend.LegendForm.SQUARE
        l.formSize = 9f
        l.setTextSize(11f)
        l.xEntrySpace = 4f
        l.isWordWrapEnabled = true

        val myTypeface = ResourcesCompat.getFont(requireContext(), R.font.poppins_bold)

        // entry label styling
        binding.pieChart.setEntryLabelColor(Color.BLACK)
        binding.pieChart.setEntryLabelTextSize(12f)
        binding.pieChart.extraBottomOffset = 20f
        binding.pieChart.extraLeftOffset = 20f
        binding.pieChart.extraRightOffset = 20f
        binding.pieChart.setNoDataText("No Data Found")
        binding.pieChart.setNoDataTextTypeface(myTypeface)
        binding.pieChart.setNoDataTextColor(Color.BLACK)
    }

    private fun setMeetingStatusData(meetingStatusRequest: MeetingStatusRequest) {
        val entries = ArrayList<PieEntry>()
        meetingStatusRequest.let { count ->
            if (count.rejectCount == 0 && count.pendingCount == 0 && count.approveCount == 0) {

            } else {
                count.rejectCount.let {
                    if (it > 0)
                        entries.add(
                            PieEntry(
                                it.toFloat(),
                                "Rejected",
                                resources.getDrawable(R.drawable.icon2)
                            )
                        )
                }

                count.pendingCount.let {
                    if (it > 0)
                        entries.add(
                            PieEntry(
                                it.toFloat(),
                                "Pending",
                                resources.getDrawable(R.drawable.icon2)
                            )
                        )
                }

                count.approveCount.let {
                    if (it > 0)
                        entries.add(
                            PieEntry(
                                it.toFloat(),
                                "Approved",
                                resources.getDrawable(R.drawable.icon2)
                            )
                        )
                }

                val dataSet = PieDataSet(entries, "")
                dataSet.setDrawIcons(false)
                dataSet.sliceSpace = 10f
                dataSet.iconsOffset = MPPointF(0f, 40f)
                dataSet.selectionShift = 5f
                dataSet.xValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE;
                dataSet.yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE;
                dataSet.isHighlightEnabled = true
                dataSet.setAutomaticallyDisableSliceSpacing(true)
                val colors = ArrayList<Int>()

// Add new RGB colors
                colors.add(Color.rgb(255, 99, 132))   // rgb(255, 99, 132)
                colors.add(Color.rgb(54, 162, 235))   // rgb(54, 162, 235)
                colors.add(Color.rgb(255, 205, 86))   // rgb(255, 205, 86)
                colors.add(Color.rgb(201, 203, 207))  // rgb(201, 203, 207)
                colors.add(Color.rgb(75, 192, 192))   // rgb(75, 192, 192)

                dataSet.colors = colors
                //dataSet.setSelectionShift(0f);
                val data = PieData(dataSet)
//        data.setValueFormatter(PercentFormatter())
                data.setValueFormatter { value, _, _, _ -> value.toInt().toString() }
                data.setValueTextSize(12f)
                data.setValueTextColor(Color.BLACK)
                binding.pieChart.setData(data)
                binding.pieChart.setUsePercentValues(false)
                binding.pieChart.setNoDataText("No Data Found")
                binding.pieChart.setNoDataTextColor(Color.BLACK)

                // undo all highlights
                binding.pieChart.highlightValues(null)
                binding.pieChart.invalidate()
            }
        }
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
        if (e == null)
            return;
//        var statusInfo = ""
        val pe = e as PieEntry

//        h?.let {
//            when (h.x) {
//                0f -> {
//                    statusInfo = "Rejected"
//                }
//
//                1f -> {
//                    statusInfo = "Pending"
//                }
//
//                2f -> {
//                    statusInfo = "Approved"
//                }
//
//                else ->
//                    statusInfo = ""
//            }
//        }
        val action =
            HomeFragmentDirections.actionHomeFragmentToViewMeetingPurposeFragment(pe.label)
        binding.root.findNavController().navigate(action)

    }

    override fun onNothingSelected() {

    }

    private fun makePhoneCall(phoneNumber: String) {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CALL_PHONE), REQUEST_CALL_PHONE)
        } else {
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:$phoneNumber")
            startActivity(callIntent)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CALL_PHONE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(requireContext(), "Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendEmail(recipient: String, subject: String, message: String) {
        /*ACTION_SEND action to launch an email client installed on your Android device.*/
        val mIntent = Intent(Intent.ACTION_SEND)
        /*To send an email you need to specify mailto: as URI using setData() method
        and data type will be to text/plain using setType() method*/
        mIntent.data = Uri.parse("mailto:")
        mIntent.type = "text/plain"
        // put recipient email in intent
        /* recipient is put as array because you may wanna send email to multiple emails
           so enter comma(,) separated emails, it will be stored in array*/
        mIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
        //put the Subject in the intent
        mIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        //put the message in the intent
        mIntent.putExtra(Intent.EXTRA_TEXT, message)

        try {
            //start email intent
            startActivity(Intent.createChooser(mIntent, ""))
        }
        catch (e: Exception){
            //if any thing goes wrong for example no email client application or any exception
            //get and show exception message
            Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
        }
    }

    fun sendEmail(email: String) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:") // Only email apps should handle this
            putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
//            putExtra(Intent.EXTRA_SUBJECT, "")
//            putExtra(Intent.EXTRA_TEXT, "")
        }

        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(requireContext(), "No email clients installed.", Toast.LENGTH_SHORT).show()
        }
    }

}
