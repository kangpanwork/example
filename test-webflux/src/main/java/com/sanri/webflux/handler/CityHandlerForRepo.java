package com.sanri.webflux.handler;

import com.sanri.webflux.repository.CityRepository;
import com.sanri.webflux.repository.domain.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class CityHandlerForRepo {
    @Autowired
    private CityRepository cityRepository;

    public Mono<Long> save(City city){
        return Mono.create(cityMonoSink -> cityMonoSink.success(cityRepository.save(city)));
    }

    public Mono<City> findCityById(Long id){
        return Mono.justOrEmpty(cityRepository.findCityById(id));
    }

    public Flux<City> findAllCity(){
        return Flux.fromIterable(cityRepository.findAll());
    }

    public Mono<Long> modifyCity(City city){
       return Mono.create(cityMonoSink -> cityMonoSink.success(cityRepository.updateCity(city)));
    }

    public Mono<Long> deleteCity(Long id){
        return Mono.create(cityMonoSink -> cityMonoSink.success(cityRepository.delectCity(id)));
    }
}
