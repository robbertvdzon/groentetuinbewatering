package com.vdzon.convert

import com.github.bhlangonijr.chesslib.Board
import com.github.bhlangonijr.chesslib.Square
import com.github.bhlangonijr.chesslib.move.Move
import com.github.bhlangonijr.chesslib.pgn.PgnHolder
import org.junit.Ignore
import org.junit.Test
import java.io.File
import java.util.*
import java.util.function.Consumer


class ConvertTest {

    val game = """
[Event "It (cat.17)"]
[Site "Wijk aan Zee (Netherlands)"]
[Date "1999.??.??"]
[Round "?"]
[White "Garry Kasparov"]
[Black "Veselin Topalov"]
[Result "1-0"]

1. e4 d6 2. d4 Nf6 3. Nc3 g6 4. Be3 Bg7 5. Qd2 c6 6. f3 b5 7. Nge2 Nbd7 8. Bh6
Bxh6 9. Qxh6 Bb7 10. a3 e5 11. O-O-O Qe7 12. Kb1 a6 13. Nc1 O-O-O 14. Nb3 exd4
15. Rxd4 c5 16. Rd1 Nb6 17. g3 Kb8 18. Na5 Ba8 19. Bh3 d5 20. Qf4+ Ka7 21. Rhe1
d4 22. Nd5 Nbxd5 23. exd5 Qd6 24. Rxd4 cxd4 25. Re7+ Kb6 26. Qxd4+ Kxa5 27. b4+
Ka4 28. Qc3 Qxd5 29. Ra7 Bb7 30. Rxb7 Qc4 31. Qxf6 Kxa3 32. Qxa6+ Kxb4 33. c3+
Kxc3 34. Qa1+ Kd2 35. Qb2+ Kd1 36. Bf1 Rd2 37. Rd7 Rxd7 38. Bxc4 bxc4 39. Qxh8
Rd3 40. Qa8 c3 41. Qa4+ Ke1 42. f4 f5 43. Kc1 Rd2 44. Qa7 1-0        
    """.trimIndent()


    @Test
    @Ignore
    fun test() {

        val board = Board()
        board.doMove(Move(Square.E2, Square.E4))
        System.out.println(board.toString())

        val d = File(".")
        println(d.canonicalPath)

        val pgn = PgnHolder("gamekort.pgn")
        pgn.loadPgn()


        val blackStoreSquares = listOf<StoreSquare>(
            StoreSquare("A21"),
            StoreSquare("B21"),
            StoreSquare("C21"),
            StoreSquare("D21"),
            StoreSquare("E21"),
            StoreSquare("F21"),
            StoreSquare("G21"),
            StoreSquare("H21"),
            StoreSquare("A20"),
            StoreSquare("B20"),
            StoreSquare("C20"),
            StoreSquare("D20"),
            StoreSquare("E20"),
            StoreSquare("F20"),
            StoreSquare("G20"),
            StoreSquare("H20")
        )
        val whiteStoreSquares = listOf<StoreSquare>(
            StoreSquare("A11"),
            StoreSquare("B11"),
            StoreSquare("C11"),
            StoreSquare("D11"),
            StoreSquare("E11"),
            StoreSquare("F11"),
            StoreSquare("G11"),
            StoreSquare("H11"),
            StoreSquare("A10"),
            StoreSquare("B10"),
            StoreSquare("C10"),
            StoreSquare("D10"),
            StoreSquare("E10"),
            StoreSquare("F10"),
            StoreSquare("G10"),
            StoreSquare("H10")
        )
        val myMoves = mutableListOf<ChessMoveParent>()



        for (game in pgn.game) {
            game.loadMoveText()
            val moves = game.halfMoves
            val board = Board()
            //Replay all the moves from the game and print the final position in FEN format
            var whitesMove = true
            var nr: Int = 0
            for (move in moves) {
                nr++
                if (move.san.contains("x")) {
                    // slag!
                    val armNr = if (whitesMove) 1 else 0
                    val storeSquare = if (whitesMove) {
                        whiteStoreSquares.filter { !it.occupied }.first()
                    } else {
                        blackStoreSquares.filter { !it.occupied }.first()
                    }
                    storeSquare.occupied = true;
                    myMoves.add(ChessSlag(move.from.value(), move.to.value(), storeSquare.pos, armNr, nr))
                    board.doMove(move)
                    whitesMove = !whitesMove;

                } else if (move.san.equals("O-O-O")) {
                    if (move.from.value().equals("E8")) {
                        myMoves.add(ChessMove("A8", "D8", nr))
                    }
                    if (move.from.value().equals("E1")) {
                        myMoves.add(ChessMove("A1", "D1", nr))
                    }
                    myMoves.add(ChessMove(move.from.value(), move.to.value(), nr))
                    board.doMove(move)
                    whitesMove = !whitesMove;
                } else if (move.san.equals("O-O") && move.from.equals("E1")) {
                    if (move.from.value().equals("E1")) {
                        myMoves.add(ChessMove("H1", "F1", nr))
                    }
                    if (move.from.value().equals("E8")) {
                        myMoves.add(ChessMove("H8", "F8", nr))
                    }
                    myMoves.add(ChessMove(move.from.value(), move.to.value(), nr))
                    board.doMove(move)
                    whitesMove = !whitesMove;
                } else {
                    myMoves.add(ChessMove(move.from.value(), move.to.value(), nr))
                    board.doMove(move)
                    whitesMove = !whitesMove;
                }
            }
        }

        val demo =
            myMoves.map {
                toDemo(it)
            }.joinToString("")
        File("demo-kort2.txt").writeText(demo)

    }

