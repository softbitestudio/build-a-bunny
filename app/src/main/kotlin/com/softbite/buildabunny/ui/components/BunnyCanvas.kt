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
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.softbite.buildabunny.data.model.CharacterConfig

@Composable
fun BunnyCanvas(
    config: CharacterConfig,
    modifier: Modifier = Modifier,
) {
    Canvas(
        modifier = modifier
            .aspectRatio(0.75f)
            .semantics { contentDescription = "Bunny preview: ${config.name}" },
    ) {
        drawBackground(config.background)
        drawEars(config.earStyle, furColor(config.furColor), config.furColor == "spotted")
        drawBody(config.bodyShape, furColor(config.furColor), config.furColor == "spotted")
        drawHead(furColor(config.furColor), config.furColor == "spotted")
        drawEyes(config.eyeStyle, eyeColorOf(config.eyeColor))
        drawNose(noseColorOf(config.noseColor))
        drawMouth(config.mouthStyle)
        drawAccessory(config.accessory)
    }
}

// ─── Background ──────────────────────────────────────────────────────────────

private fun DrawScope.drawBackground(background: String) {
    when (background) {
        "meadow" -> {
            drawRect(Color(0xFF87CEEB))
            drawRect(
                color = Color(0xFF66BB6A),
                topLeft = Offset(0f, size.height * 0.72f),
                size = Size(size.width, size.height * 0.28f),
            )
            drawOval(
                color = Color(0xFF81C784),
                topLeft = Offset(-size.width * 0.1f, size.height * 0.65f),
                size = Size(size.width * 1.2f, size.height * 0.18f),
            )
        }
        "sky" -> {
            drawRect(Brush.verticalGradient(listOf(Color(0xFF64B5F6), Color(0xFFBBDEFB))))
            repeat(5) { i ->
                val cx = size.width * (0.15f + i * 0.18f)
                val cy = size.height * (0.08f + (i % 3) * 0.07f)
                drawOval(Color.White.copy(alpha = 0.8f), Offset(cx, cy), Size(size.width * 0.15f, size.height * 0.05f))
                drawOval(Color.White.copy(alpha = 0.7f), Offset(cx - size.width * 0.05f, cy + size.height * 0.01f), Size(size.width * 0.12f, size.height * 0.04f))
            }
        }
        "stars" -> {
            drawRect(Brush.verticalGradient(listOf(Color(0xFF0D0D2B), Color(0xFF1A237E))))
            listOf(
                0.1f to 0.05f, 0.35f to 0.09f, 0.6f to 0.04f, 0.85f to 0.12f,
                0.22f to 0.18f, 0.78f to 0.22f, 0.5f to 0.14f, 0.92f to 0.06f,
                0.08f to 0.28f, 0.45f to 0.25f, 0.7f to 0.30f,
            ).forEach { (fx, fy) ->
                drawCircle(Color.White, radius = 2.5f, center = Offset(fx * size.width, fy * size.height))
            }
        }
        "rainbow" -> {
            val colors = listOf(
                Color(0xFFFF6B6B), Color(0xFFFF9800), Color(0xFFFFEB3B),
                Color(0xFF81C784), Color(0xFF64B5F6), Color(0xFFCE93D8),
            )
            val stripH = size.height / colors.size
            colors.forEachIndexed { i, c ->
                drawRect(c.copy(alpha = 0.35f), Offset(0f, i * stripH), Size(size.width, stripH))
            }
            drawRect(Color.White.copy(alpha = 0.25f))
        }
        "plain_pink" -> drawRect(Color(0xFFFCE4EC))
        else -> drawRect(Color(0xFFF8F8F8))
    }
}

// ─── Ears ────────────────────────────────────────────────────────────────────

private fun DrawScope.drawEars(style: String, fur: Color, spotted: Boolean) {
    val earW = size.width * 0.12f
    val earH = size.height * 0.30f
    val innerColor = Color(0xFFFFB6C1).copy(alpha = 0.7f)

    when (style) {
        "upright" -> {
            val lx = size.width * 0.30f - earW / 2
            val rx = size.width * 0.70f - earW / 2
            val ty = size.height * 0.06f
            drawEarShape(lx, ty, earW, earH, fur, innerColor, spotted, rotation = -8f)
            drawEarShape(rx, ty, earW, earH, fur, innerColor, spotted, rotation = 8f)
        }
        "floppy" -> {
            val lx = size.width * 0.14f
            val rx = size.width * 0.74f
            val ty = size.height * 0.20f
            val flopH = earH * 0.9f
            drawEarShape(lx, ty, earW, flopH, fur, innerColor, spotted, rotation = -60f)
            drawEarShape(rx, ty, earW, flopH, fur, innerColor, spotted, rotation = 60f)
        }
        "lop" -> {
            val ty = size.height * 0.08f
            drawEarShape(size.width * 0.30f - earW / 2, ty, earW, earH, fur, innerColor, spotted, rotation = -8f)
            drawEarShape(size.width * 0.74f, size.height * 0.22f, earW, earH * 0.88f, fur, innerColor, spotted, rotation = 58f)
        }
    }
}

