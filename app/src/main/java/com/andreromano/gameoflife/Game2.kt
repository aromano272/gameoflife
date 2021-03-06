package com.andreromano.gameoflife

import android.graphics.*
import androidx.core.graphics.toRectF
import com.andreromano.gameoflife.extensions.*

class Game2 {

    enum class Direction {
        UP,
        RIGHT,
        DOWN,
        LEFT
    }

    private var currentLevel: Level? = Level.ONE_VERTICAL_PASSAGE

    private var screenWidth = 0
    private var screenHeight = 0
    private var sceneWidth = 0
    private var sceneHeight = 0
    private var entityWidth = 0
    private var entityHeight = 0
    private var mapTileWidth = 0
    private var mapTileHeight = 0

    private var frameCount: Long = 0

    private var score: Int = 0

    private var pacman: PacmanEntity? = null
    private var entitiesMap: Array<Array<EntityOld?>> = Array<Array<EntityOld?>>(mapTileHeight) {
        Array(mapTileWidth) {
            null
        }
    }

    private val redStrokePaint = Paint().apply {
        style = Paint.Style.STROKE
        color = Color.RED
        strokeWidth = 2f
    }

    private val greenPaint = Paint().apply {
        style = Paint.Style.FILL
        color = Color.GREEN
    }

    private val blackPaint = Paint().apply {
        style = Paint.Style.FILL
        color = Color.BLACK
    }

    private val bluePaint = Paint().apply {
        style = Paint.Style.FILL
        color = Color.BLUE
    }

    private val yellowPaint = Paint().apply {
        style = Paint.Style.FILL
        color = Color.YELLOW
    }

