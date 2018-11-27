package com.libwave.desktop.service;

import java.awt.Dimension;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.libwave.desktop.domain.Config;

@Service
public class AddTrackService implements InitializingBean {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private TrackService trackService;

	@Autowired
	private ConfigService configService;

	public void add(JFrame parent) {

		JFileChooser chooser = new JFileChooser();

		Config config = configService.getConfig().get();

		Dimension fileChooserSize = config.getFileChooserSize();
		chooser.setPreferredSize(fileChooserSize);

		chooser.setCurrentDirectory(new java.io.File(config.getFileChooserLocation()));

		chooser.setDialogTitle("Please, select a music file");
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setMultiSelectionEnabled(true);
		
		String[] exts = trackService.getAudioExtensions().toArray(new String[trackService.getAudioExtensions().size()]);
		
		chooser.setFileFilter(new FileNameExtensionFilter("Audio tracks", exts));
		
		if (chooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {

			log.debug("getCurrentDirectory(): " + chooser.getCurrentDirectory());

			config.setFileChooserLocation(chooser.getCurrentDirectory().getAbsolutePath());
			config.setFileChooserSize(chooser.getSize());

			log.debug("getSelectedFile() : " + chooser.getSelectedFiles());

			trackService.add(chooser.getSelectedFiles());

			configService.save(config);

		} else {
			log.debug("No Selection ");
		}

	}

	@Override
	public void afterPropertiesSet() throws Exception {
		log.debug("Inited AddTrackService");
	}

}
