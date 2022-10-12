package com.wejuai.accounts.config;

import com.wejuai.accounts.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author YQ.Huang
 */
@Configuration
public class SecurityConfig {
    public static final String SESSION_LOGIN = "login";
    public static final String BIND_REQUEST = "bindInfo";
    public static final String WX_SESSION_INFO = "wxInfo";
    public static final String WX_SESSION_KEY = "sessionKey";

    private final UserRepository userRepository;

    public SecurityConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    ApiSecurityFilter apiSecurityFilter() {
        return new ApiSecurityFilter(userRepository);
    }

    @Bean
    FilterRegistrationBean<ApiSecurityFilter> apiSecurityFilterRegistration(ApiSecurityFilter apiSecurityFilter) {
        FilterRegistrationBean<ApiSecurityFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(apiSecurityFilter);
        registration.addUrlPatterns("/api/*");
        return registration;
    }

    public static class ApiSecurityFilter implements Filter {

        private static final Logger logger = LoggerFactory.getLogger(ApiSecurityFilter.class);

        private final UserRepository userRepository;

        public ApiSecurityFilter(UserRepository userRepository) {
            this.userRepository = userRepository;
        }

        @Override
        public void init(FilterConfig filterConfig) {
            logger.info("api安全框架启动");
        }

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
            HttpServletRequest servletRequest = (HttpServletRequest) request;
            String ip = servletRequest.getHeader("x-real-ip");
            if (StringUtils.isBlank(ip)) {
                ip = request.getRemoteAddr();
            }
            logger.debug("访问用户来源ip: " + ip);
            HttpServletResponse servletResponse = (HttpServletResponse) response;
            Object userId = servletRequest.getSession().getAttribute(SESSION_LOGIN);
            if (userId == null) {
                if (!servletRequest.getContextPath().contains("validateLogin")) {
                    logger.debug("Session中不存在[{}]属性，用户未登录，ip：[{}]", SESSION_LOGIN, ip);
                }
                servletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
            if (userRepository.isBan(userId.toString())) {
                servletRequest.getSession().removeAttribute(SESSION_LOGIN);
                servletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "该账号已被封禁");
                return;
            }
            if (userRepository.isDel(userId.toString())){
                servletRequest.getSession().removeAttribute(SESSION_LOGIN);
                servletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);

                return;
            }
            chain.doFilter(request, response);
        }

        @Override
        public void destroy() {
        }
    }

}
