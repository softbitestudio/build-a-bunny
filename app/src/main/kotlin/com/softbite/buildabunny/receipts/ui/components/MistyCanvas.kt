package com.softbite.buildabunny.receipts.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics

private val MistyPurple = Color(0xFF8B5CF6)
private val InnerEarPink = Color(0xFFFFB6C1)
private val Outline = Color(0xFF2A1A0E)
private val Blush = Color(0xFFFF9EB5)

@Composable
fun MistyCanvas(modifier: Modifier = Modifier) {
    Canvas(
        modifier = modifier.semantics { contentDescription = "Misty the bunny" },
    ) {
        val w = size.width
        val h = size.height

        // Ears (drawn behind head)
        val earW = w * 0.145f
        val earH = h * 0.36f
        val earTopY = h * 0.02f
        val leftEarCx = w * 0.355f
        val rightEarCx = w * 0.645f

        listOf(leftEarCx, rightEarCx).forEach { earCx ->
            drawOval(MistyPurple, Offset(earCx - earW / 2f, earTopY), Size(earW, earH))
            drawOval(
                InnerEarPink,
                Offset(earCx - earW * 0.30f, earTopY + earH * 0.09f),
                Size(earW * 0.60f, earH * 0.74f),
            )
            drawOval(
                Outline.copy(alpha = 0.35f),
                Offset(earCx - earW / 2f, earTopY),
                Size(earW, earH),
                style = Stroke(1.4f * density, cap = StrokeCap.Round),
            )
        }

        // Head
        val hCx = w * 0.5f
        val hCy = h * 0.625f
        val hRx = w * 0.375f
        val hRy = h * 0.325f
        drawOval(MistyPurple, Offset(hCx - hRx, hCy - hRy), Size(hRx * 2f, hRy * 2f))

        // Cheek blush
        val blushR = hRx * 0.26f
        drawCircle(Blush.copy(alpha = 0.38f), blushR, Offset(hCx - hRx * 0.63f, hCy + hRy * 0.24f))
        drawCircle(Blush.copy(alpha = 0.38f), blushR, Offset(hCx + hRx * 0.63f, hCy + hRy * 0.24f))

        // Eyes
        val eyeSpread = w * 0.128f
        val eyeY = hCy - h * 0.062f
        val eyeR = w * 0.088f

        listOf(hCx - eyeSpread, hCx + eyeSpread).forEach { ex ->
            drawCircle(Color.White, eyeR, Offset(ex, eyeY))
            drawCircle(Outline, eyeR * 0.58f, Offset(ex, eyeY + eyeR * 0.09f))
            drawCircle(Color.White, eyeR * 0.19f, Offset(ex - eyeR * 0.18f, eyeY - eyeR * 0.14f))
            // Half-lid for judge-y look
            drawRect(MistyPurple, Offset(ex - eyeR, eyeY - eyeR), Size(eyeR * 2f, eyeR * 0.70f))
            drawCircle(Outline.copy(alpha = 0.35f), eyeR, Offset(ex, eyeY), style = Stroke(1.3f * density))
        }

        // Nose
        val noseY = hCy + h * 0.038f
        val nw = w * 0.044f
        val nh = h * 0.026f
        drawPath(Path().apply {
            moveTo(hCx, noseY + nh)
            lineTo(hCx - nw, noseY)
            lineTo(hCx + nw, noseY)
            close()
        }, InnerEarPink)

        // Philtrum
        drawLine(
            Outline.copy(alpha = 0.28f),
            Offset(hCx, noseY + nh),
            Offset(hCx, hCy + h * 0.092f),
            1.2f * density,
        )

        // Smirk (asymmetric — higher on left, giving side-eye energy)
        val mouthY = hCy + h * 0.096f
        drawPath(Path().apply {
            moveTo(hCx - w * 0.070f, mouthY + h * 0.007f)
            cubicTo(
                hCx - w * 0.018f, mouthY + h * 0.024f,
                hCx + w * 0.032f, mouthY + h * 0.019f,
                hCx + w * 0.078f, mouthY - h * 0.002f,
            )
        }, Outline.copy(alpha = 0.62f), style = Stroke(1.7f * density, cap = StrokeCap.Round))

        // Head outline
        drawOval(
            Outline.copy(alpha = 0.35f),
            Offset(hCx - hRx, hCy - hRy),
            Size(hRx * 2f, hRy * 2f),
            style = Stroke(1.4f * density),
        )
    }
}
