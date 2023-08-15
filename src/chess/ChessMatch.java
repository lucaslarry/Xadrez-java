package chess;

import boardgame.Board;
import boardgame.Piece;
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
    public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition){
        Position source = sourcePosition.toPosition();
        Position target = targetPosition.toPosition();
        validateSourcePosition(source);
        validateTargetPosition(source,target);
        Piece capturedPiece = makeMove(source,target);
        return (ChessPiece)capturedPiece;

    }
    private Piece makeMove(Position source, Position target){
        Piece p = board.removePiece(source);
        Piece capturedPiece = board.removePiece(target);
        board.placePiece(p,target);
        return capturedPiece;

    }
    private void validateSourcePosition(Position position){
        if (!board.thereIsAPiece(position)){
            throw new ChessException("Não existe uma peça neste local");}
        if (!board.piece(position).isThereAnyPossibleMove()){
            throw new ChessException("Essa jogada não é possível");
        }
    }

    private void validateTargetPosition(Position source, Position target){
        if(!board.piece(source).possibleMove(target)){
            throw new ChessException("Essa jogada não é possível. A peça escolhida não pode ser movida para este local.");
        }

    }
    private void placeNewPiece(char column,int row, ChessPiece piece){
        board.placePiece(piece, new ChessPosition(column,row).toPosition());
    }

    public boolean[][] possibleMoves(ChessPosition sourcePosition) {
        Position position = sourcePosition.toPosition();
        validateSourcePosition(position);
        return board.piece(position).possibleMoves();
    }
    private void inicialSetup(){
        placeNewPiece('a', 1,new Torre(board, Color.BRANCO));
        placeNewPiece('h',1,new Torre(board, Color.BRANCO));
        placeNewPiece('e',1,new Rei(board, Color.BRANCO));
        placeNewPiece('d',1,new Rainha(board, Color.BRANCO));
        placeNewPiece('b',1,new Cavalo(board, Color.BRANCO));
        placeNewPiece('g',1,new Cavalo(board, Color.BRANCO));
        placeNewPiece('c',1,new Bispo(board, Color.BRANCO));
        placeNewPiece('f',1,new Bispo(board, Color.BRANCO));
        placeNewPiece('a',2,new Peao(board, Color.BRANCO));
        placeNewPiece('b',2,new Peao(board, Color.BRANCO));
        placeNewPiece('c',2,new Peao(board, Color.BRANCO));
        placeNewPiece('d',2,new Peao(board, Color.BRANCO));
        placeNewPiece('e',2,new Peao(board, Color.BRANCO));
        placeNewPiece('f',2,new Peao(board, Color.BRANCO));
        placeNewPiece('g',2,new Peao(board, Color.BRANCO));
        placeNewPiece('h',2,new Peao(board, Color.BRANCO));

        placeNewPiece('a',8,new Torre(board, Color.PRETO));
        placeNewPiece('h',8,new Torre(board, Color.PRETO));
        placeNewPiece('e',8,new Rei(board, Color.PRETO));
        placeNewPiece('d',8,new Rainha(board, Color.PRETO));
        placeNewPiece('b',8,new Cavalo(board, Color.PRETO));
        placeNewPiece('g',8,new Cavalo(board, Color.PRETO));
        placeNewPiece('c',8,new Bispo(board, Color.PRETO));
        placeNewPiece('f',8,new Bispo(board, Color.PRETO));
        placeNewPiece('a',7,new Peao(board, Color.PRETO));
        placeNewPiece('b',7,new Peao(board, Color.PRETO));
        placeNewPiece('c',7,new Peao(board, Color.PRETO));
        placeNewPiece('d',7,new Peao(board, Color.PRETO));
        placeNewPiece('e',7,new Peao(board, Color.PRETO));
        placeNewPiece('f',7,new Peao(board, Color.PRETO));
        placeNewPiece('g',7,new Peao(board, Color.PRETO));
        placeNewPiece('h',7,new Peao(board, Color.PRETO));


    }
}
