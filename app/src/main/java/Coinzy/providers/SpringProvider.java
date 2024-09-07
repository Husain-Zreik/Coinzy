package Coinzy.providers;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import Coinzy.config.SpringConfig;

public class SpringProvider {

    private static final ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);

    private SpringProvider() {
        // Prevent instantiation
    }

    public static ApplicationContext getContext() {
        return context;
    }
}
