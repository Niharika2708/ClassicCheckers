package Checkers;

import processing.core.PApplet;
import processing.event.MouseEvent;

import java.util.*;

public class App extends PApplet {

    public static final int CELLSIZE = 48;
    public static final int SIDEBAR = 0;
    public static final int BOARD_WIDTH = 8;
    public static final int[] BLACK_RGB = {181, 136, 99};  
    public static final int[] WHITE_RGB = {240, 217, 181};
    public static final Colour HIGHLIGHT_BLUE = new Colour(0, 0, 255);

    public static final float[][][] coloursRGB = new float[][][] {
        //default - white & black
        {
                {WHITE_RGB[0], WHITE_RGB[1], WHITE_RGB[2]},
                {BLACK_RGB[0], BLACK_RGB[1], BLACK_RGB[2]}
        },
        //green
        {
                {105, 138, 76}, //when on white cell
                {105, 138, 76} //when on black cell
        },
        //blue
        {
                {196,224,232},
                {170,210,221}
        }
	};

    public static int WIDTH = CELLSIZE*BOARD_WIDTH+SIDEBAR;
    public static int HEIGHT = BOARD_WIDTH*CELLSIZE;

    public static final int FPS = 60;

	private Cell[][] board;
	private CheckersPiece currentSelected;
	private HashSet<Cell> selectedCells;
	private HashMap<Character, HashSet<CheckersPiece>> piecesInPlay = new HashMap<>();
	private char currentPlayer = 'w';


    public App() {
        
    }

	@Override
    public void settings() {
        size(WIDTH, HEIGHT);
    }

	@Override
    public void setup() {

        frameRate(FPS);
		this.board = new Cell[BOARD_WIDTH][BOARD_WIDTH];
		HashSet<CheckersPiece> w = new HashSet<>();
        HashSet<CheckersPiece> b = new HashSet<>();
        piecesInPlay.put('w', w);
        piecesInPlay.put('b', b);

        for (int i = 0; i < board.length; i++) {
            for (int i2 = 0; i2 < board[i].length; i2++) {
                board[i][i2] = new Cell(i2,i);

                if ((i2+i) % 2 == 1) {
                    if (i < 3) {
                        //white piece
                        board[i][i2].setPiece(new CheckersPiece('w'));
                        w.add(board[i][i2].getPiece());
                    } else if (i >= 5) {
                        //black piece
                        board[i][i2].setPiece(new CheckersPiece('b'));
                        b.add(board[i][i2].getPiece());
                    }
                }
            }
        }
    }


    @Override
    public void mousePressed(MouseEvent e) {

        int x = e.getX();
        int y = e.getY();
        if (x < 0 || x >= App.WIDTH || y < 0 || y >= App.HEIGHT) return;
    
        Cell clicked = board[y / App.CELLSIZE][x / App.CELLSIZE];

        // Move the selected piece to the clicked cell if it's a potential move
        if (currentSelected != null && clicked.isPotentialMove()) {

            if (isJumpMove(currentSelected.getPosition(), clicked)) {
                handleCapture(currentSelected.getPosition(), clicked);
            }
    
            // Move the piece
            movePiece(currentSelected, clicked);

            // Switch turns
            currentPlayer = (currentPlayer == 'w') ? 'b' : 'w';

            // Reset current selection
            currentSelected = null;
            clearPotentialMoves();

            return;
        }

        if (clicked.getPiece() != null && clicked.getPiece().getColour() == currentPlayer) {
            if (clicked.getPiece() == currentSelected) {
                currentSelected = null;
                clearPotentialMoves();
            } else {
                currentSelected = clicked.getPiece();
                clearPotentialMoves();
                Set<Cell> availableMoves = currentSelected.getAvailableMoves(board);
                for (Cell move : availableMoves) {
                    move.setPotentialMove(true);
                }
            }
        }

    }

    private boolean isJumpMove(Cell start, Cell end) {
        return Math.abs(start.getX() - end.getX()) == 2 && Math.abs(start.getY() - end.getY()) == 2;
    }
    
    private void handleCapture(Cell start, Cell end) {

        int midX = (start.getX() + end.getX()) / 2;
        int midY = (start.getY() + end.getY()) / 2;
        Cell midCell = board[midY][midX];
        
        if (midCell.getPiece() != null && midCell.getPiece().getColour() != currentSelected.getColour()) {
            // Capture the opponent's piece
            piecesInPlay.get(midCell.getPiece().getColour()).remove(midCell.getPiece());
            midCell.setPiece(null);
        }
    }

    private void movePiece(CheckersPiece piece, Cell destination) {

        // Remove piece from current cell
        Cell currentCell = piece.getPosition();
        currentCell.setPiece(null);
    
        // Set piece to the new cell
        destination.setPiece(piece);
        piece.setPosition(destination);

        Cell pieceCurrentCell = piece.getPosition();

        // Remove the piece from its current cell
        pieceCurrentCell.setPiece(null);

        // Set the piece to the destination cell
        destination.setPiece(piece);
        piece.setPosition(destination);

        // Promotion logic
        if (!piece.isKing() && ((piece.getColour() == 'w' && destination.getY() == BOARD_WIDTH - 1) || 
            (piece.getColour() == 'b' && destination.getY() == 0))) {
            piece.makeKing();
        }
        
    }

    private void clearPotentialMoves() {
        for (Cell[] row : board) {
            for (Cell cell : row) {
                cell.setPotentialMove(false);
            }
        }
    }
    
	@Override
    public void draw() {
        this.noStroke();
        background(WHITE_RGB[0], WHITE_RGB[1], WHITE_RGB[2]);
		//draw the board
		for (int i = 0; i < board.length; i++) {
            for (int i2 = 0; i2 < board[i].length; i2++) {
                // Highlight cell in green
				if (currentSelected != null && board[i][i2].getPiece() == currentSelected) {
					this.setFill(1, (i2+i) % 2);
					this.rect(i2*App.CELLSIZE, i*App.CELLSIZE, App.CELLSIZE, App.CELLSIZE);
				} else if ((i2+i) % 2 == 1) {
					//black cell
					this.fill(BLACK_RGB[0], BLACK_RGB[1], BLACK_RGB[2]);
					this.rect(i2*App.CELLSIZE, i*App.CELLSIZE, App.CELLSIZE, App.CELLSIZE);
				}
				board[i][i2].draw(this); 
			}
		}

        for (int i = 0; i < board.length; i++) {
            for (int i2 = 0; i2 < board[i].length; i2++) {
                if (board[i][i2].isPotentialMove()) {
                    // Highlight cell in blue
                    this.setFill(2, (i2+i) % 2);
                    this.rect(i2*App.CELLSIZE, i*App.CELLSIZE, App.CELLSIZE, App.CELLSIZE);
                }
            }
        }

        for (int i = 0; i < board.length; i++) {
            for (int i2 = 0; i2 < board[i].length; i2++) {
                board[i][i2].draw(this); 
            }
        }

        if (piecesInPlay.get('w').size() == 0 || piecesInPlay.get('b').size() == 0) {

            String winner = piecesInPlay.get('w').isEmpty() ? "Black wins!" : "White wins!";
            textAlign(CENTER, CENTER);
            textSize(32);
            text(winner, WIDTH / 2, HEIGHT / 2);
        }
        
    }

	public void setFill(int colourCode, int blackOrWhite) {
		this.fill(coloursRGB[colourCode][blackOrWhite][0], coloursRGB[colourCode][blackOrWhite][1], coloursRGB[colourCode][blackOrWhite][2]);
	}

    public static void main(String[] args) {
        PApplet.main("Checkers.App");
    }

}