    inner class PacmanEntity(
        screenPos: Vec2,
        tilePos: Vec2,
        width: Int,
        height: Int,
    ) : EntityOld(screenPos, tilePos, width, height) {

        override val canvasRect: RectF
            get() = super.canvasRect.scale(0.75f)

        val velocity = 3

        override fun updateAndRender(canvas: Canvas) {
            if (!::direction.isInitialized && ::queuedDirection.isInitialized) direction = queuedDirection
            if (::direction.isInitialized && ::queuedDirection.isInitialized) {
                var newX = x
                var newY = y

                if (direction == Direction.LEFT && queuedDirection == Direction.RIGHT) direction = queuedDirection
                if (direction == Direction.RIGHT && queuedDirection == Direction.LEFT) direction = queuedDirection
                if (direction == Direction.UP && queuedDirection == Direction.DOWN) direction = queuedDirection
                if (direction == Direction.DOWN && queuedDirection == Direction.UP) direction = queuedDirection

                when (direction) {
                    Direction.UP -> newY = y - velocity
                    Direction.RIGHT -> newX = x + velocity
                    Direction.DOWN -> newY = y + velocity
                    Direction.LEFT -> newX = x - velocity
                }
                if (direction != queuedDirection) {
                    when (direction) {
                        Direction.UP ->
                            if (queuedDirection == Direction.LEFT) {
                                val testTilePos = tilePos.copy(x = tileX - 1)
                                val testScreenPos = getScreenPosFromTilePos(testTilePos)
                                val testEntity = getEntity(testTilePos)
                                if (testEntity !is WallEntity && testScreenPos.y <= y && testScreenPos.y >= newY) {
                                    val deltaY = testScreenPos.y - newY
                                    val velocityLeft = velocity - deltaY
                                    newY = testScreenPos.y
                                    newX -= velocityLeft
                                    direction = Direction.LEFT
                                }
                            } else if (queuedDirection == Direction.RIGHT) {
                                val testTilePos = tilePos.copy(x = tileX + 1)
                                val testScreenPos = getScreenPosFromTilePos(testTilePos)
                                val testEntity = getEntity(testTilePos)
                                if (testEntity !is WallEntity && testScreenPos.y <= y && testScreenPos.y >= newY) {
                                    val deltaY = testScreenPos.y - newY
                                    val velocityLeft = velocity - deltaY
                                    newY = testScreenPos.y
                                    newX += velocityLeft
                                    direction = Direction.RIGHT
                                }
                            }
                        Direction.RIGHT ->
                            if (queuedDirection == Direction.UP) {
                                val testTilePos = tilePos.copy(y = tileY - 1)
                                val testScreenPos = getScreenPosFromTilePos(testTilePos)
                                val testEntity = getEntity(testTilePos)
                                if (testEntity !is WallEntity && testScreenPos.x >= x && testScreenPos.x <= newX) {
                                    val deltaX = newX - testScreenPos.x
                                    val velocityLeft = velocity - deltaX
                                    newX = testScreenPos.x
                                    newY -= velocityLeft
                                    direction = Direction.UP
                                }
                            } else if (queuedDirection == Direction.DOWN) {
                                val testTilePos = tilePos.copy(y = tileY + 1)
                                val testScreenPos = getScreenPosFromTilePos(testTilePos)
                                val testEntity = getEntity(testTilePos)
                                if (testEntity !is WallEntity && testScreenPos.x >= x && testScreenPos.x <= newX) {
                                    val deltaX = newX - testScreenPos.x
                                    val velocityLeft = velocity - deltaX
                                    newX = testScreenPos.x
                                    newY += velocityLeft
                                    direction = Direction.DOWN
                                }
                            }

                        Direction.DOWN ->
                            if (queuedDirection == Direction.LEFT) {
                                val testTilePos = tilePos.copy(x = tileX - 1)
                                val testScreenPos = getScreenPosFromTilePos(testTilePos)
                                val testEntity = getEntity(testTilePos)
                                if (testEntity !is WallEntity && testScreenPos.y >= y && testScreenPos.y <= newY) {
                                    val deltaY = newY - testScreenPos.y
                                    val velocityLeft = velocity - deltaY
                                    newY = testScreenPos.y
                                    newX -= velocityLeft
                                    direction = Direction.LEFT
                                }
                            } else if (queuedDirection == Direction.RIGHT) {
                                val testTilePos = tilePos.copy(x = tileX + 1)
                                val testScreenPos = getScreenPosFromTilePos(testTilePos)
                                val testEntity = getEntity(testTilePos)
                                if (testEntity !is WallEntity && testScreenPos.y >= y && testScreenPos.y <= newY) {
                                    val deltaY = newY - testScreenPos.y
                                    val velocityLeft = velocity - deltaY
                                    newY = testScreenPos.y
                                    newX += velocityLeft
                                    direction = Direction.RIGHT
                                }
                            }

                        Direction.LEFT ->
                            if (queuedDirection == Direction.UP) {
                                val testTilePos = tilePos.copy(y = tileY - 1)
                                val testScreenPos = getScreenPosFromTilePos(testTilePos)
                                val testEntity = getEntity(testTilePos)
                                if (testEntity !is WallEntity && testScreenPos.x <= x && testScreenPos.x >= newX) {
                                    val deltaX = testScreenPos.x - newX
                                    val velocityLeft = velocity - deltaX
                                    newX = testScreenPos.x
                                    newY -= velocityLeft
                                    direction = Direction.UP
                                }
                            } else if (queuedDirection == Direction.DOWN) {
                                val testTilePos = tilePos.copy(y = tileY + 1)
                                val testScreenPos = getScreenPosFromTilePos(testTilePos)
                                val testEntity = getEntity(testTilePos)
                                if (testEntity !is WallEntity && testScreenPos.x <= x && testScreenPos.x >= newX) {
                                    val deltaX = testScreenPos.x - newX
                                    val velocityLeft = velocity - deltaX
                                    newX = testScreenPos.x
                                    newY += velocityLeft
                                    direction = Direction.DOWN
                                }
                            }

                    }
                }


                // check if out of bounds and wrap
                if (newX >= sceneWidth) {
                    newX -= sceneWidth
                }
                if (newX < 0) {
                    newX += sceneWidth
                }
                if (newY >= sceneHeight) {
                    newY -= sceneHeight
                }
                if (newY < 0) {
                    newY += sceneHeight
                }

                val testPos = Vec2(newX, newY)
                val testTile = let {
                    val testTilePos = getTilePosFromScreenPos(testPos)
                    when (direction) {
                        Direction.UP -> testTilePos.copy(y = testTilePos.y - 1)
                        Direction.RIGHT -> testTilePos.copy(x = testTilePos.x + 1)
                        Direction.DOWN -> testTilePos.copy(y = testTilePos.y + 1)
                        Direction.LEFT -> testTilePos.copy(x = testTilePos.x - 1)
                    }
                }
                val testEntity = getEntity(testTile.correctTileOutOfBounds())

                // TODO: Check all entities between pacman and testEntity to prevent tunneling during high speed moves or low frame rate
                if (testEntity !is WallEntity) {
                    screenPos = testPos
                    tilePos = getTilePosFromScreenPos(screenPos)
                } else {
                    // makes pacman snug to the wall when a collision is detected on newX or newY
                    when (direction) {
                        Direction.UP -> {
                            val newTileY = testTile.y + 1
                            val newScreenY = getScreenYFromTileY(newTileY)
                            y = newScreenY
                        }
                        Direction.RIGHT -> {
                            val newTileX = testTile.x - 1
                            val newScreenX = getScreenXFromTileX(newTileX)
                            x = newScreenX
                        }
                        Direction.DOWN -> {
                            val newTileY = testTile.y - 1
                            val newScreenY = getScreenYFromTileY(newTileY)
                            y = newScreenY
                        }
                        Direction.LEFT -> {
                            val newTileX = testTile.x + 1
                            val newScreenX = getScreenXFromTileX(newTileX)
                            x = newScreenX
                        }
                    }
                    tilePos = getTilePosFromScreenPos(screenPos)
                }

                if (testEntity is FoodEntity) {
                    score += 100
                    removeEntity(testEntity)
                }
            }
            val left = x - width / 2
            val top = y - height / 2
            val right = x + width / 2
            val bottom = y + height / 2

            canvas.drawOval(canvasRect, yellowPaint)
            // enables wrapping around the scene
            if (left < 0) canvas.drawOval(canvasRect.copy(sceneWidth.toFloat() - -left, y - height / 2f, sceneWidth.toFloat() - -left + width, y + height / 2f).scale(0.75f), yellowPaint)
            if (top < 0) canvas.drawOval(canvasRect.copy(x - width / 2f, sceneHeight.toFloat() - -top, x + width / 2f, sceneHeight.toFloat() - -top + height).scale(0.75f), yellowPaint)
            if (right > sceneWidth) canvas.drawOval(Rect(right - sceneWidth - width, y - height / 2, right - sceneWidth, bottom).toRectF().scale(0.75f), yellowPaint)
            if (bottom > sceneHeight) canvas.drawOval(Rect(x - width / 2, bottom - sceneHeight - height, right, bottom - sceneHeight).toRectF().scale(0.75f), yellowPaint)

            // test entity grid bounding box
            val testX = tileX * width
            val testY = tileY * height
            canvas.drawRect(Rect(testX, testY, testX + width, testY + height), redStrokePaint)

            val testTileToScreenRect: (Vec2) -> Rect = { (x, y) ->
                Rect(x * width, y * height, x * width + width, y * height + height)
            }

            val testPos = Vec2(x, y)
            val testRect = screenRect
            val testTLTile = getTilePosFromScreenPos(testRect.topLeft)
            val testTLEntity = getEntity(testTLTile.correctTileOutOfBounds())
            val paintTL = Paint().apply {
                style = Paint.Style.STROKE
                color = Color.WHITE
                strokeWidth = 4f
            }
            canvas.drawRect(testTileToScreenRect(testTLTile), paintTL)

            val testTRTile = getTilePosFromScreenPos(testRect.topRight)
            val testTREntity = getEntity(testTRTile.correctTileOutOfBounds())
            val paintTR = Paint().apply {
                style = Paint.Style.STROKE
                color = Color.CYAN
                strokeWidth = 4f
            }
            canvas.drawRect(testTileToScreenRect(testTRTile), paintTR)

            val testBLTile = getTilePosFromScreenPos(testRect.bottomLeft)
            val testBLEntity = getEntity(testBLTile.correctTileOutOfBounds())
            val paintBL = Paint().apply {
                style = Paint.Style.STROKE
                color = Color.MAGENTA
                strokeWidth = 4f
            }
            canvas.drawRect(testTileToScreenRect(testBLTile), paintBL)

            val testBRTile = getTilePosFromScreenPos(testRect.bottomRight)
            val testBREntity = getEntity(testBRTile.correctTileOutOfBounds())
            val paintBR = Paint().apply {
                style = Paint.Style.STROKE
                color = Color.GRAY
                strokeWidth = 4f
            }
            canvas.drawRect(testTileToScreenRect(testBRTile), paintBR)

        }
    }

