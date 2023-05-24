package com.vdzon.java.schaakspel

import com.github.bhlangonijr.chesslib.Square
import com.github.bhlangonijr.chesslib.move.Move
import java.io.File
import java.util.concurrent.TimeUnit

object ComputerPlayer {
    fun getMove(fen: String): Move{
        createFile(fen)

        val cmd: List<String> = listOf("/usr/bin/expect","/tmp/chess.dat")
        val res = runCommand(cmd=cmd)
        val bestMove = res?.substringAfterLast("bestmove")?.trim()?.toUpperCase()?:""
        val from = bestMove.substring(0,2)
        val to = bestMove.substring(2,4)
        return Move(Square.fromValue(from), Square.fromValue(to))
    }

    fun createFile(fen: String){

        val exe1 = File("/home/robbert/Downloads/stockfish_14.1_linux_x64/stockfish_14.1_linux_x64")
        val exe2 = File("/usr/games/stockfish")
        val exe3 = File("/opt/homebrew/bin/stockfish")
        var exeName = if (exe2.isFile) "/usr/games/stockfish"
        else if (exe1.isFile) "/home/robbert/Downloads/stockfish_14.1_linux_x64/stockfish_14.1_linux_x64"
        else if (exe3.isFile) "/opt/homebrew/bin/stockfish"
        else throw RuntimeException("Stockfish not found")
        println("use stockfish:"+exeName)


        val file ="""
            #!/usr/bin/expect
            spawn $exeName
            expect -timeout 1000  Stockfish
            send "uci \r"
            expect -timeout 1000  uciok
            send "isready\r"
            expect -timeout 1000  readyok
            send "ucinewgame\r"
            send "position fen $fen\r"
            send "go movetime 1000\r"
            expect bestmove            
        """.trimIndent()
        File("/tmp/chess.dat").writeText(file)

    }

    fun runCommand(
        workingDir: File = File("."),
        timeoutAmount: Long = 60,
        timeoutUnit: TimeUnit = TimeUnit.SECONDS,
        cmd: List<String>

    ): String? = runCatching {
        ProcessBuilder(cmd)
            .directory(workingDir)
            .redirectOutput(ProcessBuilder.Redirect.PIPE)
            .redirectError(ProcessBuilder.Redirect.PIPE)
            .start().also { it.waitFor(timeoutAmount, timeoutUnit) }
            .inputStream.bufferedReader().readText()
    }.onFailure { it.printStackTrace() }.getOrNull()
}