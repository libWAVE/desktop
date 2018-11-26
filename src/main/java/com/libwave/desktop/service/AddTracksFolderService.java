package com.libwave.desktop.service;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddTracksFolderService implements InitializingBean {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private TrackService trackService;
	
	public void add(JFrame parent) {

		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new java.io.File("g:/music"));
		chooser.setDialogTitle("Please, select a folder");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		//
		// disable the "All files" option.
		//
		chooser.setAcceptAllFileFilterUsed(false);
		//
		if (chooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
			log.debug("getCurrentDirectory(): " + chooser.getCurrentDirectory());
			log.debug("getSelectedFile() : " + chooser.getSelectedFile());
			
			trackService.add(chooser.getSelectedFile());
			
		} else {
			log.debug("No Selection ");
		}
		

	}

	@Override
	public void afterPropertiesSet() throws Exception {
		log.debug("Inited AddTracksFolderService");
	}

}
