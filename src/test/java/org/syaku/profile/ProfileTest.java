package org.syaku.profile;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Properties;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @site http://syaku.tistory.com
 * @since 16. 3. 23.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath*:config/*-context.xml"})
public class ProfileTest {

	@Autowired private Environment env;
	@Resource(name="config") private Properties config;

	@Test
	public void test() {
		System.out.println(Arrays.toString(env.getActiveProfiles()));
		System.out.println(Arrays.toString(env.getDefaultProfiles()));

		System.out.println(config.getProperty("name"));
		System.out.println(config.getProperty("good"));
		System.out.println(config.getProperty("bad"));
	}

}
