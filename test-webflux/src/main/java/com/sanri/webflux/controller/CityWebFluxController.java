package com.sanri.webflux.controller;

import com.sanri.webflux.handler.CityHandlerForRepo;
import com.sanri.webflux.repository.domain.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;

@RestController
@RequestMapping("/city")
public class CityWebFluxController {
    @Autowired
    private CityHandlerForRepo cityHandlerForRepo;

    @GetMapping("/{id}")
    public Mono<City> findCityById(@PathVariable("id") Long id ){
        return cityHandlerForRepo.findCityById(id);
    }

    @GetMapping
    public Flux<City> findAllCity(){
        return cityHandlerForRepo.findAllCity();
    }

    @PostMapping
    public Mono<Long> saveCity(@RequestBody City city){
        return cityHandlerForRepo.save(city);
    }

    @PutMapping
    public Mono<Long> modifyCity(@RequestBody City city){
        return cityHandlerForRepo.modifyCity(city);
    }

    @DeleteMapping("/{id}")
    public Mono<Long> deleteCity(@PathVariable("id") Long id){
        return cityHandlerForRepo.deleteCity(id);
    }
}
