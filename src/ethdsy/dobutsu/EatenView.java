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
		int nbPieceOutUp = 0;
		int nbPieceOutDown = 0;
		for (Piece piece : game.getPosition().getPieces()) {
			if (piece.isOut()) {
				if (piece.isUp()) {
					piece.setLocation(nbPieceOutUp % 3, nbPieceOutUp / 3);
					nbPieceOutUp++;
				} else {
					piece.setLocation(nbPieceOutDown % 3, 2 + (nbPieceOutDown / 3));
					nbPieceOutDown++;
				}
				
				drawPiece(piece, canvas);
			}
		}

	}

}
