package org.syaku.demo.domain;

import org.springframework.stereotype.Component;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @site http://syaku.tistory.com
 * @since 16. 3. 28.
 */
@Component
public class BasicDemo implements Demo {
	String name = "1111";

	public String getName() {
		return name;
	}
}
