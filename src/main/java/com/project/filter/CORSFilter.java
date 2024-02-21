//package com.project.filter;
//
//import jakarta.servlet.*;
//import org.springframework.stereotype.Component;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//import org.springframework.web.filter.CorsFilter;
//
//import java.io.IOException;
//import java.util.Arrays;
//import java.util.List;
//
//
//@Component
//public class CORSFilter implements Filter {
//
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//        Filter.super.init(filterConfig);
//    }
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
////        public CorsFilter corsFilter() {
//            CorsConfiguration corsConfiguration = new CorsConfiguration();
//            corsConfiguration.setAllowCredentials(true);
//            corsConfiguration.setAllowedOrigins(List.of("http://localhost:4200"));
//            corsConfiguration.setAllowedHeaders(Arrays.asList("Origin", "Access-Control-Allow-Origin", "Content-Type",
//                    "Accept", "Authorization", "Origin, Accept", "X-Requested-With",
//                    "Access-Control-Request-Method", "Access-Control-Request-Headers"));
//            corsConfiguration.setExposedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Authorization",
//                    "Access-Control-Allow-Origin", "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"));
//            corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//            UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
//            urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
////            return new CorsFilter(urlBasedCorsConfigurationSource);
//            CorsFilter corsFilter = new CorsFilter(urlBasedCorsConfigurationSource);
//            corsFilter.doFilter(request, response, chain);
////        }
//    }
//
//    @Override
//    public void destroy() {
//        Filter.super.destroy();
//    }
//}
