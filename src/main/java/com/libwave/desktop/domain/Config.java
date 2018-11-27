package com.libwave.desktop.domain;

import java.awt.Dimension;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "CONFIG")
public class Config {

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "UUID", nullable = false)
	private String uuid;

	@Column(name = "DATE", nullable = false)
	private Date date;

	@Column(name = "FILE_CHOOSER_SIZE", nullable = true)
	private String fileChooserSize;

	@Column(name = "FILE_CHOOSER_LOCATION", nullable = true, length = 4096)
	private String fileChooserLocation;

	public Dimension getFileChooserSize() {
		String w = fileChooserSize.split("x")[0];
		String h = fileChooserSize.split("x")[1];

		return new Dimension(Integer.parseInt(w), Integer.parseInt(h));
	}

	public String getFileChooserLocation() {
		return fileChooserLocation;
	}

	public void setFileChooserLocation(String fileChooserLocation) {
		this.fileChooserLocation = fileChooserLocation;
	}

	public void setFileChooserSize(Dimension size) {
		this.fileChooserSize = (int) size.getWidth() + "x" + (int) size.getHeight();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "Config [id=" + id + ", uuid=" + uuid + ", date=" + date + "]";
	}

}
