package com.andreromano.gameoflife

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import kotlin.random.Random

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

    val numCols = 100
    val numRows = 100
    val cells: Array<Array<Cell>> = Array(numCols) { col -> Array(numRows) { row -> Cell(row, col, false) } }

    fun update() {
//        Any live cell with fewer than two live neighbours dies, as if by underpopulation.
//        Any live cell with two or three live neighbours lives on to the next generation.
//        Any live cell with more than three live neighbours dies, as if by overpopulation.
//        Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.


//            Any live cell with two or three live neighbours survives.
//            Any dead cell with three live neighbours becomes a live cell.
//            All other live cells die in the next generation. Similarly, all other dead cells stay dead.
        val cellsToBeKilled = mutableListOf<Cell>()
        val cellsToBeBorn = mutableListOf<Cell>()
        (0 + 1 until numCols - 1).forEach { y ->
            (0 + 1 until numRows - 1).forEach { x ->
                val cell = getCellAt(x, y)
                val amountOfLivingNeighbours = cell.amountOfLivingNeighbours()

                if (cell.isAlive) {
                    if (amountOfLivingNeighbours == 2 || amountOfLivingNeighbours == 3) {
                        // stays alive
                    } else {
                        cellsToBeKilled += cell
                    }
                } else {
                    if (amountOfLivingNeighbours == 3) {
                        cellsToBeBorn += cell
                    }
                }
            }
        }
        cellsToBeKilled.forEach { cell -> cell.isAlive = false }
        cellsToBeBorn.forEach { cell -> cell.isAlive = true }
    }

    fun render(canvas: Canvas) {
        require(numCols == numRows)
        val cellSize = canvas.width / numCols

        (0 + 1 until numCols - 1).forEach { y ->
            (0 + 1 until numRows - 1).forEach forEachX@ { x ->
                val cell = getCellAt(x, y)
                if (!cell.isAlive) return@forEachX
                val left = cell.x * cellSize
                val top = cell.y * cellSize
                val rect = Rect(
                    left,
                    top,
                    left + cellSize,
                    top + cellSize
                )
                if (cell.isAlive) canvas.drawRect(rect, fillPaint)
                canvas.drawRect(rect, strokePaint)
            }
        }
        val a = 1
    }

    fun Cell.amountOfLivingNeighbours(): Int {
        var count = 0

        if (getCellAt(x = x - 1, y = y - 1).isAlive) count++
        if (getCellAt(x = x    , y = y - 1).isAlive) count++
        if (getCellAt(x = x + 1, y = y - 1).isAlive) count++
        if (getCellAt(x = x - 1, y = y    ).isAlive) count++

        if (getCellAt(x = x + 1, y = y    ).isAlive) count++
        if (getCellAt(x = x - 1, y = y + 1).isAlive) count++
        if (getCellAt(x = x    , y = y + 1).isAlive) count++
        if (getCellAt(x = x + 1, y = y + 1).isAlive) count++

        return count
    }

    fun getCellAt(x: Int, y: Int): Cell {
        // TODO: implement wrap around
        return cells[y][x]
    }

    fun loadLevel(pattern: String) {
        val rows = pattern.split("\n")

        rows.forEachIndexed { y, row ->
            row.forEachIndexed { x, c ->
                when (c) {
                    '▮' -> {
                        cells[y + numCols / 2][x + numRows / 2].isAlive = true
                    }
                    '▯' -> {
                    }
                    else -> {}
                }
            }
        }
    }

    data class Cell(
        val x: Int,
        val y: Int,
        var isAlive: Boolean
    ) {
        fun update() {

        }

        fun render(canvas: Canvas) {

        }

        override fun equals(other: Any?): Boolean = other is Cell && other.x == x && other.y == y
    }

}