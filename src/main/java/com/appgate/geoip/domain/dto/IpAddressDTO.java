package com.appgate.geoip.domain.dto;

import java.util.Optional;
import java.util.regex.Pattern;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class IpAddressDTO {
   private Optional<String> ipAddress;
   private static final Pattern PATTERN = Pattern.compile(
		   "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

   public IpAddressDTO(Optional<String> ipAddress) {
      this.ipAddress = ipAddress;
   }
   
	public boolean isValidIpAddress() {
		if(ipAddress.isPresent()) {
			return PATTERN.matcher(ipAddress.get()).matches();
		}
		return Boolean.FALSE;
	}

}