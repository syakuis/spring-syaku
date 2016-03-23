package org.syaku.profile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Properties;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @site http://syaku.tistory.com
 * @since 16. 3. 23.
 */
public class Profile {

	@Autowired private Environment env;
	@Resource(name="config") private Properties config;

	@PostConstruct
	public void init() {
		System.out.println(Arrays.toString(env.getActiveProfiles()));
		System.out.println(Arrays.toString(env.getDefaultProfiles()));

		System.out.println(config.getProperty("name"));
		System.out.println(config.getProperty("good"));
		System.out.println(config.getProperty("bad"));
	}
}
