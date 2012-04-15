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

public class DobutsuView extends BaseBoardView {
	public DobutsuView(android.content.Context context) {
		super(context);
	}

	public DobutsuView(android.content.Context context, android.util.AttributeSet attrs) {
		super(context, attrs);
	}

	public DobutsuView(android.content.Context context, android.util.AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	void init(Resources resources) {
        super.init(resources);
		Game game = new Game(resources);
		setGame(game);
        game.init();
		game.start(this, new HumanPlayer(true), new HumanPlayer(false), 2);
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

	public void onFinishedMove() {
		game.onNextMove();
	}

	void onGameWon(Player player) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
		builder.setTitle("Game Over")
		    .setCancelable(false)
			.setPositiveButton("OK" , new DialogInterface.OnClickListener() {		
				public void onClick(DialogInterface p1, int p2)
				{
					init(getContext().getResources());
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
