package com.libwave.desktop.service;

import java.io.File;

import com.libwave.desktop.domain.Track;

public interface AudioFileTagReaderService {
	public void read(File file, Track track);
}
