package com.appgate.geoip.domain.model;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "geolocationip")
public class GeolocationIp implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@EmbeddedId
	private IpId ipId;
	private String countryCode;
	private String country;
	private String region;
	private String city;
	private String latitude;
	private String longitude;
	private String timeZone;
	
	
	public GeolocationIp() {
	}
	
	public GeolocationIp(IpId ipId, String countryCode, String country, String region, String city, String latitude,
			String longitude, String timeZone) {
		super();
		this.ipId = ipId;
		this.countryCode = countryCode;
		this.country = country;
		this.region = region;
		this.city = city;
		this.latitude = latitude;
		this.longitude = longitude;
		this.timeZone = timeZone;
	}


	
	
}