package com.keremkulac.journeylog.util

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import android.widget.TextView
import com.keremkulac.journeylog.R


class ExpandableLayoutManager {
    private var isExpanded = false

    fun toggleLayout(layout: View, toggleIcon: ImageView) {
        if (layout.visibility == View.VISIBLE) {
            animateLayoutCollapse(layout)
            isExpanded = false
            toggleIcon.setImageDrawable(layout.context.getDrawable(R.drawable.ic_open))
        } else {
            animateLayoutExpand(layout)
            isExpanded = true
            toggleIcon.setImageDrawable(layout.context.getDrawable(R.drawable.ic_close))
        }
    }

    fun changeToggleText(textView: TextView, openText: String, closeText: String) {
        if (isExpanded) {
            textView.text = closeText
        } else {
            textView.text = openText
        }
    }

    private fun animateLayoutExpand(layout: View) {
        layout.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val targetHeight = layout.measuredHeight

        layout.layoutParams.height = 0
        layout.visibility = View.VISIBLE

        val animation = ValueAnimator.ofInt(0, targetHeight)
        animation.addUpdateListener { animator ->
            layout.layoutParams.height = animator.animatedValue as Int
            layout.requestLayout()
        }

        animation.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                layout.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            }
        })

        animation.duration = 0
        animation.interpolator = AccelerateDecelerateInterpolator()
        animation.start()
    }

    private fun animateLayoutCollapse(layout: View) {
        val initialHeight = layout.measuredHeight

        val animation = ValueAnimator.ofInt(initialHeight, 0)
        animation.addUpdateListener { animator ->
            layout.layoutParams.height = animator.animatedValue as Int
            layout.requestLayout()
        }

        animation.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                layout.visibility = View.GONE
                layout.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            }
        })

        animation.duration = 200
        animation.interpolator = AccelerateDecelerateInterpolator()
        animation.start()
    }
}