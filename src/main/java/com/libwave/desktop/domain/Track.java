package com.libwave.desktop.domain;

import java.io.File;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

@SuppressWarnings("serial")
@Entity
@Table(name = "TRACK")
public class Track {

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "PATH", nullable = false, length = 4096, unique = true)
	private String path;

	@Column(name = "UUID", nullable = false, length = 64, unique = true)
	private String uuid;

	@Column(name = "FILENAME", nullable = false, length = 1024)
	private String fileName;

	@Column(name = "TITLE", length = 1024)
	private String title;

	@Column(name = "ARTIST", length = 1024)
	private String artist;

	@Column(name = "ALBUM", length = 1024)
	private String album;

	@Column(name = "GENRE", length = 1024)
	private String genre;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	@Override
	public String toString() {
		return path + " (" + FileUtils.byteCountToDisplaySize(new File(path).length()) + ")";
	}

}