    private fun Vec2.correctScreenOutOfBounds(): Vec2 {
        var x = this.x
        var y = this.y
        // check if out of bounds and wrap
        if (x >= sceneWidth) {
            x -= sceneWidth
        }
        if (x < 0) {
            x += sceneWidth
        }
        if (y >= sceneHeight) {
            y -= sceneHeight
        }
        if (y < 0) {
            y += sceneHeight
        }

        return Vec2(x, y)
    }

    private fun Vec2.correctTileOutOfBounds(): Vec2 {
        var x = this.x
        var y = this.y
        // check if out of bounds and wrap
        if (x >= mapTileWidth) {
            x -= mapTileWidth
        }
        if (x < 0) {
            x += mapTileWidth
        }
        if (y >= mapTileHeight) {
            y -= mapTileHeight
        }
        if (y < 0) {
            y += mapTileHeight
        }

        return Vec2(x, y)
    }


    inner class PowerUpEntity(
        screenPos: Vec2,
        tilePos: Vec2,
        width: Int,
        height: Int,
    ) : EntityOld(screenPos, tilePos, width, height) {

        override val canvasRect: RectF
            get() = super.canvasRect.scale(0.5f)

        private val paint = Paint().apply {
            style = Paint.Style.FILL
            color = Color.LTGRAY
        }

        override fun updateAndRender(canvas: Canvas) {
            canvas.drawOval(canvasRect, paint)
        }
    }

