package ethdsy.dobutsu;

import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class HumanPlayer extends Player implements OnTouchListener {
	public HumanPlayer(boolean up) {
		super(up);
	}

	@Override
	public boolean nextMove(DobutsuView board) {
		register(board);

		Point p = null;
		Piece piece = null;
		Position pos = board.getPosition();
		do {
			// select all possible pieces
			board.clearBordered();

			Piece checkingPiece = pos.isChess(up);
			if (checkingPiece != null) {
				Piece king = pos.getKing(up);
				if (king.getPossibleMoves(pos).length > 0)
					board.addBordered(king.getLocation());
				Piece[] canEat = pos.canEat(checkingPiece);
				for (Piece apiece : canEat)
					board.addBordered(apiece.getLocation());
			} else {
				Piece[] pieces = pos.getPieces(up);
				boolean draw = true;
				for (Piece apiece : pieces) {
					if (apiece.getPossibleMoves(pos).length > 0) {
						board.addBordered(apiece.getLocation());
						draw = false;
					}
				}
				if (draw)
					return false;
			}
			board.invalidate();

			// wait for the selection
			p = getSelection();
			piece = pos.getPiece(p.x, p.y);
			while (piece == null || piece.isUp() != up || !board.isBordered(piece.getLocation())) {
				p = getSelection();
				piece = pos.getPiece(p.x, p.y);
			}
			board.clearSelected();
			board.addSelected(p);

			// select all possible moves
			board.clearBordered();
			Point[] possibleMoves = (checkingPiece == null || pos.getKing(up) == piece) ? piece.getPossibleMoves(pos) : new Point[] { checkingPiece
					.getLocation() };
			boolean isPromoting = isPromoting(possibleMoves);
			if (isPromoting) {
				// TODO promotion (canapé ?)
//				int result = JOptionPane.showConfirmDialog(board, new JLabel(new ImageIcon(piece.getImage(true))), "Promotion",
//						JOptionPane.YES_NO_OPTION);
//				p = result == JOptionPane.YES_OPTION ? Poussin.PROMOTING : piece.getLocation();
			} else {
				for (Point move : possibleMoves) {
					board.addBordered(move);
				}
				board.invalidate();

				// wait for target position
				do {
					p = getSelection();
				} while (!contains(p, possibleMoves) && !p.equals(piece.getLocation()));
			}
		} while (p.equals(piece.getLocation()));
		Piece pieceMangee = pos.getPiece(p.x, p.y);
		eat(pieceMangee);

		if (p == Poussin.PROMOTING) {
			p = piece.getLocation();
		}
		piece.setLocation(p.x, p.y);
		if (piece.isOut()) {
			piece.setOut(false);
			board.clearSelected();
		}
		board.addSelected(p);

		unregister(board);
		return true;
	}

	private void unregister(final DobutsuView board) {
		board.setOnTouchListener(null);
	}

	private void register(final DobutsuView board) {
		board.setOnTouchListener(this);
	}

	private boolean contains(Point p, Point[] points) {
		for (Point point : points) {
			if (p.equals(point))
				return true;
		}
		return false;
	}

	private Point selection;

	private Point getSelection() {
		synchronized (this) {
			selection = null;
			while (selection == null) {
				try {
					wait();
				} catch (InterruptedException e) {
				}
			}
		}
		return selection;
	}

	public boolean onTouch(View v, MotionEvent event) {
		// synchronized (this) {
		selection = DobutsuView.fromScreen((int) event.getX(), (int) event.getY());
		notify();
		// }
		return true;
	}
}
