package ru.bulash.models;

import java.awt.*;

public class Cell {
	private Point point;	// Координаты клетки
	private Which which = Which.NEUTRAL;	// Кто сделал ход в клетке
	private int menace = 0;	// Вес угрозы со стороны людей (для AI)

	public Point getPoint() {
		return point;
	}

	public Cell(Point point) {
		this.point = point;
	}

	public Which getWhich() {
		return which;
	}

	public int getMenace() {
		return menace;
	}

	public void setMenace(int menace) {
		this.menace = menace;
	}

	public void setWhich(Which which) {
		this.which = which;
	}

	public Point getNeighbor(Direction direction, int dimension) {
		Point dot = (Point) this.point.clone();

		switch (direction) {
			case WEST -> {
				dot.x--;
				if (dot.x < 0) return null;
			}
			case EAST -> {
				dot.x++;
				if (dot.x >= dimension) return null;
			}
			case NORTH -> {
				dot.y--;
				if (dot.y < 0) return null;
			}
			case SOUTH -> {
				dot.y++;
				if (dot.y >= dimension) return null;
			}
			case NORTHWEST -> {
				dot.x--;
				dot.y--;
				if (dot.x < 0 || dot.y < 0) return null;
			}
			case NORTHEAST -> {
				dot.x++;
				dot.y--;
				if (dot.x >= dimension || dot.y < 0) return null;
			}
			case SOUTHWEST -> {
				dot.x--;
				dot.y++;
				if (dot.x < 0 || dot.y >= dimension) return null;
			}
			case SOUTHEAST -> {
				dot.x++;
				dot.y++;
				if (dot.x >= dimension || dot.y >= dimension) return null;
			}
		}
		return dot;
	}
}
