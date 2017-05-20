package me.tintoll;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import org.springframework.web.client.RestTemplate;
import org.thymeleaf.dialect.springdata.SpringDataDialect;

import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;
import java.util.concurrent.TimeUnit;


/*
	그리고 여기서 주의할 점은 우리는 LocalDatetime을 쓰고 있다. 아직 Jpa에서는 LocalDatetime을 지원하지 않는다.
	아마도 Jpa2.1이 나오고 java8이 릴리즈 되어기 때문이다. 그래서 데이터베이스와 LocalDatetime을 매핑 시켜줘야 하는데 아래와 같이 추가 하자.
	Jsr310JpaConverters 클래스는 Spring에 있는 클래스이며 클래스안에 static class 로 LocalDateTimeConverter가 있다.
	따로 구현하고 싶다면 LocalDateTimeConverter를 복사해 커스텀하게 만들면 된다. 그러면 굳이 EntityScan을 할필요는 없다.
 */
@SpringBootApplication
@EntityScan(basePackageClasses = {SpringBlogApplication.class, Jsr310JpaConverters.class})
@EnableCaching
public class SpringBlogApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBlogApplication.class, args);
	}

	@Bean
	public SpringDataDialect springDataDialect() {
		return new SpringDataDialect();
	}



	/*
		캐시 설정
		blog.category라는 키에 1분동안 캐시를 한다고 지정해줬다.
		Duration 에는 ONE_DAY, ONE_HOUR, THIRTY_MINUTES, TEN_MINUTES 기타 등등
		여러가지의 시간이 있기는 하지만 거기에 없는 시간이 있다면 만들면 된다.

		설정 속성중에 statisticsEnabled 모니터링을 할 수 있는 속성이다. 해당 캐시의 통계를 확인 할 수 있다. jconsole로 확인 가능하다.
	 */
	public static final Duration TEN_SECONDS = new Duration(TimeUnit.SECONDS, 10);

	@Bean
	public JCacheManagerCustomizer cacheManagerCustomizer() {
		return cm -> {
			cm.createCache("blog.category", initConfiguration(TEN_SECONDS));
			cm.createCache("github.user", initConfiguration(Duration.ONE_HOUR));
		};
	}

	private MutableConfiguration<Object, Object> initConfiguration(Duration duration) {
		return new MutableConfiguration<>().setStatisticsEnabled(true)
				.setExpiryPolicyFactory(CreatedExpiryPolicy.factoryOf(duration));
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
