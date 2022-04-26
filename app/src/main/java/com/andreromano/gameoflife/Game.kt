package com.andreromano.gameoflife

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect

@Suppress("RedundantIf")
class Game {

    private val fillPaint = Paint().apply {
        style = Paint.Style.FILL
        color = Color.YELLOW
    }

    private val strokePaint = Paint().apply {
        style = Paint.Style.STROKE
        color = Color.BLACK
        strokeWidth = 2f
    }

    val cells = mutableSetOf<Cell>()

    fun update() {
//        Any live cell with fewer than two live neighbours dies, as if by underpopulation.
//        Any live cell with two or three live neighbours lives on to the next generation.
//        Any live cell with more than three live neighbours dies, as if by overpopulation.
//        Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.


//            Any live cell with two or three live neighbours survives.
//            Any dead cell with three live neighbours becomes a live cell.
//            All other live cells die in the next generation. Similarly, all other dead cells stay dead.

        val cellsToBeChecked = cells.toMutableSet()
        cells.forEach { cell ->
            createEmptyNeighbours(cell).forEach { cell ->
                if (!cellsToBeChecked.contains(cell)) cellsToBeChecked += cell
            }
        }

        val cellsToBeRessurected = mutableSetOf<Cell>()
        val cellsToBeKilled = mutableSetOf<Cell>()
        cellsToBeChecked.forEach { cell ->
            val amountOfLivingNeighbours = cell.amountOfLivingNeighbours()
            if (cell.isAlive) {
                if (amountOfLivingNeighbours in 2..3) {
                    // stays alive
                } else {
                    cellsToBeKilled += cell
                }
            } else {
                if (amountOfLivingNeighbours == 3) {
                    cellsToBeRessurected += cell.copy(isAlive = true)
                }
            }
        }

        cells.clear()
        cells += cellsToBeChecked.filter { it.isAlive }.toSet()
        cells += cellsToBeRessurected
        cells -= cellsToBeKilled

        return
//        val cellsToBeKilled = mutableSetOf<Cell>()
//        val candidatesForRessurection = mutableSetOf<Cell>()
//        val cellsToBeRessurected = mutableSetOf<Cell>()
//        cells.forEach { cell ->
//            candidatesForRessurection += createEmptyNeighbours(cell).minus(cells)
//            val amountOfLivingNeighbours = cell.amountOfLivingNeighbours()
//            if (amountOfLivingNeighbours == 2 || amountOfLivingNeighbours == 3) {
//                // live
//            } else {
//                cellsToBeKilled += cell
//            }
//        }
//
//        candidatesForRessurection.forEach { candidate ->
//            if (candidate.amountOfLivingNeighbours() >= 3) cellsToBeRessurected += candidate
//        }
//
//        cells -= cellsToBeKilled
//        cells += cellsToBeRessurected
    }

    var viewportStartX = 0
    var viewportStartY = 0
    var viewportEndX = 0
    var viewportEndY = 0
    fun render(canvas: Canvas) {
        var lowX = Int.MAX_VALUE
        var highX = 0
        var lowY = Int.MAX_VALUE
        var highY = 0

        cells.forEach { cell ->
            if (cell.x < lowX) lowX = cell.x
            if (cell.x > highX) highX = cell.x
            if (cell.y < lowY) lowY = cell.y
            if (cell.y > highY) highY = cell.y
        }

        val gridSize = 40
        val minMargin = 0
        val maxMargin = 6
        val visibleCellsX = highX - lowX + maxMargin * 2
        val visibleCellsY = highY - lowY + maxMargin * 2
        val cellSize: Int

        val stretchViewport = true
        if (stretchViewport) {
            cellSize = canvas.width / maxOf(visibleCellsX, visibleCellsY)
            viewportStartX = lowX
            viewportEndX = highX
            viewportStartY = lowY
            viewportEndY = highY
        } else {
            cellSize = canvas.width / gridSize
            if (viewportStartX > lowX || viewportEndX < highX) {
                viewportStartX = lowX
                viewportEndX = viewportStartX + gridSize
            }
            if (viewportStartY > lowY || viewportEndY < highY) {
                viewportStartY = lowY
                viewportEndY = viewportStartY + gridSize
            }
        }


        cells.forEach { cell ->
            val left = (cell.x - viewportStartX + 0) * cellSize
            val top = (cell.y - viewportStartY + 0) * cellSize
            val rect = Rect(
                left,
                top,
                left + cellSize,
                top + cellSize
            )
            canvas.drawRect(rect, fillPaint)
            canvas.drawRect(rect, strokePaint)
        }
    }

    fun Cell.amountOfLivingNeighbours2(): Int {
        var amount = 0

        val livingCells = cells.filter { it.isAlive }
        livingCells.forEach { cell ->
            if (cell.x == this.x - 1 && cell.y == this.y - 1) amount++
            if (cell.x == this.x     && cell.y == this.y - 1) amount++
            if (cell.x == this.x + 1 && cell.y == this.y - 1) amount++
            if (cell.x == this.x - 1 && cell.y == this.y    ) amount++

            if (cell.x == this.x + 1 && cell.y == this.y    ) amount++
            if (cell.x == this.x - 1 && cell.y == this.y + 1) amount++
            if (cell.x == this.x     && cell.y == this.y + 1) amount++
            if (cell.x == this.x + 1 && cell.y == this.y + 1) amount++
        }

        return amount
    }

    fun Cell.amountOfLivingNeighbours(): Int = cells.count { cell ->
        when {
            cell.x == this.x - 1 && cell.y == this.y - 1 -> cell.isAlive
            cell.x == this.x     && cell.y == this.y - 1 -> cell.isAlive
            cell.x == this.x + 1 && cell.y == this.y - 1 -> cell.isAlive
            cell.x == this.x - 1 && cell.y == this.y     -> cell.isAlive

            cell.x == this.x + 1 && cell.y == this.y     -> cell.isAlive
            cell.x == this.x - 1 && cell.y == this.y + 1 -> cell.isAlive
            cell.x == this.x     && cell.y == this.y + 1 -> cell.isAlive
            cell.x == this.x + 1 && cell.y == this.y + 1 -> cell.isAlive
            else -> false
        }
    }

    fun createEmptyNeighbours(cell: Cell): Set<Cell> = setOf(
        Cell(x = cell.x - 1, y = cell.y - 1, false),
        Cell(x = cell.x    , y = cell.y - 1, false),
        Cell(x = cell.x + 1, y = cell.y - 1, false),
        Cell(x = cell.x - 1, y = cell.y    , false),

        Cell(x = cell.x + 1, y = cell.y    , false),
        Cell(x = cell.x - 1, y = cell.y + 1, false),
        Cell(x = cell.x    , y = cell.y + 1, false),
        Cell(x = cell.x + 1, y = cell.y + 1, false),
    )

    fun loadLevel(pattern: String) {
        val rows = pattern.split("\n")

        rows.forEachIndexed { y, row ->
            row.forEachIndexed { x, c ->
                when (c) {
                    '▮' -> {
                        cells += Cell(x, y, true)
                    }
                    '▯' -> {
                    }
                    else -> {}
                }
            }
        }
    }

    data class Cell(
        override var x: Int,
        override var y: Int,
        val isAlive: Boolean
    ) : Entity(x, y) {
        override fun update() {

        }

        override fun render(canvas: Canvas) {

        }

        override fun equals(other: Any?): Boolean = other is Cell && other.x == x && other.y == y
    }

}