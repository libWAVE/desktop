package com.libwave.desktop.service;

import java.io.File;

import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnvironmentService {

	private static Logger log = LoggerFactory.getLogger(EnvironmentService.class);

	public static File getUserDataFolder() {
		File folder = new File(SystemUtils.getUserHome(), ".libwave" + File.separator);
		log.debug("getUserDataFolder: " + folder);
		return folder;
	}

	public static File getDbFile() {
		File dbFile = new File(getUserDataFolder(), "tracks.db");
		log.debug("getDbFile: " + dbFile);
		return dbFile;
	}

}
