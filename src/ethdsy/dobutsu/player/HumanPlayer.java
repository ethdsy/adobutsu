package ethdsy.dobutsu.player;

import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import ethdsy.dobutsu.DobutsuView;
import ethdsy.dobutsu.Position;
import ethdsy.dobutsu.pieces.Piece;
import ethdsy.dobutsu.pieces.Poussin;

public class HumanPlayer extends Player implements OnTouchListener {
	private Point[] possibleMoves;
	private Point selection;
	private Piece piece;

	public HumanPlayer(boolean up) {
		super(up);
	}

	@Override
	public boolean nextMove(DobutsuView board) {
		if (initBoard(board))
		    // wait for the selection
		    register(board);
		else
		    return false;
		
		return true;
    }
	
	private boolean initBoard(DobutsuView board) {
		selection = null;
		piece = null;
		Position pos = board.getPosition();

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
			if (draw) {
				return false;
			}	
		}
		board.invalidate();
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


	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() != MotionEvent.ACTION_UP)
			return false;
		DobutsuView board = (DobutsuView) v;
		selection = board.fromScreen((int) event.getX(), (int) event.getY());

        if (piece == null) {
			return firstTouch(board, selection);
		} 
	
	    if (!contains(selection, possibleMoves) || selection.equals(piece.getLocation()))  {
			initBoard(board);	
			return true;
		}
		
		return lastTouch(board, selection);
	}
	
	private boolean lastTouch(DobutsuView board, Point selection)
	{		
		Position pos = board.getPosition();
		
		pos.applyMove(piece, selection);
		
		if (piece.isOut())
		{
			board.clearSelected();
		}
		board.addSelected(selection);
		
		unregister(board);
		board.onFinishedMove();
		
		return true;
	}	
	
	private boolean firstTouch(DobutsuView board, Point selection)
	{
		Position pos = board.getPosition();
		piece = pos.getPiece(selection.x, selection.y);
		if (piece == null || piece.isUp() != up || !board.isBordered(piece.getLocation())) {
			return false;
		}
		board.clearSelected();
		board.addSelected(selection);
		
		// select all possible moves
		board.clearBordered();
		Piece checkingPiece = pos.isChess(up);
		possibleMoves = (checkingPiece == null || pos.getKing(up) == piece) ? piece.getPossibleMoves(pos) : new Point[] { checkingPiece
			.getLocation() };
		boolean isPromoting = isPromoting(possibleMoves);
		if (isPromoting) {
			selection = Poussin.PROMOTING;
			lastTouch(board, selection);
			// TODO promotion (canap� ?)
//				int result = JOptionPane.showConfirmDialog(board, new JLabel(new ImageIcon(piece.getImage(true))), "Promotion",
//						JOptionPane.YES_NO_OPTION);
//				p = result == JOptionPane.YES_OPTION ? Poussin.PROMOTING : piece.getLocation();
		} else {
			for (Point move : possibleMoves) {
				board.addBordered(move);
			}
			board.invalidate();
		}

        // wait for target position
		return true;	
	}
}
