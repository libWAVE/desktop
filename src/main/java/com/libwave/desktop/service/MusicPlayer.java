package com.libwave.desktop.service;

import javax.swing.JPanel;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.libwave.desktop.domain.Track;
import com.libwave.desktop.ui.StatusBar;

@SuppressWarnings("serial")
@Service
public class MusicPlayer extends JPanel implements InitializingBean, DisposableBean {

	@Autowired
	private StatusBar statusBar;

	private AudioPlayer audioPlayer = new AudioPlayer();

	private long currentlyPlayedMusicPointer;

	public static enum Status {
		PLAYING, PAUSED, STOPPED
	}

	private Status status = Status.STOPPED;

	private Track currentTrack;

	public Track getCurrentTrack() {
		return currentTrack;
	}

	@Override
	public void afterPropertiesSet() throws Exception {

		audioPlayer.init();

	}

	public void play(Track track) {

		if (track != null) {

			if (currentlyPlayedMusicPointer > 0) {
				stop();
				audioPlayer.closeMusic(currentlyPlayedMusicPointer);
				currentlyPlayedMusicPointer = 0;
			}

			currentlyPlayedMusicPointer = audioPlayer.loadMusic(track.getPath());

			if (currentlyPlayedMusicPointer != 0) {
				audioPlayer.playMusicFadeIn(currentlyPlayedMusicPointer, 1000);
			}

			status = Status.PLAYING;

			currentTrack = track;
			
			statusBar.setStatus("Playing " + track.getFileName());

		}
	}

	@Override
	public void destroy() throws Exception {
		audioPlayer.shutdown();
	}

	public void stop() {
		audioPlayer.stop();
		currentTrack = null;
		status = Status.STOPPED;
		statusBar.resetStatus();

	}

	public void pause() {
		audioPlayer.pause();
		status = Status.PAUSED;
	}

	public Status getStatus() {
		return status;
	}

	public void resume() {
		audioPlayer.resume();
		status = Status.PLAYING;
	}

}
