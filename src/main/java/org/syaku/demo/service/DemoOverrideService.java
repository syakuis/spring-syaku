package org.syaku.demo.service;

import org.springframework.stereotype.Service;
import org.syaku.demo.Print;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @site http://syaku.tistory.com
 * @since 16. 3. 25.
 */
public class DemoOverrideService {

	private String name;
	private String value;

	public void setName(String name) {
		this.name = name;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

	public void init() {
		Print.text(name);
		Print.text(value);
	}
}
