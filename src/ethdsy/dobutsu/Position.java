package ethdsy.dobutsu;

import android.graphics.*;
import java.util.*;

public class Position {
	private static final int KING_UP = 1;
	private static final int KING_DOWN = 6;

	private final Piece[] pieces;
	private final Piece[][] fastAccess = new Piece[DobutsuView.WIDTH][DobutsuView.HEIGHT];
	
	Position(Piece[] pieces) {
		this.pieces = pieces;
		fillFastAccess();
	}
	
	Position(Position pos) {
		Piece[] otherPieces = pos.getPieces();
		pieces = new Piece[otherPieces.length];
		for (int i = 0; i < otherPieces.length; i++) {
			pieces[i] = otherPieces[i].clone();
		}
		fillFastAccess();
	}

	private void fillFastAccess() {
		for (Piece piece : pieces) {
			if (!piece.isOut())
				fastAccess[piece.getX()][piece.getY()] = piece;
		}
	}

	public Piece[] getPieces() {
		return pieces;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(pieces);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Position other = (Position) obj;
		if (!Arrays.equals(pieces, other.pieces))
			return false;
		return true;
	}
	
	Lion getKing(boolean up) {
		Piece lion = getPieces()[up ? KING_UP : KING_DOWN];
		return (Lion) lion;
	}

	Piece isChess(boolean up) {
		Piece lion = getKing(up);
		Point lionLoc = lion.getLocation();
		for (Piece piece : pieces) {
			if (piece.isUp() != up && !piece.isOut()) {
				Point[] possibleMoves = piece.getPossibleMoves(this);
				for (Point p : possibleMoves) {
					if (p.equals(lionLoc))
						return piece;
				}
			}
		}
		
		return null;
	}

	public void addFreeSquares(ArrayList<Point> points) {
		for (int i = 0; i < DobutsuView.WIDTH; i++) {
			for (int j = 0; j < DobutsuView.HEIGHT; j++) {
				if (fastAccess[i][j] == null)
					points.add(new Point(i, j));
			}
		}
	}
	
	void removeFriendsAndBorders(Piece piece, ArrayList<Point> points) {
		boolean up = piece.isUp();
		for (Iterator<Point> it = points.iterator(); it.hasNext();) {
			Point point = it.next();
			if (point != Poussin.PROMOTING && (point.x < 0 || point.y < 0 ||
					point.x >= DobutsuView.WIDTH || point.y >= DobutsuView.HEIGHT)) {
				it.remove();
				continue;
			}
			Piece pieceOnSquare = getPiece(point.x, point.y);
			if (pieceOnSquare != null && pieceOnSquare.isUp() == up)
				it.remove();
		}
	}

	Piece getPiece(int x, int y) {
		if (x < DobutsuView.WIDTH)
			return fastAccess[x][y];
		
		for (Piece piece : pieces) {
			if (piece.isOut() && piece.getX() == x && piece.getY() == y)
				return piece;
		}
		
		return null;
	}
	
	Piece getSimilarPiece(Piece piece) {
		if (!piece.isOut())
			return getPiece(piece.getX(), piece.getY());
		
		for (Piece myPiece : pieces) {
			if (myPiece.isOut() && myPiece.isUp() == piece.isUp() && 
					piece.getClass() == myPiece.getClass())
				return myPiece;
		}

		return null;
	}
	
	Piece[] getPieces(boolean up) {
		ArrayList<Piece> myPieces = new ArrayList<Piece>(pieces.length);
		for (Piece piece : pieces) {
			if (piece.isUp() == up) {
				myPieces.add(piece);
			}
		}
		return myPieces.toArray(new Piece[myPieces.size()]);
	}
	
	Piece[] canEat(Piece target) {
		ArrayList<Piece> pieces = new ArrayList<Piece>();
		for (Piece piece : getPieces()) {
			if (!piece.isOut() &&
					piece.isUp() != target.isUp()) {
				Point[] possibleMoves = piece.getPossibleMoves(this);
				for (Point p : possibleMoves) {
					if (p.equals(target.getLocation())) {
						pieces.add(piece);
						break;
					}
				}
			}
		}
		
		return pieces.toArray(new Piece[pieces.size()]);
	}
	
	private boolean isEatable(Piece target) {
		for (Piece piece : getPieces()) {
			if (!piece.isOut() &&
					piece.isUp() != target.isUp()) {
				if (piece.canMoveTo(target.getX(), target.getY(), this, true))
						return true;
			}
		}
		
		return false;
	}

	void applyMove(Piece fromPiece, Point toLocation) {
		Piece piece = getSimilarPiece(fromPiece);

		boolean isPromoting = toLocation == Poussin.PROMOTING;
		if (isPromoting) {
			piece.setLocation(piece.getX(), piece.getY());
		}
		else {

			Piece pieceMangee = getPiece(toLocation.x, toLocation.y);
			if (pieceMangee != null) {
				pieceMangee.setOut(true);
				pieceMangee.setUp(!pieceMangee.isUp());
			}
			if (!piece.isOut())
				fastAccess[fromPiece.getX()][fromPiece.getY()] = null;
			fastAccess[toLocation.x][toLocation.y] = piece;
			piece.setLocation(toLocation.x, toLocation.y);
			if (piece.isOut())
				piece.setOut(false);
		}
	}
	
	boolean hasLost(boolean up) {
		Piece checkingPiece = isChess(up);
		return ( checkingPiece != null && 
				!getKing(up).isMovable(this) &&
				!isEatable(checkingPiece)) ||
				getKing(!up).getLocation().y == (up ? 0 : 3);
		
	}
}
