package com.softbite.buildabunny.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.softbite.buildabunny.data.model.CharacterConfig

private val OUTLINE = Color(0xFF2A1A0E)
private val INNER_EAR = Color(0xFFFFB6C1)
private val BELLY = Color(0xFFF5ECD7)

@Composable
fun BunnyCanvas(
    config: CharacterConfig,
    modifier: Modifier = Modifier,
) {
    Canvas(
        modifier = modifier
            .aspectRatio(3f / 4f)
            .semantics { contentDescription = "Bunny preview: ${config.name}" },
    ) {
        val fur = furColor(config.furColor)
        val shading = fur.darken(0.12f)

        drawBackground(config.background)
        drawTail(fur, shading)
        drawEars(config.earStyle, fur, shading)
        drawLegs(config.bodyShape, fur, shading)
        drawArms(config.bodyShape, fur, shading)
        drawTorso(config.bodyShape, fur, shading)
        drawHead(fur, shading)
        drawFaceDetail(
            eyeStyle = config.eyeStyle,
            eyeColor = eyeColorOf(config.eyeColor),
            noseColor = noseColorOf(config.noseColor),
            mouthStyle = config.mouthStyle,
        )
        drawAccessory(config.accessory)
    }
}

// ─── Helpers ─────────────────────────────────────────────────────────────────

private fun Color.darken(by: Float) = Color(
    red = (red * (1f - by)).coerceIn(0f, 1f),
    green = (green * (1f - by)).coerceIn(0f, 1f),
    blue = (blue * (1f - by)).coerceIn(0f, 1f),
    alpha = alpha,
)

private fun DrawScope.outline(width: Float = 2.5f) = Stroke(
    width = width * density,
    cap = StrokeCap.Round,
    join = StrokeJoin.Round,
)

// px helpers — all coordinates expressed as fractions of canvas dimensions
private fun DrawScope.px(xFrac: Float) = size.width * xFrac
private fun DrawScope.py(yFrac: Float) = size.height * yFrac
private fun DrawScope.pt(xFrac: Float, yFrac: Float) = Offset(px(xFrac), py(yFrac))

// ─── Background ──────────────────────────────────────────────────────────────

private fun DrawScope.drawBackground(background: String) {
    when (background) {
        "meadow" -> {
            drawRect(Brush.verticalGradient(listOf(Color(0xFF87CEEB), Color(0xFFB3E5FC))))
            drawRect(
                color = Color(0xFF66BB6A),
                topLeft = pt(0f, 0.76f),
                size = Size(size.width, py(0.24f)),
            )
            drawOval(Color(0xFF81C784), pt(-0.05f, 0.70f), Size(px(1.10f), py(0.14f)))
        }
        "sky" -> {
            drawRect(Brush.verticalGradient(listOf(Color(0xFF42A5F5), Color(0xFFBBDEFB))))
            repeat(4) { i ->
                val cx = px(0.15f + i * 0.24f)
                val cy = py(0.08f + (i % 2) * 0.06f)
                drawOval(Color.White.copy(alpha = 0.75f), Offset(cx, cy), Size(px(0.18f), py(0.06f)))
            }
        }
        "stars" -> {
            drawRect(Brush.verticalGradient(listOf(Color(0xFF0D0D2B), Color(0xFF1A237E))))
            listOf(
                0.10f to 0.04f, 0.30f to 0.08f, 0.55f to 0.03f, 0.80f to 0.10f,
                0.20f to 0.16f, 0.70f to 0.18f, 0.45f to 0.12f, 0.90f to 0.06f,
                0.08f to 0.26f, 0.62f to 0.22f,
            ).forEach { (fx, fy) ->
                drawCircle(Color.White, 2.5f, pt(fx, fy))
            }
        }
        "rainbow" -> {
            listOf(
                Color(0xFFFF6B6B), Color(0xFFFF9800), Color(0xFFFFEB3B),
                Color(0xFF81C784), Color(0xFF64B5F6), Color(0xFFCE93D8),
            ).forEachIndexed { i, c ->
                val stripH = size.height / 6f
                drawRect(c.copy(alpha = 0.40f), Offset(0f, i * stripH), Size(size.width, stripH))
            }
            drawRect(Color.White.copy(alpha = 0.20f))
        }
        "plain_pink" -> drawRect(Color(0xFFFCE4EC))
        else -> drawRect(Color(0xFFF0F0F0))
    }
}

