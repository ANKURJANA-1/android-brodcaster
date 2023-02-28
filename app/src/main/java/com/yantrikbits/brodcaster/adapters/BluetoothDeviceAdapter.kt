package com.yantrikbits.brodcaster.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.yantrikbits.brodcaster.R
import com.yantrikbits.brodcaster.model.BTDevice

class BluetoothDeviceAdapter(
    private val context: Context,
    private val devices: ArrayList<BTDevice> = ArrayList()
) :
    RecyclerView.Adapter<BluetoothDeviceAdapter.BluetoothDeviceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BluetoothDeviceViewHolder {
        return BluetoothDeviceViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.btdevice_layout,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BluetoothDeviceViewHolder, position: Int) {
        holder.bound(devices[position])
    }

    override fun getItemCount(): Int {
        return devices.size
    }

    inner class BluetoothDeviceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val name: AppCompatTextView = view.findViewById(R.id.device_name)
        private val address: AppCompatTextView = view.findViewById(R.id.device_address)

        fun bound(device: BTDevice) {
            name.text = device.deviceName
            address.text = device.address
        }
    }
}


