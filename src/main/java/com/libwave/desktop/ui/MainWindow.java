package com.libwave.desktop.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.libwave.desktop.DesktopClient;
import com.libwave.desktop.service.AddTracksFolderService;
import com.libwave.desktop.service.TrackService;

@Component
public class MainWindow implements InitializingBean, ActionListener {

	private static final String ADD_FOLDER = "ADD_FOLDER";

	private Logger log = LoggerFactory.getLogger(this.getClass());

	private JFrame frame;

	@Autowired
	private TrackService trackService;

	@Autowired
	private AddTracksFolderService addTracksFolderService;

	@Override
	public void afterPropertiesSet() throws Exception {

		frame = new JFrame("libWAVE (" + trackService.count() + " tracks)");
		frame.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent arg0) {
				DesktopClient.close();
			}

		});
		frame.setLayout(new BorderLayout());
		frame.setIconImage(new ImageIcon(this.getClass().getResource("/images/logo.png")).getImage());

		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setSize(600, 800);
		frame.setLocationRelativeTo(null);

		setupMenu();

		frame.setVisible(true);

	}

	private void setupMenu() {

		JMenuBar mb = new JMenuBar();
		{
			JMenu menu = new JMenu("Tracks");

			JMenuItem addFolder = new JMenuItem("Add a folder");
			addFolder.setMnemonic('F');
			addFolder.setActionCommand(ADD_FOLDER);
			addFolder.addActionListener(this);
			addFolder.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, ActionEvent.CTRL_MASK));
			menu.add(addFolder);

			mb.add(menu);

		}

		frame.setJMenuBar(mb);

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (ADD_FOLDER.equals(e.getActionCommand())) {
			addTracksFolderService.add(frame);
		}

	}

}
