package com.tekskills.st_tekskills.presentation.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import com.tekskills.st_tekskills.presentation.viewmodel.MainActivityViewModel
import com.tekskills.st_tekskills.R
import com.google.android.material.snackbar.Snackbar
import com.tekskills.st_tekskills.data.model.AddCheckInRequest
import com.tekskills.st_tekskills.data.model.MeetingPurposeResponseData
import com.tekskills.st_tekskills.databinding.FragmentViewPurposeMeetingsBinding
import com.tekskills.st_tekskills.presentation.adapter.ViewMeetingPurposeAdapter
import com.tekskills.st_tekskills.utils.AppUtil
import com.tekskills.st_tekskills.utils.RestApiStatus
import com.tekskills.st_tekskills.utils.SmartDialog
import com.tekskills.st_tekskills.utils.SmartDialogBuilder
import com.tekskills.st_tekskills.utils.SmartDialogClickListener
import com.tekskills.st_tekskills.utils.SuccessResource
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class ViewMeetingPurposeFragment : ParentFragment() {
    val viewModel: MainActivityViewModel by viewModels()
    private lateinit var binding: FragmentViewPurposeMeetingsBinding

    @Inject
    @Named("view_meetings")
    lateinit var adapter: ViewMeetingPurposeAdapter
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val args: ViewMeetingPurposeFragmentArgs by navArgs()
    private var opportunityID: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_view_purpose_meetings, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        viewModel = (activity as MainActivity).viewModel

        if (args.opportunityID != null) {
            opportunityID = args.opportunityID!!
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        viewModel._resMeetingPurposeList.postValue(SuccessResource.loading(null))

        adapter.setOnTaskStatusChangedListener {

        }
        adapter.setOnItemClickListener {
            val action =
                ViewMeetingPurposeFragmentDirections.actionViewMeetingsToViewMeetingDetailsFragment(
                    it.id.toString()
                )
            binding.root.findNavController().navigate(action)
//            editTaskInformation(it)
        }
        adapter.setOnEditItemClickListener {
//            val action =
//                ViewMeetingPurposeFragmentDirections.actionViewOpportunityFragmentToEditAssignedProjectFragment()
//            binding.root.findNavController().navigate(action)
//            editTaskInformation(it)
        }
        adapter.setOnCheckINItemClickListener {
            val action =
                ViewMeetingPurposeFragmentDirections.actionViewMeetingsToNewCheckInFragment(it.id.toString())
            binding.root.findNavController().navigate(action)
//            viewModel.putUserMeetingCheckIN(it.id.toString())
        }

        adapter.setOnCheckOUTItemClickListener {
            getCurrentLocation(it.id.toString(),it)
        }

        adapter.setOnAddMOMItemClickListener {
            val action =
                ViewMeetingPurposeFragmentDirections.actionViewMeetingsToNewAddMOMFragment(it.id.toString())
            binding.root.findNavController().navigate(action)
        }

        adapter.setOnCallContactPersonItemClickListener {
//            val callIntent = Intent(Intent.ACTION_CALL)
//            callIntent.setData(
//                Uri.parse("tel:" + it.customerPhone))
//            requireActivity().startActivity(callIntent)
        }

        adapter.setOnEmailContactPersonItemClickListener {
//            val emailIntent = Intent(
//                Intent.ACTION_SENDTO, Uri.fromParts(
//                    "mailto", it.custmerEmail, null
//                )
//            )
//            requireActivity().startActivity(Intent.createChooser(emailIntent, ""))
        }

        initRecyclerView()

        callMeetingPurposeDetails()

//        viewModel._resMeetingPurposeList.value = DummyData().createDummyData()

        viewModel.resMeetingPurposeList.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                RestApiStatus.SUCCESS -> {
                    binding.progress.visibility = View.GONE
                    if (it.data != null) {
                        it.data.let { list ->
                            if (list.isEmpty()) binding.avMeetings.visibility = View.VISIBLE
                            else binding.avMeetings.visibility = View.GONE
                            adapter.differ.submitList(list)
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
                        callMeetingPurposeDetails()
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
                        callMeetingPurposeDetails()
                    } else {
                        Snackbar.make(
                            binding.root, "Something Went Wrong Check-OUT", Snackbar.LENGTH_SHORT
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

        binding.fab.setOnClickListener {
            val action =
                ViewMeetingPurposeFragmentDirections.actionViewMeetingsToNewMeetingPurpose("")
            it.findNavController().navigate(action)
        }
    }


//    private fun makePhoneCall(phoneNumber: String) {
//        if (phoneNumber.trim().isNotEmpty()) {
//            if (ContextCompat.checkSelfPermission(
//                    requireContext(),
//                    android.Manifest.permission.CALL_PHONE
//                ) != PackageManager.PERMISSION_GRANTED
//            ) {
//                ActivityCompat.requestPermissions(
//                    requireActivity(),
//                    arrayOf(android.Manifest.permission.CALL_PHONE),
//                    REQUEST_CALL_PERMISSION
//                )
//            } else {
//                val dialIntent = Intent(Intent.ACTION_CALL)
//                dialIntent.data = Uri.parse("tel:$phoneNumber")
//                startActivity(dialIntent)
//            }
//        } else {
////            Toast.makeText(this, "Phone number is empty", Toast.LENGTH_SHORT).show()
//        }
//    }

    private fun callMeetingPurposeDetails() {
        if (opportunityID != null && opportunityID != "")
            viewModel.getMeetingPurposeByStatus(opportunityID)
        else
            viewModel.getMeetingPurpose("")
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
                                latitude,
                                longitude,
                                meetingDetails!!.userCordinates.destinationLatitude.toDouble(),
                                meetingDetails!!.userCordinates.destinationLongitude.toDouble(),
                                1000F
                            )
                        ) {
                            viewModel.putUserMeetingCheckOUT(purposeID, checkin)
                            Timber.tag("Location").d("Lat: $latitude, Lon: $longitude")
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
                                        Timber.tag("TAG")
                                            .d("onViewCreated: okay for alert dialog exceeds")
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
                }
            } else {
                Timber.tag("Location").w("Failed to get location.")
            }
        }
    }

    private fun initRecyclerView() {
        binding.rvMeetings.adapter = adapter
        binding.rvMeetings.layoutManager = LinearLayoutManager(requireContext())
//        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
//        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }

    companion object {
        private const val REQUEST_CALL_PERMISSION = 1
    }
}