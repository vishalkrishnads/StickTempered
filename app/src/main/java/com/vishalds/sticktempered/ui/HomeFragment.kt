package com.vishalds.sticktempered.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.annotation.RequiresApi
import androidx.core.view.WindowCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.vishalds.sticktempered.R
import com.vishalds.sticktempered.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: TemperedViewModel

    // keeps track of whether the user has previously changed the brightness manually
    private var userChanged: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let { WindowCompat.setDecorFitsSystemWindows(it.window, false) }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        viewModel = ViewModelProvider(this)[TemperedViewModel::class.java]

        setSelectedBrightness()

        binding.brightness.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {

            @SuppressLint("SetTextI18n")
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                if (p2) viewModel.setBrightness(p1, true)
                if (!userChanged) userChanged = true
            }

            // implemented only to avoid warnings
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        }
        )

        binding.goButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_pasteFragment)
        }

        binding.permissions.setOnClickListener {
            startActivity(Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS))
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onResume() {
        super.onResume()
        setSelectedBrightness()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setSelectedBrightness() {
        if (!userChanged) activity?.applicationContext.let {
            if (Settings.System.canWrite(it)) {
                binding.permissionsButton.visibility = View.GONE
                binding.brightness.visibility = View.VISIBLE
                it?.brightness?.let { brightness ->
                    viewModel.setDefaultBrightness(brightness)
                    viewModel.setBrightness(brightness, false)
                }
            } else {
                binding.brightness.visibility = View.GONE
                binding.permissionsButton.visibility = View.VISIBLE
            }
        }
    }

}

val Context.brightness: Int
    get() {
        return Settings.System.getInt(
            this.contentResolver,
            Settings.System.SCREEN_BRIGHTNESS,
            0
        )
    }