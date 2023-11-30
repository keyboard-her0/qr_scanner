package com.keyboardhero.qr.core.base

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.keyboardhero.qr.core.utils.permission.Permission
import com.keyboardhero.qr.core.utils.permission.PermissionUtil
import com.keyboardhero.qr.features.widget.ProcessDialog

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity(), DialogCommonView {
    private lateinit var permissionUtil: PermissionUtil

    lateinit var binding: VB
        private set
    abstract val bindingInflater: (LayoutInflater) -> VB

    private val processDialog: ProcessDialog by lazy { ProcessDialog(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = bindingInflater.invoke(layoutInflater)

        setContentView(binding.root)
        // Init immediately after create activity
        permissionUtil = PermissionUtil(this)
    }

    /**
     * Request grants permissions.
     *
     * @param permissions (variable number of arguments): List permission want to request
     * (e.g.,[Manifest.permission.ACCESS_COARSE_LOCATION], [Manifest.permission.READ_EXTERNAL_STORAGE],...)
     *
     * @param callback : Return result of request. There are two parameters:
     * areGrantedAll: True if all permissions are granted. False if at least one of those is declined.
     * deniedPermissions: List [Permission] request which are declined by user.
     */
    fun requestPermissions(
        vararg permissions: String,
        callback: (areGrantedAll: Boolean, deniedPermissions: List<Permission>) -> Unit,
    ) {
        permissionUtil.request(*permissions, callback = callback)
    }

    override fun showSingleOptionDialog(
        title: String?,
        message: String,
        button: String,
        listener: DialogButtonClickListener?,
    ): AlertDialog? {
        return buildDialog(title, message)
            .setPositiveButton(button) { dialog, _ ->
                listener?.invoke(dialog) ?: dialog.dismiss()
            }
            .show()
    }

    override fun showSingleOptionDialog(
        title: Int?,
        message: Int,
        button: Int,
        listener: DialogButtonClickListener?,
    ): AlertDialog? {
        return showSingleOptionDialog(
            title = title?.let { getString(it) },
            message = getString(message),
            button = getString(button),
            listener = listener,
        )
    }

    override fun showDoubleOptionsDialog(
        title: String?,
        message: String,
        firstButton: String,
        secondButton: String,
        firstButtonListener: DialogButtonClickListener?,
        secondButtonListener: DialogButtonClickListener?,
    ): AlertDialog? {
        return buildDialog(title, message)
            .setPositiveButton(firstButton) { dialog, _ ->
                firstButtonListener?.invoke(dialog) ?: dialog.dismiss()
            }
            .setNegativeButton(secondButton) { dialog, _ ->
                secondButtonListener?.invoke(dialog) ?: dialog.dismiss()
            }
            .show()
    }

    override fun showDoubleOptionsDialog(
        title: Int?,
        message: Int,
        firstButton: Int,
        secondButton: Int,
        firstButtonListener: DialogButtonClickListener?,
        secondButtonListener: DialogButtonClickListener?,
    ): AlertDialog? {
        return showDoubleOptionsDialog(
            title = title?.let { getString(it) },
            message = getString(message),
            firstButton = getString(firstButton),
            secondButton = getString(secondButton),
            firstButtonListener = firstButtonListener,
            secondButtonListener = secondButtonListener,
        )
    }

    private fun buildDialog(title: String?, message: String): AlertDialog.Builder {
        return AlertDialog.Builder(this)
            .setCancelable(false)
            .setTitle(title)
            .setMessage(message)
    }

    internal fun reCreateActivitySmooth() {
        startActivity(
            intent?.also {
                it.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            },
        )
        finish()
    }

    fun showLoading() {
        if (!processDialog.isShowing) {
            processDialog.show()
        }
    }

    fun hideLoading() {
        if (processDialog.isShowing) {
            processDialog.dismiss()
        }
    }
}
