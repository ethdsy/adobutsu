package ethdsy.dobutsu;

import android.content.res.*;
import java.util.*;

public class Game {
	private Position gamePos;
	
	Game(Resources res) {
		Piece[] pieces = new Piece[] {
			new Giraffe(0, 0, true, res),
			new Lion(1, 0, true, res),
			new Elephant(2, 0, true, res),
			new Poussin(1, 1, true, res),
			new Poussin(1, 2, false, res),
			new Elephant(0, 3, false, res),
			new Lion(1, 3, false, res),
			new Giraffe(2, 3, false, res)
		};
		gamePos = new Position(pieces);
	}

	public Piece[] getPieces() {
		return getPosition().getPieces();
	}

	public void init() {
		for (Piece piece : getPosition().getPieces()) {
			piece.init();
		}
	}
//	
//	public void start(final Board board, final Player player1, final Player player2, final int pause) {
//		Player actualPlayer = player1;
//		HashMap<Position, Integer> positions = new HashMap<Position, Integer>();
//		boolean draw = false;
//
//		while(!hasLost(actualPlayer) && !draw) {
//			int nb = addPosition(positions);
//			draw = (nb == 3) || !actualPlayer.nextMove(board);
//			board.repaint();
//
//			if (pause > 0)
//				try {
//					Thread.sleep(pause * 1000);
//				} catch (InterruptedException e) {
//				}
//			actualPlayer = actualPlayer == player1 ? player2 : player1;
//		}
//
//		String msg = draw ? "DRAW!" : "Player " + (actualPlayer.isUp() ? "DOWN" : "UP") + " won!"; 
//		JOptionPane.showMessageDialog(board, 
//				msg, "Game finished", JOptionPane.INFORMATION_MESSAGE);
//		
//	}

	private int addPosition(HashMap<Position, Integer> positions) {
		Position newPos = new Position(getPosition());
		Integer nb = positions.get(newPos);
		if (nb == null)
			nb = 0;
		Integer newValue = nb + 1;
		positions.put(newPos, newValue);

		return newValue;
	}

//	private boolean hasLost(Player actualPlayer) {
//		boolean up = actualPlayer.isUp();
//		return getPosition().hasLost(up);
//	}
	
	public Position getPosition() {
		return gamePos;
	}
}
