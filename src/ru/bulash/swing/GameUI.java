package ru.bulash.swing;

import ru.bulash.Game;
import ru.bulash.GameOptions;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameUI extends JFrame {
	public JPanel getGameField() {
		return gameField;
	}
	public JPanel footer;

	public void setGameField(JPanel gameField) {
		this.gameField = gameField;
	}

	private JPanel gameField;

	public GameUI() {
		super("Игра Крестики-нолики");
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(super.EXIT_ON_CLOSE);
	}

	public void initMenu() {
		Container contentPane = this.getContentPane();
		JMenuBar menu = new JMenuBar();

		JMenu first = new JMenu("Игра");
		JMenuItem menuItem = new JMenuItem("Настроить и играть");
		menuItem.addActionListener(e -> {
			OptionsUI dialog = new OptionsUI(this);
			dialog.init();
			//dialog.setPreferredSize(new Dimension(500, 280));
			dialog.setLocationRelativeTo(null);
			dialog.setResizable(false);
			dialog.pack();
			dialog.setVisible(true);
		});
		first.add(menuItem);

		first.addSeparator();

		menuItem = new JMenuItem("Выход");
		menuItem.addActionListener(e -> System.exit(0));
		first.add(menuItem);

		menu.add(first);
		contentPane.add(menu, BorderLayout.NORTH);
	}

	public void updateFooter(String text) {
		Container contentPane = this.getContentPane();
		if (this.footer != null)
			contentPane.remove(this.footer);
		FlowLayout layout = new FlowLayout();
		layout.setAlignment(FlowLayout.LEFT);
		this.footer = new JPanel(layout);
		this.footer.setBorder(new EmptyBorder(10, 10, 10, 10));

		JLabel footerText = new JLabel(text);
		footer.add(footerText, layout);
		contentPane.add(this.footer, BorderLayout.SOUTH);
	}

	public void init() {
		Container contentPane = this.getContentPane();
		contentPane.setPreferredSize(new Dimension(500, 500));
		contentPane.setMinimumSize(new Dimension(200, 200));
		contentPane.setLayout(new BorderLayout());

		this.initMenu();
		this.pack();
	}

	public void updateGameField() {
		JPanel temp = new JPanel();
		temp.setLayout(new GridBagLayout());
		temp.setBorder(new EmptyBorder(20, 20, 20, 20));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		JButton btnCell;
		for (int row = 0; row < GameOptions.dimension; row++) {
			for (int column = 0; column < GameOptions.dimension; column++) {
				btnCell = new JButton("");
				gbc.gridx = column;
				gbc.gridy = row;
				btnCell.setPreferredSize(new Dimension(30, 30));
				Point point = new Point(column, row);
				btnCell.putClientProperty("cell", point);
				btnCell.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						JButton temp = (JButton) e.getSource();
						Point point = (Point) temp.getClientProperty("cell");
						Game.onPress(point, temp);
					}
				});
				temp.add(btnCell, gbc);
			}
		}

		Container contentPane = this.getContentPane();
		if (this.getGameField() != null) {
			contentPane.remove(this.getGameField());
		}
		contentPane.add(temp, BorderLayout.CENTER);
		this.setGameField(temp);

		this.pack();
		this.setVisible(true);
		this.updateFooter("Игрок-1, ваш ход!");
	}

	public JButton getButton(Point point) {
		for (Component component : this.getGameField().getComponents()) {
			JButton button = (JButton) component;
			Point cellPoint = (Point) button.getClientProperty("cell");
			if (cellPoint.equals(point)) return button;
		}
		return null;
	}

	public void lockButtons() {
		for (Component component : this.getGameField().getComponents()) {
			JButton button = (JButton) component;
			button.setEnabled(false);
		}
	}

	public void run() {
		this.setVisible(true);
	}
}
