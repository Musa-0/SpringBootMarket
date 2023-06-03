package com.ClothesShop.java.clothesShop.configurations;



import com.ClothesShop.java.clothesShop.services.AccountService;
import com.ClothesShop.java.clothesShop.utils.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    AccountService accountService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()//укажем url требующие авторизации
                .antMatchers("/products/product/**").permitAll()
                .antMatchers("/organizations/organization/**").permitAll()
                .antMatchers("/accounts/account/**").permitAll()
                .antMatchers("/auth/**").permitAll()
                .antMatchers("/static/**").permitAll()
                .antMatchers("/media/**").permitAll()
                .antMatchers("/").permitAll()
                .antMatchers("/admin/**").hasAuthority("ADMIN")
                .anyRequest().authenticated()
                .and()
                //.and().logout().invalidateHttpSession(true).deleteCookies("JSESSIONID")
                .csrf().disable();

        http.headers().httpStrictTransportSecurity().disable();
        // second
        http.headers().httpStrictTransportSecurity()
                .maxAgeInSeconds(0)
                .includeSubDomains(true);
    }


    @Bean
   protected DaoAuthenticationProvider daoAuthenticationProvider(){
        // Его задача, проверять существует ли тот или иной пользователь или нет
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder.getBCryptPasswordEncoder());       //укажем что надо хешировать по функции passwordEncoder()
        authenticationProvider.setUserDetailsService(accountService);         //тут мы передаем сервис, который будет говорить
        // как нам находить пользователей по имени из базы(аутентификация)
        return authenticationProvider;

   }

}
