package com.appgate.geoip.domain.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;

@Data
@Embeddable
public class IpId implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Column(name = "ip_from", insertable = false, updatable = false)
	private long ipFrom;
	@Column(name = "ip_to", insertable = false, updatable = false)
	private long ipTo;

	public IpId() {}
	
	public IpId(long ipFrom, long ipTo) {
		super();
		this.ipFrom = ipFrom;
		this.ipTo = ipTo;
	}
	
	

}