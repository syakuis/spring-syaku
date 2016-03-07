package org.syaku.ex.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.syaku.ex.domain.Ex;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @site http://syaku.tistory.com
 * @since 16. 3. 8.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:config/*-context.xml" })
public class ExServiceTest {

	@Resource(name = "exService") ExService exService;

	@Test
	public void select() {
		Ex ex = new Ex();
		ex.setName("syaku");

		Map<String, Object> json = new HashMap<>();
		json.put("name","111");
		ex.setJson(json);

		exService.insert(ex);
		exService.insert(ex);
		exService.insert(ex);
		exService.insert(ex);
		exService.insert(ex);


		System.out.println(exService.getExList());
	}
}
