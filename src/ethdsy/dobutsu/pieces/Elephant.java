package ethdsy.dobutsu.pieces;

import java.util.ArrayList;

import android.content.res.Resources;
import android.graphics.Point;
import ethdsy.dobutsu.Position;
import ethdsy.dobutsu.R;

public class Elephant extends Piece {
	private static final int IMAGE = R.drawable.elephant;
	private static final int IMAGER = R.drawable.elephantr;

	public Elephant(int x, int y, boolean up, Resources res) {
		super(res.getDrawable(IMAGE),
				res.getDrawable(IMAGER),
				x, y, up);
	}

	@Override
	public void addPossibleMoves(ArrayList<Point> points) {
		points.add(new Point(getX() + 1, getY() + 1));
		points.add(new Point(getX() - 1, getY() + 1));
		points.add(new Point(getX() + 1, getY() - 1));
		points.add(new Point(getX() - 1, getY() - 1));
	}

	@Override
	public boolean canMoveTo(int x, int y, Position pos, boolean withChess) {
		if (isOut())
			return false;
		int diffx = x - getX();
		int diffy = y - getY();
		return ((diffx == 1 || diffx == -1) &&
				(diffy == 1 || diffy == -1));
	}
}
