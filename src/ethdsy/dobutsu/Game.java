package ethdsy.dobutsu;

import java.util.HashMap;

import android.content.res.Resources;
import ethdsy.dobutsu.pieces.Elephant;
import ethdsy.dobutsu.pieces.Giraffe;
import ethdsy.dobutsu.pieces.Lion;
import ethdsy.dobutsu.pieces.Piece;
import ethdsy.dobutsu.pieces.Poussin;
import ethdsy.dobutsu.player.Player;

public class Game {
	private Position gamePos;
	
	private Player player1;
	private Player player2;
	private Player actualPlayer;
	private DobutsuView board;
	
	private HashMap<Position, Integer> positions;
    private boolean draw;

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
	
	public void start(final DobutsuView board, final Player player1, final Player player2, boolean firstUp) {
		this.player1 = player1;
		this.player2 = player2;
		actualPlayer = firstUp ? player1 : player2;
		positions = new HashMap<Position, Integer>();
		draw = false;
		this.board = board;

        actualPlayer.nextMove(board);
	}

	private int addPosition(HashMap<Position, Integer> positions) {
		Position newPos = new Position(getPosition());
		Integer nb = positions.get(newPos);
		if (nb == null)
			nb = 0;
		Integer newValue = nb + 1;
		positions.put(newPos, newValue);

		return newValue;
	}

	private boolean hasLost(Player actualPlayer) {
		boolean up = actualPlayer.isUp();
		return getPosition().hasLost(up);
	}
	
	public Position getPosition() {
		return gamePos;
	}
	
	public void onNextMove() {
		board.invalidate();
		Player previousPlayer = actualPlayer;
		actualPlayer = actualPlayer == player1 ? player2 : player1;
		
		if (draw)
			board.onGameWon(null);
		else if (hasLost(actualPlayer)) 
			board.onGameWon(previousPlayer);
		else if (!actualPlayer.nextMove(board))
		    board.onGameWon(null);
		else {
		    int nb = addPosition(positions);
			if (nb == 3)
				board.onGameWon(null);
		}

		
	}
}
