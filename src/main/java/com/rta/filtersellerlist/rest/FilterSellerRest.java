package com.rta.filtersellerlist.rest; 

import com.rta.filtersellerlist.dto.Bid;
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
import java.util.*;


@RestController
@RequestMapping("/filterSellers")
public class FilterSellerRest {

    @Value("${demo.domainname}")
    private String domainname;

    private RestTemplate carStoreService;
    private RestTemplate bidStoreService;
    private String bidStoreUrl;

    private Map<String, String> vars = new HashMap<>();

    private String carStoreUrl;

    public FilterSellerRest() {
        this.carStoreService = new RestTemplate();
        this.carStoreService.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        this.bidStoreService = new RestTemplate();
        this.bidStoreService.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
    }

    @PostConstruct
    public void setupurls() {
        String http = "http://";
        this.carStoreUrl      = http + domainname + "/api/filteredsellerscars/{name}";
        this.bidStoreUrl      = http + domainname + "/bidstore/getbidsforlist/";
    }

    @RequestMapping(value = "/for/{name}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseFilteredSellerLIstDto> getMyCarsforSale(@PathVariable String name) throws URISyntaxException {

        ResponseFilteredSellerLIstDto responseFilteredSellerLIstDto = new ResponseFilteredSellerLIstDto();
        responseFilteredSellerLIstDto.setCars(this.carStoreService.getForObject(this.carStoreUrl, ((Class<ArrayList<Car>>) (Object) ArrayList.class), name));
        doHighestBid(responseFilteredSellerLIstDto);
        return new ResponseEntity<>(responseFilteredSellerLIstDto, new HttpHeaders(), HttpStatus.OK);
    }

    public void doHighestBid(ResponseFilteredSellerLIstDto responseFilteredSellerLIstDto) {
        List<String> vins = getListofVins(responseFilteredSellerLIstDto.getCars());
        if(vins.isEmpty()) {
            responseFilteredSellerLIstDto.setHighestBidMap(new HashMap<>());
        } else {
            responseFilteredSellerLIstDto.setHighestBidMap(
                this.bidStoreService.postForObject(this.bidStoreUrl, vins, ((Class<Map<String, Bid>>) (Object) Map.class), vars)
            );
        }
    }

    private List<String> getListofVins(List<Car> sellersCars) {
        List<String> vins = new ArrayList<>();

        //This is odd, however, due to mashalling issues in Jackson its this or object mapper....
        for (Object o : sellersCars) {
            String vin =  (String)((LinkedHashMap)o).get("vin");
            vins.add(vin);
        }
        return vins;
    }
}
