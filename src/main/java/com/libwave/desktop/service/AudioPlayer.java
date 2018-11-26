package com.libwave.desktop.service;

public class AudioPlayer {

	static {
		System.loadLibrary("libAudioPlayer");
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
	
	public static void main(String[] args) throws InterruptedException {
		
		AudioPlayer ap = new AudioPlayer();
		
		ap.init();
		
		long loadMusic = ap.loadMusic("c:/tmp/Rolemusic_-_03_-_Python.ogg");
		
		ap.playMusicFadeIn(loadMusic, 2000);
		
		Thread.sleep(3000);
		
		ap.stopFadeOut(400);
		
		Thread.sleep(2000);
		
		ap.shutdown();
		
	}
}