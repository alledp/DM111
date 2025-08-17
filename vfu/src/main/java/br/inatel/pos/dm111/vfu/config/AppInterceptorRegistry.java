package br.inatel.pos.dm111.vfu.config;

import br.inatel.pos.dm111.vfu.api.core.interceptor.AuthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class AppInterceptorRegistry implements WebMvcConfigurer {

    private static final List<String> ENDPOINTS_PATTERN = List.of(
            "/valefood/users**",
            "/valefood/users/**",
            "/valefood/restaurants**",
            "/valefood/restaurants/**"
    );

    private final AuthInterceptor interceptor;

    public AppInterceptorRegistry(AuthInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor).addPathPatterns(ENDPOINTS_PATTERN);
    }
}
