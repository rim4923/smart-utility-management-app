@file:OptIn(ExperimentalAnimationApi::class)

package com.example.capstoneutilitrack.ui.signup

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import com.example.capstoneutilitrack.R

@Composable
fun StepProgressIndicator(currentStep: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        (1..3).forEach { step ->
            StepCircle(
                step = step,
                isCompleted = step < currentStep,
                isCurrent = step == currentStep
            )

            if (step < 3) {
                StepConnector(isActive = currentStep > step)
            }
        }
    }
}

@Composable
fun StepCircle(step: Int, isCompleted: Boolean, isCurrent: Boolean) {
    val backgroundColor = when {
        isCompleted -> colorResource(R.color.steel_blue)
        isCurrent -> colorResource(R.color.soft_blue)
        else -> colorResource(R.color.creamex)
    }

    val borderColor = colorResource(R.color.creamex).copy(0.8f)

    Box(
        modifier = Modifier
            .size(32.dp)
            .border(2.dp, borderColor, CircleShape)
            .background(backgroundColor, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        AnimatedContent(
            targetState = isCompleted,
            transitionSpec = {
                fadeIn(tween(500)) + scaleIn(tween(500)) with fadeOut(tween(500)) + scaleOut(tween(500))
            },
            label = "StepCircleTransition"
        ) { done ->
            if (done) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_check),
                    contentDescription = "Done",
                    tint = colorResource(R.color.deep_blue),
                    modifier = Modifier.size(16.dp)
                )
            } else {
                Text(
                    text = step.toString(),
                    color = if (isCurrent) colorResource(R.color.steel_blue) else colorResource(R.color.sand),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}




@Composable
fun StepConnector(isActive: Boolean) {
    val fillProgress by animateFloatAsState(
        targetValue = if (isActive) 1f else 0f,
        animationSpec = tween(durationMillis = 600),
        label = "ConnectorFill"
    )

    Box(
        modifier = Modifier
            .width(100.dp)
            .height(2.dp)
            .background(colorResource(R.color.sand))
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(fillProgress)
                .background(colorResource(R.color.steel_blue))
        )
    }
}



