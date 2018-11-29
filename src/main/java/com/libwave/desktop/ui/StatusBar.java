package com.libwave.desktop.ui;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StatusBar extends JPanel implements InitializingBean {

	private JLabel status = new JLabel();

	@Autowired
	private PlayerPanel playerPanel;

	public void setStatus(String msg) {
		status.setText(msg);
	}

	@Override
	public void afterPropertiesSet() throws Exception {

		status.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		status.setText("Welcome to libWAVE!");
		this.add(status, BorderLayout.WEST);
		this.add(playerPanel, BorderLayout.EAST);

	}

}
