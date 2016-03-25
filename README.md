# 스프링 빈 라이프사이클

스프링 설정에 따라 어떤 순서로 클래스가 생성되는 지 알아보고 싶었다.
이런 순서가 단순한 개발에는 큰 의미가 없어보이지만 규모가 큰 프로젝트나 프레임워크를 개발할때는 중요한 역활을 하기도하고 문제를 발생시키는 원인이 되기도 한다.

> 블로그 포스팅 : http://syaku.tistory.com/312  
Github 소스 : https://github.com/syakuis/spring-syaku/tree/spring-bean


### 개발환경

> Mac OS X  
Java 7  
Apache Tomcat 7  
Spring 4.2.4.RELEASE  

## 스프링 설정 로드 순서

- [1]DemoService: context(root-context.xml) 설정에서 component-scan 에 의해 주입된다.
- [2]DemoSupport: context(sub-context.xml) 설정에서 직접 주입한다.
- [3]Demo2Support: 자바 Configuration(DemoConfig.java) 의해 직접 주입한다.
- [4]DemoController: servlet(root-servlet.xml) 설정에서 직접 주입한다.
- [5]Demo2Controller: servlet(root-servlet.xml) 설정에서  component-scan 에 의해 주입된다.

#### WAS 구동 로그

```
==>DemoService.postConstruct
==>Demo2Controller.postConstruct
==>DemoSupport.init
==>Demo2Support.init

# 웹페이지 접속 후 로그

==>Demo2Controller.postConstruct
==>DemoController.init
```

1. context 설정 로드 [1]
2. servlet 설정 로드 [5]
3. context에 직접 주입한 bean 로드 [2]
4. 자바 Configuration 로드 [3]
4. 웹페이지에 최초 접속하면 servlet 설정에 직접 주입한 bean 로드됨. 이 과정에서 servlet 설정을 다시 한번 로드됨. [4]

유닉스와 윈도우 운영체제의 로드 순서는 같았으나 리눅스는 달랐다. (예전 프로젝트때 리눅스가 로드 순서가 다르다는 것을 확인했었다.) 귀찮아서 리눅스는 테스트 하지 않았다. 그리고 설정 파일 입력 순서에도 영향을 받는 다.

```
<!-- 스프링에서 로드 순서를 정한다. -->
<context-param>
	<param-name>contextConfigLocation</param-name>
	<param-value>classpath*:config/*-context.xml</param-value>
</context-param>

<!-- sub부터 로드된다. -->
<context-param>
	<param-name>contextConfigLocation</param-name>
	<param-value>classpath*:config/sub-context.xml, classpath*:config/root-context.xml</param-value>
</context-param>
```

### 변경 후 로그

```
==>DemoSupport.init
==>DemoService.postConstruct
==>Demo2Controller.postConstruct
==>Demo2Support.init
```

## Controller 에서 AOP 와 Inteceptor 실행 순서

테스트를 위해 몇가지 설정을 추가하고 자바 클래스를 만든다.

#### root-context.xml aop 설정을 추가한다.

```
<aop:aspectj-autoproxy/>
```

#### root-servlet.xml aop 설정과 인터셉터 설정을 추가한다.

```
<aop:aspectj-autoproxy/>

<mvc:interceptors>
	<mvc:interceptor>
		<mvc:mapping path="/demo"/>
		<bean class="org.syaku.demo.aop.DemoInterceptor"/>
	</mvc:interceptor>
</mvc:interceptors>
```

그리고 DemoAOP.java 와 DemoInterceptor.java 를 만든다.
`/demo` 페이지에 접속하여 로그를 출력한다.

#### 로그

```
==>Spring Interceptor before
==>Spring AOP before
==>Spring AOP after
==>Spring Interceptor after
```

인터셉터가 먼저 호츨되고 aop 실행된다. 그리고 인터셉터가 후처리됨을 알 수 있다.


## 주입된 클래스 property override 하기


#### root-context.xml

```xml
	<bean id="demoOverrideService" class="org.syaku.demo.service.DemoOverrideService">
		<property name="name" value="syaku" />
	</bean>
```

#### sub-context.xml

```xml
	<bean id="demoOverrideService2" parent="demoOverrideService" class="org.syaku.demo.service.DemoOverrideService" init-method="init">
		<property name="value" value="http://syaku.tistory.com" />
	</bean>
```

#### DemoOverrideService.java

```java
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

	public void init() {
		Print.text(name);
		Print.text(value);
	}
}

```

#### 로그

```
==>DemoService.postConstruct
==>Demo2Controller.postConstruct
==>DemoSupport.init
==>syaku
==>http://syaku.tistory.com
==>Demo2Support.init
```

2개의 다른 빈을 생성하여 parent 속성을 이용하여 override 할 수 있다. 하지만 내가 필요한 건 다른 빈이 아니여야 한다. 최초 설정부터 입력받게 하는 수 밖에...

요즘 해야할 일도 많고 공부해야할 것도 많아 블로그는 뒷전인듯~ I will be back!!! d



