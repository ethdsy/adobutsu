package ethdsy.dobutsu;

import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.*;
import java.util.*;

public class Poussin extends Piece {
	private static final int IMAGE = R.drawable.poussin;
	private static final int IMAGER = R.drawable.poussinr;
	static final Point PROMOTING = new Point(42, 42);
	
	private Poule poule;
	private boolean isPoule;
	
	Poussin(int x, int y, boolean up, Resources res) {
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
		else if (isPromoting())
			points.add(PROMOTING);
		else
			points.add(new Point(getX(), getY() + (isUp() ? 1 : -1)));
	}

	@Override
	void setLocation(int x, int y) {
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
	void draw(Canvas g, int x, int y, int size) {
		if (isPoule)
			poule.draw(g, x, y, size);
		else
			super.draw(g, x, y, size);
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
	
	boolean isPoule() {
		return isPoule;
	}
	
	@Override
	protected Piece clone() {
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
