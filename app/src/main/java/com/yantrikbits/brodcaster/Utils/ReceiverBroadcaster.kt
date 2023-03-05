package com.yantrikbits.brodcaster.Utils

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.yantrikbits.brodcaster.model.BTDevice

class ReceiverBroadcaster(val devices: ArrayList<BTDevice>) : BroadcastReceiver() {
    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent!!.action) {
            BluetoothDevice.ACTION_FOUND -> {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                val device: BluetoothDevice? =
                    intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                devices.add(
                    BTDevice(
                        deviceName = device!!.name,
                        UUIDS = device!!.uuids.toString(),
                        address = device.address
                    )
                )
            }
        }
    }
}