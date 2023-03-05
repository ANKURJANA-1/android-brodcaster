package com.yantrikbits.brodcaster.ui

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.permissionx.guolindev.PermissionX
import com.yantrikbits.brodcaster.R
import com.yantrikbits.brodcaster.Utils.ReceiverBroadcaster
import com.yantrikbits.brodcaster.adapters.BluetoothDeviceAdapter
import com.yantrikbits.brodcaster.constants.BluetoothStatus
import com.yantrikbits.brodcaster.constants.Constant
import com.yantrikbits.brodcaster.model.BTDevice

class MainActivity : AppCompatActivity() {

    private var bluetoothStatus: BluetoothStatus = BluetoothStatus.OFF
    private lateinit var bluetoothOnButton: AppCompatButton
    private lateinit var bluetoothOffButton: AppCompatButton
    private lateinit var boundDevices: RecyclerView
    private var bluetoothAdapter: BluetoothAdapter? = null
    private lateinit var bluetoothEnableIntent: Intent
    private lateinit var bluetoothDiscoveryIntentFilter: IntentFilter
    private val listOfDevice: ArrayList<BTDevice> = ArrayList()
    private lateinit var receiverBroadcaster: ReceiverBroadcaster

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bluetoothOnButton = findViewById(R.id.bluetoothOnButton)
        bluetoothOffButton = findViewById(R.id.bluetoothOffButton)
        boundDevices = findViewById(R.id.bound_devices)
        receiverBroadcaster = ReceiverBroadcaster(listOfDevice)
        val bluetoothManager = this.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter
        bluetoothEnableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        permission()
        onBluetooth()
        bluetoothOff()
        //getBoundedDevice()
        addDataToBluetoothDevicesAdapter()
        registerReceiver()
    }

    @Deprecated("Deprecated in Java")
    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Constant.REQUEST_CODE_FOR_BLUETOOTH_ENABLE) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(
                    this@MainActivity,
                    "Bluetooth is On.",
                    Toast.LENGTH_LONG
                ).show()
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(
                    this@MainActivity,
                    "Bluetooth Enable Cancelled.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }


    @SuppressLint("MissingPermission")
    private fun onBluetooth() {
        bluetoothOnButton.setOnClickListener {
            if (bluetoothAdapter == null) {
                Toast.makeText(
                    this@MainActivity,
                    "Your Device NotSupport Bluetooth",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                if (!bluetoothAdapter!!.isEnabled) {
                    startActivityForResult(
                        bluetoothEnableIntent,
                        Constant.REQUEST_CODE_FOR_BLUETOOTH_ENABLE
                    )
                }
            }
            bluetoothStatus = BluetoothStatus.ON
        }
        bluetoothAdapter!!.startDiscovery()
    }

    @SuppressLint("MissingPermission")
    private fun bluetoothOff() {
        bluetoothOffButton.setOnClickListener {
            if (bluetoothAdapter!!.isEnabled) {
                bluetoothAdapter!!.disable()
                bluetoothStatus = BluetoothStatus.OFF
                Toast.makeText(
                    this@MainActivity,
                    "Bluetooth off",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun permission() {
        PermissionX.init(this)
            .permissions(
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.BLUETOOTH_SCAN
            )
            .onExplainRequestReason { scope, deniedList ->
                scope.showRequestReasonDialog(
                    deniedList,
                    "Core fundamental are based on these permissions",
                    "OK",
                    "Cancel"
                )
            }
            .request { allGranted, grantedList, deniedList ->
                if (allGranted) {
                    Toast.makeText(this, "All permissions are granted", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(
                        this,
                        "These permissions are denied: $deniedList",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    @SuppressLint("MissingPermission")
    private fun getBoundedDevice() {
        val findBluetoothDevices = bluetoothAdapter!!.bondedDevices
        findBluetoothDevices.forEach { device ->
            listOfDevice.add(
                BTDevice(
                    deviceName = device.name,
                    UUIDS = device.uuids.toString(),
                    address = device.address
                )
            )
        }
    }

    private fun addDataToBluetoothDevicesAdapter() {
        boundDevices.adapter = BluetoothDeviceAdapter(
            this@MainActivity,
            listOfDevice
        )
        boundDevices.layoutManager = LinearLayoutManager(this@MainActivity)
        boundDevices.hasFixedSize()
    }

    @SuppressLint("MissingPermission")
    private fun registerReceiver() {
        bluetoothAdapter!!.startDiscovery()
        bluetoothDiscoveryIntentFilter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        registerReceiver(
            receiverBroadcaster,
            bluetoothDiscoveryIntentFilter
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiverBroadcaster)
    }
}