package com.libwave.desktop;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.commons.lang3.SystemUtils;
import org.pushingpixels.substance.api.skin.SubstanceBusinessBlackSteelLookAndFeel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.libwave.desktop.common.Fonts;

public class DesktopClient {

	private static Logger log = LoggerFactory.getLogger(DesktopClient.class);

	private static ClassPathXmlApplicationContext ctx;

	public static void main(String[] args) throws UnsupportedLookAndFeelException {

		if (SystemUtils.IS_OS_WINDOWS_10 == false && SystemUtils.IS_OS_WINDOWS_2012 == false) {
			JDialog.setDefaultLookAndFeelDecorated(true);
			JFrame.setDefaultLookAndFeelDecorated(true);
		}

		UIManager.setLookAndFeel(new SubstanceBusinessBlackSteelLookAndFeel());

		SwingUtilities.invokeLater(() -> {
			setUIFont(new javax.swing.plaf.FontUIResource(Fonts.getMainFont()));
			ctx = new ClassPathXmlApplicationContext("classpath:/spring-context.xml");
			ctx.start();
		});

	}

	public static void close() {

		try {
			ctx.stop();
			ctx.close();
		} catch (Exception e) {
			log.error("Error while closing context: ", e);
		}

		System.exit(0);

	}

	private static void setUIFont(javax.swing.plaf.FontUIResource f) {
		java.util.Enumeration keys = UIManager.getDefaults().keys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value != null && value instanceof javax.swing.plaf.FontUIResource)
				UIManager.put(key, f);
		}
	}

}
