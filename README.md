## 스프링 시큐리티 다양한 암호화 사용하기 : Spring Security

### 개발환경
> Java 7.x  
Spring 4.x  
Spring Security 4.x


스프링 시큐리티에는 기본적으로 제공하는 암호화가 있고 KISA에서 표준으로 제시하는 방식도 있다. 이와 같은 다양한 암호화 방식을 사용하기에 불편하지 않고 확장성을 고려한 PasswordEncoder 인터페이스를 사용해 재작성하였다.

### 스프링 시큐리티 암호화 클래스 종류
3.1 버전부터 PasswordEncoder 방식이 바뀌었다. BCryptPasswordEncoder 암호화할때 salt를 따로 입력받지 않고 암호화되는 과정에서 랜덤된 키값을 이용해 암호화하여 매번 새로운 값을 만들어준다. 그래서 보다 안전한 암호호 값을 사용할 수 있다.

* BCryptPasswordEncoder  
스프링 시큐리티에서 기본적을 사용하는 암호화 방식으로 암호화가 될때마다 새로운 값을 생성한다. 임의적인 값을 추가해서 암호화하지 않아도 된다. (salt 사용하지 않는다.)

* StandardPasswordEncoder  
SHA-256 암호화를 사용한다.

* NoOpPasswordEncoder  
암호화를 사용하지 않을때 사용한다.

스프링시큐리티에서 제공하는 기본 암호화외에 더 많은 암호화를 사용하기 위해 PasswordEncoder 인터페이스를 사용해서 아래와같이 SHA 암호화 클래스를 만들었다.

```java
package org.syaku.mei.security.password;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @site http://syaku.tistory.com
 * @since 16. 2. 18.
 */
public class PasswordEncoding implements PasswordEncoder {
	private PasswordEncoder passwordEncoder;
	
	public PasswordEncoding() {
		this.passwordEncoder = new BCryptPasswordEncoder();
	}

	public PasswordEncoding(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public String encode(CharSequence rawPassword) {
		return passwordEncoder.encode(rawPassword);
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		return passwordEncoder.matches(rawPassword, encodedPassword);
	}
}

```
확장성을 고려한 암호화 클래스를 주입받기 위한 클래스이다. 기본적으로 BCryptPasswordEncoder 암호화를 사용한다.  
암호화는 encode를 사용하고, 암호화 값의 비교는 matches 를 사용한다.

rawPassword: 암호화 되지않은 값  
encodedPassword : 암호화된 값

```java
package org.syaku.mei.security.password;

import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @site http://syaku.tistory.com
 * @since 16. 2. 18.
 */
public class SHAPasswordEncoder implements PasswordEncoder {
	private ShaPasswordEncoder shaPasswordEncoder;
	private Object salt = null;

	public SHAPasswordEncoder() {
		shaPasswordEncoder = new ShaPasswordEncoder();
	}

	public SHAPasswordEncoder(int sha) {
		shaPasswordEncoder = new ShaPasswordEncoder(sha);
	}

	public void setEncodeHashAsBase64(boolean encodeHashAsBase64) {
		shaPasswordEncoder.setEncodeHashAsBase64(encodeHashAsBase64);
	}

	public void setSalt(Object salt) {
		this.salt = salt;
	}

	@Override
	public String encode(CharSequence rawPassword) {
		return shaPasswordEncoder.encodePassword(rawPassword.toString(), salt);
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		return shaPasswordEncoder.isPasswordValid(encodedPassword, rawPassword.toString(), salt);
	}
}

```

SHA 의 다양한 암호호를 사용하기 위한 암호화 클래스이다.

sha: 암호화 비트를 입력 (1, 128, 256, 512)  
encodeHashAsBase64: SHA에서 제공하는 Base64 를 암호화할지 여부

### 암호화 테스트

```java
package org.syaku.mei.security.password;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @site http://syaku.tistory.com
 * @since 16. 2. 18.
 */

public class PasswordEncoderTest {

	@Test
	public void test() {
		String password = "1234";
		SHAPasswordEncoder shaPasswordEncoder = new SHAPasswordEncoder(512);
		shaPasswordEncoder.setEncodeHashAsBase64(true);
		PasswordEncoding passwordEncoding = new PasswordEncoding(shaPasswordEncoder);

		System.out.println("SHA 암호화: " + passwordEncoding.encode(password));
		System.out.println("SHA 비교: " + passwordEncoding.matches(password, passwordEncoding.encode(password)));

		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		passwordEncoding = new PasswordEncoding(passwordEncoder);

		System.out.println("BCrypt 암호화: " + passwordEncoding.encode(password));
		System.out.println("BCrypt 비교: " + passwordEncoding.matches(password, passwordEncoding.encode(password)));
		
		// 다시 암호화
		System.out.println("BCrypt 암호화: " + passwordEncoding.encode(password));
		System.out.println("BCrypt 비교: " + passwordEncoding.matches(password, passwordEncoding.encode(password)));
	}
}

```

### 결과

```
SHA 암호화: 1ARVn2Auq2/WAqx2gNrL+q3RNjAzXpUfCXrzkA6d4Xa22yhRLy4AC50E+6UTPoscbo31nbOoq51gvkuXzJ6B2w==
SHA 비교: true
1. BCrypt 암호화: $2a$10$VxNTrB7uRCPyeJkd5hRey.V5QfB7SwX5DwDS7AFCnjbpyr1paF7Gu
1. BCrypt 비교: true
2. BCrypt 암호화: $2a$10$ydupZ3YTAsFaz9Jy7Q6jIe9mk3HlccvxTILF0/AIXPDCZXQT8wbRm
2. BCrypt 비교: true
Disconnected from the target VM, address: '127.0.0.1:63674', transport: 'socket'

Process finished with exit code 0
```

BCrypt 를 두번 암호화했을때 결과 값이 다르다는 것을 확인할 수 있다.

### GitHub 소스

https://github.com/syakuis/spring-syaku/tree/spring-security-passwordencoder

