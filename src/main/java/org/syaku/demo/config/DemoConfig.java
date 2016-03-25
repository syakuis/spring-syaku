package org.syaku.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.syaku.demo.support.Demo2Support;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @site http://syaku.tistory.com
 * @since 16. 3. 25.
 */
@Configuration
public class DemoConfig {

	@Bean(initMethod = "init")
	public Demo2Support demo2Support(){
		return new Demo2Support();
	}
}