// ─── Cottontail ──────────────────────────────────────────────────────────────

private fun DrawScope.drawTail(fur: Color, shading: Color) {
    val cx = px(0.72f)
    val cy = py(0.60f)
    val r = px(0.08f)
    // fluffy white puff with slight shading
    drawCircle(Color.White, r * 1.1f, Offset(cx, cy))
    drawCircle(Color.White.copy(alpha = 0.6f), r * 0.7f, Offset(cx - r * 0.15f, cy - r * 0.15f))
    drawCircle(OUTLINE, r * 1.1f, Offset(cx, cy), style = outline(1.5f))
}

// ─── Ears ────────────────────────────────────────────────────────────────────

private fun DrawScope.drawEars(style: String, fur: Color, shading: Color) {
    when (style) {
        "floppy" -> drawFloppyEars(fur, shading)
        "lop" -> {
            drawStandingEar(fur, shading, right = false)
            drawFloppyEar(fur, shading, right = true)
        }
        else -> {
            drawStandingEar(fur, shading, right = false)
            drawStandingEar(fur, shading, right = true)
        }
    }
}

private fun DrawScope.drawStandingEar(fur: Color, shading: Color, right: Boolean) {
    val cx = if (right) px(0.585f) else px(0.415f)
    val baseY = py(0.175f)
    val tipY = py(0.02f)
    val earW = px(0.10f)
    val earH = baseY - tipY

    // outer ear fill
    val path = Path().apply {
        moveTo(cx - earW * 0.5f, baseY)
        cubicTo(cx - earW * 0.6f, baseY - earH * 0.4f, cx - earW * 0.35f, tipY + px(0.02f), cx, tipY)
        cubicTo(cx + earW * 0.35f, tipY + px(0.02f), cx + earW * 0.6f, baseY - earH * 0.4f, cx + earW * 0.5f, baseY)
        close()
    }
    drawPath(path, fur)
    drawPath(path, shading.copy(alpha = 0.18f))  // shading on edges

    // inner ear (pink)
    val innerPath = Path().apply {
        val iw = earW * 0.42f
        val iy = tipY + earH * 0.12f
        moveTo(cx - iw * 0.4f, baseY - earH * 0.05f)
        cubicTo(cx - iw * 0.5f, baseY - earH * 0.50f, cx - iw * 0.25f, iy + px(0.02f), cx, iy)
        cubicTo(cx + iw * 0.25f, iy + px(0.02f), cx + iw * 0.50f, baseY - earH * 0.50f, cx + iw * 0.4f, baseY - earH * 0.05f)
        close()
    }
    drawPath(innerPath, INNER_EAR)
    drawPath(path, OUTLINE, style = outline(2f))
}

private fun DrawScope.drawFloppyEars(fur: Color, shading: Color) {
    drawFloppyEar(fur, shading, right = false)
    drawFloppyEar(fur, shading, right = true)
}

private fun DrawScope.drawFloppyEar(fur: Color, shading: Color, right: Boolean) {
    val sign = if (right) 1f else -1f
    val baseX = px(0.50f) + sign * px(0.14f)
    val baseY = py(0.19f)
    val tipX = px(0.50f) + sign * px(0.38f)
    val tipY = py(0.36f)
    val earW = px(0.09f)

    val path = Path().apply {
        moveTo(baseX - earW * 0.3f, baseY)
        cubicTo(tipX - earW * 0.8f, baseY + (tipY - baseY) * 0.4f, tipX - earW, tipY - earW * 0.3f, tipX - earW * 0.4f, tipY)
        cubicTo(tipX, tipY + earW * 0.2f, tipX + earW * 0.6f, tipY - earW * 0.3f, tipX + earW * 0.4f, tipY - earW)
        cubicTo(tipX + earW * 0.8f, baseY + (tipY - baseY) * 0.4f, baseX + earW * 0.5f, baseY + earW * 0.2f, baseX + earW * 0.3f, baseY)
        close()
    }
    drawPath(path, fur)
    val innerPath = Path().apply {
        moveTo(baseX - earW * 0.1f, baseY + earW * 0.1f)
        cubicTo(tipX - earW * 0.5f, baseY + (tipY - baseY) * 0.45f, tipX - earW * 0.5f, tipY - earW * 0.2f, tipX, tipY - earW * 0.15f)
        cubicTo(tipX + earW * 0.5f, tipY - earW * 0.2f, tipX + earW * 0.3f, baseY + (tipY - baseY) * 0.45f, baseX + earW * 0.1f, baseY + earW * 0.1f)
        close()
    }
    drawPath(innerPath, INNER_EAR)
    drawPath(path, OUTLINE, style = outline(2f))
}

