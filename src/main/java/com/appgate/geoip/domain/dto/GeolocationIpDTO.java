package com.appgate.geoip.domain.dto;

import lombok.Data;

@Data
public class GeolocationIpDTO {
   private Long ipFrom;
   private Long ipTo;
   private String countryCode;
   private String country;
   private String region;
   private String city;
   private String latitude;
   private String longitude;
   private String timeZone;
}