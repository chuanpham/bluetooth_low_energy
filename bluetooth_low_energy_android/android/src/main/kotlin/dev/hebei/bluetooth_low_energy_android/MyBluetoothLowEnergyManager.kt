package dev.hebei.bluetooth_low_energy_android

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.PluginRegistry

abstract class MyBluetoothLowEnergyManager(val context: Context) {
    companion object {
        const val AUTHORIZE_CODE = 0x00
        const val SHOW_APP_SETTINGS_CODE = 0x01
    }

    private val mBroadcastReceiver: BroadcastReceiver by lazy { MyBroadcastReceiver(this) }
    private val mRequestPermissionsResultListener: PluginRegistry.RequestPermissionsResultListener by lazy { MyRequestPermissionResultListener(this) }
    private val mActivityResultListener: PluginRegistry.ActivityResultListener by lazy { MyActivityResultListener(this) }

    private lateinit var mBinding: ActivityPluginBinding

    init {
        val filter = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
        context.registerReceiver(mBroadcastReceiver, filter)
    }

    val activity: Activity get() = mBinding.activity

    fun onAttachedToActivity(binding: ActivityPluginBinding) {
        binding.addRequestPermissionsResultListener(mRequestPermissionsResultListener)
        binding.addActivityResultListener(mActivityResultListener)
        mBinding = binding
    }

    fun onDetachedFromActivity() {
        mBinding.removeRequestPermissionsResultListener(mRequestPermissionsResultListener)
        mBinding.removeActivityResultListener(mActivityResultListener)
    }

    abstract fun onReceive(context: Context, intent: Intent)
    abstract fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, results: IntArray): Boolean
    abstract fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean
}

