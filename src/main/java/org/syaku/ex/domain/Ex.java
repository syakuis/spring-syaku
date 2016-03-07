package org.syaku.ex.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.Map;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @site http://syaku.tistory.com
 * @since 16. 3. 7.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Ex {
	private String name;
	private Map<String, Object> json;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, Object> getJson() {
		return json;
	}

	public void setJson(Map<String, Object> json) {
		this.json = json;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
