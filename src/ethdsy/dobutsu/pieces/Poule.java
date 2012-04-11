package ethdsy.dobutsu.pieces;

import java.util.ArrayList;

import android.content.res.Resources;
import android.graphics.Point;
import ethdsy.dobutsu.Position;
import ethdsy.dobutsu.R;

public class Poule extends Piece {
	private static final int IMAGE = R.drawable.poule;
	private static final int IMAGER = R.drawable.pouler;

	Poule(boolean up, Resources res) {
		super(res.getDrawable(IMAGE),
				res.getDrawable(IMAGER),
				-1, -1, up);
	}

	@Override
	public void addPossibleMoves(ArrayList<Point> points) {
		points.add(new Point(getX() + 1, getY() + (isUp() ? 1 : -1)));
		points.add(new Point(getX() - 1, getY() + (isUp() ? 1 : -1)));
		points.add(new Point(getX(), getY()+1));
		points.add(new Point(getX(), getY()-1));
		points.add(new Point(getX()+1, getY()));
		points.add(new Point(getX()-1, getY()));
	}

	@Override
	public boolean canMoveTo(int x, int y, Position pos, boolean withChess) {
		if (isOut())
			return false;
		
		ArrayList<Point> points = new ArrayList<Point>(6);
		addPossibleMoves(points);
		return points.contains(new Point(x, y));
	}
}
