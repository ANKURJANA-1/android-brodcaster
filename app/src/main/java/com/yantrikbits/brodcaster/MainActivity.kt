package com.yantrikbits.brodcaster

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.service.autofill.OnClickAction
import android.view.View.OnClickListener
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import com.yantrikbits.brodcaster.constants.BluetoothStatus
import com.yantrikbits.brodcaster.constants.Constant

class MainActivity : AppCompatActivity() {

    private var bluetoothStatus: BluetoothStatus = BluetoothStatus.OFF
    private lateinit var bluetoothOnOffButton: AppCompatButton
    private var bluetoothAdapter: BluetoothAdapter? = null
    private lateinit var bluetoothEnableIntent: Intent


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bluetoothOnOffButton = findViewById(R.id.bluetoothButton)
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        bluetoothEnableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        onOffBluetooth()
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


    private fun onOffBluetooth() {
        bluetoothOnOffButton.setOnClickListener {
            if (bluetoothStatus == BluetoothStatus.ON) {
                Toast.makeText(
                    this@MainActivity,
                    "Bluetooth already On.",
                    Toast.LENGTH_LONG
                )
                    .show()
            } else if (bluetoothStatus == BluetoothStatus.OFF) {
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
            } else {
                if (bluetoothAdapter!!.isEnabled) {
                    if (ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.BLUETOOTH_CONNECT
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                    }
                    bluetoothAdapter!!.disable()
                }
            }
        }
    }
}