package com.keremkulac.journeylog.presentation.ui.appSettings

import LocaleHelper
import android.app.AlertDialog
import android.os.Bundle
import android.view.View
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
            val languages = arrayOf(
                requireContext().getString(R.string.turkish_language_title),
                requireContext().getString(R.string.english_language_title)
            )
            AlertDialog.Builder(requireContext())
                .setTitle(requireContext().getString(R.string.select_language_title))
                .setSingleChoiceItems(languages, -1) { dialog, which ->
                    when (which) {
                        0 -> {
                            setLanguage("tr")
                        }

                        1 -> {
                            setLanguage("en")
                        }
                    }
                    dialog.dismiss()
                    requireActivity().recreate()
                }
                .show()
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