package ru.bulash;

import ru.bulash.models.Cell;
import ru.bulash.models.Square;
import ru.bulash.models.Which;

import java.awt.*;

public class Gamer {
	private Square gameField = null;
	private Which role;

	public Gamer(Square gameField, Which role) {
		this.gameField = gameField;
		this.role = role;
	}

	public Which getRole() {
		return this.role;
	}

	/**
	 * Оценка выигрыша игрока
	 *
	 * @return Признак победы
	 */
	public boolean wins() {
		return this.gameField.wins(this.getRole());
	}

	public void turn(Cell cell) {
		this.turn(cell.getPoint());
	}

	public Point turn(Point point) {
		Point dot = point;
		switch(this.getRole()) {
			case HUMAN1 -> {
				Cell cell = this.gameField.getCell(point);
				if(cell.getWhich() == Which.NEUTRAL) {
					cell.setWhich(Which.HUMAN1);
				}
			}
			case HUMAN2 -> {
				Cell cell = this.gameField.getCell(point);
				if(cell.getWhich() == Which.NEUTRAL) {
					cell.setWhich(Which.HUMAN2);
				}
			}
			case AI -> {
				this.gameField.dropMenace();
				this.gameField.weightMenace();
				dot = this.gameField.beatMenace();
			}
		}
		return dot;
	}
}
