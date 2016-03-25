package org.syaku.demo.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.syaku.demo.Print;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @site http://syaku.tistory.com
 * @since 16. 3. 25.
 */

@Aspect
@Component
public class DemoAOP {

	@Pointcut("execution(* org.syaku.demo.web.*Controller.*(..))")
	public void pointcut() {
	}

	@Around("pointcut()")
	public Object run(ProceedingJoinPoint point) throws Throwable {

		Print.text("Spring AOP before");
		Object object = point.proceed();
		Print.text("Spring AOP after");
		return object;
	}
}
