package com.rta.filtersellerlist.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ResponseFilteredSellerLIstDto {

    List<Car> cars;

    Map<String, Bid> highestBidMap;
}
