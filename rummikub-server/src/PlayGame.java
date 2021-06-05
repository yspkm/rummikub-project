import java.io.IOException;

public class PlayGame 
{
	public static void main(String[] args) throws IOException, InterruptedException
	{
		GameProcessor game = new GameProcessor();
		do {
			game.printGameState();
			game.moveTile();
		} while (!game.isFinish());
	}
}