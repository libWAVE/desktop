package com.libwave.desktop.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.SwingUtilities;

import org.springframework.beans.factory.annotation.Autowired;

public class MainWindowIcon {

	static Color tr = new Color(255, 255, 255, 0);
	static Color waveColor = new Color(0, 255, 0);

	public static void draw(Integer[] data) {

		BufferedImage bim = new BufferedImage(96, 96, BufferedImage.TYPE_4BYTE_ABGR);

		Graphics2D g = (Graphics2D) bim.getGraphics();

		g.setBackground(tr);
		g.clearRect(0, 0, bim.getWidth(), bim.getHeight());

		g.setColor(waveColor);
		for (int i = 0; i < bim.getWidth(); i += 1) {

			int v = Math.abs(data[i] / bim.getWidth() / 32000 * bim.getHeight() / 255);

			g.drawLine(i, bim.getHeight(), i, bim.getHeight() - v);

			// System.out.print(v+" ");
		}

		g.dispose();

		SwingUtilities.invokeLater(() -> { 
			
			MainWindow.setIcon(bim);
			
		});
		
	}

}
