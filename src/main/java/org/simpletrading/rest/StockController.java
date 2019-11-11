package org.simpletrading.rest;


import org.simpletrading.repo.SymbolRepository;
import org.simpletrading.rsocket.RSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
//import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/stock")
public class StockController {

    @Autowired
    private SymbolRepository symbolRepository;

    @GetMapping()
    public ResponseEntity getSymbols() {
        return new ResponseEntity<>(symbolRepository.findAll(), HttpStatus.OK);
    }

    @Autowired
    private RSocketService rSocketService;

    @GetMapping(value = "/{symbol}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Long> getBySymbol(@PathVariable String symbol) {
        return rSocketService.getPriceBySym(symbol);
    }


}
