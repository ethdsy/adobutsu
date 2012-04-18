package ethdsy.dobutsu;

import android.app.*;
import android.os.*;
import ethdsy.dobutsu.player.*;

public class MainActivity extends Activity
{
    private boolean oddGame;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
        startGame();
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
		game.start(dobutsuView, new ComputerPlayer(true, 1), new HumanPlayer(false), oddGame);
	}
}
