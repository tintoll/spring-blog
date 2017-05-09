package me.tintoll;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/*
	그리고 여기서 주의할 점은 우리는 LocalDatetime을 쓰고 있다. 아직 Jpa에서는 LocalDatetime을 지원하지 않는다.
	아마도 Jpa2.1이 나오고 java8이 릴리즈 되어기 때문이다. 그래서 데이터베이스와 LocalDatetime을 매핑 시켜줘야 하는데 아래와 같이 추가 하자.
	Jsr310JpaConverters 클래스는 Spring에 있는 클래스이며 클래스안에 static class 로 LocalDateTimeConverter가 있다.
	따로 구현하고 싶다면 LocalDateTimeConverter를 복사해 커스텀하게 만들면 된다. 그러면 굳이 EntityScan을 할필요는 없다.
 */
@SpringBootApplication
@EntityScan(basePackageClasses = {SpringBlogApplication.class, Jsr310JpaConverters.class})
public class SpringBlogApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBlogApplication.class, args);
	}

}
