package com.vdzon.java.schaakspel

import com.github.bhlangonijr.chesslib.move.Move
import kotlinx.serialization.Serializable

@Serializable
data class ChessBoard(
    val squares: List<SquareField>,
    val player: String,
    val availableMoves: List<ChessMove>,
    val draw: Boolean,
    val mate: Boolean,
    val kingAttached: Boolean
)

@Serializable
data class ChessMove(
    val from: String,
    val to: String,
    ){
}

fun Move.toChessMove() = ChessMove(this.from.name, this.to.name)

fun List<Move>.toChessMoves() = this.map { it.toChessMove() }
