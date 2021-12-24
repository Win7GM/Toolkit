package com.jnu.toolkit.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.jnu.toolkit.data.CircleSpriter
import java.util.*


class GameView : SurfaceView, SurfaceHolder.Callback {
    private var surfaceHolder: SurfaceHolder? = null

    constructor(context: Context?) : super(context) {
        initView()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initView()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView()
    }

    private fun initView() {
        surfaceHolder = holder
        surfaceHolder!!.addCallback(this)
    }

    private var drawThread: DrawThread? = null
    private var isTouched = false
    private var touchedX = -1f
    private var touchedY = -1f
    override fun surfaceCreated(surfaceHolder: SurfaceHolder) {
        drawThread = DrawThread()
        drawThread!!.start()
        setOnTouchListener { view, motionEvent ->
            if (MotionEvent.ACTION_DOWN == motionEvent.action) {
                touchedX = motionEvent.x
                touchedY = motionEvent.y
                isTouched = true
            }
            false
        }
    }

    override fun surfaceChanged(surfaceHolder: SurfaceHolder, i: Int, i1: Int, i2: Int) {}
    override fun surfaceDestroyed(surfaceHolder: SurfaceHolder) {
        drawThread!!.myStop()
        try {
            drawThread!!.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    private inner class DrawThread : Thread() {
        private val spriters: ArrayList<CircleSpriter> = ArrayList<CircleSpriter>()
        private var isStopped = false
        fun myStop() {
            isStopped = true
        }

        override fun run() {
            super.run()
            var canvas: Canvas? = null
            var hitCount = 0
            val paint = Paint()
            while (!isStopped) {
                try {
                    canvas = surfaceHolder!!.lockCanvas()
                    canvas.drawColor(Color.GRAY)
                    if (isTouched) {
                        for (index in spriters.indices) {
                            if (spriters[index].isShot(touchedX, touchedY)) {
                                hitCount++
                            }
                        }
                        canvas.drawText("t.x " + touchedX, 500f, 600f, paint)
                        canvas.drawText("t.y " + touchedY, 500f, 800f, paint)
                    }
                    for (index in spriters.indices) {
                        spriters[index].move()
                    }
                    for (index in spriters.indices) {
                        spriters[index].draw(canvas)
                    }
                    isTouched = false
                    paint.textSize = 100f
                    paint.color = Color.GREEN
                    canvas.drawText("hit $hitCount", 100f, 100f, paint)
                    canvas.drawText("0.x " + spriters[0].x, 500f, 500f, paint)
                    canvas.drawText("0.y " + spriters[0].y, 500f, 700f, paint)
                } catch (e: Exception) {
                } finally {
                    if (null != canvas) {
                        surfaceHolder!!.unlockCanvasAndPost(canvas)
                    }
                }
                try {
                    sleep(20)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }

        init {
            spriters.add(
                CircleSpriter(
                    this@GameView.width.toFloat() / 2,
                    this@GameView.height.toFloat() / 2,
                    100F,
                    this@GameView.width.toFloat(),
                    this@GameView.height.toFloat()
                )
            )
        }
    }
}