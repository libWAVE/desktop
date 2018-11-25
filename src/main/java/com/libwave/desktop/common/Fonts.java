package com.libwave.desktop.common;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Fonts {

	private static final Log log = LogFactory.getLog(Fonts.class);

	public static Font getMainFont() {
		Font font = null;
		try {
			font = Font.createFont(Font.PLAIN, Fonts.class.getResourceAsStream("/font/DejaVuSansMono.ttf"));
		} catch (FontFormatException e) {
			log.error("Error: ", e);
		} catch (IOException e) {
			log.error("Error: ", e);
		}
		font = font.deriveFont(19.0f);
		return font;
	}

}