    private fun toDemo(it: ChessMoveParent): String {
        return when (it) {
            is ChessMove -> {
                "@${it.from} 0#\n" +
                        "pak 0#\n" +
                        "@${it.to} 0#\n" +
                        "zet 0#\n"
            }
            is ChessSlag -> {
                val pakkerNr = it.pakkerNr
                val otherPakker = if (it.pakkerNr == 0) 1 else 0
                "" +
                        "@${it.from} $pakkerNr#\n" +
                        "pak $pakkerNr#\n" +
                        "@${it.to} $otherPakker#\n" +
                        "pak $otherPakker#\n" +
                        "@${it.to} $pakkerNr#\n" +
                        "zet $pakkerNr#\n" +
                        "@${it.slagTo} $otherPakker#\n" +
                        "zet $otherPakker#\n"
            }
            else -> ""
        }
    }


    @Test
    fun test1() {
        val demo = """
            @E2#
            @A21#
            @E2#
            @A11#
        """.trimIndent()
        runOnce(demo)
    }


    private fun runOnce(text: String?) {
        val split = text!!.split("#".toRegex()).toTypedArray()
        Arrays.asList(*split).forEach(
            Consumer { row: String? ->
                if (row != null && !row.startsWith("#")) {
                    if (row.trim { it <= ' ' }.startsWith("@")) {
                        println("moveto:$row")
                        println("ss:" + row.trim().replace("@", ""))
                        val d = row.trim().replace("@", "")
                        println("Moveto: " + d)
                    }
                    if (row.trim { it <= ' ' }.startsWith("pak")) {
                        println("clamp")
                    } else if (row.trim { it <= ' ' }.startsWith("zet")) {
                        println("zet")
                    } else if (row.trim { it <= ' ' }.startsWith("sleep")) {
                        println("clamp")
                    } else if (row.trim { it <= ' ' }.startsWith("home")) {
                    } else {
                        val splitWords = row.split(",".toRegex()).toTypedArray()
                    }
                }
            }
        )
    }
}

data class StoreSquare(val pos: String, var occupied: Boolean = false)
interface ChessMoveParent
data class ChessMove(val from: String, val to: String, val nr: Int) : ChessMoveParent
data class ChessSlag(val from: String, val to: String, val slagTo: String, val pakkerNr: Int, val nr: Int) :
    ChessMoveParent
