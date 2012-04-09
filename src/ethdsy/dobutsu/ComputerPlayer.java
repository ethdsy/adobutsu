package ethdsy.dobutsu;

import java.util.ArrayList;
import java.util.Random;

import android.graphics.Point;

public class ComputerPlayer extends Player {
	private static final int POUSSIN_POINTS = 1;
	private static final int GIRAFFE_POINTS = 5;
	private static final int ELEFANT_POINTS = 3;
	private static final int POULE_POINTS = 10;
	
	private Random rand = new Random();
	private final int level;
	
	public ComputerPlayer(boolean up, int level) {
		super(up);
		this.level = level;
	}

	@Override
	public boolean nextMove(final DobutsuView board) {
		board.clearBordered();
		board.clearSelected();

		final Position pos = board.getPosition();
		final Move move = oneMove(up, pos, level);
		if (move == null)
			return false;

//		try {
//			SwingUtilities.invokeAndWait(new Runnable(){
//				@Override
//				public void run() {
//					Point oldLocation = move.piece.getLocation();
//					Piece pieceMangee = pos.getPiece(move.location.x, move.location.y);
//					eat(pieceMangee);
//					move.piece.setLocation(move.location.x, move.location.y);
//					
//					board.addSelected(move.location);
//					if (move.piece.isOut()) {
//						move.piece.setOut(false);
//					} else {
//						board.addSelected(oldLocation);
//					}
//				}
//			});
//		} catch (Throwable t) {
//		}

		return true;
	}

	private Move oneMove(boolean up, Position pos, int l) {
		ArrayList<Piece> movablePieces = new ArrayList<Piece>(); 
		Piece checkingPiece = pos.isChess(up);
		if (checkingPiece != null) {
			Piece king = pos.getKing(up);
			if (king.getPossibleMoves(pos).length > 0)
				movablePieces.add(king);
			Piece[] canEat = pos.canEat(checkingPiece);
			for (Piece apiece : canEat)
				movablePieces.add(apiece);
		}
		else {
			Piece[] pieces = pos.getPieces(up);
			boolean draw = true;
			for (Piece apiece : pieces) {
				if (apiece.getPossibleMoves(pos).length > 0) {
					movablePieces.add(apiece);
					draw = false;
				}
			}
			if (draw)
				return null;
		}

		//select all possible moves
		int bestValue = Integer.MIN_VALUE;
		Move bestMove = null;
		for (Piece piece : movablePieces) {
			Point[] possibleMoves = (checkingPiece == null || pos.getKing(up) == piece) ? 
					piece.getPossibleMoves(pos) :
						new Point[] {checkingPiece.getLocation()};
			for (Point newLoc : possibleMoves) {
				Move move = new Move(piece, newLoc);
				int value = testPosition(up, pos, move, l);
				if (value > bestValue || 
						(value == bestValue && rand.nextBoolean())) {
					bestValue = value;
					bestMove = move;
				}
			}
		}
		
		return bestMove;
	}

	private int testPosition(boolean up, Position pos, Move move, int l) {
		Position testPos = new Position(pos);
		testPos.applyMove(move.piece, move.location);
		
		if (testPos.hasLost(!up))
			return 100;
		
		if (l == 0)
			return evaluate(up, testPos);

		Move bestMove = oneMove(!up, testPos, l - 1);
		if (bestMove == null)
			return 0;

		testPos.applyMove(bestMove.piece, bestMove.location);
		return evaluate(up, testPos);
	}
	
	private int evaluate(boolean up, Position testPos) {
		int nb = 0;
		if (testPos.hasLost(up))
			return -100;
		if (testPos.hasLost(!up))
			return 100;
		for (Piece piece : testPos.getPieces()) {
			int val = 0;
			if (piece instanceof Poussin) {
				if (((Poussin)piece).isPoule())
					val = POULE_POINTS;
				else
					val = POUSSIN_POINTS;
			} else if (piece instanceof Giraffe)
				val = GIRAFFE_POINTS;
			else if (piece instanceof Elephant)
				val = ELEFANT_POINTS;
			
			if (piece.isUp() == up) 
				nb += val;
			else
				nb -= val;
		}
		return nb;
	}

	private static class Move {
		Piece piece;
		Point location;
		
		public Move(Piece piece, Point location) {
			this.piece = piece;
			this.location = location;
		}
	}
}
