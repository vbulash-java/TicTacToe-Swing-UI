import ru.bulash.Game;
//simport ru.bulash.console.TicTacToe;
import ru.bulash.swing.GameUI;

public class Main {
	public static void main(String[] args) {
//		TicTacToe game = new TicTacToe();
//		game.init();
//		game.run();
		GameUI ui = new GameUI();
		ui.init();
		ui.run();
	}
}