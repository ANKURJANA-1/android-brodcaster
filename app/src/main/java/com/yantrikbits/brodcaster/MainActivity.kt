package com.yantrikbits.brodcaster

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.permissionx.guolindev.PermissionX
import com.yantrikbits.brodcaster.constants.BluetoothStatus
import com.yantrikbits.brodcaster.constants.Constant

class MainActivity : AppCompatActivity() {

    private var bluetoothStatus: BluetoothStatus = BluetoothStatus.OFF
    private lateinit var bluetoothOnButton: AppCompatButton
    private lateinit var bluetoothOffButton: AppCompatButton
    private var bluetoothAdapter: BluetoothAdapter? = null
    private lateinit var bluetoothEnableIntent: Intent


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bluetoothOnButton = findViewById(R.id.bluetoothOnButton)
        bluetoothOffButton = findViewById(R.id.bluetoothOffButton)
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        bluetoothEnableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        permission()
        onBluetooth()
        bluetoothOff()
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
    }

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

    private fun permission() {
        PermissionX.init(this)
            .permissions(
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.BLUETOOTH_ADMIN
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

}