private fun DrawScope.drawEarShape(
    x: Float, y: Float, w: Float, h: Float,
    fur: Color, inner: Color, spotted: Boolean,
    rotation: Float,
) {
    withTransform({
        rotate(rotation, Offset(x + w / 2, y + h / 2))
    }) {
        drawRoundRect(fur, Offset(x, y), Size(w, h), CornerRadius(w / 2))
        if (spotted) {
            drawOval(Color(0xFF8B6914).copy(alpha = 0.4f), Offset(x + w * 0.1f, y + h * 0.1f), Size(w * 0.8f, h * 0.4f))
        }
        drawRoundRect(inner, Offset(x + w * 0.25f, y + h * 0.12f), Size(w * 0.5f, h * 0.65f), CornerRadius(w / 4))
    }
}

// ─── Body ────────────────────────────────────────────────────────────────────

private fun DrawScope.drawBody(shape: String, fur: Color, spotted: Boolean) {
    val (bw, bh) = when (shape) {
        "slim" -> size.width * 0.48f to size.height * 0.40f
        "chubby" -> size.width * 0.70f to size.height * 0.50f
        else -> size.width * 0.58f to size.height * 0.45f
    }
    val bx = (size.width - bw) / 2
    val by = size.height * 0.52f

    drawOval(fur, Offset(bx, by), Size(bw, bh))
    if (spotted) {
        drawOval(Color(0xFF8B6914).copy(alpha = 0.35f), Offset(bx + bw * 0.55f, by + bh * 0.15f), Size(bw * 0.3f, bh * 0.3f))
        drawOval(Color(0xFF8B6914).copy(alpha = 0.28f), Offset(bx + bw * 0.15f, by + bh * 0.5f), Size(bw * 0.22f, bh * 0.22f))
    }
    // belly highlight
    drawOval(
        Color.White.copy(alpha = 0.30f),
        Offset(bx + bw * 0.25f, by + bh * 0.20f),
        Size(bw * 0.50f, bh * 0.45f),
    )
}

// ─── Head ────────────────────────────────────────────────────────────────────

private fun DrawScope.drawHead(fur: Color, spotted: Boolean) {
    val hw = size.width * 0.50f
    val hh = size.height * 0.36f
    val hx = (size.width - hw) / 2
    val hy = size.height * 0.20f

    drawOval(fur, Offset(hx, hy), Size(hw, hh))
    if (spotted) {
        drawOval(Color(0xFF8B6914).copy(alpha = 0.30f), Offset(hx + hw * 0.6f, hy + hh * 0.1f), Size(hw * 0.28f, hh * 0.30f))
    }
    // cheek blush
    val blush = Color(0xFFFF9EB5).copy(alpha = 0.35f)
    drawOval(blush, Offset(hx + hw * 0.05f, hy + hh * 0.55f), Size(hw * 0.22f, hh * 0.20f))
    drawOval(blush, Offset(hx + hw * 0.73f, hy + hh * 0.55f), Size(hw * 0.22f, hh * 0.20f))
}

// ─── Eyes ────────────────────────────────────────────────────────────────────

private fun DrawScope.drawEyes(style: String, irisColor: Color) {
    val cx = size.width / 2
    val ey = size.height * 0.36f
    val spread = size.width * 0.11f
    val r = size.width * 0.055f

    when (style) {
        "sleepy" -> {
            drawSleepyEye(cx - spread, ey, r, irisColor)
            drawSleepyEye(cx + spread, ey, r, irisColor)
        }
        "wide" -> {
            drawRoundEye(cx - spread, ey, r * 1.25f, irisColor)
            drawRoundEye(cx + spread, ey, r * 1.25f, irisColor)
        }
        "sparkle" -> {
            drawSparkleEye(cx - spread, ey, r, irisColor)
            drawSparkleEye(cx + spread, ey, r, irisColor)
        }
        else -> {
            drawRoundEye(cx - spread, ey, r, irisColor)
            drawRoundEye(cx + spread, ey, r, irisColor)
        }
    }
}

private fun DrawScope.drawRoundEye(cx: Float, cy: Float, r: Float, irisColor: Color) {
    drawCircle(Color.White, r, Offset(cx, cy))
    drawCircle(irisColor, r * 0.65f, Offset(cx, cy))
    drawCircle(Color.Black, r * 0.35f, Offset(cx, cy))
    drawCircle(Color.White, r * 0.14f, Offset(cx - r * 0.18f, cy - r * 0.18f))
}

