package com.libwave.desktop.service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

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

	@Override
	public void afterPropertiesSet() throws Exception {
		if (configDao.count() == 0) {
			Config c = new Config();
			c.setId(1);
			c.setDate(new Date());
			c.setUuid(UUID.randomUUID().toString());
			configDao.save(c);
		}
	}

}