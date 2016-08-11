package com.rta.filtersellerlist.rest; 

import com.rta.filtersellerlist.dto.Car;
import com.rta.filtersellerlist.dto.ResponseFilteredSellerLIstDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/filterSellers")
public class FilterSellerRest {

    @Value("${demo.domainname}")
    private String domainname;

    private RestTemplate carStoreService;

    private String carStoreUrl;

    public FilterSellerRest() {
        this.carStoreService = new RestTemplate();
        this.carStoreService.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
    }

    @PostConstruct
    public void setupurls() {
        String http = "http://";
        this.carStoreUrl      = http + domainname + "/api/filteredsellerscars/{name}";
    }

    @RequestMapping(value = "/for/{name}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseFilteredSellerLIstDto> getMyCarsforSale(@PathVariable String name) throws URISyntaxException {

        List<Car> sellersCars = this.carStoreService.getForObject(this.carStoreUrl, ((Class<ArrayList<Car>>) (Object) ArrayList.class), name);

        ResponseFilteredSellerLIstDto responseBuyerWatching = new ResponseFilteredSellerLIstDto();
        responseBuyerWatching.setCars(sellersCars);

        return new ResponseEntity<>(responseBuyerWatching, new HttpHeaders(), HttpStatus.OK);
    }
}
