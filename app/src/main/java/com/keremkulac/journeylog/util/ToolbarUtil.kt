package com.keremkulac.journeylog.util

import androidx.appcompat.app.AppCompatActivity

object ToolbarUtil {
    fun setToolbar(
        appCompatActivity: AppCompatActivity,
        titleVisibility: Boolean,
        homeAsUpVisibility: Boolean
    ) {
        appCompatActivity.supportActionBar?.setDisplayShowTitleEnabled(titleVisibility)
        appCompatActivity.supportActionBar?.setDisplayHomeAsUpEnabled(homeAsUpVisibility)
    }
}