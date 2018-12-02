package com.libwave.desktop.service;

import java.awt.Dimension;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.libwave.desktop.dao.ConfigDao;
import com.libwave.desktop.domain.Config;

@Service
public class ConfigService implements InitializingBean {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ConfigDao configDao;

	public Optional<Config> getConfig() {
		return configDao.findById(1);
	}

	public String getUuid() {
		return getConfig().get().getUuid();
	}

	@Transactional
	public void save(Config config) {
		this.configDao.save(config);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (configDao.count() == 0) {
			Config c = new Config();
			c.setId(1);
			c.setDate(new Date());
			c.setUuid(UUID.randomUUID().toString());
			c.setFileChooserSize(new Dimension(800, 600));
			c.setFileChooserLocation(SystemUtils.getUserHome().getAbsolutePath());
			configDao.save(c);
		}
		
		log.debug("Desktop UUID: " + getConfig().get().getUuid());
	}

}