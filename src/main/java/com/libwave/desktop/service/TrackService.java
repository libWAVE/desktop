package com.libwave.desktop.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.SwingUtilities;
import javax.transaction.Transactional;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.libwave.desktop.dao.TrackDao;
import com.libwave.desktop.domain.Track;
import com.libwave.desktop.ui.MainWindow;
import com.libwave.desktop.ui.StatusBar;

@Service
public class TrackService {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private TrackDao trackDao;

	@Autowired
	private AudioFileTagReaderService audioInfoService;

	@Autowired
	private StatusBar statusBar;
	
	@Autowired
	private MainWindow mainWindow;

	private Queue<File> queue = new LinkedBlockingQueue<>();

	@Transactional
	public long count() {
		return trackDao.count();
	}

	public void add(File fileOrFolder) {
		queue.add(fileOrFolder);
	}

	@Transactional
	@Scheduled(fixedDelay = 50)
	public void process() throws InterruptedException {

		log.debug("Process tracks");

		File fileOrFolder = queue.poll();

		if (fileOrFolder != null) {

			log.debug("Adding: " + fileOrFolder);

			if (fileOrFolder.isDirectory()) {
				addFolder(fileOrFolder);
			} else {
				addFile(fileOrFolder);
			}

			statusBar.setStatus("Ready");
			
			mainWindow.updateTitle();

		} else {
			Thread.sleep(1000);
		}

	}

	private void addFolder(File folder) {

		File[] files = folder.listFiles();

		for (File file : files) {

			if (file.isDirectory()) {
				addFolder(file);
				continue;
			}

			addFile(file);

			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
			}
		}

	}

	private boolean isAudioFile(File file) {
		return file.canRead() && file.getAbsolutePath().toLowerCase().endsWith("mp3");
	}

	private void addFile(File file) {

		log.debug("Add file: " + file);

		SwingUtilities.invokeLater(() -> {
			String path = file.getAbsolutePath();
			if (path.length() > 64) {
				path = path.substring(0, 16) + "..." + path.substring(path.length() - 16, path.length());
			}
			statusBar.setStatus(path);
			mainWindow.updateTitle();
		});

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

			log.debug("Saving track: " + t);

			trackDao.save(t);
			log.debug("Saved track: " + t);
		}

	}

	@Transactional
	public void removeAll() {
		trackDao.deleteAll();
		statusBar.setStatus("Deleting all tracks...");
		mainWindow.updateTitle();
		statusBar.setStatus("Ready");
		
	}

	public void add(File[] selectedFiles) {
		for (File f:selectedFiles) {
			add(f);
		}
	}
	
}
