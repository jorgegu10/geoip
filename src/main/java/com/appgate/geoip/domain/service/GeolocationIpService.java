package com.appgate.geoip.domain.service;

import java.util.Collection;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appgate.geoip.domain.dataprovider.IGeolocationIp;
import com.appgate.geoip.domain.dto.response.ResponseGeoIp;
import com.appgate.geoip.domain.exception.BusinessException;
import com.appgate.geoip.domain.model.GeolocationIp;

/**
 * Service GeolocationIpService
 * 
 * @author jorge.gutierrez
 */
@Service
public class GeolocationIpService {
	
	private static final Logger logger = LoggerFactory.getLogger(GeolocationIpService.class);
	
	@Autowired
	private IGeolocationIp iGeolocationIp;

	/**
	 * Servicio encargado de ir a consultar la información de una IP dada en Base de
	 * datos
	 * 
	 * @param ipFilter
	 * @return
	 */
	public ResponseGeoIp getByIp(String ipFilter) {
		try {
			Optional<GeolocationIp> ip;
			Long ipDecimal;
			ipDecimal = this.ipToLong(ipFilter);
			Collection<GeolocationIp> ipsList = this.iGeolocationIp.findByIp(ipDecimal);
			ip = ipsList.stream().findFirst();
			
			return ip.isPresent() ? mappingResponseGeoIpSuccess(ip.get())
					: mappingResponseGeoIpError("No se encontró información para la IP dada");
		} catch (BusinessException e) {
			logger.error("Ha ocurrido un error al buscar la IP",e);
			return mappingResponseGeoIpError("Ha ocurrido un error al buscar la IP");
		}
	}

	/**
	 * Retorna la ip en formato decimal
	 * 
	 * @param ipAddress
	 * @return
	 * @throws BusinessException 
	 */
	private long ipToLong(String ipAddress) throws BusinessException {
		try {
			String[] ipAddressInArray = ipAddress.split("\\.");
			long result = 0L;
			
			for (int i = 0; i < ipAddressInArray.length; ++i) {
				int power = 3 - i;
				long ip = (long) Integer.parseInt(ipAddressInArray[i]);
				result = (long) ((double) result + (double) ip * Math.pow(256.0D, (double) power));
			}
			
			return result;
		} catch (Exception e) {
			throw new BusinessException("Ha ocurrido un error convirtiendo la ip a formato decimal", e);
		}

	}

	/**
	 * Mapea objeto de respuesta
	 * 
	 * @param responseObj
	 * @return
	 */
	private ResponseGeoIp mappingResponseGeoIpSuccess(GeolocationIp responseObj) {

		ResponseGeoIp response = new ResponseGeoIp();
		response.setCity(responseObj.getCity());
		response.setCountry(responseObj.getCountry());
		response.setCountryCode(responseObj.getCountryCode());
		response.setRegion(responseObj.getRegion());
		response.setTimeZone(responseObj.getTimeZone());

		response.setStatusCode("0");
		response.setStatusDesc("SUCCESS");
		return response;
	}

	/**
	 * Mapea objeto de respuesta
	 * 
	 * @param responseObj
	 * @return
	 */
	private ResponseGeoIp mappingResponseGeoIpError(String error) {

		ResponseGeoIp response = new ResponseGeoIp();
		response.setStatusCode("1F");
		response.setStatusDesc(error);
		return response;
	}
}