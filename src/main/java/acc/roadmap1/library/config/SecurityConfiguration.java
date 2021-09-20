package acc.roadmap1.library.config;

import acc.roadmap1.library.repository.AccountRepository;
import acc.roadmap1.library.repository.PrivilegeRepository;
import acc.roadmap1.library.repository.ReaderRepository;
import acc.roadmap1.library.repository.RoleRepository;
import acc.roadmap1.library.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public UserDetailsService userDetailsService(AccountRepository accountRepository, PasswordEncoder passwordEncoder
            , RoleRepository roleRepository, ReaderRepository readerRepository,
                                                 PrivilegeRepository privilegeRepository) {
        return new SecurityService(accountRepository, passwordEncoder, roleRepository, readerRepository,
                privilegeRepository);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/", "/register").permitAll()
                .antMatchers("/register/admin").hasAuthority("MANAGE_ACCOUNTS")
                .antMatchers("/*").authenticated()
                .and()
                .formLogin()
                    .loginPage("/login")
                    .loginProcessingUrl("/login")
                    .defaultSuccessUrl("/", true)
                    .permitAll()
                .and()
                .logout().logoutSuccessUrl("/").permitAll();
    }
}
