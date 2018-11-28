package com.libwave.desktop.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class MainWindowIcon {

	private static BufferedImage bim = new BufferedImage(96, 96, BufferedImage.TYPE_4BYTE_ABGR);

	static {
		Graphics g = bim.getGraphics();

		g.setColor(Color.green);
		g.fillRect(0, 0, bim.getWidth(), bim.getHeight());

		g.dispose();
	}

	public static BufferedImage getIcon() {
		return bim;
	}
	
	

	static Color tr = new Color(255, 255, 255, 0);

	public static void draw(Integer[] data) {

		Graphics2D g = (Graphics2D) bim.getGraphics();

		g.setBackground(tr);
		g.clearRect(0, 0, bim.getWidth(), bim.getHeight());

		g.setColor(Color.BLACK);
		for (int i = 0; i < bim.getWidth(); i += 1) {

			int v = Math.abs(data[i] / bim.getWidth() / 32000 * bim.getHeight() / 255);

			g.drawLine(i, bim.getHeight(), i, bim.getHeight() - v);

			// System.out.print(v+" ");
		}

		g.dispose();

		MainWindow.setIcon(bim);
	}

}
