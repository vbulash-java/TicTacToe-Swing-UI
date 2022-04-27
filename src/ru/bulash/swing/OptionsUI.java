package ru.bulash.swing;

import ru.bulash.Game;
import ru.bulash.GameOptions;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class OptionsUI extends JDialog {
	private GameUI gameUI;
	private final JPanel contentPane = new JPanel();
	private final JPanel canvas = new JPanel();
	//private JTextField textName1;
	//private JTextField textName2;
	private JSlider sliderDimension;
	private JLabel labelDimension;
	private JSlider sliderWinSequence;
	private JLabel labelWinSequence;

	private final JButton btnSave = new JButton();
	private final JButton btnClose = new JButton();

	public OptionsUI(GameUI gameUI) {
		this.gameUI = gameUI;
		setTitle("Настройки игры в Крестики-нолики");
		setContentPane(contentPane);
		setModal(true);
		getRootPane().setDefaultButton(btnSave);

		btnSave.addActionListener(event -> onSave());

		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				onClose();
			}
		});
	}

	public void init() {
		contentPane.setLayout(new BorderLayout());
		contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));

		//GridLayout layout = new GridLayout(5, 2);
		GridLayout layout = new GridLayout(3, 2);
		layout.setVgap(20);
		canvas.setLayout(layout);

		canvas.add(new JLabel("Режим игры:"));

		JRadioButton btnHumanAI = new JRadioButton("Человек против искусственного интеллекта");
		btnHumanAI.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GameOptions.mode = 0;
				//textName2.setEnabled(false);
			}
		});
		JRadioButton btnHumanHuman = new JRadioButton("Человек против человека");
		btnHumanHuman.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GameOptions.mode = 1;
				//textName2.setEnabled(true);
			}
		});
		ButtonGroup groupMode = new ButtonGroup();
		groupMode.add(btnHumanAI);
		groupMode.add(btnHumanHuman);

		JPanel radioPanel = new JPanel(new GridLayout(0, 1));
		radioPanel.add(btnHumanAI);
		radioPanel.add(btnHumanHuman);
		canvas.add(radioPanel);

		/*
		canvas.add(new JLabel("Игрок-1, ваше имя:"));
		textName1 = new JTextField(GameOptions.name1);
		textName1.setText(GameOptions.name1);
		textName1.setMaximumSize(new Dimension(-1, 50));
		canvas.add(textName1);
		canvas.add(new JLabel("Игрок-2, ваше имя:"));
		textName2 = new JTextField(GameOptions.name1);
		textName1.setText(GameOptions.name2);
		textName2.setMaximumSize(new Dimension(-1, 50));
		textName2.setEnabled(false);
		GameOptions.name2 = textName2.getText();
		canvas.add(textName2);
		 */

		switch (GameOptions.mode) {
			case 0 -> btnHumanAI.setSelected(true);
			case 1 -> btnHumanHuman.setSelected(true);
		}

		labelDimension = new JLabel("<html><p>Размер игрового поля:</p></html>");
		canvas.add(labelDimension);
		sliderDimension = new JSlider(JSlider.HORIZONTAL, 3, 20, 3);
		sliderDimension.setMajorTickSpacing(5);
		sliderDimension.setMinorTickSpacing(1);
		sliderDimension.setPaintTicks(true);
		sliderDimension.setPaintLabels(true);
		sliderDimension.setValue(GameOptions.dimension);
		labelDimension.setText(String.format("<html><p>Размер игрового поля (%d)</p></html>", sliderDimension.getValue()));
		sliderDimension.addChangeListener(event -> {
			JSlider slider = (JSlider) event.getSource();
			labelDimension.setText(String.format("<html><p>Размер игрового поля (%d)</p></html>", slider.getValue()));
			sliderWinSequence.setMaximum(slider.getValue());
		});
		canvas.add(sliderDimension);

		labelWinSequence = new JLabel("<html><p>Длина выигрышной последовательности:</p></html>");
		canvas.add(labelWinSequence);
		sliderWinSequence = new JSlider(JSlider.HORIZONTAL);
		sliderWinSequence.setMinimum(sliderDimension.getMinimum());
		sliderWinSequence.setMaximum(sliderDimension.getValue());
		sliderWinSequence.setMajorTickSpacing(5);
		sliderWinSequence.setMinorTickSpacing(1);
		sliderWinSequence.setPaintTicks(true);
		sliderWinSequence.setPaintLabels(true);
		sliderWinSequence.setValue(GameOptions.winSequence);
		labelWinSequence.setText(String.format("<html><p>Длина выигрышной последовательности (%d):</p></html>", sliderWinSequence.getValue()));
		sliderWinSequence.addChangeListener(event -> {
			JSlider slider = (JSlider) event.getSource();
			labelWinSequence.setText(String.format("<html><p>Длина выигрышной последовательности (%d):</p></html>", slider.getValue()));
		});
		canvas.add(sliderWinSequence);

		contentPane.add(canvas, BorderLayout.CENTER);

		JPanel footer = new JPanel();
		footer.setLayout(new FlowLayout(FlowLayout.RIGHT));

		JPanel buttons = new JPanel();
		buttons.setLayout(new FlowLayout());
		btnSave.setText("Сохранить и играть");
		btnSave.addActionListener(e -> onSave());
		buttons.add(btnSave);
		btnClose.setText("Закрыть");
		btnClose.addActionListener(e -> onClose());
		buttons.add(btnClose);

		footer.add(buttons);
		contentPane.add(footer, BorderLayout.SOUTH);
	}

	private void onSave() {
		//GameOptions.name1 = textName1.getText();
		//GameOptions.name2 = textName2.getText();
		GameOptions.dimension = sliderDimension.getValue();
		GameOptions.winSequence = sliderWinSequence.getValue();

		Game.onOptions(this.gameUI);
		dispose();
	}

	private void onClose() {
		// ...
		dispose();
	}
}
