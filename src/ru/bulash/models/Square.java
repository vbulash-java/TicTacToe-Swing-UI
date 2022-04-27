package ru.bulash.models;

import ru.bulash.Game;

import java.awt.*;

public class Square {
	private int dimension;
	private int winSequence;
	private Cell[] cells;
	private Game game;

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public Square(int dimension, int winSequence) {
		this.dimension = dimension;
		this.winSequence = winSequence;
		this.cells = new Cell[dimension * dimension];

		for (int row = 0, direct = 0; row < dimension; row++) {
			for (int column = 0; column < dimension; column++, direct++) {
				this.cells[direct] = new Cell(new Point(column, row));
			}
		}
	}

	public int getDimension() {
		return dimension;
	}

	public Cell getCell(Point point) {
		return this.cells[point.x + point.y * this.dimension];
	}

	/**
	 * Наличие свободных ходов на поле
	 *
	 * @return true есть возможность хода
	 */
	public boolean hasTurn() {
		for (Cell cell : this.cells) {
			if (cell.getWhich() == Which.NEUTRAL) return true;
		}
		return false;
	}

	private boolean checkWinDirection(Cell start, Which actor, Direction direction) {
		Cell checked = start;
		int seq = 0;
		do {
			seq++;
			if (seq == this.winSequence) {
				return true;
			}
			Point dot = checked.getNeighbor(direction, this.getDimension());
			if (dot == null) return false;
			checked = this.cells[dot.x + dot.y * this.dimension];
		} while (checked.getWhich() == actor);
		return false;
	}

	public boolean wins(Which actor) {
		for (Cell cell : this.cells) {
			// Берем клетку за отправную точку и сканируем ее соседей по вектору на предмет выигрышной последовательности
			if (cell.getWhich() == actor) {
				if (checkWinDirection(cell, actor, Direction.NORTHEAST)) return true;
				if (checkWinDirection(cell, actor, Direction.EAST)) return true;
				if (checkWinDirection(cell, actor, Direction.SOUTHEAST)) return true;
				if (checkWinDirection(cell, actor, Direction.SOUTH)) return true;
			}
		}
		return false;
	}

	public void dropMenace() {
		for (Cell cell : this.cells) {
			cell.setMenace(0);
		}
	}

	/**
	 * Взвешиваем угрозы от хода человека по определенному вектору
	 *
	 * @param start     Клетка хода человека
	 * @param direction Вектор анализа
	 */
	private void weightMenaceDirection(Cell start, Direction direction) {
		int menace = 1;
		boolean started = true;
		Cell checked = start;
		do {
			Point dot = checked.getNeighbor(direction, this.getDimension());
			if (dot == null) return;    // Оценку угроз завершаем вместе с вектором
			checked = this.cells[dot.x + dot.y * this.dimension];
			if (checked.getWhich() == Which.HUMAN1 && started) {
				menace++;    // Увеличиваем угрозу от человека с ростом непрерывной цепочки
			} else if (checked.getWhich() == Which.NEUTRAL) {
				started = false;
				checked.setMenace(checked.getMenace() + menace--); // Уменьшаем угрозу с удалением от непрерывного вектора ходов
			} else return;
		} while (menace != 0);
	}

	/**
	 * Взвешиваем угрозы от человека по разным векторам
	 *
	 */
	public void weightMenace() {
		for (Cell cell: this.cells) {
			if (cell.getWhich() != Which.HUMAN1) {
				continue;
			}
			Direction[] directions = Direction.values();
			for (Direction direction : directions) {
				weightMenaceDirection(cell, direction);
			}
		}
	}

	/**
	 * Устранить угрозу человека - сделать блокирующий ход AI
	 * Требуется предварительная оценка угроз в свете последнего хода человека (weightMenace)
	 */
	public Point beatMenace() {
		// Поиск максимальной угрозы от хода человека
		int max = 0;
		Cell cellMax = null;
		for (Cell cell: this.cells) {
			if (cellMax == null) {
				cellMax = cell;
			}
			if (max < cell.getMenace()) {
				max = cell.getMenace();
				cellMax = cell;
			}
		}
		// Устранение максимальной угрозы от хода человека
		assert cellMax != null;
		cellMax.setWhich(Which.AI);	// Сделали ход в ячейку с максимальной угрозой от человека

		return cellMax.getPoint();
	}
}