private fun DrawScope.drawSleepyEye(cx: Float, cy: Float, r: Float, irisColor: Color) {
    drawCircle(Color.White, r, Offset(cx, cy))
    drawCircle(irisColor, r * 0.60f, Offset(cx, cy + r * 0.1f))
    drawCircle(Color.Black, r * 0.32f, Offset(cx, cy + r * 0.1f))
    // eyelid covering top half
    val path = Path().apply {
        addRect(Rect(cx - r, cy - r, cx + r, cy))
    }
    drawPath(path, Color.White.copy(alpha = 0.85f))
    drawLine(Color.Black.copy(alpha = 0.8f), Offset(cx - r, cy), Offset(cx + r, cy), strokeWidth = r * 0.18f, cap = StrokeCap.Round)
}

private fun DrawScope.drawSparkleEye(cx: Float, cy: Float, r: Float, irisColor: Color) {
    drawCircle(Color.White, r, Offset(cx, cy))
    drawCircle(irisColor, r * 0.65f, Offset(cx, cy))
    drawCircle(Color.Black, r * 0.35f, Offset(cx, cy))
    drawCircle(Color.White, r * 0.22f, Offset(cx - r * 0.15f, cy - r * 0.15f))
    drawCircle(Color.White, r * 0.10f, Offset(cx + r * 0.20f, cy + r * 0.10f))
}

// ─── Nose ────────────────────────────────────────────────────────────────────

private fun DrawScope.drawNose(noseColor: Color) {
    val nx = size.width / 2
    val ny = size.height * 0.46f
    val nw = size.width * 0.06f
    val nh = size.height * 0.03f
    drawOval(noseColor, Offset(nx - nw / 2, ny - nh / 2), Size(nw, nh))
}

// ─── Mouth ───────────────────────────────────────────────────────────────────

private fun DrawScope.drawMouth(style: String) {
    val mx = size.width / 2
    val my = size.height * 0.50f
    val mouthColor = Color(0xFF5D3A1A)
    val sw = size.width * 0.016f

    when (style) {
        "neutral" -> {
            drawLine(mouthColor, Offset(mx - size.width * 0.06f, my), Offset(mx + size.width * 0.06f, my), sw, StrokeCap.Round)
        }
        "silly" -> {
            val path = Path().apply {
                moveTo(mx - size.width * 0.08f, my)
                cubicTo(mx - size.width * 0.04f, my + size.height * 0.04f, mx + size.width * 0.04f, my - size.height * 0.02f, mx + size.width * 0.08f, my + size.height * 0.03f)
            }
            drawPath(path, mouthColor, style = Stroke(width = sw, cap = StrokeCap.Round))
        }
        "surprised" -> {
            drawOval(mouthColor, Offset(mx - size.width * 0.04f, my - size.height * 0.015f), Size(size.width * 0.08f, size.height * 0.06f))
            drawOval(Color(0xFFFFCCBC), Offset(mx - size.width * 0.03f, my - size.height * 0.008f), Size(size.width * 0.06f, size.height * 0.045f))
        }
        else -> { // smile
            val path = Path().apply {
                moveTo(mx - size.width * 0.07f, my)
                cubicTo(mx - size.width * 0.04f, my + size.height * 0.04f, mx + size.width * 0.04f, my + size.height * 0.04f, mx + size.width * 0.07f, my)
            }
            drawPath(path, mouthColor, style = Stroke(width = sw, cap = StrokeCap.Round))
        }
    }

    // whiskers
    val wColor = Color(0xFF888888).copy(alpha = 0.6f)
    val wSw = size.width * 0.008f
    val ny = size.height * 0.46f
    drawLine(wColor, Offset(size.width * 0.18f, ny - size.height * 0.01f), Offset(size.width * 0.40f, ny), wSw, StrokeCap.Round)
    drawLine(wColor, Offset(size.width * 0.18f, ny + size.height * 0.015f), Offset(size.width * 0.40f, ny + size.height * 0.012f), wSw, StrokeCap.Round)
    drawLine(wColor, Offset(size.width * 0.82f, ny - size.height * 0.01f), Offset(size.width * 0.60f, ny), wSw, StrokeCap.Round)
    drawLine(wColor, Offset(size.width * 0.82f, ny + size.height * 0.015f), Offset(size.width * 0.60f, ny + size.height * 0.012f), wSw, StrokeCap.Round)
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
    val cx = size.width / 2
    val cy = size.height * 0.57f
    val bw = size.width * 0.08f
    val bh = size.height * 0.04f
    val color = Color(0xFFE91E63)
    val path = Path().apply {
        moveTo(cx, cy)
        lineTo(cx - bw, cy - bh)
        lineTo(cx - bw, cy + bh)
        close()
    }
    drawPath(path, color)
    val path2 = Path().apply {
        moveTo(cx, cy)
        lineTo(cx + bw, cy - bh)
        lineTo(cx + bw, cy + bh)
        close()
    }
    drawPath(path2, color)
    drawCircle(Color(0xFFAD1457), bh * 0.6f, Offset(cx, cy))
}

