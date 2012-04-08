package ethdsy.dobutsu;

import android.graphics.*;
import android.graphics.drawable.*;
import android.view.*;
import android.widget.*;
import java.util.*;

public class DobutsuView extends ImageView
{
	private static final Point ORIGIN = new Point(25, 28);
	private static final int SQUARE_SIZE = 115;
	static final int WIDTH = 3;
	static final int HEIGHT = 4;
	
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
		selected.clear();
		bordered.clear();
		game.init();
	}
	
	void setGame(Game game) {
		this.game = game;
	}
	
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		Drawable img = getDrawable();
		Rect rect = img.getBounds();
		//canvas.drawRect(rect, new Paint());
		
		int nbPieceOutUp = 0;
		int nbPieceOutDown = 0;
		for (Piece piece : game.getPosition().getPieces()) {
			if (piece.isOut()) {
				if (piece.isUp()) {
					piece.setLocation(WIDTH + (nbPieceOutUp % 3), nbPieceOutUp / 3);
					nbPieceOutUp++;
				}
				else {
					piece.setLocation(WIDTH + (nbPieceOutDown % 3), 2 + (nbPieceOutDown / 3));
					nbPieceOutDown++;
				}
			}
			
			int x = toXScreen(piece.getX());
			int y = toYScreen(piece.getY());
			piece.draw(canvas, x, y, (int)(SQUARE_SIZE * getImScale()));
		}
		
	}
	
	public void onSizeChanged(int x, int y, int w, int h) {
		invalidate();
	}
	
	public void onVisibilityChanged(View view, int state) {
	    invalidate();
	}
	
	private float getImScale() {
		Drawable img = getDrawable();
		Rect rect = img.getBounds();
		return ((float) rect.width()) / img.getIntrinsicWidth();
	}
	
	private int toXScreen(int x) {
//		int screenX = ORIGIN.x + x * SQUARE_SIZE;
		
		Drawable img = getDrawable();
		Rect rect = img.getBounds();
		int w = img.getIntrinsicWidth() - getPaddingLeft() - (ORIGIN.x * 2);
		return rect.left + ORIGIN.x + x * w / WIDTH;
//		return rect.left + (int)(screenX / getImScale());
	}
	
	private int toYScreen(int y) {
		int screenY = ORIGIN.y + y * SQUARE_SIZE;
		
		Drawable img = getDrawable();
		Rect rect = img.getBounds();
		int h = img.getIntrinsicHeight() - getPaddingTop() - (ORIGIN.y * 2);
		return rect.top + ORIGIN.y + y * h / HEIGHT;
		
//		return rect.top + (int)(screenY * getImScale());
	}
	
	public static Point fromScreen(int x, int y) {
		if (x < ORIGIN.x || y < ORIGIN.y ||
			y > ORIGIN.y + HEIGHT * SQUARE_SIZE)
			return null;
		Point p = new Point((x - ORIGIN.x) / SQUARE_SIZE,
							(y - ORIGIN.y) / SQUARE_SIZE);
		return p;
	}
//	
//	void removeFriendsAndBorders(Piece piece, ArrayList<Point> points) {
//		boolean up = piece.isUp();
//		for (Iterator<Point> it = points.iterator(); it.hasNext();) {
//			Point point = it.next();
//			if (point != Poussin.PROMOTING && (point.x < 0 || point.y < 0 ||
//				point.x >= WIDTH || point.y >= HEIGHT)) {
//				it.remove();
//				continue;
//			}
//			Piece pieceOnSquare = getPosition().getPiece(point.x, point.y);
//			if (pieceOnSquare != null && pieceOnSquare.isUp() == up)
//				it.remove();
//		}
//	}
//	
//	
	/*
	 
	 void init() {
	 selected.clear();
	 bordered.clear();
	 game.init();
	 }
	 
	 void setGame(Game game) {
	 this.game = game;
	 }
	 
	 @Override
	 public void paint(Graphics g) {
	 g.drawImage(back, 0, 0, null);
	 g.drawImage(back2, back.getWidth(null), 0, null);
	 
	 g.setColor(Color.YELLOW.brighter());
	 for (Point point : selected) {
	 int x = toXScreen(point.x);
	 int y = toYScreen(point.y);
	 g.fillRect(x + 2, y + 2, SQUARE_SIZE - 6, SQUARE_SIZE - 6);
	 }
	 
	 g.setColor(Color.RED);
	 ((Graphics2D)g).setStroke(new BasicStroke(4f));
	 for (Point point : bordered) {
	 int x = toXScreen(point.x);
	 int y = toYScreen(point.y);
	 g.drawRect(x, y, SQUARE_SIZE - 2, SQUARE_SIZE - 2);
	 }
	 
	 int nbPieceOutUp = 0;
	 int nbPieceOutDown = 0;
	 for (Piece piece : game.getPosition().getPieces()) {
	 if (piece.isOut()) {
	 if (piece.isUp()) {
	 piece.setLocation(WIDTH + (nbPieceOutUp % 3), nbPieceOutUp / 3);
	 nbPieceOutUp++;
	 }
	 else {
	 piece.setLocation(WIDTH + (nbPieceOutDown % 3), 2 + (nbPieceOutDown / 3));
	 nbPieceOutDown++;
	 }
	 }
	 
	 int x = toXScreen(piece.getX());
	 int y = toYScreen(piece.getY());
	 piece.draw(g, x, y, SQUARE_SIZE);
	 }
	 }
	 
	 private int toXScreen(int x) {
	 return ORIGIN.x + x * SQUARE_SIZE;
	 }
	 
	 private int toYScreen(int y) {
	 return ORIGIN.y + y * SQUARE_SIZE;
	 }
	 
	 public static Point fromScreen(int x, int y) {
	 if (x < ORIGIN.x || y < ORIGIN.y ||
	 y > ORIGIN.y + HEIGHT * SQUARE_SIZE)
	 return null;
	 Point p = new Point((x - ORIGIN.x) / SQUARE_SIZE,
	 (y - ORIGIN.y) / SQUARE_SIZE);
	 return p;
	 }
	 
	 void removeFriendsAndBorders(Piece piece, ArrayList<Point> points) {
	 boolean up = piece.isUp();
	 for (Iterator<Point> it = points.iterator(); it.hasNext();) {
	 Point point = it.next();
	 if (point != Poussin.PROMOTING && (point.x < 0 || point.y < 0 ||
	 point.x >= WIDTH || point.y >= HEIGHT)) {
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
	 
	 public void addFreeSquares(ArrayList<Point> points) {
	 //add all squares
	 for (int i = 0; i < WIDTH; i++) {
	 for (int j = 0; j < HEIGHT; j++) {
	 points.add(new Point(i, j));
	 }
	 }
	 
	 //remove all pieces
	 for (Piece piece : game.getPieces()) {
	 if (!piece.isOut()) {
	 points.remove(piece.getLocation());
	 }
	 }
	 }
	 
	 Position getPosition() {
	 return game.getPosition();
	 }
	 }
	 
*/
} 
