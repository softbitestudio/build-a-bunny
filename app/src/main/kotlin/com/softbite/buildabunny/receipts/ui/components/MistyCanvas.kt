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

// Misty — pastel goth purple bunny with floppy lop ears and golden eyes
private val MistyFur = Color(0xFF9B59B6)
private val MistyFurLight = Color(0xFFBB77D4)
private val InnerEarColor = Color(0xFFE8A0F0)
private val EyeGold = Color(0xFFF5C518)
private val EyeAmber = Color(0xFFD4AC0D)
private val OutlineColor = Color(0xFF2A1040)
private val NoseColor = Color(0xFFFFAFCC)
private val BlushColor = Color(0xFFE879F9)

@Composable
fun MistyCanvas(modifier: Modifier = Modifier) {
    Canvas(
        modifier = modifier.semantics { contentDescription = "Misty" },
    ) {
        val w = size.width
        val h = size.height

        val headCy = h * 0.58f
        val headCx = w * 0.50f
        val headRx = w * 0.36f
        val headRy = h * 0.30f

        // ── Floppy lop ears ───────────────────────────────────────────────────
        val leftEarPath = Path().apply {
            moveTo(headCx - headRx * 0.55f, headCy - headRy * 0.55f)
            cubicTo(
                headCx - headRx * 1.20f, headCy - headRy * 0.80f,
                headCx - headRx * 1.55f, headCy - headRy * 0.20f,
                headCx - headRx * 1.40f, headCy + headRy * 0.35f,
            )
            cubicTo(
                headCx - headRx * 1.30f, headCy + headRy * 0.65f,
                headCx - headRx * 0.80f, headCy + headRy * 0.35f,
                headCx - headRx * 0.45f, headCy - headRy * 0.30f,
            )
            close()
        }
        val leftEarInnerPath = Path().apply {
            moveTo(headCx - headRx * 0.60f, headCy - headRy * 0.40f)
            cubicTo(
                headCx - headRx * 1.05f, headCy - headRy * 0.60f,
                headCx - headRx * 1.30f, headCy - headRy * 0.10f,
                headCx - headRx * 1.18f, headCy + headRy * 0.25f,
            )
            cubicTo(
                headCx - headRx * 1.10f, headCy + headRy * 0.45f,
                headCx - headRx * 0.72f, headCy + headRy * 0.25f,
                headCx - headRx * 0.56f, headCy - headRy * 0.22f,
            )
            close()
        }
        drawPath(leftEarPath, MistyFur)
        drawPath(leftEarInnerPath, InnerEarColor)
        drawPath(leftEarPath, OutlineColor.copy(alpha = 0.3f), style = Stroke(1.2f * density, cap = StrokeCap.Round))

        val rightEarPath = Path().apply {
            moveTo(headCx + headRx * 0.55f, headCy - headRy * 0.55f)
            cubicTo(
                headCx + headRx * 1.20f, headCy - headRy * 0.80f,
                headCx + headRx * 1.55f, headCy - headRy * 0.20f,
                headCx + headRx * 1.40f, headCy + headRy * 0.35f,
            )
            cubicTo(
                headCx + headRx * 1.30f, headCy + headRy * 0.65f,
                headCx + headRx * 0.80f, headCy + headRy * 0.35f,
                headCx + headRx * 0.45f, headCy - headRy * 0.30f,
            )
            close()
        }
        val rightEarInnerPath = Path().apply {
            moveTo(headCx + headRx * 0.60f, headCy - headRy * 0.40f)
            cubicTo(
                headCx + headRx * 1.05f, headCy - headRy * 0.60f,
                headCx + headRx * 1.30f, headCy - headRy * 0.10f,
                headCx + headRx * 1.18f, headCy + headRy * 0.25f,
            )
            cubicTo(
                headCx + headRx * 1.10f, headCy + headRy * 0.45f,
                headCx + headRx * 0.72f, headCy + headRy * 0.25f,
                headCx + headRx * 0.56f, headCy - headRy * 0.22f,
            )
            close()
        }
        drawPath(rightEarPath, MistyFur)
        drawPath(rightEarInnerPath, InnerEarColor)
        drawPath(rightEarPath, OutlineColor.copy(alpha = 0.3f), style = Stroke(1.2f * density, cap = StrokeCap.Round))

        // ── Head ──────────────────────────────────────────────────────────────
        drawOval(MistyFur, Offset(headCx - headRx, headCy - headRy), Size(headRx * 2f, headRy * 2f))
        val tuftY = headCy - headRy * 0.92f
        drawOval(MistyFurLight, Offset(headCx - w * 0.10f, tuftY - h * 0.04f), Size(w * 0.20f, h * 0.08f))

        // Cheek blush
        drawCircle(BlushColor.copy(alpha = 0.22f), headRx * 0.28f, Offset(headCx - headRx * 0.60f, headCy + headRy * 0.22f))
        drawCircle(BlushColor.copy(alpha = 0.22f), headRx * 0.28f, Offset(headCx + headRx * 0.60f, headCy + headRy * 0.22f))

        // ── Golden eyes with sassy half-lid ───────────────────────────────────
        val eyeSpread = w * 0.125f
        val eyeY = headCy - h * 0.055f
        val eyeR = w * 0.092f

        listOf(headCx - eyeSpread, headCx + eyeSpread).forEach { ex ->
            drawCircle(Color.White, eyeR, Offset(ex, eyeY))
            drawCircle(EyeGold, eyeR * 0.72f, Offset(ex, eyeY + eyeR * 0.06f))
            drawCircle(EyeAmber, eyeR * 0.48f, Offset(ex, eyeY + eyeR * 0.06f))
            drawCircle(OutlineColor, eyeR * 0.28f, Offset(ex, eyeY + eyeR * 0.06f))
            drawCircle(Color.White, eyeR * 0.16f, Offset(ex - eyeR * 0.20f, eyeY - eyeR * 0.18f))
            drawRect(MistyFur, Offset(ex - eyeR * 1.05f, eyeY - eyeR), Size(eyeR * 2.1f, eyeR * 0.74f))
            drawLine(OutlineColor.copy(alpha = 0.50f), Offset(ex - eyeR * 0.85f, eyeY + eyeR * 0.75f), Offset(ex + eyeR * 0.85f, eyeY + eyeR * 0.75f), 1.2f * density, StrokeCap.Round)
            drawLine(OutlineColor.copy(alpha = 0.45f), Offset(ex + eyeR * 0.80f, eyeY + eyeR * 0.60f), Offset(ex + eyeR * 1.10f, eyeY + eyeR * 0.20f), 1.1f * density, StrokeCap.Round)
            drawCircle(OutlineColor.copy(alpha = 0.30f), eyeR, Offset(ex, eyeY), style = Stroke(1.1f * density))
        }

        // ── Nose ──────────────────────────────────────────────────────────────
        val noseY = headCy + h * 0.040f
        val nw = w * 0.038f
        val nh = h * 0.022f
        drawPath(Path().apply {
            moveTo(headCx, noseY + nh); lineTo(headCx - nw, noseY); lineTo(headCx + nw, noseY); close()
        }, NoseColor)
        drawLine(OutlineColor.copy(alpha = 0.22f), Offset(headCx, noseY + nh), Offset(headCx, headCy + h * 0.088f), 1.0f * density)

        // ── Smirk ─────────────────────────────────────────────────────────────
        val mouthY = headCy + h * 0.092f
        drawPath(Path().apply {
            moveTo(headCx - w * 0.065f, mouthY + h * 0.006f)
            cubicTo(headCx - w * 0.016f, mouthY + h * 0.022f, headCx + w * 0.030f, mouthY + h * 0.018f, headCx + w * 0.075f, mouthY - h * 0.002f)
        }, OutlineColor.copy(alpha = 0.58f), style = Stroke(1.6f * density, cap = StrokeCap.Round))

        drawOval(OutlineColor.copy(alpha = 0.28f), Offset(headCx - headRx, headCy - headRy), Size(headRx * 2f, headRy * 2f), style = Stroke(1.2f * density))
    }
}
