package kg.peaksoft.giftlistb6.configs.security;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true
)
public class WebAppSecurity {

    @Bean
    @SneakyThrows
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, TokenVerifyFilter filter) {

        httpSecurity.cors().and().csrf().disable()
                .authorizeHttpRequests(auth -> { auth
                        .antMatchers("api/public/**").permitAll()
                        .antMatchers("/api-docs", "/v3/api-docs")
                            .permitAll()
                            .anyRequest()
                            .permitAll();
                });

        httpSecurity.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
