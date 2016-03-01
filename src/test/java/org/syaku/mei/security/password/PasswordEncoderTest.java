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

		System.out.println("1. BCrypt 암호화: " + passwordEncoding.encode(password));
		System.out.println("1. BCrypt 비교: " + passwordEncoding.matches(password, passwordEncoding.encode(password)));

		// 다시 암호화
		System.out.println("2. BCrypt 암호화: " + passwordEncoding.encode(password));
		System.out.println("2. BCrypt 비교: " + passwordEncoding.matches(password, passwordEncoding.encode(password)));
	}
}
