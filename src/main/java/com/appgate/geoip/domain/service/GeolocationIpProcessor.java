package com.appgate.geoip.domain.service;

import com.appgate.geoip.domain.dto.GeolocationIpDTO;
import com.appgate.geoip.domain.model.GeolocationIp;
import com.appgate.geoip.domain.model.IpId;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

/**
 * Componente GeolocationIpProcessor
 * 
 * @author jorge.gutierrez
 */
@Component
public class GeolocationIpProcessor implements ItemProcessor<GeolocationIpDTO, GeolocationIp> {
   
	@Override
	public GeolocationIp process(GeolocationIpDTO geolocationIpDTO) throws Exception {
      GeolocationIp geolocationIp = new GeolocationIp();
      IpId ipId = new IpId();
      ipId.setIpFrom(geolocationIpDTO.getIpFrom());
      ipId.setIpTo(geolocationIpDTO.getIpTo());
      geolocationIp.setIpId(ipId);
      geolocationIp.setCity(geolocationIpDTO.getCity());
      geolocationIp.setCountry(geolocationIpDTO.getCountry());
      geolocationIp.setCountryCode(geolocationIpDTO.getCountryCode());
      geolocationIp.setLatitude(geolocationIpDTO.getLatitude());
      geolocationIp.setLongitude(geolocationIpDTO.getLongitude());
      geolocationIp.setRegion(geolocationIpDTO.getRegion());
      geolocationIp.setTimeZone(geolocationIpDTO.getTimeZone());
      return geolocationIp;
   }

}