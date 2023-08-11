package chess;

import boardgame.Board;
import boardgame.Position;
import chess.pieces.King;
import chess.pieces.Rook;

public class ChessMatch {
    private final Board board;

    public ChessMatch(){
        board = new Board(8,8);
        inicialSetup();
    }
    public ChessPiece[][] getPieces(){
        ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumn()];
        for (int i = 0;i< board.getRows(); i++){
            for (int j = 0; j< board.getColumn(); j++){
                mat[i][j] = (ChessPiece) board.piece(i,j);
            }
        }
        return mat;

    }
    private void inicialSetup(){
        board.placePiece(new Rook(board, Color.BRANCO), new Position(0,0));
        board.placePiece(new Rook(board, Color.BRANCO), new Position(0,7));
        board.placePiece(new King(board, Color.BRANCO), new Position(0,4));

        board.placePiece(new Rook(board, Color.PRETO), new Position(7,0));
        board.placePiece(new Rook(board, Color.PRETO), new Position(7,7));
        board.placePiece(new King(board, Color.PRETO), new Position(7,4));


    }
}