    inner class WallEntity(
        screenPos: Vec2,
        tilePos: Vec2,
        width: Int,
        height: Int,
    ) : EntityOld(screenPos, tilePos, width, height) {

        private val paint = Paint().apply {
            style = Paint.Style.FILL
            color = Color.BLUE
        }

        override fun updateAndRender(canvas: Canvas) {
            canvas.drawRect(screenRect, paint)
        }
    }

    inner class FoodEntity(
        screenPos: Vec2,
        tilePos: Vec2,
        width: Int,
        height: Int,
    ) : EntityOld(screenPos, tilePos, width, height) {

        override val canvasRect: RectF
            get() = super.canvasRect.scale(0.2f)

        private val paint = Paint().apply {
            style = Paint.Style.FILL
            color = Color.LTGRAY
        }

        private val strokePaint = Paint().apply {
            style = Paint.Style.STROKE
            strokeWidth = 2.0.toPx
            color = Color.LTGRAY
        }

        override fun updateAndRender(canvas: Canvas) {
            canvas.drawOval(canvasRect, paint)
        }
    }

    private val textPaint = Paint().apply {
        style = Paint.Style.FILL
        textSize = 16f.toPx
        color = Color.WHITE
    }

    private val boldTextPaint = Paint().apply {
        style = Paint.Style.FILL
        textSize = 16f.toPx
        typeface = Typeface.DEFAULT_BOLD
        color = Color.WHITE
    }

    fun updateAndRender(canvas: Canvas) {
        entitiesMap.forEachIndexed { y, rows ->
            rows.forEachIndexed { x, entity ->
                if (entity != pacman) entity?.updateAndRender(canvas)
            }
        }
        pacman?.updateAndRender(canvas)
        canvas.drawText("Rect Pos: (${pacman?.x}, ${pacman?.y})", sceneWidth / 2f, sceneHeight / 2f, textPaint)
        canvas.drawText("Score: $score", 50f, sceneHeight - 50f, boldTextPaint)

        // clear padding
        canvas.drawRect(Rect(sceneWidth, 0, screenWidth, screenHeight), blackPaint)
        canvas.drawRect(Rect(0, sceneHeight, screenWidth, screenHeight), blackPaint)

        frameCount++
    }

    lateinit var direction: Direction
    lateinit var queuedDirection: Direction

