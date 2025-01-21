package com.abelvolpi.pokemonapi.presentation.customviews

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.LayerDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.abelvolpi.pokemonapi.presentation.R
import com.abelvolpi.pokemonapi.presentation.databinding.ViewStatsCustomProgressBarBinding

class CustomHorizontalProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var maxValue: Int = 300
    private var progressColor: Int = Color.BLUE
    private var binding: ViewStatsCustomProgressBarBinding

    init {
        binding = ViewStatsCustomProgressBarBinding.inflate(LayoutInflater.from(context), this)
        obtainStyledAttributes(context, attrs)
        configureProgressBar()
    }

    private fun obtainStyledAttributes(context: Context, attrs: AttributeSet?) {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CustomHorizontalProgressBar,
            0,
            0
        ).apply {
            try {
                setStatsNameTextView(getString(R.styleable.CustomHorizontalProgressBar_statsName))
                maxValue = getInt(R.styleable.CustomHorizontalProgressBar_maxValue, 300)
                progressColor = getColor(
                    R.styleable.CustomHorizontalProgressBar_progressColor,
                    ContextCompat.getColor(
                        context,
                        androidx.appcompat.R.color.material_blue_grey_800
                    )
                )
            } finally {
                recycle()
            }
        }
    }

    private fun setStatsNameTextView(name: String?) {
        binding.statsNameTextView.text = name ?: ""
    }

    private fun configureProgressBar() {
        binding.horizontalProgressBarStats.max = maxValue

        val layerDrawable = binding.horizontalProgressBarStats.progressDrawable as? LayerDrawable
        layerDrawable?.let {
            val progressDrawable = it.findDrawableByLayerId(android.R.id.progress)
            progressDrawable?.setTint(progressColor)
        }
    }

    fun setProgress(value: Int, animate: Boolean = true) {
        binding.statsValueTextView.text = value.toString()
        if (animate) {
            animateProgress(value)
        } else {
            binding.horizontalProgressBarStats.progress = value
        }
    }

    private fun animateProgress(value: Int) {
        ObjectAnimator.ofInt(binding.horizontalProgressBarStats, "progress", value).apply {
            duration = 1000
            start()
        }
    }
}
