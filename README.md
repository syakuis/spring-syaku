## 스프링 myBatis JSON 형식 입력하고 출력하기 : Spring myBatis JSONType input output

### 개발환경
> Java 7.x  
Spring 4.x  
myBatis 3.2.8  
jackson 2.0.1  
H2 1.4.180

myBatis 를 이용한 JSON 형식의 데이터를 저장하고 조회하는 로직을 간단하게 구현하는 방법을 설명하려고 한다.


### 데이터의 흐름
저장: [클라이언트] --JSON--> [Spring] --String JSON--> [DB]  
조회: [DB] --String JSON--> [Spring] --Object JSON--> [클라이언트]

데이터의 흐름은 클라이언트에서 전송된 json 형식의 데이터를 서버 DB에 저장하고 다시 클라이언트로 json 데이터를 넘겨주는 작업인데... 흔히 json 데이터를 문자열로 변경하고 db에 저장한 후 출력할때는 다시 문자열을 객체로 변경하고 프론트엔드로 넘겨주는 식이다. 
이렇게하면 domain의 데이터 형식이 문자열이여야하나 객체여야하나 혼동이 생길수도 있고 바인딩 과정이 복잡해지는 문제가 발생한다.

저장: [클라이언트] --JSON--> [Spring] --Object JSON--> [DB: JSON Parsing]  
조회: [DB: JSON Parsing] --Object JSON--> [Spring] --JSON--> [클라이언트]

그래서 위의 데이터 흐름과 같이 JSON 파싱작업은 myBatis에서 하고 json 데이터 형식은 항상 객체로 유지하도록하여 심플한 로직을 구현할 수 있게 하였다.

### 로직 구현

**테이블**

```
CREATE TABLE ex (
  name VARCHAR(10),
  json  VARCHAR(4000)
);
```

**도메인 클래스**

```java
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
```

myBatis에서 지원하는 타입헨들러를 이용하면 쉽게 구현이 가능하다. JacksonParsing 클래스는 jackson 라이브러리를 이용하여 json 형식을 간편하게 파싱하기 위해 따로 만든 클래스이다.

```java
package org.syaku.ex.mapper.type;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.syaku.common.object.JacksonParsing;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @site http://syaku.tistory.com
 * @since 16. 3. 8.
 */
public class JSONTypeHandler extends BaseTypeHandler<Object> {

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType)
			throws SQLException {

		String p = JacksonParsing.toString(parameter);
		ps.setObject(i, p);
	}

	@Override
	public Object getNullableResult(ResultSet rs, String columnName)
			throws SQLException {
		Object d = rs.getObject(columnName);
		if(d == null) return d;
		return JacksonParsing.toMap(d.toString());
	}

	@Override
	public Object getNullableResult(ResultSet rs, int columnIndex)
			throws SQLException {
		Object d = rs.getObject(columnIndex);
		if(d == null) return d;
		return JacksonParsing.toMap(d.toString());
	}

	@Override
	public Object getNullableResult(CallableStatement cs, int columnIndex)
			throws SQLException {
		Object d = cs.getObject(columnIndex);
		if(d == null) return d;
		return JacksonParsing.toMap(d.toString());
	}
}
```

- setNonNullParameter: 저장할때 데이터 파싱
- getNullableResult, getNullableResult, getNullableResult : 조회할때 데이터 파싱

만들어지 타입헨들러를 맵퍼에 적용해보자.

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="org.syaku.ex.mapper.ExMapper">

	<resultMap id="ex" type="org.syaku.ex.domain.Ex">
		<result property="name" column="name" />
		<result property="json" column="json" typeHandler="org.syaku.ex.mapper.type.JSONTypeHandler" />
	</resultMap>

	<select id="select" resultMap="ex">
		SELECT * FROM ex
	</select>

	<insert id="insert">
		INSERT INTO ex (
			name,
			json
		) VALUES (
			#{name},
			#{json, typeHandler=org.syaku.ex.mapper.type.JSONTypeHandler}
		)
	</insert>

</mapper>
```

typeHandler 속성에 `org.syaku.ex.mapper.type.JSONTypeHandler` 패키지를 포함한 클래스명을 입력하면 된다.

### 테스트

```java
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
		json.put("name","syaku");
		json.put("value","spring");
		ex.setJson(json);

		exService.insert(ex);
		exService.insert(ex);
		exService.insert(ex);
		exService.insert(ex);
		exService.insert(ex);

		for(Ex data : exService.getExList()) {
			System.out.println(data.toString());
		}

	}
}
```

**결과**

```
org.syaku.ex.domain.Ex@2f7f2ae[name=syaku,json={name=syaku, value=spring}]
org.syaku.ex.domain.Ex@645d7f07[name=syaku,json={name=syaku, value=spring}]
org.syaku.ex.domain.Ex@363ed80b[name=syaku,json={name=syaku, value=spring}]
org.syaku.ex.domain.Ex@57d9d5ff[name=syaku,json={name=syaku, value=spring}]
org.syaku.ex.domain.Ex@1e708568[name=syaku,json={name=syaku, value=spring}]
```


