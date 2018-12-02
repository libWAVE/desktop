/*

MIT License

Copyright (c) 2018 libWAVE team

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
package com.libwave.desktop.job;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.libwave.api.Urls;
import com.libwave.api.model.desktop.GetClientRequestsResponse;
import com.libwave.desktop.service.ConfigService;

@Component
public class GetClientRequestsJob {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private RestTemplate restTemplate;

	@Value("${libwave.backend.url}")
	private String backendUrl;
	
	@Autowired
	private ConfigService configService;
	
	@Scheduled(fixedDelay = DateUtils.MILLIS_PER_SECOND * 5)
	public void process() {
		
		String url = backendUrl + Urls.DESKTOP_GET_CLIENT_REQUESTS;
		url = StringUtils.replace(url, "{dektopUuid}", configService.getUuid());
		
		log.debug("url: " + url);
		
		GetClientRequestsResponse resp = restTemplate.getForObject(url, GetClientRequestsResponse.class);
		
		log.debug("Server response: " + resp);

		try {
			Thread.sleep(RandomUtils.nextLong(0, DateUtils.MILLIS_PER_SECOND * 5));
		} catch (InterruptedException e) {
		}
	}

}
