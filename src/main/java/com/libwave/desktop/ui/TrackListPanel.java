
package com.libwave.desktop.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.libwave.desktop.domain.Track;
import com.libwave.desktop.service.MusicPlayer;
import com.libwave.desktop.service.TrackService;

@SuppressWarnings("serial")
@Component
public class TrackListPanel extends JPanel implements InitializingBean {

	private static final String PLAY_MUSIC = "PLAY_MUSIC";

	private Logger log = LoggerFactory.getLogger(this.getClass());

	private JTable table;

	private DefaultTableModel tableModel;

	@Autowired
	private TrackService trackService;

	@Autowired
	private MusicPlayer musicPlayer;

	@Override
	public void afterPropertiesSet() throws Exception {

		tableModel = new DefaultTableModel(new String[] { "Filename" }, 0) {

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		table = new JTable(tableModel);

		// 'Enter key' event
		KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
		table.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(enter, PLAY_MUSIC);
		table.getActionMap().put(PLAY_MUSIC, new PlayTrackAction());

		table.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent mouseEvent) {

				JTable table = (JTable) mouseEvent.getSource();

				if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
					play();
				}
			}

		});

		this.setLayout(new BorderLayout());

		updateTracks();

		this.add(new JScrollPane(table));

	}

	public void play() {
		new PlayTrackAction().actionPerformed(null);
	}

	public void addTrack(Track t) {
		tableModel.addRow(new Track[] { t });
	}

	public void updateTracks() {

		if (tableModel.getRowCount() > 0) {
			for (int i = tableModel.getRowCount() - 1; i > -1; i--) {
				tableModel.removeRow(i);
			}
		}

		Iterable<Track> tracks = trackService.findAll();

		for (Track t : tracks) {
			tableModel.addRow(new Track[] { t });
		}

	}

	public Track getSelectedTrack() {
		Track track = null;
		if (table.getSelectedRow() >= 0) {
			track = (Track) table.getValueAt(table.getSelectedRow(), 0);
		}
		return track;
	}

	private class PlayTrackAction extends AbstractAction {

		@Override
		public void actionPerformed(ActionEvent e) {
			musicPlayer.play(getSelectedTrack());
		}
	}

}
