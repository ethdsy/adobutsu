package ethdsy.dobutsu;

import android.content.res.*;
import android.graphics.*;
import ethdsy.dobutsu.pieces.*;

public class EatenView extends BaseBoardView {
	public EatenView(android.content.Context context) {
		super(context);
	}

	public EatenView(android.content.Context context, android.util.AttributeSet attrs) {
		super(context, attrs);
	}

	public EatenView(android.content.Context context, android.util.AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

    @Override
    protected void init(Resources resources) {
		super.init(resources);
		setScaleType(ScaleType.FIT_END);
	}
	
	@Override
	protected void drawPieces(Canvas canvas) {
		if (game == null) return;
		
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
				
				drawPiece(piece, canvas);
			}
		}

	}

    public Point fromScreen(int x, int y) {
		Point p = super.fromScreen(x, y);
		if (p != null)
		    p.x += GRID_WIDTH;
		return p;
	}
	
	public Rect toScreen(int x, int y) {
		return super.toScreen(x - GRID_WIDTH, y);
	}
}
