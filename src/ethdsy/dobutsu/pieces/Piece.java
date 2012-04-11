package ethdsy.dobutsu.pieces;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import ethdsy.dobutsu.Position;

public abstract class Piece implements Cloneable {
	private final static Point OUT = new Point(10, 10);
	private static final int PADDING = 20;
	private final Drawable image;
	private final Drawable imageR;
	private int x;
	private int y;
	private boolean up;
	private boolean out;

	Piece(Drawable image, Drawable imageR, int x, int y, boolean up) {
		this.image = image;
		this.imageR = imageR;
		this.x = x;
		this.y = y;
		this.up = up;
	}

	public void draw(Canvas g, Rect r) {
		Drawable toDraw = up ? imageR : image;
		r.inset(PADDING, PADDING);
		toDraw.setBounds(r);
		toDraw.draw(g);
	}

	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setUp(boolean up) {
		this.up = up;
	}

	public boolean isUp() {
		return up;
	}

	public void setOut(boolean out) {
		this.out = out;
		if (out)
			setLocation(OUT.x + (isUp() ? 0 : 1), OUT.y);
	}

	public boolean isOut() {
		return out;
	}

	public abstract void addPossibleMoves(ArrayList<Point> points);

	public Point[] getPossibleMoves(Position position) {
		ArrayList<Point> points = new ArrayList<Point>();
		if (out) {
			position.addFreeSquares(points);
		} else {
			addPossibleMoves(points);
			position.removeFriendsAndBorders(this, points);
		}
		return points.toArray(new Point[points.size()]);
	}

	boolean isMovable(Position position) {
		if (out)
			return true;
		ArrayList<Point> points = new ArrayList<Point>(8);
		addPossibleMoves(points);
		position.removeFriendsAndBorders(this, points);
		return points.size() > 0;
	}

	public void init() {
	}

	public Point getLocation() {
		return new Point(x, y);
	}

	@Override
	public Piece clone() {
		try {
			return (Piece) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}

	synchronized Drawable getImage(boolean promoting) {
		return isUp() ? imageR : image;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (out ? 1231 : 1237);
		result = prime * result + (up ? 1231 : 1237);
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Piece other = (Piece) obj;
		if (out != other.out)
			return false;
		if (up != other.up)
			return false;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	public abstract boolean canMoveTo(int x, int y, Position pos, boolean withChess);
}
