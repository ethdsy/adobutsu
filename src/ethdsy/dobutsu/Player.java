package ethdsy.dobutsu;

import android.graphics.Point;


public abstract class Player {
	
	protected boolean up;

	public Player(boolean up) {
		this.up = up;
	}

	public abstract boolean nextMove(DobutsuView board);

	protected void eat(Piece pieceMangee) {
		if (pieceMangee == null) {
			return;
		}
		
		pieceMangee.setOut(true);
		pieceMangee.setUp(!pieceMangee.isUp());
	}

	public boolean isUp() {
		return up;
	}

	protected boolean isPromoting(Point[] possibleMoves) {
		return possibleMoves.length == 1 && 
			possibleMoves[0] == Poussin.PROMOTING;
	}
}
