package com.daiyanping.springcloud.common.config;

//@Configuration
//@EnableWebSecurity
//public class WebSecurity extends WebSecurityConfigurerAdapter {
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.httpBasic().and().authorizeRequests().anyRequest()
//                .fullyAuthenticated();
//        http.sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//        http.csrf().disable();
//        super.configure(http);
//    }
//
//    @Override
//    public void configure(AuthenticationManagerBuilder auth)
//            throws Exception {
//        auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder()).withUser("root").password(new BCryptPasswordEncoder().encode("enjoy")).roles("USER").
//                and().withUser("admin").password(new BCryptPasswordEncoder().encode("enjoy")).roles("USER", "ADMIN");
//    }
//
//}
