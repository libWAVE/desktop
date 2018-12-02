package com.libwave.desktop.ui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.libwave.desktop.common.Icons;
import com.libwave.desktop.domain.Track;
import com.libwave.desktop.service.MusicPlayer;
import com.libwave.desktop.service.MusicPlayer.Status;

@SuppressWarnings("serial")
@Component
public class PlayerPanel extends JPanel implements InitializingBean, ActionListener {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	private static final String STOP = "STOP";
	private static final String PLAY_OR_PAUSE = "PLAY_OR_PAUSE";

	@Autowired
	private MusicPlayer musicPlayer;

	@Autowired
	private TrackListPanel trackListPanel;

	private MusicPlayer.Status status = MusicPlayer.Status.STOPPED;

	private Icon iconPlay = Icons.getIcon("play_24.png");

	private Icon iconStop = Icons.getIcon("stop_24.png");

	private Icon iconPaused = Icons.getIcon("pause_24.png");

	private JButton btnPlay;

	private JButton btnStop;

	@Scheduled(fixedDelay = 500)
	public void update() {

//		log.debug("Player status: " + musicPlayer.getStatus());

		SwingUtilities.invokeLater(() -> {

			if (false == musicPlayer.getStatus().equals(status)) {

				this.status = musicPlayer.getStatus();

				if (status == Status.PLAYING) {

					btnPlay.setIcon(iconPaused);
					btnStop.setEnabled(true);

				} else if (status == Status.PAUSED) { 

					btnPlay.setIcon(iconPlay);

				} else if (status == Status.STOPPED) {

					btnPlay.setIcon(iconPlay);
					btnStop.setEnabled(false);

				}

			}

		});

	}

	@Override
	public void afterPropertiesSet() throws Exception {

		this.setLayout(new FlowLayout());

		this.btnPlay = new JButton(this.iconPlay);
		this.btnPlay.setActionCommand(PLAY_OR_PAUSE);
		this.btnPlay.addActionListener(this);
		this.add(this.btnPlay);

		this.btnStop = new JButton(this.iconStop);
		this.btnStop.setActionCommand(STOP);
		this.btnStop.addActionListener(this);
		this.add(btnStop);

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (PLAY_OR_PAUSE.equals(e.getActionCommand())) {
			
			if (this.status.equals(Status.PAUSED)) {
				musicPlayer.resume();
			} else if (this.status.equals(Status.PLAYING)) {
				musicPlayer.pause();
			} else if (this.status.equals(Status.STOPPED)) {
				Track track = trackListPanel.getSelectedTrack();
				musicPlayer.play(track);
			}

		} else if (STOP.equals(e.getActionCommand())) {

			musicPlayer.stop();

		}
	}

}
