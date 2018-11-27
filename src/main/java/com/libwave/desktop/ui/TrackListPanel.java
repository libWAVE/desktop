
package com.libwave.desktop.ui;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.libwave.desktop.domain.Track;
import com.libwave.desktop.service.AudioPlayer;
import com.libwave.desktop.service.TrackService;

@Component
public class TrackListPanel extends JPanel implements InitializingBean, DisposableBean {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	private JTable table;

	private DefaultTableModel tableModel;

	@Autowired
	private TrackService trackService;

	private AudioPlayer ap = new AudioPlayer();

	private long currentlyPlayedMusicPointer;

	@Override
	public void afterPropertiesSet() throws Exception {
		
		ap.init();

		tableModel = new DefaultTableModel(new String[] { "Filename" }, 0) {

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		table = new JTable(tableModel);

		table.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent mouseEvent) {

				JTable table = (JTable) mouseEvent.getSource();

				Point point = mouseEvent.getPoint();

				int row = table.rowAtPoint(point);

				if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {

					Track t = (Track) table.getValueAt(table.getSelectedRow(), 0);

					if (t != null) {

						log.debug("Play: " + t);

						if (currentlyPlayedMusicPointer > 0) {
							ap.stopFadeOut(400);
							ap.closeMusic(currentlyPlayedMusicPointer);
							currentlyPlayedMusicPointer = 0;
						}

						currentlyPlayedMusicPointer = ap.loadMusic(t.getPath());
						
						if (currentlyPlayedMusicPointer != 0) {
							ap.playMusicFadeIn(currentlyPlayedMusicPointer, 1000);
						}
					}

				}
			}
		});

		this.setLayout(new BorderLayout());

		updateTracks();

		this.add(new JScrollPane(table));

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

	@Override
	public void destroy() throws Exception {
		ap.shutdown();
	}

}
