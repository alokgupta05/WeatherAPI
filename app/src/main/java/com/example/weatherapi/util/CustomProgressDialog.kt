package com.example.weatherapi.util

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.weatherapi.R

/**
Custom Dialog to display loader
 **/
class CustomProgressDialog(context: Context) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

         window?.also {
                 it.setBackgroundDrawable(
                     ContextCompat.getDrawable(
                         context,
                         android.R.color.transparent
                     )
                 )
                 it.setLayout(
                     ViewGroup.LayoutParams.WRAP_CONTENT,
                     ViewGroup.LayoutParams.WRAP_CONTENT
                 )
             }
        setContentView()
    }

    private fun setContentView() {
        val contentView =
            LayoutInflater.from(context).inflate(R.layout.layout_progress_loader, null)
        setContentView(contentView)
    }
}
