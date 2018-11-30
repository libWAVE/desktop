package com.libwave.desktop.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.libwave.desktop.common.Icons;

@Component
public class PlayerPanel extends JPanel implements InitializingBean, ActionListener {

	private static final String PLAY = "PLAY";
	@Autowired
	private TrackListPanel trackListPanel;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		
		this.setLayout(new FlowLayout());
		
		JButton btnPause = new JButton(Icons.getIcon("pause_24.png"));
		btnPause.setEnabled(false);
		this.add(btnPause);
		
		JButton btnPlay = new JButton(Icons.getIcon("play_24.png"));
		this.add(btnPlay);
		btnPlay.setActionCommand(PLAY);
		btnPlay.addActionListener(this);
		
		JButton btnStop = new JButton(Icons.getIcon("stop_24.png"));
		btnStop.setEnabled(false);
		this.add(btnStop);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (PLAY.equals(e.getActionCommand())) {
			trackListPanel.play();
		}
	}

	
	
}
