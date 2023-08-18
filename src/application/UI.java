package application;

import boardgame.Piece;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.Color;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class UI {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";

        public static void clearScreen(){
        System.out.print("\033[h\033[2J");
        System.out.flush();
    }
    public static ChessPosition readChessPosition(Scanner sc){
        try{
        String s = sc.nextLine();
        char column = s.charAt(0);
        int row = Integer.parseInt(s.substring(1));
        return new ChessPosition(column,row);}
        catch (RuntimeException e){
            throw new InputMismatchException("Erro lendo posição do xadrez, valores válidos sao de a1 até h8");
        }

    }
    public static void printBoard(ChessPiece[][] pieces){
        for (int i = 0; i < pieces.length; i++){
            System.out.print(8-i + " ");
            for (int j=0; j<pieces.length; j++){
                printPiece(pieces[i][j],false);

            }
            System.out.println("");
        }
        System.out.print("  a b c d e f g h");
    }


    public static void printMatch(ChessMatch chessMatch, List<ChessPiece> captured){
            printBoard(chessMatch.getPieces());
            System.out.println();
            printCapturedPieces(captured);
            System.out.println();
            System.out.println("Turno: " + chessMatch.getTurn());
            System.out.println("Esperando o jogador: " + chessMatch.getCurrentPlayer());
    }
    public static void printBoard(ChessPiece[][] pieces, boolean[][] possibleMoves) {
        for (int i = 0; i < pieces.length; i++) {
            System.out.print((8 - i) + " ");
            for (int j = 0; j < pieces.length; j++) {
                printPiece(pieces[i][j], possibleMoves[i][j]);
            }
            System.out.println();
        }
        System.out.print("  a b c d e f g h");
    }

    private static void printPiece(ChessPiece piece, boolean background){
        if (background) {
                System.out.print(ANSI_GREEN_BACKGROUND);
		}
        if (piece == null) {
            System.out.print("-  " + ANSI_RESET);
        }else{
            System.out.print(piece + ANSI_RESET);
        }
        System.out.print(" ");

    }
    private static void printCapturedPieces(List<ChessPiece> captured) {
        List<ChessPiece> white = captured.stream().filter(x -> x.getColor() == Color.BRANCO).collect(Collectors.toList());
        List<ChessPiece> black = captured.stream().filter(x -> x.getColor() == Color.PRETO).collect(Collectors.toList());
        System.out.println("Peças Capturadas:");
        System.out.print("Branco: ");
        System.out.println(Arrays.toString(white.toArray()));
        System.out.print(ANSI_RESET);
        System.out.print("Preto: ");
        System.out.println(Arrays.toString(black.toArray()));
        System.out.print(ANSI_RESET);
    }
}
