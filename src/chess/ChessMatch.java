package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.*;


public class ChessMatch {

    private int turn;
    private Color currentPlayer;
    private Board board;
    private boolean check;
    private boolean checkMate;
    private ChessPiece enPassantVulnerable;
    private ChessPiece promoted;

    private List<Piece> piecesOnTheBoard = new ArrayList<>();
    private List<Piece> capturedPieces = new ArrayList<>();

    public ChessMatch() {
        board = new Board(8, 8);
        turn = 1;
        currentPlayer = Color.BRANCO;
        inicialSetup();
    }

    public int getTurn() {
        return turn;
    }

    public Color getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean getCheck() {
        return check;
    }

    public boolean getCheckMate() {
        return checkMate;
    }

    public ChessPiece getEnPassantVulnerable() {
        return enPassantVulnerable;
    }

    public ChessPiece getPromoted() {
        return promoted;
    }

    public ChessPiece[][] getPieces() {
        ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];
        for (int i=0; i<board.getRows(); i++) {
            for (int j=0; j<board.getColumns(); j++) {
                mat[i][j] = (ChessPiece) board.piece(i, j);
            }
        }
        return mat;
    }

    public boolean[][] possibleMoves(ChessPosition sourcePosition) {
        Position position = sourcePosition.toPosition();
        validateSourcePosition(position);
        return board.piece(position).possibleMoves();
    }

    public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
        Position source = sourcePosition.toPosition();
        Position target = targetPosition.toPosition();
        validateSourcePosition(source);
        validateTargetPosition(source, target);
        Piece capturedPiece = makeMove(source, target);

        if (testCheck(currentPlayer)) {
            undoMove(source, target, capturedPiece);
            throw new ChessException("Você não pode colocar a si mesmo em check");
        }

        ChessPiece movedPiece = (ChessPiece)board.piece(target);

        // #specialmove promotion
        promoted = null;
        if (movedPiece instanceof Peao) {
            if ((movedPiece.getColor() == Color.BRANCO && target.getRow() == 0) || (movedPiece.getColor() == Color.PRETO && target.getRow() == 7)) {
                promoted = (ChessPiece)board.piece(target);
                promoted = replacePromotedPiece("Q");
            }
        }

        check = (testCheck(opponent(currentPlayer))) ? true : false;

        if (testCheckMate(opponent(currentPlayer))) {
            checkMate = true;
        }
        else {
            nextTurn();
        }

        // #specialmove en passant
        if (movedPiece instanceof Peao && (target.getRow() == source.getRow() - 2 || target.getRow() == source.getRow() + 2)) {
            enPassantVulnerable = movedPiece;
        }
        else {
            enPassantVulnerable = null;
        }

        return (ChessPiece)capturedPiece;
    }

    public ChessPiece replacePromotedPiece(String type) {
        if (promoted == null) {
            throw new IllegalStateException("Não existe uma peça que possa ser promovida");
        }
        if (!type.equals("B") && !type.equals("N") && !type.equals("R") & !type.equals("Q")) {
            return promoted;
        }

        Position pos = promoted.getChessPosition().toPosition();
        Piece p = board.removePiece(pos);
        piecesOnTheBoard.remove(p);

        ChessPiece newPiece = newPiece(type, promoted.getColor());
        board.placePiece(newPiece, pos);
        piecesOnTheBoard.add(newPiece);

        return newPiece;
    }

    private ChessPiece newPiece(String type, Color color) {
        if (type.equals("B")) return new Torre(board, color);
        if (type.equals("N")) return new Cavalo(board, color);
        if (type.equals("Q")) return new Rainha(board, color);
        return new Torre(board, color);
    }

    private Piece makeMove(Position source, Position target) {
        ChessPiece p = (ChessPiece)board.removePiece(source);
        p.increaseMoveCount();
        Piece capturedPiece = board.removePiece(target);
        board.placePiece(p, target);

        if (capturedPiece != null) {
            piecesOnTheBoard.remove(capturedPiece);
            capturedPieces.add(capturedPiece);
        }

        if (p instanceof Rei && target.getColumn() == source.getColumn() + 2) {
            Position sourceT = new Position(source.getRow(), source.getColumn() + 3);
            Position targetT = new Position(source.getRow(), source.getColumn() + 1);
            ChessPiece rook = (ChessPiece)board.removePiece(sourceT);
            board.placePiece(rook, targetT);
            rook.increaseMoveCount();
        }

        if (p instanceof Rei && target.getColumn() == source.getColumn() - 2) {
            Position sourceT = new Position(source.getRow(), source.getColumn() - 4);
            Position targetT = new Position(source.getRow(), source.getColumn() - 1);
            ChessPiece rook = (ChessPiece)board.removePiece(sourceT);
            board.placePiece(rook, targetT);
            rook.increaseMoveCount();
        }

        if (p instanceof Peao) {
            if (source.getColumn() != target.getColumn() && capturedPiece == null) {
                Position pawnPosition;
                if (p.getColor() == Color.BRANCO) {
                    pawnPosition = new Position(target.getRow() + 1, target.getColumn());
                }
                else {
                    pawnPosition = new Position(target.getRow() - 1, target.getColumn());
                }
                capturedPiece = board.removePiece(pawnPosition);
                capturedPieces.add(capturedPiece);
                piecesOnTheBoard.remove(capturedPiece);
            }
        }

        return capturedPiece;
    }

    private void undoMove(Position source, Position target, Piece capturedPiece) {
        ChessPiece p = (ChessPiece)board.removePiece(target);
        p.decreaseMoveCount();
        board.placePiece(p, source);

        if (capturedPiece != null) {
            board.placePiece(capturedPiece, target);
            capturedPieces.remove(capturedPiece);
            piecesOnTheBoard.add(capturedPiece);
        }

        if (p instanceof Rei && target.getColumn() == source.getColumn() + 2) {
            Position sourceT = new Position(source.getRow(), source.getColumn() + 3);
            Position targetT = new Position(source.getRow(), source.getColumn() + 1);
            ChessPiece rook = (ChessPiece)board.removePiece(targetT);
            board.placePiece(rook, sourceT);
            rook.decreaseMoveCount();
        }

        if (p instanceof Rei && target.getColumn() == source.getColumn() - 2) {
            Position sourceT = new Position(source.getRow(), source.getColumn() - 4);
            Position targetT = new Position(source.getRow(), source.getColumn() - 1);
            ChessPiece rook = (ChessPiece)board.removePiece(targetT);
            board.placePiece(rook, sourceT);
            rook.decreaseMoveCount();
        }

        if (p instanceof Peao) {
            if (source.getColumn() != target.getColumn() && capturedPiece == enPassantVulnerable) {
                ChessPiece pawn = (ChessPiece)board.removePiece(target);
                Position pawnPosition;
                if (p.getColor() == Color.BRANCO) {
                    pawnPosition = new Position(3, target.getColumn());
                }
                else {
                    pawnPosition = new Position(4, target.getColumn());
                }
                board.placePiece(pawn, pawnPosition);
            }
        }
    }

    private void validateSourcePosition(Position position){
        if (!board.thereIsAPiece(position)){
            throw new ChessException("Não existe uma peça neste local");}
        if (!board.piece(position).isThereAnyPossibleMove()){
            throw new ChessException("Essa jogada não é possível");
        }
        if(currentPlayer!= ((ChessPiece)board.piece(position)).getColor()){
            throw new ChessException("Essa peça não é sua");
        }
    }

    private void validateTargetPosition(Position source, Position target){
        if(!board.piece(source).possibleMove(target)){
            throw new ChessException("Essa jogada não é possível. A peça escolhida não pode ser movida para este local.");
        }
    }


    private Color opponent(Color color) {
        return (color == Color.BRANCO) ? Color.PRETO : Color.BRANCO;
    }

    private ChessPiece king(Color color) {
        List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());
        for (Piece p : list) {
            if (p instanceof Rei) {
                return (ChessPiece)p;
            }
        }
        throw new IllegalStateException("Não existe um Rei " + color + " no tabuleiro");
    }

    private boolean testCheck(Color color) {
        Position kingPosition = king(color).getChessPosition().toPosition();
        List<Piece> opponentPieces = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == opponent(color)).collect(Collectors.toList());
        for (Piece p : opponentPieces) {
            boolean[][] mat = p.possibleMoves();
            if (mat[kingPosition.getRow()][kingPosition.getColumn()]) {
                return true;
            }
        }
        return false;
    }

    private boolean testCheckMate(Color color) {
        if (!testCheck(color)) {
            return false;
        }
        List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());
        for (Piece p : list) {
            boolean[][] mat = p.possibleMoves();
            for (int i=0; i<board.getRows(); i++) {
                for (int j=0; j<board.getColumns(); j++) {
                    if (mat[i][j]) {
                        Position source = ((ChessPiece)p).getChessPosition().toPosition();
                        Position target = new Position(i, j);
                        Piece capturedPiece = makeMove(source, target);
                        boolean testCheck = testCheck(color);
                        undoMove(source, target, capturedPiece);
                        if (!testCheck) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private void placeNewPiece(char column, int row, ChessPiece piece) {
        board.placePiece(piece, new ChessPosition(column, row).toPosition());
        piecesOnTheBoard.add(piece);
    }

    private void inicialSetup(){
        placeNewPiece('a', 1,new Torre(board, Color.BRANCO));
        placeNewPiece('h',1,new Torre(board, Color.BRANCO));
        placeNewPiece('e',1,new Rei(board, Color.BRANCO, this));
        placeNewPiece('d',1,new Rainha(board, Color.BRANCO));
        placeNewPiece('b',1,new Cavalo(board, Color.BRANCO));
        placeNewPiece('g',1,new Cavalo(board, Color.BRANCO));
        placeNewPiece('c',1,new Bispo(board, Color.BRANCO));
        placeNewPiece('f',1,new Bispo(board, Color.BRANCO));
        placeNewPiece('a',2,new Peao(board, Color.BRANCO, this));
        placeNewPiece('b',2,new Peao(board, Color.BRANCO, this));
        placeNewPiece('c',2,new Peao(board, Color.BRANCO, this));
        placeNewPiece('d',2,new Peao(board, Color.BRANCO, this));
        placeNewPiece('e',2,new Peao(board, Color.BRANCO, this));
        placeNewPiece('f',2,new Peao(board, Color.BRANCO, this));
        placeNewPiece('g',2,new Peao(board, Color.BRANCO, this));
        placeNewPiece('h',2,new Peao(board, Color.BRANCO, this));

        placeNewPiece('a',8,new Torre(board, Color.PRETO));
        placeNewPiece('h',8,new Torre(board, Color.PRETO));
        placeNewPiece('e',8,new Rei(board, Color.PRETO, this));
        placeNewPiece('d',8,new Rainha(board, Color.PRETO));
        placeNewPiece('b',8,new Cavalo(board, Color.PRETO));
        placeNewPiece('g',8,new Cavalo(board, Color.PRETO));
        placeNewPiece('c',8,new Bispo(board, Color.PRETO));
        placeNewPiece('f',8,new Bispo(board, Color.PRETO));
        placeNewPiece('a',7,new Peao(board, Color.PRETO, this));
        placeNewPiece('b',7,new Peao(board, Color.PRETO, this));
        placeNewPiece('c',7,new Peao(board, Color.PRETO, this));
        placeNewPiece('d',7,new Peao(board, Color.PRETO, this));
        placeNewPiece('e',7,new Peao(board, Color.PRETO, this));
        placeNewPiece('f',7,new Peao(board, Color.PRETO, this));
        placeNewPiece('g',7,new Peao(board, Color.PRETO, this));
        placeNewPiece('h',7,new Peao(board, Color.PRETO, this));

    }
    private void nextTurn(){
        turn++;
        currentPlayer = (currentPlayer == Color.BRANCO) ? Color.PRETO :  Color.BRANCO;
    }
}