    fun onViewEvent(viewEvent: ViewEvent) {
        when (viewEvent) {
            ViewEvent.UP_ARROW_PRESSED -> {}
            ViewEvent.UP_ARROW_RELEASED -> queuedDirection = Direction.UP
            ViewEvent.DOWN_ARROW_PRESSED -> {}
            ViewEvent.DOWN_ARROW_RELEASED -> queuedDirection = Direction.DOWN
            ViewEvent.LEFT_ARROW_PRESSED -> {}
            ViewEvent.LEFT_ARROW_RELEASED -> queuedDirection = Direction.LEFT
            ViewEvent.RIGHT_ARROW_PRESSED -> {}
            ViewEvent.RIGHT_ARROW_RELEASED -> queuedDirection = Direction.RIGHT
            ViewEvent.RESTART_CLICKED -> restartLevel()
        }.exhaustive
    }

    private fun removeEntity(entity: EntityOld) {
        entitiesMap[entity.tileY][entity.tileX] = null
    }

    private fun getScreenXFromTileX(tileX: Int): Int = tileX * entityWidth + entityWidth / 2
    private fun getScreenYFromTileY(tileY: Int): Int = tileY * entityHeight + entityHeight / 2

    private fun getTileXFromScreenX(screenX: Int): Int = screenX / entityWidth
    private fun getTileYFromScreenY(screenY: Int): Int = screenY / entityHeight

    private fun getScreenPosFromTilePos(tilePos: Vec2): Vec2 {
        val tileX = getScreenXFromTileX(tilePos.x)
        val tileY = getScreenYFromTileY(tilePos.y)

        return Vec2(tileX, tileY)
    }

    private fun getTilePosFromScreenPos(screenPos: Vec2): Vec2 {
        val tileX = getTileXFromScreenX(screenPos.x)
        val tileY = getTileYFromScreenY(screenPos.y)

        return Vec2(tileX, tileY)
    }

    private fun getTilePosFromScreenPos(x: Int, y: Int): Vec2 {
        val tileX = getTileXFromScreenX(x)
        val tileY = getTileYFromScreenY(y)

        return Vec2(tileX, tileY)
    }

    private fun getEntity(tilePos: Vec2): EntityOld? = entitiesMap[tilePos.y][tilePos.x]

    fun sceneSizeChanged(w: Int, h: Int) {
        screenWidth = w
        screenHeight = h

        currentLevel?.let { loadLevel(it) }
    }

    fun restartLevel() {
        currentLevel?.let { loadLevel(it) }
    }

    fun loadLevel(level: Level) {
        score = 0
        val rows = level.gameboard.split("\n")

        mapTileWidth = rows.first().length
        mapTileHeight = rows.size
        entityWidth = screenWidth / mapTileWidth
        entityHeight = screenHeight / mapTileHeight

        // TODO: Pad and center gameboard
        val remainderWidth = screenWidth % mapTileWidth
        val remainderHeight = screenHeight % mapTileHeight

        sceneWidth = screenWidth - remainderWidth
        sceneHeight = screenHeight - remainderHeight

        entitiesMap = Array<Array<EntityOld?>>(mapTileHeight) {
            Array(mapTileWidth) {
                null
            }
        }

        rows.forEachIndexed { y, row ->
            row.forEachIndexed { x, c ->
                val tilePos = Vec2(x, y)
                val screenPos = getScreenPosFromTilePos(tilePos)
                when (c) {
                    'P' -> {
                        val entity = PacmanEntity(screenPos, tilePos, entityWidth, entityHeight)
                        entitiesMap[y][x] = entity
                        pacman = entity
                    }
                    'o' -> {
                        val entity = PowerUpEntity(screenPos, tilePos, entityWidth, entityHeight)
                        entitiesMap[y][x] = entity
                    }
                    '.' -> {
                        val entity = FoodEntity(screenPos, tilePos, entityWidth, entityHeight)
                        entitiesMap[y][x] = entity
                    }
                    '#' -> {
                        val entity = WallEntity(screenPos, tilePos, entityWidth, entityHeight)
                        entitiesMap[y][x] = entity
                    }
                    else -> {}
                }
            }
        }
    }

    fun directionChanged(direction: Direction) {
//        this.direction = direction
    }


    enum class ViewEvent {
        UP_ARROW_PRESSED,
        UP_ARROW_RELEASED,
        DOWN_ARROW_PRESSED,
        DOWN_ARROW_RELEASED,
        LEFT_ARROW_PRESSED,
        LEFT_ARROW_RELEASED,
        RIGHT_ARROW_PRESSED,
        RIGHT_ARROW_RELEASED,
        RESTART_CLICKED,
    }

}