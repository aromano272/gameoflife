package com.andreromano.gameoflife

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

class Game {

    private val yellowPaint = Paint().apply {
        style = Paint.Style.FILL
        color = Color.YELLOW
    }

    val cells = mutableSetOf<Cell>()

    fun update() {
//            Any live cell with two or three live neighbours survives.
//            Any dead cell with three live neighbours becomes a live cell.
//            All other live cells die in the next generation. Similarly, all other dead cells stay dead.

        val cellsToBeKilled = mutableSetOf<Cell>()
        val candidatesForRessurection = mutableSetOf<Cell>()
        val cellsToBeRessurected = mutableSetOf<Cell>()
        cells.forEach { cell ->
            candidatesForRessurection += createEmptyNeighbours(cell)
            val amountOfLivingNeighbours = cell.amountOfLivingNeighbours()
            if (amountOfLivingNeighbours == 2 || amountOfLivingNeighbours == 3) {
                // live
            } else {
                cellsToBeKilled += cell
            }
        }

        candidatesForRessurection.forEach { candidate ->
            if (candidate.amountOfLivingNeighbours() >= 3) cellsToBeRessurected += candidate
        }

        cells -= cellsToBeKilled
        cells += cellsToBeRessurected
    }

    fun render(canvas: Canvas) {
        var lowX = 0
        var highX = 0
        var lowY = 0
        var highY = 0

        cells.forEach { cell ->
            if (cell.x < lowX) lowX = cell.x
            if (cell.x > highX) highX = cell.x
            if (cell.y < lowY) lowY = cell.y
            if (cell.y > highY) highY = cell.y
        }

        val gridSize = 10
        val marginSize = 1
        val cellSize = canvas.width / gridSize

        cells.forEach { cell ->
            canvas.drawRect(
                cell.x * cellSize + marginSize * cellSize * 1.0f,
                cell.y * cellSize + marginSize * cellSize * 1.0f,
                cell.x * cellSize + cellSize + marginSize * cellSize * 1.0f,
                cell.x * cellSize + cellSize + marginSize * cellSize * 1.0f,
                yellowPaint
            )
        }
    }

    fun Cell.amountOfLivingNeighbours(): Int = cells.count { cell ->
        when {
            cell.x == this.x - 1 && cell.y == this.y - 1 -> true
            cell.x == this.x     && cell.y == this.y - 1 -> true
            cell.x == this.x + 1 && cell.y == this.y - 1 -> true
            cell.x == this.x - 1 && cell.y == this.y     -> true

            cell.x == this.x + 1 && cell.y == this.y     -> true
            cell.x == this.x - 1 && cell.y == this.y + 1 -> true
            cell.x == this.x     && cell.y == this.y + 1 -> true
            cell.x == this.x + 1 && cell.y == this.y + 1 -> true
            else -> false
        }
    }

    fun createEmptyNeighbours(cell: Cell): Set<Cell> = setOf(
        Cell(x = cell.x - 1, y = cell.y - 1),
        Cell(x = cell.x    , y = cell.y - 1),
        Cell(x = cell.x + 1, y = cell.y - 1),
        Cell(x = cell.x - 1, y = cell.y    ),

        Cell(x = cell.x + 1, y = cell.y    ),
        Cell(x = cell.x - 1, y = cell.y + 1),
        Cell(x = cell.x    , y = cell.y + 1),
        Cell(x = cell.x + 1, y = cell.y + 1),
    )

    fun loadLevel(pattern: Pattern) {
        val rows = pattern.gameboard.split("\n")

        rows.forEachIndexed { y, row ->
            row.forEachIndexed { x, c ->
                when (c) {
                    '▮' -> {
                        cells += Cell(x, y)
                    }
                    '▯' -> {
                    }
                    else -> {}
                }
            }
        }
    }

    data class Cell(override var x: Int, override var y: Int) : Entity(x, y) {
        override fun update() {

        }

        override fun render(canvas: Canvas) {

        }
    }

}