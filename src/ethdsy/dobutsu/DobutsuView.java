package ethdsy.dobutsu;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Matrix.ScaleToFit;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class DobutsuView extends ImageView  {
	private static final Point ORIGIN = new Point(25, 29);
	private static final int SQUARE_SIZE = 115;
	private static final int SQUARE_PADDING = 20;
	static final int GRID_WIDTH = 3;
	static final int GRID_HEIGHT = 4;

	private Game game;

	private List<Point> selected = new ArrayList<Point>();
	private List<Point> bordered = new ArrayList<Point>();

	public DobutsuView(android.content.Context context) {
		super(context);
		game = new Game(context.getResources());
		init();
	}

	public DobutsuView(android.content.Context context, android.util.AttributeSet attrs) {
		super(context, attrs);
		game = new Game(context.getResources());
		init();
	}

	public DobutsuView(android.content.Context context, android.util.AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		game = new Game(context.getResources());
		init();
	}

	void init() {
		setClickable(true);
		setKeepScreenOn(true);
		setScaleType(ScaleType.FIT_START);
		setAdjustViewBounds(true);
		selected.clear();
		bordered.clear();
		game.init();
//		game.start(this, new HumanPlayer(true), new HumanPlayer(false), 2);
	}

	void setGame(Game game) {
		this.game = game;
	}

	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		int nbPieceOutUp = 0;
		int nbPieceOutDown = 0;
		for (Piece piece : game.getPosition().getPieces()) {
			if (piece.isOut()) {
				if (piece.isUp()) {
					piece.setLocation(GRID_WIDTH + (nbPieceOutUp % 3), nbPieceOutUp / 3);
					nbPieceOutUp++;
				} else {
					piece.setLocation(GRID_WIDTH + (nbPieceOutDown % 3), 2 + (nbPieceOutDown / 3));
					nbPieceOutDown++;
				}
			}

			Rect r = toScreen(piece.getX(), piece.getY());
			piece.draw(canvas, r);
		}

	}

	public void onSizeChanged(int x, int y, int w, int h) {
		invalidate();
	}

	public void onVisibilityChanged(View view, int state) {
		invalidate();
	}
	
	private Point toScreen(Point p) {
		float pts[] = new float[] { p.x, p.y };
		getImageMatrix().mapPoints(pts);
		return new Point((int) pts[0], (int) pts[1]);
	}

	private Rect toScreen(int x, int y) {
		float origx = ORIGIN.x + x * SQUARE_SIZE;
		float origy = ORIGIN.y + y * SQUARE_SIZE;
		RectF r = new RectF(origx, origy, origx + SQUARE_SIZE, origy + SQUARE_SIZE);

		// It is strange, the view matrix does not match :/
		Matrix m = new Matrix();
		m.setRectToRect(new RectF(0, 0, 390, 500), new RectF(0, 0, getMeasuredWidth(), getMeasuredHeight()), ScaleToFit.CENTER);
		// Matrix imageMatrix = getImageMatrix();
		// System.out.println("JB>" + imageMatrix);
		// imageMatrix.mapRect(r);
		m.mapRect(r);

		return new Rect((int) r.left + SQUARE_PADDING, (int) r.top + SQUARE_PADDING, (int) r.right - SQUARE_PADDING, (int) r.bottom - SQUARE_PADDING);
	}

	public static Point fromScreen(int x, int y) {
		if (x < ORIGIN.x || y < ORIGIN.y || y > ORIGIN.y + GRID_HEIGHT * SQUARE_SIZE)
			return null;
		Point p = new Point((x - ORIGIN.x) / SQUARE_SIZE, (y - ORIGIN.y) / SQUARE_SIZE);
		return p;
	}

	void removeFriendsAndBorders(Piece piece, ArrayList<Point> points) {
		boolean up = piece.isUp();
		for (Iterator<Point> it = points.iterator(); it.hasNext();) {
			Point point = it.next();
			if (point != Poussin.PROMOTING && (point.x < 0 || point.y < 0 || point.x >= GRID_WIDTH || point.y >= GRID_HEIGHT)) {
				it.remove();
				continue;
			}
			Piece pieceOnSquare = getPosition().getPiece(point.x, point.y);
			if (pieceOnSquare != null && pieceOnSquare.isUp() == up)
				it.remove();
		}
	}

	/*
	 * @Override public void paint(Graphics g) { g.drawImage(back, 0, 0, null);
	 * g.drawImage(back2, back.getWidth(null), 0, null);
	 * 
	 * g.setColor(Color.YELLOW.brighter()); for (Point point : selected) { int x
	 * = toXScreen(point.x); int y = toYScreen(point.y); g.fillRect(x + 2, y +
	 * 2, SQUARE_SIZE - 6, SQUARE_SIZE - 6); }
	 * 
	 * g.setColor(Color.RED); ((Graphics2D)g).setStroke(new BasicStroke(4f));
	 * for (Point point : bordered) { int x = toXScreen(point.x); int y =
	 * toYScreen(point.y); g.drawRect(x, y, SQUARE_SIZE - 2, SQUARE_SIZE - 2); }
	 * 
	 * int nbPieceOutUp = 0; int nbPieceOutDown = 0; for (Piece piece :
	 * game.getPosition().getPieces()) { if (piece.isOut()) { if (piece.isUp())
	 * { piece.setLocation(WIDTH + (nbPieceOutUp % 3), nbPieceOutUp / 3);
	 * nbPieceOutUp++; } else { piece.setLocation(WIDTH + (nbPieceOutDown % 3),
	 * 2 + (nbPieceOutDown / 3)); nbPieceOutDown++; } }
	 * 
	 * int x = toXScreen(piece.getX()); int y = toYScreen(piece.getY());
	 * piece.draw(g, x, y, SQUARE_SIZE); } }
	 */

	public void clearSelected() {
		selected.clear();
	}

	public void clearBordered() {
		bordered.clear();
	}

	public void addSelected(Point point) {
		selected.add(point);
	}

	public void addBordered(Point p) {
		bordered.add(p);
	}

	public boolean isBordered(Point p) {
		return bordered.contains(p);
	}

	public void addFreeSquares(ArrayList<Point> points) {
		// add all squares
		for (int i = 0; i < GRID_WIDTH; i++) {
			for (int j = 0; j < GRID_HEIGHT; j++) {
				points.add(new Point(i, j));
			}
		}

		// remove all pieces
		for (Piece piece : game.getPieces()) {
			if (!piece.isOut()) {
				points.remove(piece.getLocation());
			}
		}
	}

	Position getPosition() {
		return game.getPosition();
	}
	
	public void onClick(View v) {
		
	}

	public boolean onTouch(View v, MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		Point p = fromScreen((int)x, (int)y);
		
		return true;
	}
}
