package com.keremkulac.journeylog.presentation.ui.appSettings

import LocaleHelper
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import com.keremkulac.journeylog.R
import com.keremkulac.journeylog.databinding.FragmentAppSettingsBinding
import com.keremkulac.journeylog.util.BaseFragment
import javax.inject.Inject


class AppSettingsFragment :
    BaseFragment<FragmentAppSettingsBinding>(FragmentAppSettingsBinding::inflate) {

    @Inject
    lateinit var appSettingsFragment: AppSettingsFragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        showLanguageDialog()
        setLanguageText(LocaleHelper.loadLanguagePreference(requireContext()))
    }


    private fun showLanguageDialog() {
        binding.language.setOnClickListener {
            val dialog = Dialog(requireContext())
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.item_language_select)
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            val window = dialog.window
            val params = WindowManager.LayoutParams()
            if (window != null) {
                params.apply {
                    copyFrom(window.attributes)
                    width = WindowManager.LayoutParams.MATCH_PARENT
                    height = WindowManager.LayoutParams.WRAP_CONTENT
                }
                window.attributes = params
            }
            val turkishRadioButton = dialog.findViewById<RadioButton>(R.id.radioTurkish)
            val englishRadioButton = dialog.findViewById<RadioButton>(R.id.radioEnglish)
            turkishRadioButton.setOnClickListener {
                setLanguage("tr")
                dialog.dismiss()
                requireActivity().recreate()
            }
            englishRadioButton.setOnClickListener {
                setLanguage("en")
                dialog.dismiss()
                requireActivity().recreate()
            }
            dialog.show()
        }
    }

    private fun setLanguageText(selectedLanguage: String) {
        if (selectedLanguage == "tr") {
            binding.language.text = requireContext().getString(R.string.turkish_language_title)
        } else {
            binding.language.text = requireContext().getString(R.string.english_language_title)
        }
    }

    private fun setLanguage(languageCode: String) {
        LocaleHelper.setLocale(requireActivity(), languageCode)
        LocaleHelper.saveLanguagePreference(requireContext(), languageCode)
        setLanguageText(languageCode)
    }

}