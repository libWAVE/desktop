package com.libwave.desktop.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.libwave.desktop.DesktopClient;
import com.libwave.desktop.common.Alert;
import com.libwave.desktop.common.Icons;
import com.libwave.desktop.service.AddTrackService;
import com.libwave.desktop.service.AddTracksFolderService;
import com.libwave.desktop.service.TrackService;

@Component
public class MainWindow implements InitializingBean, ActionListener {

	private static final String EXIT = "EXIT";

	private static final String REMOVE_ALL = "REMOVE_ALL";

	private static final String ADD_FOLDER = "ADD_FOLDER";

	private static final String ADD_FILE = "ADD_FILE";

	private Logger log = LoggerFactory.getLogger(this.getClass());

	private static JFrame frame;

	@Autowired
	private TrackService trackService;

	@Autowired
	private StatusBar statusBar;

	@Autowired
	private TrackListPanel trackListPanel;

	@Autowired
	private AddTracksFolderService addTracksFolderService;

	@Autowired
	private AddTrackService addTrackService;

	@Override
	public void afterPropertiesSet() throws Exception {

		frame = new JFrame();
		frame.setMinimumSize(new Dimension(340, 340));
		frame.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent arg0) {
				DesktopClient.close();
			}

		});
		frame.setLayout(new BorderLayout());

		frame.setIconImages(Icons.getAppIcons());

		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setSize(600, 800);
		frame.setLocationRelativeTo(null);

		frame.add(trackListPanel, BorderLayout.CENTER);
		frame.add(statusBar, BorderLayout.PAGE_END);

		updateTitle();

		setupMenu();

		frame.setVisible(true);

	}

	public void updateTitle() {
		frame.setTitle("libWAVE (" + trackService.count() + " tracks)");
	}

	private void setupMenu() {

		JMenuBar mb = new JMenuBar();

		{
			JMenu menu = new JMenu("File");
			mb.add(menu);

			JMenuItem addFile = new JMenuItem("Add files");
			addFile.setMnemonic('A');
			addFile.setActionCommand(ADD_FILE);
			addFile.setIcon(Icons.getIcon("add.png"));
			addFile.addActionListener(this);
			addFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
			menu.add(addFile);

			JMenuItem addFolder = new JMenuItem("Add a folder");
			addFolder.setMnemonic('F');
			addFolder.setIcon(Icons.getIcon("folder_add.png"));
			addFolder.setActionCommand(ADD_FOLDER);
			addFolder.addActionListener(this);
			addFolder.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, ActionEvent.CTRL_MASK));
			menu.add(addFolder);

			menu.addSeparator();

			JMenuItem removeTracks = new JMenuItem("Remove all tracks");
			removeTracks.setActionCommand(REMOVE_ALL);
			removeTracks.setIcon(Icons.getIcon("delete.png"));
			removeTracks.addActionListener(this);
			menu.add(removeTracks);

			menu.addSeparator();

			JMenuItem exit = new JMenuItem("Exit");
			exit.setMnemonic('x');
			exit.setActionCommand(EXIT);
			exit.addActionListener(this);
			exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
			menu.add(exit);

		}

		/*
		 * {
		 * 
		 * JMenu menu = new JMenu("Player"); mb.add(menu);
		 * 
		 * { JMenuItem item = new JMenuItem("Play", Icons.getIcon("play_24.png"));
		 * item.setMnemonic('P');
		 * 
		 * item.setActionCommand("PLAY"); item.addActionListener(this); menu.add(item);
		 * } menu.addSeparator(); { JMenuItem item = new JMenuItem("Pause",
		 * Icons.getIcon("pause_24.png")); item.setMnemonic('A');
		 * item.setActionCommand("PAUSE"); item.addActionListener(this); menu.add(item);
		 * }
		 * 
		 * { JMenuItem item = new JMenuItem("Resume"); item.setMnemonic('R');
		 * item.setActionCommand("RESUME"); item.addActionListener(this);
		 * menu.add(item); }
		 * 
		 * menu.addSeparator(); { JMenuItem item = new JMenuItem("Stop",
		 * Icons.getIcon("stop_24.png")); item.setMnemonic('S');
		 * item.setActionCommand("STOP"); item.addActionListener(this); menu.add(item);
		 * } }
		 */
		frame.setJMenuBar(mb);

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (ADD_FOLDER.equals(e.getActionCommand())) {
			addTracksFolderService.add(frame);
		} else if (REMOVE_ALL.equals(e.getActionCommand())) {
			if (Alert.confirm("Are you sure you want to remove all tracks?")) {
				trackService.removeAll();
			}
		} else if (ADD_FILE.equals(e.getActionCommand())) {
			addTrackService.add(frame);
		} else if (EXIT.equals(e.getActionCommand())) {
			DesktopClient.close();
		}

	}

	public static void setIcon(BufferedImage bim) {
		if (frame != null && bim != null) {
			frame.setIconImage(bim);
		}
	}

}
