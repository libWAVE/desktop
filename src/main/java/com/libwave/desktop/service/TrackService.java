package com.libwave.desktop.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.transaction.Transactional;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.libwave.desktop.dao.TrackDao;
import com.libwave.desktop.domain.Track;

@Service
public class TrackService {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private TrackDao trackDao;

	@Autowired
	private AudioFileTagReaderService audioInfoService;

	@Transactional
	public long count() {
		return trackDao.count();
	}

	@Transactional
	public void add(File fileOrFolder) {

		log.debug("Adding: " + fileOrFolder);

		if (fileOrFolder.isDirectory()) {
			addFolder(fileOrFolder);
		} else {
			addFile(fileOrFolder);
		}

	}

	private void addFolder(File folder) {

		File[] files = folder.listFiles();

		for (File file : files) {
			addFile(file);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
		}

	}

	private boolean isAudioFile(File file) {
		return file.canRead() && file.getAbsolutePath().toLowerCase().endsWith("mp3");
	}

	private void addFile(File file) {

		log.debug("Add file: " + file);

		if (false == isAudioFile(file)) {
			log.warn("Can't read: " + file);
			return;
		}

		Track t = new Track();
		t.setFileName(file.getName());
		t.setPath(file.getAbsolutePath());
		audioInfoService.read(file, t);

		try (InputStream is = new BufferedInputStream(new FileInputStream(file))) {

			t.setUuid(DigestUtils.sha256Hex(is));

		} catch (Exception e) {
			log.error("Error: ", e);
		}

		if (trackDao.findByUuid(t.getUuid()) == null) {
			trackDao.save(t);
			log.debug("Saved track: " + t);
		}

	}
}