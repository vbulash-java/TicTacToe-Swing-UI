package ru.bulash;

import ru.bulash.models.Square;
import ru.bulash.models.Which;
import ru.bulash.swing.GameUI;

import javax.swing.*;
import java.awt.*;

public class Game {
	private static Square model;
	private static GameUI ui;
	private static Gamer[] gamers;
	private static int iGamer = 0;

	public static Square getModel() {
		return model;
	}

	public static void setModel(Square model) {
		Game.model = model;
	}

	public static GameUI getUi() {
		return ui;
	}

	public static void setUi(GameUI ui) {
		Game.ui = ui;
	}

	public static Gamer[] getGamers() {
		return gamers;
	}

	public static void setGamers(Gamer[] gamers) {
		Game.gamers = gamers;
	}

	public static Gamer gamer() {
		return getGamers()[iGamer];
	}

	public static Gamer nextGamer() {
		iGamer++;
		if (iGamer == getGamers().length) iGamer = 0;
		return gamer();
	}

	public static void onPress(Point point, JButton btnCell) {
		String who = "";
		switch (Game.gamer().getRole()) {
			case HUMAN1 -> who = "Игрок-1";
			case HUMAN2 -> who = "Игрок-2";
			case AI -> who = "Искусственный интеллект";
		}
		// Стратегия человека:
		// 	Сделать ход (в модели)
		// Стратегия искусственного интеллекта:
		//	Оценить угрозу от человека
		//	Парировать угрозу от человека (сделать ход в ячейку с максимальной угрозой)
		point = Game.gamer().turn(point);
		if (Game.gamer().getRole() == Which.AI) {
			btnCell = Game.getUi().getButton(point);
		}
		// Общая часть:
		// 	Отметить кнопку и клетку, в которых сделан ход (новый текст на кнопке)
		// TODO Сделать графику на кнопке соответственно игроку, сделавшему ход
		switch (Game.getModel().getCell(point).getWhich()) {
			case HUMAN1 -> btnCell.setText("X1");
			case HUMAN2 -> btnCell.setText("X2");
			case AI -> btnCell.setText("AI");
			case NEUTRAL -> btnCell.setText("  ");
		}
		btnCell.setEnabled(false);
		// 	Проверить - текущий игрок победил или нет. Если победил - заблокировать все дальнейшие ходы и дать сообщение о победе.
		if(Game.gamer().wins()) {
			btnCell.getParent().setEnabled(false);
			Game.getUi().updateFooter(String.format("%s победил (ходов подряд: %d)!", who, GameOptions.winSequence));
			Game.getUi().lockButtons();
			return;
		}
		// 	Если игрок не победил - остались ли ходы. Если ходов не осталось - сообщение "ничья, закончились ходы"
		if(!Game.getModel().hasTurn()) {
			Game.getUi().updateFooter("Ничья, ходы закончились");
			return;
		}
		//	Если ходы остались - передаем ход следующему игроку (по кругу)
		Game.nextGamer();
		switch (Game.gamer().getRole()) {
			case HUMAN1 -> who = "Игрок-1";
			case HUMAN2 -> who = "Игрок-2";
			case AI -> who = "Искусственный интеллект";
		}
		Game.getUi().updateFooter(who + ", ваш ход");
		if (Game.gamer().getRole() == Which.AI) {
			Game.onPress(null, null);	// Ход искусственного интеллекта сделать автоматически
		}
	}

	public static void onOptions(GameUI gameUI) {
		setModel(new Square(GameOptions.dimension, GameOptions.winSequence));
		setUi(gameUI);
		setGamers(new Gamer[]{
				new Gamer(Game.getModel(), Which.HUMAN1),
				new Gamer(Game.getModel(), GameOptions.mode == 1 ? Which.HUMAN2 : Which.AI)
		});
		getUi().updateGameField();
		Game.getUi().updateFooter("Игрок-1, ваш ход");
	}

	public static void init() {
		//
	}
}
