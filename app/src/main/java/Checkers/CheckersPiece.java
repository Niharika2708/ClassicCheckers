package Checkers;

import java.util.HashSet;
import java.util.Set;

public class CheckersPiece {

    private char colour;
    private Cell position;
    private boolean isKing = false;

    public CheckersPiece(char c) {
        this.colour = c;
    }

    public char getColour() {
        return this.colour;
    }

    public void setPosition(Cell p) {
        this.position = p;
    }

    public Cell getPosition() {
        return this.position;
    }

    public boolean isKing() {
        return isKing;
    }

    public void makeKing() {
        isKing = true;
    }

    public Set<Cell> getAvailableMoves(Cell[][] board) {

        Set<Cell> moves = new HashSet<>();
        int x = position.getX();
        int y = position.getY();
        int[] directions = isKing ? new int[]{-1, 1} : new int[]{(colour == 'w') ? 1 : -1};

        for (int direction : directions) {
            for (int dx : new int[]{-1, 1}) {
                int nextY = y + direction;
                int nextX = x + dx;

                // Check for diagonal moves
                if (isWithinBoard(nextX, nextY, board) && board[nextY][nextX].getPiece() == null) {
                    moves.add(board[nextY][nextX]);
                }

                // Check for diagonal jumps
                int jumpY = y + 2 * direction;
                int jumpX = x + 2 * dx;
                if (isWithinBoard(jumpX, jumpY, board) && board[jumpY][jumpX].getPiece() == null && board[nextY][nextX].getPiece() != null) {
                    moves.add(board[jumpY][jumpX]);
                }
            }
        }
        return moves;
    }

    private boolean isWithinBoard(int x, int y, Cell[][] board) {
        return x >= 0 && x < board.length && y >= 0 && y < board[0].length;
    }

    public void draw(App app) {

        app.strokeWeight(5.0f);
        int baseColor = (colour == 'w') ? 255 : 0;
        app.fill(baseColor);
        app.stroke(255 - baseColor);

        int cellCenterX = position.getX() * App.CELLSIZE + App.CELLSIZE / 2;
        int cellCenterY = position.getY() * App.CELLSIZE + App.CELLSIZE / 2;

        app.ellipse(cellCenterX, cellCenterY, App.CELLSIZE * 0.8f, App.CELLSIZE * 0.8f);

        if (isKing) {

			app.strokeWeight(4.0f);
        	app.fill(baseColor);
        	app.stroke(255 - baseColor);

			app.ellipse(cellCenterX, cellCenterY, App.CELLSIZE * 0.5f, App.CELLSIZE * 0.5f);

            app.ellipse(cellCenterX, cellCenterY, App.CELLSIZE * 0.2f, App.CELLSIZE * 0.2f);

        }
        app.noStroke();
    }
}