private fun DrawScope.drawTopHat() {
    val cx = size.width / 2
    val brimY = size.height * 0.20f
    val hatColor = Color(0xFF212121)
    val brimW = size.width * 0.46f
    val crownW = size.width * 0.28f
    val crownH = size.height * 0.14f
    // brim
    drawRoundRect(hatColor, Offset(cx - brimW / 2, brimY - size.height * 0.015f), Size(brimW, size.height * 0.03f), CornerRadius(4f))
    // crown
    drawRoundRect(hatColor, Offset(cx - crownW / 2, brimY - crownH), Size(crownW, crownH), CornerRadius(6f))
    // hat band
    drawRoundRect(Color(0xFFE91E63), Offset(cx - crownW / 2, brimY - size.height * 0.035f), Size(crownW, size.height * 0.02f), CornerRadius(2f))
}

private fun DrawScope.drawFlower() {
    val cx = size.width * 0.68f
    val cy = size.height * 0.22f
    val petalR = size.width * 0.045f
    val petalColor = Color(0xFFFF9800)
    val petalOffsets = listOf(0f, 60f, 120f, 180f, 240f, 300f)
    petalOffsets.forEach { angle ->
        val rad = Math.toRadians(angle.toDouble())
        val px = cx + (petalR * 1.1f * Math.cos(rad)).toFloat()
        val py = cy + (petalR * 1.1f * Math.sin(rad)).toFloat()
        drawCircle(petalColor, petalR, Offset(px, py))
    }
    drawCircle(Color(0xFFFFEB3B), petalR * 0.8f, Offset(cx, cy))
}

private fun DrawScope.drawGlasses() {
    val cy = size.height * 0.37f
    val lx = size.width * 0.345f
    val rx = size.width * 0.655f
    val r = size.width * 0.075f
    val strokeW = size.width * 0.018f
    val frameColor = Color(0xFF795548)
    drawCircle(Color.Transparent, r, Offset(lx, cy))
    drawCircle(frameColor, r, Offset(lx, cy), style = Stroke(width = strokeW))
    drawCircle(frameColor, r, Offset(rx, cy), style = Stroke(width = strokeW))
    // bridge
    drawLine(frameColor, Offset(lx + r, cy), Offset(rx - r, cy), strokeWidth = strokeW, cap = StrokeCap.Round)
    // temples
    drawLine(frameColor, Offset(lx - r, cy), Offset(size.width * 0.12f, cy - size.height * 0.01f), strokeW, StrokeCap.Round)
    drawLine(frameColor, Offset(rx + r, cy), Offset(size.width * 0.88f, cy - size.height * 0.01f), strokeW, StrokeCap.Round)
}

private fun DrawScope.drawScarf() {
    val cy = size.height * 0.57f
    val scarfColor = Color(0xFF2196F3)
    val scarfDark = Color(0xFF1565C0)
    drawRoundRect(scarfColor, Offset(size.width * 0.15f, cy - size.height * 0.025f), Size(size.width * 0.70f, size.height * 0.05f), CornerRadius(8f))
    drawRoundRect(scarfDark, Offset(size.width * 0.38f, cy - size.height * 0.035f), Size(size.width * 0.24f, size.height * 0.07f), CornerRadius(6f))
    drawRoundRect(scarfColor, Offset(size.width * 0.40f, cy + size.height * 0.03f), Size(size.width * 0.10f, size.height * 0.08f), CornerRadius(4f))
    drawRoundRect(Color(0xFF64B5F6), Offset(size.width * 0.52f, cy + size.height * 0.035f), Size(size.width * 0.08f, size.height * 0.06f), CornerRadius(4f))
}

// ─── Color helpers ───────────────────────────────────────────────────────────

private fun furColor(id: String): Color = when (id) {
    "cream" -> Color(0xFFFFF0CC)
    "brown" -> Color(0xFFA0724A)
    "grey" -> Color(0xFFBBBBBB)
    "black" -> Color(0xFF424242)
    "spotted" -> Color(0xFFF5F5F0)
    else -> Color(0xFFF5F5F0) // white
}

private fun eyeColorOf(id: String): Color = when (id) {
    "blue" -> Color(0xFF4A90D9)
    "green" -> Color(0xFF4CAF50)
    "pink" -> Color(0xFFE91E8C)
    "black" -> Color(0xFF222222)
    else -> Color(0xFF6B3A2A) // brown
}

private fun noseColorOf(id: String): Color = when (id) {
    "brown" -> Color(0xFF8B6914)
    "black" -> Color(0xFF333333)
    else -> Color(0xFFFF9EB5) // pink
}
