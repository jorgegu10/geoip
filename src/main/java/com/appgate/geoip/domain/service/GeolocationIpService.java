package com.appgate.geoip.domain.service;

import com.appgate.geoip.domain.dataprovider.IGeolocationIp;
import com.appgate.geoip.domain.model.GeolocationIp;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GeolocationIpService {
   @Autowired
   private IGeolocationIp iGeolocationIp;

   public GeolocationIp saveGeolocationIp(GeolocationIp geolocationIp) {
      return (GeolocationIp)this.iGeolocationIp.save(geolocationIp);
   }

   public Collection<GeolocationIp> getByIp(String ipFilter) {
      Long ipDecimal = this.ipToLong(ipFilter);
      return this.iGeolocationIp.findByIp(ipDecimal);
   }

   private long ipToLong(String ipAddress) {
      String[] ipAddressInArray = ipAddress.split("\\.");
      long result = 0L;

      for(int i = 0; i < ipAddressInArray.length; ++i) {
         int power = 3 - i;
         long ip = (long)Integer.parseInt(ipAddressInArray[i]);
         result = (long)((double)result + (double)ip * Math.pow(256.0D, (double)power));
      }

      return result;
   }
}