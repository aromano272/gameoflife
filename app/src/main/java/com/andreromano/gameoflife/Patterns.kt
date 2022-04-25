package com.andreromano.gameoflife

import kotlin.random.Random


object Patterns {

    object Still {
        val Block =
            """
        ▯▯▯▯
        ▯▮▮▯
        ▯▮▮▯
        ▯▯▯▯
    """.trimIndent()

        val BeeHive =
            """
        ▯▯▯▯▯▯
        ▯▯▮▮▯▯
        ▯▮▯▯▮▯
        ▯▯▮▮▯▯
        ▯▯▯▯▯▯
    """.trimIndent()

        val Loaf =
            """
        ▯▯▯▯▯▯
        ▯▯▮▮▯▯
        ▯▮▯▯▮▯
        ▯▯▮▯▮▯
        ▯▯▯▮▯▯
        ▯▯▯▯▯▯
    """.trimIndent()
    }

    object Oscillators {
        val Blinker =
            """
        ▯▯▯▯▯
        ▯▮▮▮▯
        ▯▯▯▯▯
    """.trimIndent()

        val Toad =
            """
        ▯▯▯▯▯▯
        ▯▯▮▮▮▯
        ▯▮▮▮▯▯
        ▯▯▯▯▯▯
    """.trimIndent()

        val PentaDecathlon =
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

    }

    object Spaceship {
        val Glider =
            """
        ▯▯▯▯▯
        ▯▯▮▯▯
        ▯▯▯▮▯
        ▯▮▮▮▯
        ▯▯▯▯▯
    """.trimIndent()

        val LWSS =
            """
        ▯▯▯▯▯▯
        ▯▮▯▯▮▯
        ▯▯▯▯▯▮
        ▯▮▯▯▯▮
        ▯▯▮▮▮▮
    """.trimIndent()
    }

    object Generators {
        val Acorn =
            """
        ▯▯▯▯▯▯▯▯▯
        ▯▯▮▯▯▯▯▯▯
        ▯▯▯▯▮▯▯▯▯
        ▯▮▮▯▯▮▮▮▯
        ▯▯▯▯▯▯▯▯▯
    """.trimIndent()
    }

    object Rand {
        fun generate(width: Int, height: Int): String = buildString {
            (0..height).forEach {
                (0..width).forEach {
                    if (Random.nextBoolean()) append("▮") else append("▯")
                }
                append("\n")
            }
        }
    }

}