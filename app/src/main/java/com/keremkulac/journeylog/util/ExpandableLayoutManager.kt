package com.keremkulac.journeylog.util

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator

class ExpandableLayoutManager {
    private var expandedLayout: View? = null

    fun toggleLayout(layout: View) {
        expandedLayout?.let {
            if (it != layout && it.visibility == View.VISIBLE) {
                animateLayoutCollapse(it)
            }
        }

        if (layout.visibility == View.VISIBLE) {
            animateLayoutCollapse(layout)
            expandedLayout = null
        } else {
            animateLayoutExpand(layout)
            expandedLayout = layout
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

        animation.duration = 300
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