package com.libwave.desktop.service;

import java.util.Arrays;
import java.util.Queue;

import javax.swing.JOptionPane;

import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.libwave.desktop.ui.MainWindowIcon;

public class AudioPlayer {

	private static final Logger log = LoggerFactory.getLogger(AudioPlayer.class);

	private static Queue<Integer[]> queue = new CircularFifoQueue<>(16);

	static {
		try {
			System.loadLibrary("libAudioPlayer");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Cannot load AudioPlayer library");
		}

		new Thread(() -> {
			Thread.currentThread().setName("Icon Waves Update Thread");
			while (true) {
				Integer[] poll = queue.poll();
				if (poll != null) {
					MainWindowIcon.draw(poll);
				}
				try {
					Thread.sleep(30);
				} catch (InterruptedException e) {
				}
			}

		}).start();
	}

	// TODO optimize
	public static void audioDataCallback(int[] data) {
		Integer[] boxed = Arrays.stream(data).boxed().toArray(Integer[]::new);
		queue.add(boxed);
	}

	public native void init();

	public native void shutdown();

	public native void resume();

	public native void stop();

	public native void stopFadeOut(int ms);

	public native void pause();

	public native long loadMusic(String file);

	public native void closeMusic(long song);

	public native void playMusic(long song);

	public native void playMusicFadeIn(long song, int ms);
	
	public native boolean isPlaying();
	
	public native boolean isPaused();
	
	/*
	 * public static void main(String[] args) throws InterruptedException {
	 * 
	 * AudioPlayer ap = new AudioPlayer();
	 * 
	 * ap.init();
	 * 
	 * long loadMusic = ap.loadMusic("c:/downloads/2ND_PM.S3M");
	 * 
	 * ap.playMusicFadeIn(loadMusic, 2000);
	 * 
	 * Thread.sleep(30000);
	 * 
	 * ap.stopFadeOut(400);
	 * 
	 * Thread.sleep(2000);
	 * 
	 * ap.shutdown();
	 * 
	 * }
	 */

}
