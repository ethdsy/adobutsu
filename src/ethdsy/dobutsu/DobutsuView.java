package ethdsy.dobutsu;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.graphics.Matrix.*;
import android.view.*;
import android.widget.*;
import ethdsy.dobutsu.pieces.*;
import ethdsy.dobutsu.player.*;
import java.util.*;

public class DobutsuView extends ImageView {
	private static final Point ORIGIN = new Point(25, 29);
	private static final int SQUARE_SIZE = 115;
	static final int GRID_WIDTH = 3;
	static final int GRID_HEIGHT = 4;

	public Matrix matrix;

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
		matrix = new Matrix();

		setClickable(true);
		setKeepScreenOn(true);
		setScaleType(ScaleType.FIT_START);
		setAdjustViewBounds(true);
		selected.clear();
		bordered.clear();
		game.init();
		game.start(this, new HumanPlayer(true), new HumanPlayer(false), 2);
	}

	void setGame(Game game) {
		this.game = game;
	}

	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Paint paint = new Paint();
		paint.setColor(Color.YELLOW);
		for (Point point : selected) {
			Rect rect = toScreen(point.x, point.y);
			canvas.drawRect(rect.left + 2, rect.top + 2, rect.right - 6, rect.bottom - 6, paint);
		}

		paint.setColor(Color.RED);
		paint.setStrokeWidth(4f);
		paint.setStyle(Paint.Style.STROKE);
		for (Point point : bordered) {
			Rect rect = toScreen(point.x, point.y);
			canvas.drawRect(rect.left, rect.top, rect.right - 2, rect.bottom - 2, paint);
		}

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
		matrix.setRectToRect(new RectF(0, 0, 390, 500), new RectF(0, 0, getMeasuredWidth(), getMeasuredHeight()), ScaleToFit.CENTER);

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
		// Matrix imageMatrix = getImageMatrix();
		// System.out.println("JB>" + imageMatrix);
		// imageMatrix.mapRect(r);
		matrix.mapRect(r);

		return new Rect((int) r.left, (int) r.top, (int) r.right, (int) r.bottom);
	}

	public Point fromScreen(int x, int y) {

		Point p = null;
		Matrix inv = new Matrix(matrix);
		boolean invert = inv.invert(inv);
		if (invert) {
			float[] pt = new float[] { x, y };
			inv.mapPoints(pt);
			if (pt[0] < ORIGIN.x || pt[1] < ORIGIN.y  || pt[1] > ORIGIN.y + GRID_HEIGHT * SQUARE_SIZE)
				return null;
			p = new Point(((int) pt[0] - ORIGIN.x) / SQUARE_SIZE, ((int) pt[1] - ORIGIN.y) / SQUARE_SIZE);
			//			System.out.println("JB> " + x + "," + y + " -> " + pt[0] + "," + pt[1] + " = " + p);
		}
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

	public void addFreeSquares(ArrayList<Point> points) { // add all squares
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

	public Position getPosition() {
		return game.getPosition();
	}
	
	public void onFinishedMove() {
		game.onNextMove();
	}

	public void onClick(View v) {

	}

	public boolean onTouch(View v, MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		Point p = fromScreen((int) x, (int) y);

		return true;
	}
	
	void onGameWon(Player player) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
		builder.setTitle("Game Over")
		    .setCancelable(false)
			.setPositiveButton("OK" , new DialogInterface.OnClickListener() {		
				public void onClick(DialogInterface p1, int p2)
				{
					init();
				}
			});
			
		if (player != null) {
			builder.setMessage("Player " + 
					   (player.isUp()  ? "up" : "down") +
					   " won!");
			
		} 
		else
		    builder.setMessage("Draw!");
			
		AlertDialog alert = builder.create();
		
		alert.show();
	}
}
