package ethdsy.dobutsu;

import android.content.res.*;
import android.graphics.*;
import java.util.*;

public class Lion extends Piece {
	private static final int IMAGE = R.drawable.lion;
	private static final int IMAGER = R.drawable.lionr;
	
	Lion(int x, int y, boolean up, Resources res) {
		super(res.getDrawable(IMAGE),
				res.getDrawable(IMAGER),
				x, y, up);
	}

	@Override
	public void addPossibleMoves(ArrayList<Point> points) {
		int x = getX();
		int y = getY();
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				points.add(new Point(x + i, y + j));
			}
		}
	}

	@Override
	public Point[] getPossibleMoves(Position pos) {
		Point[] possibleMoves = super.getPossibleMoves(pos);
		
		//lion can not put himself in chess
		Piece[] pieces = pos.getPieces(!isUp());
		ArrayList<Point> remainingMoves = new ArrayList<Point>();
		for (Point p : possibleMoves) {
			if (!canMoveTo(pieces, p.x, p.y, pos))
					remainingMoves.add(p);
		}
		
		return remainingMoves.toArray(new Point[remainingMoves.size()]);
	}

	private boolean canMoveTo(Piece[] pieces, int x, int y, Position pos) {
		for (Piece piece : pieces)
			if (piece.canMoveTo(x, y, pos, false))
				return true;
		return false;
	}
	
	@Override
	public boolean canMoveTo(int x, int y, Position pos, boolean withChess) {
		int diffX = x - getX();
		int diffY = y - getY();
		if (diffX > 1 || diffX < -1 || diffY > 1 || diffY < -1)
			return false;
		
		if (!withChess)
			return true;
		
		Piece[] pieces = pos.getPieces(!isUp());
		for (Piece piece : pieces) {
			if (piece.canMoveTo(x, y, pos, false))
				return false;
		}
		
		return true;
	}
	
	@Override
	boolean isMovable(Position position) {
		return getPossibleMoves(position).length > 0;
	}
	
}
