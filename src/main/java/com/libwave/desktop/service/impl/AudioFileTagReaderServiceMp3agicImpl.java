package com.libwave.desktop.service.impl;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.libwave.desktop.domain.Track;
import com.libwave.desktop.service.AudioFileTagReaderService;
import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.Mp3File;

@Service
public class AudioFileTagReaderServiceMp3agicImpl implements AudioFileTagReaderService {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public void read(File file, Track track) {

		try {

			Mp3File mp3file = new Mp3File(file);

			if (mp3file.hasId3v1Tag()) {

				ID3v1 id3v1Tag = mp3file.getId3v1Tag();

				track.setAlbum(id3v1Tag.getAlbum());
				track.setArtist(id3v1Tag.getArtist());
				track.setGenre(id3v1Tag.getGenre() + " (" + id3v1Tag.getGenreDescription() + ")");
				track.setTitle(id3v1Tag.getTitle());

			} else if (mp3file.hasId3v2Tag()) {

				ID3v2 id3v2Tag = mp3file.getId3v2Tag();

				track.setAlbum(id3v2Tag.getAlbum());
				track.setArtist(id3v2Tag.getArtist());
				track.setGenre(id3v2Tag.getGenre() + " (" + id3v2Tag.getGenreDescription() + ")");
				track.setTitle(id3v2Tag.getTitle());

			}

		} catch (Exception e) {
			log.error("Error while reading audio tags: ", e);
		}

	}

}
