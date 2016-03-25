package org.syaku.demo.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.syaku.demo.Print;

import javax.annotation.PostConstruct;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @site http://syaku.tistory.com
 * @since 16. 3. 25.
 */
@Controller
public class Demo2Controller {

	@PostConstruct
	public void postConstruct() {
		Print.text("Demo2Controller.postConstruct");
	}

	@RequestMapping(value = "/demo", method = RequestMethod.GET)
	@ResponseBody
	public String procDemoView() {
		return "^^";
	}

}
