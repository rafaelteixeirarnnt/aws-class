package br.com.leaf.class3.address.dto;

public record AddressResponse(String place_id,
                              String display_name,
                              String lat,
                              String lon) {
}