// ─── Legs & Feet ─────────────────────────────────────────────────────────────

private fun DrawScope.drawLegs(shape: String, fur: Color, shading: Color) {
    val hipW = when (shape) { "slim" -> 0.17f; "chubby" -> 0.22f; else -> 0.19f }
    val thighW = when (shape) { "slim" -> 0.12f; "chubby" -> 0.16f; else -> 0.14f }

    listOf(false, true).forEach { right ->
        val sign = if (right) 1f else -1f
        val hipX = px(0.50f) + sign * px(hipW)
        val kneeX = px(0.50f) + sign * px(thighW * 0.85f)
        val ankleX = px(0.50f) + sign * px(thighW * 0.55f)
        val footTipX = px(0.50f) + sign * px(thighW * 1.6f)

        // thigh
        val thighPath = Path().apply {
            val tw = px(thighW)
            moveTo(hipX - tw * 0.4f * sign * -1, py(0.605f))
            cubicTo(hipX - tw * 0.3f * sign * -1, py(0.68f), kneeX - tw * 0.35f * sign * -1, py(0.73f), kneeX, py(0.755f))
            cubicTo(kneeX + tw * 0.35f * sign * -1, py(0.73f), hipX + tw * 0.3f * sign * -1, py(0.68f), hipX + tw * 0.4f * sign * -1, py(0.605f))
            close()
        }
        drawPath(thighPath, fur)
        drawPath(thighPath, shading.copy(alpha = 0.15f))

        // lower leg
        val tw = px(thighW)
        val legPath = Path().apply {
            moveTo(kneeX - tw * 0.32f * sign * -1, py(0.755f))
            cubicTo(kneeX - tw * 0.28f * sign * -1, py(0.82f), ankleX - tw * 0.20f * sign * -1, py(0.87f), ankleX, py(0.895f))
            cubicTo(ankleX + tw * 0.20f * sign * -1, py(0.87f), kneeX + tw * 0.28f * sign * -1, py(0.82f), kneeX + tw * 0.32f * sign * -1, py(0.755f))
            close()
        }
        drawPath(legPath, fur)

        // foot (large digitigrade-style paw)
        val footPath = Path().apply {
            moveTo(ankleX - tw * 0.18f * sign * -1, py(0.895f))
            cubicTo(ankleX - tw * 0.2f * sign * -1, py(0.945f), footTipX - tw * 0.10f, py(0.965f), footTipX, py(0.958f))
            cubicTo(footTipX + tw * 0.08f, py(0.950f), footTipX + tw * 0.06f * sign * -1, py(0.940f), ankleX + tw * 0.25f * sign * -1, py(0.92f))
            cubicTo(ankleX + tw * 0.22f * sign * -1, py(0.895f), ankleX + tw * 0.18f * sign * -1, py(0.890f), ankleX + tw * 0.18f * sign * -1, py(0.895f))
            close()
        }
        drawPath(footPath, fur)

        // toe lines on foot
        repeat(3) { t ->
            val tx = footTipX - sign * px(thighW) * (0.15f + t * 0.25f)
            drawLine(OUTLINE.copy(alpha = 0.4f), Offset(tx, py(0.945f)), Offset(tx, py(0.960f)), strokeWidth = 1.5f * density, cap = StrokeCap.Round)
        }

        drawPath(thighPath, OUTLINE, style = outline(2f))
        drawPath(legPath, OUTLINE, style = outline(2f))
        drawPath(footPath, OUTLINE, style = outline(2f))
    }
}

// ─── Arms ────────────────────────────────────────────────────────────────────

