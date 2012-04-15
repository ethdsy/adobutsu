package ethdsy.dobutsu;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Canvas;
import ethdsy.dobutsu.pieces.Piece;

public class EatenView extends BaseBoardView {
	// public EatenView(android.content.Context context) {
	// super(context);
	// }

	public EatenView(android.content.Context context, android.util.AttributeSet attrs) {
		super(context, attrs);
	}

	public EatenView(android.content.Context context, android.util.AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void init(Resources resources, android.util.AttributeSet attrs) {
		super.init(resources, attrs);
		setScaleType(ScaleType.FIT_END);
	}

	public Game getGame() {
		DobutsuView dv = (DobutsuView) (((Activity) getContext()).findViewById(R.id.dobutsuview));
		return dv.getGame();
	}

	@Override
	protected void drawPieces(Canvas canvas) {
		int nbPieceOutUp = 0;
		int nbPieceOutDown = 0;
		for (Piece piece : getGame().getPosition().getPieces()) {
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
