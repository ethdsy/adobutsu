package ethdsy.dobutsu;

import android.app.*;
import android.content.*;
import android.content.res.*;
import android.graphics.*;
import android.graphics.Matrix.*;
import android.view.*;
import android.widget.*;
import ethdsy.dobutsu.pieces.*;
import ethdsy.dobutsu.player.*;
import java.util.*;

public class BaseBoardView extends ImageView {
	private static final Point ORIGIN = new Point(25, 29);
	static final int SQUARE_SIZE = 115;
	static final int GRID_WIDTH = 3;
	static final int GRID_HEIGHT = 4;
	
	private static final Paint SELECTED_PAINT;
	private static final Paint BORDERED_PAINT;
	
	static {
		SELECTED_PAINT = new Paint();
		SELECTED_PAINT.setColor(Color.YELLOW);

		BORDERED_PAINT = new Paint();
		BORDERED_PAINT.setColor(Color.RED);
		BORDERED_PAINT.setStrokeWidth(4f);
		BORDERED_PAINT.setStyle(Paint.Style.STROKE);
	}
	
	private Matrix matrix = new Matrix();

	protected Game game;

	private List<Point> selected = new ArrayList<Point>();
	private List<Point> bordered = new ArrayList<Point>();

	public BaseBoardView(android.content.Context context) {
		super(context);
		init(context.getResources());
	}

	public BaseBoardView(android.content.Context context, android.util.AttributeSet attrs) {
		super(context, attrs);
		init(context.getResources());
	}

	public BaseBoardView(android.content.Context context, android.util.AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
        init(context.getResources());
	}

	void init(Resources resources) {
		setClickable(true);
		setKeepScreenOn(true);
		setScaleType(ScaleType.FIT_START);
		setAdjustViewBounds(true);
		selected.clear();
		bordered.clear();
	}

	void setGame(Game game) {
		this.game = game;
	}

	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		for (Point point : selected) {
			Rect rect = toScreen(point.x, point.y);
			canvas.drawRect(rect.left + 2, rect.top + 2, rect.right - 2, rect.bottom - 2, SELECTED_PAINT);
		}

		for (Point point : bordered) {
			Rect rect = toScreen(point.x, point.y);
			canvas.drawRect(rect.left, rect.top, rect.right - 2, rect.bottom - 2, BORDERED_PAINT);
		}

        drawPieces(canvas); 

	}
	
	protected void drawPieces(Canvas canvas)
	{
		for (Piece piece : game.getPosition().getPieces()) {
			if (!piece.isOut()) {
			    drawPiece(piece, canvas);
			}
		}
	}
	
	protected void drawPiece(Piece piece, Canvas canvas) {
		Rect r = toScreen(piece.getX(), piece.getY());
		piece.draw(canvas, r);
	}

	public void onSizeChanged(int x, int y, int w, int h) {
		matrix.setRectToRect(new RectF(0, 0, 390, 500), new RectF(0, 0, getMeasuredWidth(), getMeasuredHeight()), ScaleToFit.CENTER);

		invalidate();
	}

	public void onVisibilityChanged(View view, int state) {
		invalidate();
	}

	protected Rect toScreen(int x, int y) {
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
		}
		return p;
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

	public Position getPosition() {
		return game.getPosition();
	}	
}