private fun DrawScope.drawArms(shape: String, fur: Color, shading: Color) {
    val armW = when (shape) { "slim" -> 0.075f; "chubby" -> 0.105f; else -> 0.088f }

    listOf(false, true).forEach { right ->
        val sign = if (right) 1f else -1f
        val shoulderX = px(0.50f) + sign * px(0.185f)
        val elbowX = px(0.50f) + sign * px(0.215f)
        val handX = px(0.50f) + sign * px(0.195f)
        val aw = px(armW)

        // upper arm
        val upperArm = Path().apply {
            moveTo(shoulderX - aw * 0.5f, py(0.415f))
            cubicTo(shoulderX - aw * 0.7f, py(0.50f), elbowX - aw * 0.6f, py(0.545f), elbowX, py(0.555f))
            cubicTo(elbowX + aw * 0.6f, py(0.545f), shoulderX + aw * 0.7f, py(0.50f), shoulderX + aw * 0.5f, py(0.415f))
            close()
        }
        drawPath(upperArm, fur)

        // lower arm + hand
        val lowerArm = Path().apply {
            moveTo(elbowX - aw * 0.5f, py(0.555f))
            cubicTo(elbowX - aw * 0.6f, py(0.62f), handX - aw * 0.55f, py(0.665f), handX - aw * 0.3f, py(0.680f))
            cubicTo(handX, py(0.700f), handX + aw * 0.3f, py(0.680f), handX + aw * 0.55f, py(0.665f))
            cubicTo(elbowX + aw * 0.6f, py(0.62f), elbowX + aw * 0.5f, py(0.555f), elbowX + aw * 0.5f, py(0.555f))
            close()
        }
        drawPath(lowerArm, fur)

        // paw finger lines
        repeat(3) { f ->
            val fx = handX + sign * aw * (-0.25f + f * 0.25f)
            drawLine(OUTLINE.copy(alpha = 0.4f), Offset(fx, py(0.685f)), Offset(fx, py(0.698f)), strokeWidth = 1.5f * density, cap = StrokeCap.Round)
        }

        drawPath(upperArm, OUTLINE, style = outline(2f))
        drawPath(lowerArm, OUTLINE, style = outline(2f))
    }
}

// ─── Torso ───────────────────────────────────────────────────────────────────

private fun DrawScope.drawTorso(shape: String, fur: Color, shading: Color) {
    val shoulderW = when (shape) { "slim" -> 0.175f; "chubby" -> 0.220f; else -> 0.195f }
    val waistW    = when (shape) { "slim" -> 0.110f; "chubby" -> 0.165f; else -> 0.135f }
    val hipW      = when (shape) { "slim" -> 0.160f; "chubby" -> 0.220f; else -> 0.185f }

    val torso = Path().apply {
        // left side top to bottom
        moveTo(px(0.50f - shoulderW), py(0.415f))
        cubicTo(px(0.50f - shoulderW * 1.05f), py(0.49f), px(0.50f - waistW), py(0.52f), px(0.50f - waistW), py(0.545f))
        cubicTo(px(0.50f - waistW), py(0.575f), px(0.50f - hipW), py(0.59f), px(0.50f - hipW), py(0.615f))
        // bottom
        lineTo(px(0.50f + hipW), py(0.615f))
        // right side bottom to top
        cubicTo(px(0.50f + hipW), py(0.59f), px(0.50f + waistW), py(0.575f), px(0.50f + waistW), py(0.545f))
        cubicTo(px(0.50f + waistW), py(0.52f), px(0.50f + shoulderW * 1.05f), py(0.49f), px(0.50f + shoulderW), py(0.415f))
        close()
    }
    drawPath(torso, fur)

    // belly / chest marking
    val belly = Path().apply {
        val bw = px(waistW * 0.75f)
        moveTo(px(0.50f), py(0.435f))
        cubicTo(px(0.50f) - bw * 0.6f, py(0.46f), px(0.50f) - bw * 0.5f, py(0.575f), px(0.50f), py(0.605f))
        cubicTo(px(0.50f) + bw * 0.5f, py(0.575f), px(0.50f) + bw * 0.6f, py(0.46f), px(0.50f), py(0.435f))
        close()
    }
    drawPath(belly, BELLY)

    drawPath(torso, OUTLINE, style = outline(2.5f))

    // neck
    val neckW = px(0.065f)
    drawRect(fur, Offset(px(0.50f) - neckW, py(0.355f)), Size(neckW * 2f, py(0.065f)))
}

// ─── Head ────────────────────────────────────────────────────────────────────

