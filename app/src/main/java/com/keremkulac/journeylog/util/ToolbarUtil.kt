package com.keremkulac.journeylog.util

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

object ToolbarUtil {
    fun setToolbar(
        appCompatActivity: AppCompatActivity,
        toolbar: Toolbar,
        toolbarTitle: String,
        titleVisibility: Boolean,
        homeAsUpVisibility: Boolean
    ) {
        appCompatActivity.supportActionBar?.setDisplayShowTitleEnabled(titleVisibility)
        appCompatActivity.supportActionBar?.setDisplayHomeAsUpEnabled(homeAsUpVisibility)
        toolbar.title = toolbarTitle
    }
}