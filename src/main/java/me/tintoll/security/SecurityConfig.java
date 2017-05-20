package me.tintoll.security;


import lombok.RequiredArgsConstructor;
import me.tintoll.github.GithubClient;
import me.tintoll.github.GithubUser;
import me.tintoll.user.User;
import me.tintoll.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;

@Configuration
@RequiredArgsConstructor
@EnableOAuth2Sso
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers(HttpMethod.GET, "/posts/new").hasRole("ADMIN")
            .antMatchers(HttpMethod.GET, "/posts/{id}").permitAll()
            .antMatchers("/posts/**").hasRole("ADMIN")
            .antMatchers("/categories/**").hasRole("ADMIN")
            .antMatchers("/", "/js/**", "/vendor/**", "/codemirror/**", "/markdown/**", "/login/**", "/css/**", "/img/**", "/webjars/**").permitAll()
            .anyRequest().authenticated()
            .and()
            .csrf()
            .and()
            .formLogin()
            .loginPage("/login")
            .permitAll()
            .and()
            .logout()
            .logoutSuccessUrl("/")
            .permitAll()
            .and()
            .headers()
            .frameOptions().sameOrigin();
    }

    /*
        url에 대한 권한을 부여하며 github에 로그인이 되었을 경우에 map에 user정보가 담겨서 오는데
        그중 login(여기서 login은 username을 말한다.)이라는 키를 가져와 디비에 조회한 후 없으면 데이터베이스에 넣는다.
        또한 권한도 부여해야 되므로 name이 wonwoo(필자의 github username 만약 사용한다면 자신의 아이디로 바꿔주자)이면
        관리자 권한 그 외 로그인한 사용자들은 user 권한을 주었다. PrincipalExtractor 인터페이스는 Spring boot 1.4 에 추가 된 인터페이스다.
        기본적인 구현체는 FixedPrincipalExtractor 클래스를 사용한다.

     */

    @Bean
    public AuthoritiesExtractor authoritiesExtractor() {
        return map -> {
            String username = (String) map.get("login");
            if ("tintoll".contains(username)) {
                return AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER,ROLE_ADMIN");
            } else {
                return AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER");
            }
        };
    }

    @Bean
    public PrincipalExtractor principalExtractor(GithubClient githubClient, UserRepository userRepository) {
        return map -> {
            String githubLogin = (String) map.get("login");
            User loginUser = userRepository.findByGithub(githubLogin);
            if (loginUser == null) {
                logger.info("Initialize user with githubId {}", githubLogin);
                GithubUser user = githubClient.getUser(githubLogin);
                loginUser = new User(user.getEmail(), user.getName(), githubLogin, user.getAvatar());
                userRepository.save(loginUser);
            }
            return loginUser;
        };
    }

}
