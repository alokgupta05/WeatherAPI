package com.example.weatherapi.ui


import androidx.appcompat.app.AppCompatActivity
import com.example.weatherapi.util.CustomProgressDialog

/**
 * Abstract activity class which will contain common atributes and properties for all activities,
 * All activities must extend from this activity
 */
open class AbstractActivity : AppCompatActivity() {

    var mProgressDialog: CustomProgressDialog? = null


    protected fun showProgresDialog(msg: String) {

        if (isFinishing || isDestroyed)
            return
        hideProgressDialog()
        mProgressDialog = CustomProgressDialog(this)
        mProgressDialog!!.setCancelable(false)
        mProgressDialog!!.show()
    }

    protected fun hideProgressDialog() {

        if (!isFinishing && mProgressDialog != null && mProgressDialog!!.isShowing) {
            mProgressDialog!!.dismiss()
            mProgressDialog = null
        }
    }


}
