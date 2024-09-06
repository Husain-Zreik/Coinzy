package Coinzy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import Coinzy.providers.UserProvider;
import Coinzy.sessions.UserSession;

@Configuration
@ComponentScan(basePackages = "Coinzy")
public class SpringConfig {

    @Bean
    public UserProvider userProvider() {
        return new UserProvider();
    }

    @Bean
    public UserSession userSession() {
        return new UserSession();
    }
}
