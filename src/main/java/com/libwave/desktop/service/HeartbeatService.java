package com.libwave.desktop.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.libwave.desktop.api.HeartbeatResponse;
import com.libwave.desktop.common.BackendUrls;

@Service
public class HeartbeatService {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ConfigService configService;

	@Value("${libwave.backend.url}")
	private String host;

	@Scheduled(fixedDelay = DateUtils.MILLIS_PER_SECOND * 10)
	public void beat() {

		String url = host + BackendUrls.HEARTBEAT;
		url = StringUtils.replace(url, "{uuid}", configService.getUuid());
/*
		log.debug("Heartbeat: " + url);
		
		restTemplate.getForObject(url, HeartbeatResponse.class);
*/
	}

}
