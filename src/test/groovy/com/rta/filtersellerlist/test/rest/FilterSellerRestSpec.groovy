package com.rta.filtersellerlist.test.rest

import com.rta.filtersellerlist.rest.FilterSellerRest

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate
import spock.lang.Specification


class FilterSellerRestSpec extends Specification {

    FilterSellerRest filterSellerRest;

    def setup() {
        this.filterSellerRest        = new FilterSellerRest();
    }

    def "simple test for the rest endpoint"() {

        setup:
        RestTemplate carStoreService = Mock(RestTemplate)
        this.filterSellerRest.carStoreService = carStoreService;

        when:
        ResponseEntity responseEntity = this.filterSellerRest.getMyCarsforSale("Bob");

        then:
        1 * carStoreService.getForObject(_, _, _);
        responseEntity != null;
        responseEntity.getStatusCode() == HttpStatus.OK;
    }
}
