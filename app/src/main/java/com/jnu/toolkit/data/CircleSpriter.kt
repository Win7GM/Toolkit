package com.jnu.toolkit.data

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin


class CircleSpriter(
    var x: Float,
    var y: Float,
    var radius: Float,
    var maxWidth: Float,
    var maxHeight: Float
) {
    private var direction: Double = Math.random() * 2 * PI
    private var speed = Math.random() + .5
    fun draw(canvas: Canvas) {
        val paint = Paint()
        paint.color = Color.RED
        canvas.drawCircle(x, y, radius, paint)
    }

    fun move() {
        x += (30 * speed * cos(direction)).toFloat()
        y += (30 * speed * sin(direction)).toFloat()
        if (x < 0) x += maxWidth
        if (y < 0) y += maxHeight
        if (x > maxWidth) x -= maxWidth
        if (y > maxHeight) y -= maxHeight
        speed = Math.random() + .5
        direction = Math.random() * 2 * PI
    }

    fun isShot(touchedX: Float, touchedY: Float): Boolean {
        val distance =
            ((touchedX - x) * (touchedX - x) + (touchedY - y) * (touchedY - y)).toDouble()
        return abs(distance - radius * radius) < 7500
    }

}