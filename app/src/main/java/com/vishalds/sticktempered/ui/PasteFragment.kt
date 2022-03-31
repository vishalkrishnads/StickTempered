package com.vishalds.sticktempered.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.vishalds.sticktempered.R
import com.vishalds.sticktempered.databinding.FragmentPasteBinding

class PasteFragment : Fragment() {

    private lateinit var binding: FragmentPasteBinding
    private lateinit var viewModel: TemperedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Toast.makeText(
                    activity?.applicationContext,
                    "Back button is disabled",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        activity?.onBackPressedDispatcher?.addCallback(onBackPressedCallback)
        activity?.window?.decorView?.apply {
            systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        }
    }

    override fun onResume() {
        super.onResume()
        setSelectedBrightness()
        activity?.window?.decorView?.apply {
            systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_paste, container, false)
        viewModel = ViewModelProvider(this)[TemperedViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setSelectedBrightness()
        binding.finishButton.setOnClickListener {
            findNavController().navigate(R.id.action_pasteFragment_to_homeFragment)
        }
    }

    override fun onPause() {
        super.onPause()
        setDefaultBrightness()
        activity?.window?.decorView?.apply {
            systemUiVisibility = View.VISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        setDefaultBrightness()
        activity?.window?.decorView?.apply {
            systemUiVisibility = View.VISIBLE
        }
    }

    @SuppressLint("NewApi")
    private fun setSelectedBrightness() {
        activity?.applicationContext.let {
            if (Settings.System.canWrite(it)) it?.setBrightness(viewModel.currentBrightness)
        }
    }

    @SuppressLint("NewApi")
    private fun setDefaultBrightness() {
        activity?.applicationContext.let {
            if (Settings.System.canWrite(it)) it?.setBrightness(viewModel.defaultBrightness)
        }
    }
}

fun Context.setBrightness(value: Int) {
    Settings.System.putInt(
        this.contentResolver,
        Settings.System.SCREEN_BRIGHTNESS,
        value
    )
}