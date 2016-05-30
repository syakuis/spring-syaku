package org.syaku.demo.domain;

import org.springframework.stereotype.Component;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @site http://syaku.tistory.com
 * @since 16. 3. 28.
 */
public class AddDemo implements Demo {
	String name = "2222";

	public String getName() {
		return name;
	}
}
