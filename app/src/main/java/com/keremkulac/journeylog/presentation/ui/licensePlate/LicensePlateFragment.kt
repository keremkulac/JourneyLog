package com.keremkulac.journeylog.presentation.ui.licensePlate

import android.os.Bundle
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.keremkulac.journeylog.R
import com.keremkulac.journeylog.databinding.FragmentLicensePlateBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LicensePlateFragment : BottomSheetDialogFragment(R.layout.fragment_license_plate) {

    private lateinit var binding: FragmentLicensePlateBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLicensePlateBinding.bind(view)
    }


}