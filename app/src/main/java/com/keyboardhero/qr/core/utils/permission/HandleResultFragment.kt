package com.keyboardhero.qr.core.utils.permission

import android.os.Build
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment

class HandleResultFragment : Fragment() {
    companion object {
        const val TAG = "HandleResultFragment"
    }

    private val requestPermissions: HashMap<String, (Permission) -> Unit> = HashMap()
    private lateinit var requestLauncher: ActivityResultLauncher<Array<String>>

    private var onFragmentCreated: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true

        onFragmentCreated?.invoke()
        onFragmentCreated = null

        requestLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissionsResult ->
            permissionsResult.forEach { permissionResult ->

                val permissionName = permissionResult.key
                val permissionIsGranted = permissionResult.value

                val permissionRequested = requestPermissions[permissionName]
                if (permissionRequested != null) {
                    val result = if (permissionIsGranted == true) {
                        Permission(
                            permission = permissionName,
                            granted = true,
                            preventAskAgain = false
                        )
                    } else {
                        Permission(
                            permission = permissionName,
                            granted = false,
                            preventAskAgain = !shouldShowRequestPermissionRationale(permissionName),
                        )
                    }
                    permissionRequested.invoke(result)
                }

                requestPermissions.remove(permissionName)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun request(permissions: Array<String>) {
        if (isAdded) {
            requestLauncher.launch(permissions)
        } else {
            onFragmentCreated = {
                requestLauncher.launch(permissions)
            }
        }
    }

    fun addPermissionRequest(permission: String, request: (Permission) -> Unit) {
        requestPermissions[permission] = request
    }

    fun getPermissionRequest(permission: String): ((Permission) -> Unit)? =
        requestPermissions[permission]
}
