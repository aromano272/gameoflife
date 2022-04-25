package com.andreromano.gameoflife

interface Pattern {
    val gameboard: String
}
object Patterns {

    enum class Still(override val gameboard: String) : Pattern {
        BLOCK(
            """
        ▯▯▯▯
        ▯▮▮▯
        ▯▮▮▯
        ▯▯▯▯
    """.trimIndent()
        ),

        BEEHIVE(
            """
        ▯▯▯▯▯▯
        ▯▯▮▮▯▯
        ▯▮▯▯▮▯
        ▯▯▮▮▯▯
        ▯▯▯▯▯▯
    """.trimIndent()
        ),

        LOAF(
            """
        ▯▯▯▯▯▯
        ▯▯▮▮▯▯
        ▯▮▯▯▮▯
        ▯▯▮▯▮▯
        ▯▯▯▮▯▯
        ▯▯▯▯▯▯
    """.trimIndent()
        )
    }

    enum class Oscillators(override val gameboard: String) : Pattern {
        Blinker(
            """
        ▯▯▯▯▯
        ▯▮▮▮▯
        ▯▯▯▯▯
    """.trimIndent()
        ),

        Toad(
            """
        ▯▯▯▯▯▯
        ▯▯▮▮▮▯
        ▯▮▮▮▯▯
        ▯▯▯▯▯▯
    """.trimIndent()
        ),
        PentaDecathlon(
            """
        ▯▯▯▯▯
        ▯▯▮▯▯
        ▯▯▮▯▯
        ▯▮▯▮▯
        ▯▯▮▯▯
        ▯▯▮▯▯
        ▯▯▮▯▯
        ▯▯▮▯▯
        ▯▮▯▮▯
        ▯▯▮▯▯
        ▯▯▮▯▯
        ▯▯▯▯▯
    """.trimIndent()
        ),
    }

    enum class Spaceship(override val gameboard: String) : Pattern {
        Glider(
            """
        ▯▯▯▯▯
        ▯▯▮▯▯
        ▯▯▯▮▯
        ▯▮▮▮▯
        ▯▯▯▯▯
    """.trimIndent()
        ),

        LWSS(
            """
        ▯▯▯▯▯
        ▯▮▯▯▮▯
        ▯▯▯▯▯▮
        ▯▮▯▯▯▮
        ▯▯▮▮▮▮
    """.trimIndent()
        ),
    }

}