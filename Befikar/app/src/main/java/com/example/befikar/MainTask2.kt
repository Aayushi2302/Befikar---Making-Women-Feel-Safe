package com.example.befikar

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.SmsManager
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.befikar.utils.PermissionUtils
import com.google.android.gms.location.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main_task2.*
import java.util.*

class MainTask2 : AppCompatActivity() {

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 999
    }

    private lateinit var tmpList : ArrayList<HashMap<String,String>>
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var stopMessage : Button


    val userId = FirebaseAuth.getInstance().currentUser!!.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_task2)

        setSupportActionBar(appBarMainTask2)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            appBarMainTask2.setNavigationOnClickListener {
                finish()
            }
        }

        tmpList = ArrayList()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        stopMessage = findViewById(R.id.stopMessage)

    }

    override fun onStart() {
        super.onStart()
        when {
            PermissionUtils.isAccessFineLocationGranted(this) -> {
                when {
                    PermissionUtils.isLocationEnabled(this) -> {
                        setUpLocationListener()
                    }
                    else -> {
                        PermissionUtils.showGPSNotEnabledDialog(this)
                    }
                }
            }
            else -> {
                PermissionUtils.requestAccessFineLocationPermission(
                    this,
                    LOCATION_PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    private fun setUpLocationListener() {

        loadEmergencyContacts()
        val locationRequest = LocationRequest().setInterval(180000).setFastestInterval(180000)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            PermissionUtils.requestAccessFineLocationPermission(
                this,
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
               object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)

                        for (location in locationResult.locations) {
                            latitude.text = location.latitude.toString()
                            longitude.text = location.longitude.toString()
                        }

                        val myMsg = "https://www.google.com/maps/search/?api=1&query="+latitude.text+","+longitude.text
                        val smsManager: SmsManager = SmsManager.getDefault()
                        for(element in tmpList){
                            for(ele in element){
                                if(ele.key == "Number"){
                                    smsManager.sendTextMessage(ele.value, null, myMsg, null, null)
                                }
                            }
                        }

                        stopMessage.setOnClickListener {
                            fusedLocationProviderClient.removeLocationUpdates(this)
                        }

                }
            },
            null
            //Looper.myLooper()
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    when {
                        PermissionUtils.isLocationEnabled(this) -> {
                            setUpLocationListener()
                        }
                        else -> {
                            PermissionUtils.showGPSNotEnabledDialog(this)
                        }
                    }
                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.permission_not_granted),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun loadEmergencyContacts() {
        val rootRef : FirebaseFirestore = FirebaseFirestore.getInstance()
        val applicationRef : CollectionReference = rootRef.collection("emergency")

        val applicationIdRef : DocumentReference = applicationRef.document(userId)
        applicationIdRef.get().addOnCompleteListener {task ->
            if (task.isSuccessful)
            {
                val document: DocumentSnapshot = task.result

                if(document.exists())
                {
                    tmpList = document.get("emergency contacts") as ArrayList<HashMap<String, String>>
                }
            }
        }
    }
}