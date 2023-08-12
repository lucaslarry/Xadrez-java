package chess;

import boardgame.Board;
import boardgame.Position;
import chess.pieces.*;

public class ChessMatch {
    private final Board board;

    public ChessMatch(){
        board = new Board(8,8);
        inicialSetup();
    }
    public ChessPiece[][] getPieces(){
        ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];
        for (int i = 0;i< board.getRows(); i++){
            for (int j = 0; j< board.getColumns(); j++){
                mat[i][j] = (ChessPiece) board.piece(i,j);
            }
        }
        return mat;

    }
    private void inicialSetup(){
        board.placePiece(new Torre(board, Color.BRANCO), new Position(0,0));
        board.placePiece(new Torre(board, Color.BRANCO), new Position(0,7));
        board.placePiece(new Rei(board, Color.BRANCO), new Position(0,4));
        board.placePiece(new Rainha(board, Color.BRANCO), new Position(0,3));
        board.placePiece(new Cavalo(board, Color.BRANCO), new Position(0,1));
        board.placePiece(new Cavalo(board, Color.BRANCO), new Position(0,6));
        board.placePiece(new Bispo(board, Color.BRANCO), new Position(0,2));
        board.placePiece(new Bispo(board, Color.BRANCO), new Position(0,5));
        board.placePiece(new Peao(board, Color.BRANCO), new Position(1,0));
        board.placePiece(new Peao(board, Color.BRANCO), new Position(1,1));
        board.placePiece(new Peao(board, Color.BRANCO), new Position(1,2));
        board.placePiece(new Peao(board, Color.BRANCO), new Position(1,3));
        board.placePiece(new Peao(board, Color.BRANCO), new Position(1,4));
        board.placePiece(new Peao(board, Color.BRANCO), new Position(1,5));
        board.placePiece(new Peao(board, Color.BRANCO), new Position(1,6));
        board.placePiece(new Peao(board, Color.BRANCO), new Position(1,7));

        board.placePiece(new Torre(board, Color.PRETO), new Position(7,0));
        board.placePiece(new Torre(board, Color.PRETO), new Position(7,7));
        board.placePiece(new Rei(board, Color.PRETO), new Position(7,4));
        board.placePiece(new Rainha(board, Color.PRETO), new Position(7,3));
        board.placePiece(new Cavalo(board, Color.PRETO), new Position(7,1));
        board.placePiece(new Cavalo(board, Color.PRETO), new Position(7,6));
        board.placePiece(new Bispo(board, Color.PRETO), new Position(7,2));
        board.placePiece(new Bispo(board, Color.PRETO), new Position(7,5));
        board.placePiece(new Peao(board, Color.PRETO), new Position(6,0));
        board.placePiece(new Peao(board, Color.PRETO), new Position(6,1));
        board.placePiece(new Peao(board, Color.PRETO), new Position(6,2));
        board.placePiece(new Peao(board, Color.PRETO), new Position(6,3));
        board.placePiece(new Peao(board, Color.PRETO), new Position(6,4));
        board.placePiece(new Peao(board, Color.PRETO), new Position(6,5));
        board.placePiece(new Peao(board, Color.PRETO), new Position(6,6));
        board.placePiece(new Peao(board, Color.PRETO), new Position(6,7));


    }
}
