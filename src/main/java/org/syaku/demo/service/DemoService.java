package org.syaku.demo.service;

import org.springframework.stereotype.Service;
import org.syaku.demo.Print;
import org.syaku.demo.domain.BasicDemo;
import org.syaku.demo.domain.Demo;
import org.syaku.demo.support.DemoSupport;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @site http://syaku.tistory.com
 * @since 16. 3. 25.
 */
@Service
public class DemoService {
	@Resource(name = "basicDemo") private Demo demo;

	@PostConstruct
	public void postConstruct() {
		Print.text("DemoService.postConstruct");
		Print.text(demo.getName());
	}

	public void setDemo(Demo demo) {
		this.demo = demo;
		Print.text(demo.getClass().getCanonicalName());
	}
}
