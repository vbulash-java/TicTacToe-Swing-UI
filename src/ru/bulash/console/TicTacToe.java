package ru.bulash.console;

import ru.bulash.GameOptions;
import ru.bulash.Gamer;
import ru.bulash.models.Cell;
import ru.bulash.models.Square;
import ru.bulash.models.Which;

import java.awt.*;
import java.util.Scanner;

public class TicTacToe {
	private int mode;	// 0 = human vs ai, 1 = human vs human
	private Square gameField;
	private Gamer[] gamers;

	protected final Scanner scanner = new Scanner(System.in);

	private boolean validateDimension(int dimension) {
		return dimension >= 3 && dimension <= 15;
	}

	private boolean validateWinSequence(int winSequence) {
		return validateDimension(winSequence);
	}

	private boolean validateGameMode(int mode) {
		return switch (mode) {
			case 0 -> true;
			default -> mode == 1;
		};
	}

	public void init() {
		System.out.println("Игра \"Крестики-нолики\"\n");
		System.out.println("Сначала давайте настроим игру:");

		int newMode;
		do {
			System.out.print(
					"""
							Выберите режим игры:
							0 = режим игры "Игрок 1 против Искусственного интеллекта"
							1 = режим игры "Игрок 1 против Игрока 2"
							Ваш выбор: 
							"""
			);
			newMode = this.scanner.nextInt();
		} while(!this.validateGameMode(newMode));
		GameOptions.mode = newMode;

		int newDimension;
		do {
			System.out.print("Укажите размер игрового поля (квадратного) [3 .. 15]: ");
			newDimension = this.scanner.nextInt();
		} while (!this.validateDimension(newDimension));
		GameOptions.dimension = newDimension;

		int newWinSequence;
		do {
			System.out.print("Укажите размер выигрышной последовательности: ");
			newWinSequence = this.scanner.nextInt();
		} while (!this.validateWinSequence(newWinSequence));
		GameOptions.winSequence = newWinSequence;

		System.out.print("Игрок-1, как вас зовут? ");
		GameOptions.name1 = this.scanner.next();

		if (GameOptions.mode == 1) {
			System.out.print("Игрок-2, как вас зовут? ");
			GameOptions.name2 = this.scanner.next();
		}

		this.gameField = new Square(GameOptions.dimension, GameOptions.winSequence);
		this.gamers = new Gamer[] {
				new Gamer(this.gameField, Which.HUMAN1),
				new Gamer(this.gameField, GameOptions.mode == 1 ? Which.HUMAN2 : Which.AI)
		};

		System.out.println("\nИгра настроена, давайте играть!\n");
	}

	public void run() {
		this.printGameField();

		boolean noturn, win;
		int action = 0;
		Gamer gamer;
		do {
			gamer = this.gamers[action++ % 2];
			switch (gamer.getRole()) {
				case HUMAN1, HUMAN2 -> this.humanTurn(gamer);
				case AI -> this.AITurn(gamer);
			}
			this.printGameField();

			noturn = !this.gameField.hasTurn();
			win = gamer.wins();
		} while (!noturn && !win);

		if (noturn) {
			System.out.println("\nХодов больше нет");
		} else if (gamer.getRole() == Which.HUMAN1) {
			System.out.printf("\nИгрок-1 %s, вы победили!\n", GameOptions.name1);
		} else if (gamer.getRole() == Which.HUMAN2) {
			System.out.printf("\nИгрок-2 %s, вы победили!\n", GameOptions.name2);
		} else if (gamer.getRole() == Which.AI) {
			System.out.println("\nПобедил искусственный интеллект!");
		}
	}

	protected void printGameField() {
		System.out.print("[  * ]");
		// Верхняя строка заголовков
		for (int column = 0; column < GameOptions.dimension; column++) {
			System.out.printf("[ %2d ]", column + 1);
		}
		System.out.println();
		for (int row = 0; row < GameOptions.dimension; row++) {
			for (int column = 0; column < GameOptions.dimension; column++) {
				if (column == 0) {
					System.out.printf("[ %2d ]", row + 1);
				}
				String print = "  ";
				switch(this.gameField.getCell(new Point(column, row)).getWhich()) {
					case HUMAN1 -> print = "X1";
					case HUMAN2 -> print = "X2";
					case AI -> print = "AI";
				}
				System.out.printf("[ %2s ]", print);
			}
			System.out.println();
		}
	}

	/**
	 * Ход человека
	 */
	protected void humanTurn(Gamer gamer) {
		if (gamer.getRole() == Which.HUMAN1) {
			System.out.printf("\nВаш ход, игрок-1 %s\n", GameOptions.name1);
		} else if (gamer.getRole() == Which.HUMAN2) {
			System.out.printf("\nВаш ход, игрок-2 %s\n", GameOptions.name2);
		}

		boolean valid;
		int row, column;
		Cell cell;
		do {
			System.out.print("Введите 2 числа: координату строки (вертикальная) и координату столбца (горизонтальная): ");
			row = scanner.nextInt() - 1;
			column = scanner.nextInt() - 1;
			cell = this.gameField.getCell(new Point(column, row));
			valid = (row >= 0) && (row < GameOptions.dimension);
			valid = valid && (cell.getWhich() == Which.NEUTRAL);
		} while (!valid);

		gamer.turn(cell);
	}

	/**
	 * Ход компьютера
	 */
	protected void AITurn(Gamer gamer) {
		System.out.println("\nОтветный ход искусственного интеллекта");
		gamer.turn((Point) null);
	}
}