private fun DrawScope.drawHead(fur: Color, shading: Color) {
    val cx = px(0.50f)
    val cy = py(0.255f)
    val rw = px(0.195f)
    val rh = py(0.155f)

    // main head
    drawOval(fur, Offset(cx - rw, cy - rh), Size(rw * 2f, rh * 2f))
    // jaw slightly wider
    drawOval(fur, Offset(cx - rw * 0.85f, cy - rh * 0.05f), Size(rw * 1.70f, rh * 1.20f))

    // head fur tuft
    listOf(-0.07f to -0.95f, 0f to -1.0f, 0.07f to -0.95f).forEach { (dx, dy) ->
        drawOval(fur, Offset(cx + rw * dx - px(0.04f), cy + rh * dy - py(0.03f)), Size(px(0.08f), py(0.075f)))
    }

    // outline
    val headOutline = Path().apply {
        addOval(Rect(cx - rw, cy - rh, cx + rw, cy + rh))
    }
    drawOval(OUTLINE, Offset(cx - rw, cy - rh), Size(rw * 2f, rh * 2f), style = outline(2.5f))
    drawOval(OUTLINE, Offset(cx - rw * 0.85f, cy - rh * 0.05f), Size(rw * 1.70f, rh * 1.20f), style = outline(1.5f))

    // cheek puffs
    val blush = Color(0xFFFF9EB5).copy(alpha = 0.30f)
    drawCircle(blush, rw * 0.30f, Offset(cx - rw * 0.62f, cy + rh * 0.40f))
    drawCircle(blush, rw * 0.30f, Offset(cx + rw * 0.62f, cy + rh * 0.40f))
}

// ─── Face ────────────────────────────────────────────────────────────────────

private fun DrawScope.drawFaceDetail(
    eyeStyle: String,
    eyeColor: Color,
    noseColor: Color,
    mouthStyle: String,
) {
    val cx = px(0.50f)
    val cy = py(0.255f)
    val spread = px(0.08f)
    val eyeY = cy - py(0.012f)
    val eyeR = px(0.052f)

    when (eyeStyle) {
        "sleepy" -> {
            drawSleepyEye(cx - spread, eyeY, eyeR, eyeColor)
            drawSleepyEye(cx + spread, eyeY, eyeR, eyeColor)
        }
        "wide" -> {
            drawRoundEye(cx - spread, eyeY, eyeR * 1.25f, eyeColor)
            drawRoundEye(cx + spread, eyeY, eyeR * 1.25f, eyeColor)
        }
        "sparkle" -> {
            drawSparkleEye(cx - spread, eyeY, eyeR, eyeColor)
            drawSparkleEye(cx + spread, eyeY, eyeR, eyeColor)
        }
        else -> {
            drawRoundEye(cx - spread, eyeY, eyeR, eyeColor)
            drawRoundEye(cx + spread, eyeY, eyeR, eyeColor)
        }
    }

    // nose (small inverted-triangle/oval)
    val noseY = cy + py(0.055f)
    val noseW = px(0.038f)
    val noseH = py(0.022f)
    val nosePath = Path().apply {
        moveTo(cx, noseY + noseH)
        lineTo(cx - noseW, noseY)
        lineTo(cx + noseW, noseY)
        close()
    }
    drawPath(nosePath, noseColor)
    drawPath(nosePath, OUTLINE.copy(alpha = 0.5f), style = outline(1.5f))

    // philtrum line
    drawLine(OUTLINE.copy(alpha = 0.35f), Offset(cx, noseY + noseH), Offset(cx, cy + py(0.082f)), strokeWidth = 1.5f * density)

    // mouth
    val mouthY = cy + py(0.085f)
    drawMouthShape(mouthStyle, cx, mouthY)

    // whiskers
    val wY = noseY + noseH * 0.3f
    val wColor = OUTLINE.copy(alpha = 0.22f)
    val sw = 1.2f * density
    drawLine(wColor, Offset(cx - px(0.04f), wY), Offset(cx - px(0.22f), wY - py(0.010f)), sw, StrokeCap.Round)
    drawLine(wColor, Offset(cx - px(0.04f), wY + py(0.014f)), Offset(cx - px(0.22f), wY + py(0.018f)), sw, StrokeCap.Round)
    drawLine(wColor, Offset(cx + px(0.04f), wY), Offset(cx + px(0.22f), wY - py(0.010f)), sw, StrokeCap.Round)
    drawLine(wColor, Offset(cx + px(0.04f), wY + py(0.014f)), Offset(cx + px(0.22f), wY + py(0.018f)), sw, StrokeCap.Round)
}

