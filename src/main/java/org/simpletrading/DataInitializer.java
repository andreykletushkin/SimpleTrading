package org.simpletrading;

import lombok.extern.slf4j.Slf4j;
import org.simpletrading.model.ProfileDetails;
import org.simpletrading.model.Symbol;
import org.simpletrading.model.User;
import org.simpletrading.repo.ProfileRepository;
import org.simpletrading.repo.SymbolRepository;
import org.simpletrading.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class DataInitializer implements CommandLineRunner {

    @Value("${tradeurl}")
    private String url;

    @Value("${token}")
    private String token;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private RestTemplate restTemplate = new RestTemplate();

    @Autowired
    SymbolRepository symbolRepository;

    @Override
    public void run(String... args) throws Exception {
      List<Symbol> symbols = restTemplate
                        .exchange(this.url.concat(token), HttpMethod.GET,null, new ParameterizedTypeReference<List<Symbol>>(){}).getBody();
        User user = User.builder()
                .username("usertest")
                .password(this.passwordEncoder.encode("password"))
                .roles(Arrays.asList( "ROLE_USER"))
                .build();
        ProfileDetails profileDetails = ProfileDetails.builder().firstName("AndreyKL") .user(user).build();
        user.setProfileDetails(profileDetails);
        this.userRepository.save(user);
        symbolRepository.saveAll(symbols);
        log.info("Loader completed");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
