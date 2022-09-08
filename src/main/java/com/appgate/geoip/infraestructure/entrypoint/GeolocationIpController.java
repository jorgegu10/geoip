package com.appgate.geoip.infraestructure.entrypoint;

import java.util.Optional;

import org.springframework.batch.core.JobExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.appgate.geoip.domain.dto.IpAddressDTO;
import com.appgate.geoip.domain.runner.JobRunner;
import com.appgate.geoip.domain.service.GeolocationIpService;

/**
 * Controlador API REST GeoIp
 * 
 * @author jorge.gutierrez
 */
@RestController
@RequestMapping({ "/geoip" })
public class GeolocationIpController {
//	@Autowired
	private GeolocationIpService geolocationIpService;
	private JobRunner jobRunner;

	@Autowired
	public GeolocationIpController(JobRunner jobRunner, GeolocationIpService geolocationIpService) {
		this.jobRunner = jobRunner;
		this.geolocationIpService = geolocationIpService;
	}

	@PostMapping({ "/job" })
	public String runJob() {
		this.jobRunner.runBatchJob();
		return String.format("Job GeolocationIP submitted successfully.");
	}

	@PostMapping({ "/jobSync" })
	public String runJobSync() {
		JobExecution jobExecution = this.jobRunner.runBatchJobSync();
		return String.format("Job GeolocationIP is completed with status." + jobExecution.getStatus());
	}

	@GetMapping
	public ResponseEntity<?> getByIp(String ipFilter) {
		IpAddressDTO ipAddress = new IpAddressDTO(Optional.of(ipFilter));
		if (!ipAddress.isValidIpAddress()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("IP no tiene una estructura correcta.");
		} else {
			return ResponseEntity.ok(this.geolocationIpService
					.getByIp((String) ipAddress.getIpAddress().get()));
		}
	}
}