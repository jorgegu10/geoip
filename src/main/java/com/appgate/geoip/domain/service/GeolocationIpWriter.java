package com.appgate.geoip.domain.service;

import com.appgate.geoip.domain.dataprovider.IGeolocationIp;
import com.appgate.geoip.domain.model.GeolocationIp;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Componente GeolocationIpWriter
 * 
 * @author jorge.gutierrez
 */
@Component
public class GeolocationIpWriter implements ItemWriter<GeolocationIp> {
   private static final Logger logger = LoggerFactory.getLogger(GeolocationIpWriter.class);
   @Autowired
   private IGeolocationIp iGeolocationIp;

   public void write(List<? extends GeolocationIp> geolocationIps) throws Exception {
      this.iGeolocationIp.saveAll(geolocationIps);
      logger.info("{} Geolocations Ips guardados en base de datos: " + geolocationIps.size());
   }
}