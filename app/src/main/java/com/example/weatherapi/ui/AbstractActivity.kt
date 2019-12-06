package com.example.weatherapi.ui


import androidx.appcompat.app.AppCompatActivity
import com.example.weatherapi.util.CustomProgressDialog


open class AbstractActivity : AppCompatActivity() {

    var mProgressDialog: CustomProgressDialog? = null


    protected fun showProgresDialog(msg: String) {

        if (isFinishing || isDestroyed)
            return
        hideProgressDialog()
        mProgressDialog = CustomProgressDialog(this)
        mProgressDialog!!.setCancelable(false)
        mProgressDialog!!.setMessage(msg)
        mProgressDialog!!.show()
    }

    protected fun hideProgressDialog() {

        if (!isFinishing && mProgressDialog != null && mProgressDialog!!.isShowing) {
            mProgressDialog!!.dismiss()
            mProgressDialog = null
        }
    }


}
