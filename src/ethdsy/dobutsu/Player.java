package ethdsy.dobutsu;


import android.graphics.*;

public abstract class Player
 {
	
	protected boolean up;

	public Player(boolean up) {
		this.up = up;
	}

	public abstract boolean nextMove(DobutsuView board);

	public boolean isUp() {
		return up;
	}

	protected boolean isPromoting(Point[] possibleMoves) {
		return possibleMoves.length == 1 && 
			possibleMoves[0] == Poussin.PROMOTING;
	}
}
