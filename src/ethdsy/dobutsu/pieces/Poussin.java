package ethdsy.dobutsu.pieces;

import java.util.ArrayList;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import ethdsy.dobutsu.Position;
import ethdsy.dobutsu.R;

public class Poussin extends Piece {
	private static final int IMAGE = R.drawable.poussin;
	private static final int IMAGER = R.drawable.poussinr;
	public static final Point PROMOTING = new Point(42, 42);
	
	private Poule poule;
	private boolean isPoule;
	
	public Poussin(int x, int y, boolean up, Resources res) {
		super(res.getDrawable(IMAGE),
				res.getDrawable(IMAGER),
				x, y, up);
		poule = new Poule(up, res);
	}
	
	@Override
	public void init() {
		super.init();
		poule.init();
	}

	@Override
	public void addPossibleMoves(ArrayList<Point> points) {
		if (isPoule) 
			poule.addPossibleMoves(points);
		else
			points.add(new Point(getX(), getY() + (isUp() ? 1 : -1)));
	}

	@Override
	public void setLocation(int x, int y) {
		super.setLocation(x, y);
		poule.setLocation(x, y);
		
		if (isPromoting()) {
			isPoule = true;
		}
	}

	private boolean isPromoting() {
		int y = getY();
		return !isOut() && !isPoule && 
				((y == 0 && !isUp()) || 
				(y == 3 && isUp()));
	}
	
	@Override
	public void setOut(boolean out) {
		super.setOut(out);
		isPoule = false;
	}
	
	@Override
	public void draw(Canvas g, Rect r) {
		if (isPoule)
			poule.draw(g, r);
		else
			super.draw(g, r);
	}

	@Override
	public void setUp(boolean up) {
		super.setUp(up);
		poule.setUp(up);
	}
	
	@Override
	Drawable getImage(boolean promoting) {
		if (promoting) 
			return poule.getImage(false);
		return super.getImage(promoting);
	}
	
	public boolean isPoule() {
		return isPoule;
	}
	
	@Override
	public Piece clone() {
		Poussin cloned = (Poussin) super.clone();
		cloned.poule = (Poule) poule.clone();
		return cloned;
	}
	
	@Override
	public boolean canMoveTo(int x, int y, Position pos, boolean withChess) {
		if (isOut())
			return false;
		if (isPoule)
			return poule.canMoveTo(x, y, pos, true);
		int diffY = isUp() ? 1 : -1;
		return x == getX() && y == getY() + diffY;
	}
	
	@Override
	boolean isMovable(Position position) {
		if (isPoule)
			return poule.isMovable(position);
		return super.isMovable(position);
	}
}
