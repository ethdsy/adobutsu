package ethdsy.dobutsu;

import android.content.res.*;
import android.graphics.*;
import java.util.*;

public class Giraffe extends Piece {
	private static final int IMAGE = R.drawable.giraffe;
	private static final int IMAGER = R.drawable.giraffer;

	public Giraffe(int x, int y, boolean up, Resources res) {
		super(res.getDrawable(IMAGE),
				res.getDrawable(IMAGER),
				x, y, up);
	}

	@Override
	public void addPossibleMoves(ArrayList<Point> points) {
		points.add(new Point(getX(), getY()+1));
		points.add(new Point(getX(), getY()-1));
		points.add(new Point(getX()+1, getY()));
		points.add(new Point(getX()-1, getY()));
	}
	
	@Override
	public boolean canMoveTo(int x, int y, Position pos, boolean withChess) {
		if (isOut())
			return false;
		int diffx = x - getX();
		int diffy = y - getY();
		return (((diffx == 1 || diffx == -1) && diffy == 0) ||
				((diffy == 1 || diffy == -1) && diffx == 0));
	}
	
}