private fun DrawScope.drawRoundEye(cx: Float, cy: Float, r: Float, iris: Color) {
    drawCircle(Color.White, r, Offset(cx, cy))
    drawCircle(iris, r * 0.68f, Offset(cx, cy))
    drawCircle(Color.Black, r * 0.38f, Offset(cx, cy))
    drawCircle(Color.White, r * 0.16f, Offset(cx - r * 0.18f, cy - r * 0.18f))
    drawCircle(OUTLINE, r, Offset(cx, cy), style = outline(2f))
}

private fun DrawScope.drawSleepyEye(cx: Float, cy: Float, r: Float, iris: Color) {
    drawCircle(Color.White, r, Offset(cx, cy))
    drawCircle(iris, r * 0.62f, Offset(cx, cy + r * 0.12f))
    drawCircle(Color.Black, r * 0.34f, Offset(cx, cy + r * 0.12f))
    val lid = Path().apply { addRect(Rect(cx - r, cy - r, cx + r, cy + r * 0.05f)) }
    drawPath(lid, Color.White.copy(alpha = 0.90f))
    drawLine(OUTLINE, Offset(cx - r * 1.05f, cy), Offset(cx + r * 1.05f, cy), strokeWidth = 2f * density, cap = StrokeCap.Round)
    drawCircle(OUTLINE, r, Offset(cx, cy), style = outline(2f))
}

private fun DrawScope.drawSparkleEye(cx: Float, cy: Float, r: Float, iris: Color) {
    drawCircle(Color.White, r, Offset(cx, cy))
    drawCircle(iris, r * 0.68f, Offset(cx, cy))
    drawCircle(Color.Black, r * 0.38f, Offset(cx, cy))
    drawCircle(Color.White, r * 0.24f, Offset(cx - r * 0.16f, cy - r * 0.16f))
    drawCircle(Color.White, r * 0.11f, Offset(cx + r * 0.22f, cy + r * 0.10f))
    drawCircle(OUTLINE, r, Offset(cx, cy), style = outline(2f))
}

private fun DrawScope.drawMouthShape(style: String, cx: Float, my: Float) {
    val sw = 2f * density
    when (style) {
        "neutral" -> drawLine(OUTLINE.copy(alpha = 0.7f), Offset(cx - px(0.045f), my), Offset(cx + px(0.045f), my), sw, StrokeCap.Round)
        "silly" -> {
            val path = Path().apply {
                moveTo(cx - px(0.055f), my)
                cubicTo(cx - px(0.02f), my + py(0.028f), cx + px(0.03f), my - py(0.012f), cx + px(0.055f), my + py(0.020f))
            }
            drawPath(path, OUTLINE.copy(alpha = 0.7f), style = Stroke(sw, cap = StrokeCap.Round))
        }
        "surprised" -> {
            drawOval(OUTLINE.copy(alpha = 0.7f), Offset(cx - px(0.028f), my - py(0.010f)), Size(px(0.056f), py(0.042f)))
        }
        else -> { // smile
            val path = Path().apply {
                moveTo(cx - px(0.055f), my)
                cubicTo(cx - px(0.025f), my + py(0.030f), cx + px(0.025f), my + py(0.030f), cx + px(0.055f), my)
            }
            drawPath(path, OUTLINE.copy(alpha = 0.7f), style = Stroke(sw, cap = StrokeCap.Round))
        }
    }
}

// ─── Accessories ─────────────────────────────────────────────────────────────

private fun DrawScope.drawAccessory(accessory: String) {
    when (accessory) {
        "bow_tie" -> drawBowTie()
        "top_hat" -> drawTopHat()
        "flower" -> drawFlower()
        "glasses" -> drawGlasses()
        "scarf" -> drawScarf()
    }
}

private fun DrawScope.drawBowTie() {
    val cx = px(0.50f)
    val cy = py(0.415f)
    val bw = px(0.065f)
    val bh = py(0.030f)
    val color = Color(0xFFE91E63)
    fun wing(sign: Float) = Path().apply {
        moveTo(cx, cy)
        lineTo(cx + sign * bw, cy - bh)
        lineTo(cx + sign * bw, cy + bh)
        close()
    }
    drawPath(wing(-1f), color)
    drawPath(wing(1f), color)
    drawCircle(Color(0xFFAD1457), bh * 0.65f, Offset(cx, cy))
    drawPath(wing(-1f), OUTLINE, style = outline(1.5f))
    drawPath(wing(1f), OUTLINE, style = outline(1.5f))
}

