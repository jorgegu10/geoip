package com.appgate.geoip.domain.dto.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResponseGeoIp extends ResponseGeneral {

	private String countryCode;
	private String country;
	private String region;
	private String city;
	private String timeZone;
	
	public ResponseGeoIp() {}
	public ResponseGeoIp(String countryCode, String country, String region, String city, String timeZone) {
		super();
		this.countryCode = countryCode;
		this.country = country;
		this.region = region;
		this.city = city;
		this.timeZone = timeZone;
		
	}
	
	
	
}
