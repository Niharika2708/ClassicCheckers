package Checkers;

public class Cell {

	private int x;
	private int y;
	private CheckersPiece piece;
	private boolean isPotentialMove = false;
	
	public Cell(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public void setPiece(CheckersPiece p) {
		this.piece = p;
		if (p != null) {
			p.setPosition(this);
		}
	}
	
	public CheckersPiece getPiece() {
		return this.piece;
	}

	public void setPotentialMove(boolean isPotentialMove) {
    	this.isPotentialMove = isPotentialMove;
	}

	public boolean isPotentialMove() {
    	return this.isPotentialMove;
	}
	
	public void draw(App app) {
		if (this.piece != null) this.piece.draw(app);
	}
}