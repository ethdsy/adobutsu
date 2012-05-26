package ethdsy.dobutsu;

import android.app.*;
import android.content.*;
import android.os.*;
import ethdsy.dobutsu.player.*;

public class MainActivity extends Activity
{
    enum Opponent {
		WEAK,
		STRONG,
		HUMAN
	}
	
    private boolean oddGame = false;
	private Opponent opponent = Opponent.HUMAN;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		startGame();
	}

	@Override
	protected void onResume() {
		super.onResume();
		chooseOpponent();
		
	}
	
	void startGame()
	{
		DobutsuView dobutsuView = (DobutsuView)findViewById(R.id.dobutsuview);
		Game game = new Game(getResources());
		dobutsuView.setGame(game);
		
		EatenView eatenView = (EatenView)findViewById(R.id.eatenview);
		eatenView.setGame(game);
		dobutsuView.setPeer(eatenView);
		
        game.init();
		oddGame = !oddGame;
		game.start(dobutsuView, getOpponent(), new HumanPlayer(false), oddGame);
	}
	
	private Player getOpponent() {
		switch (opponent) {
			case WEAK:
			    return new ComputerPlayer(true, 1);
			case STRONG:
			    return new ComputerPlayer(true, 2);
		}
		
		return new HumanPlayer(true);
	}
	
	private void chooseOpponent() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Choose your Opponent")
		    .setCancelable(false)
			.setPositiveButton("Weak" , new DialogInterface.OnClickListener() {		
				public void onClick(DialogInterface p1, int p2)
				{
					opponent = Opponent.WEAK;
					startGame();
				}
			})
		    .setNeutralButton("Strong", new DialogInterface.OnClickListener() {		
				public void onClick(DialogInterface p1, int p2)
				{
					opponent = Opponent.STRONG;
					startGame();
				}
			})
			.setNegativeButton("Human", new DialogInterface.OnClickListener() {		
				public void onClick(DialogInterface p1, int p2)
				{
					opponent = Opponent.HUMAN;
					startGame();
				}
			});
		
		AlertDialog alert = builder.create();

		alert.show();
		
	}
}