private fun DrawScope.drawTopHat() {
    val cx = px(0.50f)
    val brimY = py(0.115f)
    val brimW = px(0.28f)
    val crownW = px(0.17f)
    val crownH = py(0.095f)
    val hatColor = Color(0xFF212121)
    drawRoundRect(hatColor, Offset(cx - crownW, brimY - crownH), Size(crownW * 2f, crownH), CornerRadius(6f))
    drawRoundRect(hatColor, Offset(cx - brimW, brimY - py(0.012f)), Size(brimW * 2f, py(0.024f)), CornerRadius(4f))
    drawRoundRect(Color(0xFFE91E63), Offset(cx - crownW, brimY - py(0.025f)), Size(crownW * 2f, py(0.014f)), CornerRadius(2f))
    drawRoundRect(OUTLINE, Offset(cx - crownW, brimY - crownH), Size(crownW * 2f, crownH), CornerRadius(6f), style = outline(2f))
    drawRoundRect(OUTLINE, Offset(cx - brimW, brimY - py(0.012f)), Size(brimW * 2f, py(0.024f)), CornerRadius(4f), style = outline(2f))
}

private fun DrawScope.drawFlower() {
    val cx = px(0.675f)
    val cy = py(0.175f)
    val pr = px(0.038f)
    val color = Color(0xFFFF9800)
    listOf(0f, 60f, 120f, 180f, 240f, 300f).forEach { deg ->
        val rad = Math.toRadians(deg.toDouble())
        drawCircle(color, pr, Offset(cx + (pr * 1.05f * Math.cos(rad)).toFloat(), cy + (pr * 1.05f * Math.sin(rad)).toFloat()))
    }
    drawCircle(Color(0xFFFFEB3B), pr * 0.75f, Offset(cx, cy))
}

private fun DrawScope.drawGlasses() {
    val cy = py(0.248f)
    val lx = px(0.42f)
    val rx = px(0.58f)
    val r = px(0.062f)
    val sw = 2f * density
    val col = Color(0xFF795548)
    drawCircle(col, r, Offset(lx, cy), style = Stroke(sw))
    drawCircle(col, r, Offset(rx, cy), style = Stroke(sw))
    drawLine(col, Offset(lx + r, cy), Offset(rx - r, cy), sw, StrokeCap.Round)
    drawLine(col, Offset(lx - r, cy), Offset(px(0.22f), cy - py(0.008f)), sw, StrokeCap.Round)
    drawLine(col, Offset(rx + r, cy), Offset(px(0.78f), cy - py(0.008f)), sw, StrokeCap.Round)
}

private fun DrawScope.drawScarf() {
    val cy = py(0.392f)
    val scarfColor = Color(0xFF2196F3)
    drawRoundRect(scarfColor, Offset(px(0.22f), cy - py(0.020f)), Size(px(0.56f), py(0.040f)), CornerRadius(8f))
    drawRoundRect(Color(0xFF1565C0), Offset(px(0.40f), cy - py(0.026f)), Size(px(0.20f), py(0.052f)), CornerRadius(6f))
    drawRoundRect(scarfColor, Offset(px(0.42f), cy + py(0.024f)), Size(px(0.08f), py(0.060f)), CornerRadius(4f))
    drawRoundRect(Color(0xFF64B5F6), Offset(px(0.51f), cy + py(0.026f)), Size(px(0.06f), py(0.050f)), CornerRadius(4f))
}

// ─── Color maps ──────────────────────────────────────────────────────────────

private fun furColor(id: String): Color = when (id) {
    "cream"   -> Color(0xFFFFF0CC)
    "brown"   -> Color(0xFFA07848)
    "grey"    -> Color(0xFFBBBBBB)
    "black"   -> Color(0xFF484040)
    "spotted" -> Color(0xFFF5F5F0)
    else      -> Color(0xFFF2EFEA) // white
}

private fun eyeColorOf(id: String): Color = when (id) {
    "blue"  -> Color(0xFF4A90D9)
    "green" -> Color(0xFF4CAF50)
    "pink"  -> Color(0xFFE91E8C)
    "black" -> Color(0xFF282828)
    else    -> Color(0xFF6B3A2A) // brown
}

private fun noseColorOf(id: String): Color = when (id) {
    "brown" -> Color(0xFF8B6914)
    "black" -> Color(0xFF333333)
    else    -> Color(0xFFFF9EB5) // pink
}
