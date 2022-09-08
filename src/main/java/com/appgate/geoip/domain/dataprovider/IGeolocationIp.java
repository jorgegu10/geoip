package com.appgate.geoip.domain.dataprovider;

import com.appgate.geoip.domain.model.GeolocationIp;
import com.appgate.geoip.domain.model.IpId;
import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Interfaz IGeolocationIp
 * 
 * @author jorge.gutierrez
 */
@Repository
public interface IGeolocationIp extends JpaRepository<GeolocationIp, IpId> {
   @Query(
      value = "SELECT * FROM geolocationip WHERE :ipFilter BETWEEN ip_from AND ip_to",
      nativeQuery = true
   )
   Collection<GeolocationIp> findByIp(@Param("ipFilter") Long ipFilter);
}