package org.simpletrading.rsocket;

//import org.springframework.messaging.handler.annotation.MessageMapping;
import lombok.extern.slf4j.Slf4j;
import org.simpletrading.model.Symbol;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;

@Service
@Slf4j
public class RSocketService {

    private RestTemplate restTemplate = new RestTemplate();
    private String URL = "https://sandbox.iexapis.com/beta/stock/";
    private String token ="Tsk_bda97d9b9ef14982ac3459540d20bce7";

    public Flux<Long> getPriceBySym(String symbol) {
        //String url="https://sandbox.iexapis.com/beta/stock/AACL/price?token=
        String URL = String.format(this.URL+"%s/price/?token=%s",symbol,token);
        log.info(URL);
        return Flux
                .interval(Duration.ZERO, Duration.ofSeconds(10))
                .map(i -> new Long(restTemplate
                        .exchange(URL, HttpMethod.GET,null, Long.class)
                        .getBody()));
    }
}
