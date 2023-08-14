package chess.pieces;

import boardgame.Board;
import chess.ChessPiece;
import chess.Color;

public class Torre extends ChessPiece {
    public Torre(Board board, Color color) {
        super(board, color);
    }

    @Override
    public String toString() {
        if (this.getColor() == Color.BRANCO){
            return "♜";
        }
        return "♖";
    }

    @Override
    public boolean[][] possibleMoves() {
        return new boolean[0][];
    }
}
