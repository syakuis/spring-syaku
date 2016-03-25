package org.syaku.demo.service;

import org.springframework.stereotype.Service;
import org.syaku.demo.Print;

import javax.annotation.PostConstruct;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @site http://syaku.tistory.com
 * @since 16. 3. 25.
 */
@Service
public class DemoService {

	@PostConstruct
	public void postConstruct() {
		Print.text("DemoService.postConstruct");
	}
}
