package com.example.proyecto.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.proyecto.FinancialState
import com.example.proyecto.R

@Composable
fun PiggoMascot(state: FinancialState) {
    val resId = when(state) {
        FinancialState.PERFECT, FinancialState.GOOD -> R.raw.piggo_idle
        FinancialState.STABLE -> R.raw.piggo_happy
        FinancialState.WARNING -> R.raw.piggo_warning
        FinancialState.CRITICAL -> R.raw.piggo_panic
    }

    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(resId)
    )
    LottieAnimation(composition, iterations = LottieConstants.IterateForever)
}
