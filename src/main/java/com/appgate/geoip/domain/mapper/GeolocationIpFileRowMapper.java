package com.appgate.geoip.domain.mapper;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

import com.appgate.geoip.domain.dto.GeolocationIpDTO;

public class GeolocationIpFileRowMapper implements FieldSetMapper<GeolocationIpDTO> {
   
	@Override
	public GeolocationIpDTO mapFieldSet(FieldSet fieldSet) {
      GeolocationIpDTO geolocationIp = new GeolocationIpDTO();
      geolocationIp.setIpFrom(fieldSet.readLong("ipFrom"));
      geolocationIp.setIpTo(fieldSet.readLong("ipTo"));
      geolocationIp.setCity(fieldSet.readString("city"));
      geolocationIp.setCountry(fieldSet.readString("country"));
      geolocationIp.setCountryCode(fieldSet.readString("countryCode"));
      geolocationIp.setLatitude(fieldSet.readString("latitude"));
      geolocationIp.setLongitude(fieldSet.readString("longitude"));
      geolocationIp.setRegion(fieldSet.readString("region"));
      geolocationIp.setTimeZone(fieldSet.readString("timeZone"));
      return geolocationIp;
   }

   

}