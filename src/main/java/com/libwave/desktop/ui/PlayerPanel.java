package com.libwave.desktop.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import com.libwave.desktop.common.Icons;

@Component
public class PlayerPanel extends JPanel implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		this.setLayout(new FlowLayout());
		this.add(new JButton(Icons.getIcon("pause_24.png")));
		this.add(new JButton(Icons.getIcon("play_24.png")));
		this.add(new JButton(Icons.getIcon("stop_24.png")));
	}

	
	
}
