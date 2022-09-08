package com.appgate.geoip.infraestructure.entrypoint;

import javax.servlet.http.HttpServletRequest;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.appgate.geoip.domain.dto.response.ResponseGeoIp;
import com.appgate.geoip.domain.runner.JobRunner;
import com.appgate.geoip.domain.service.GeolocationIpService;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class GeoipApplicationTest {

	private static final String IP_TEST_DEFAULT = "1.0.136.192";
	
    private static final String COUNTRY_CODE_DEFAULT = "NL";
    private static final String COUNTRY_DEFAULT = "NETHERLANDS";
    private static final String REGION_DEFAULT = "DRENTHE";
    private static final String CITY_DEFAULT = "ASSEN";
    private static final String TIME_ZONE_DEFAULT = "3NT SOLUTIONS LLP";
    
    @Mock
    private GeolocationIpService geolocationIpService;
    @Mock
    private JobRunner jobRunner ;
    @Mock
    private JobLauncher simpleJobLauncher;
    @Mock
    private Job geolocationIpJob;

    @InjectMocks
    private GeolocationIpController geolocationIpController;
    

    /** {@link UserController#retrieveAllUsers(HttpServletRequest)} 
     * @throws Exception */

    @Test
    public void testGetByIp() throws Exception {
    	ResponseGeoIp response = createDefaultGeolocationIp();
        Mockito.when(geolocationIpService.getByIp(IP_TEST_DEFAULT)).thenReturn(response);
        
        ResponseEntity<?> responseEntity = geolocationIpController.getByIp(IP_TEST_DEFAULT);

        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assert.assertEquals(200, responseEntity.getStatusCodeValue());
        Assert.assertEquals(response, responseEntity.getBody());
    }
    
    @Test
    public void testRunJob() throws Exception {
        JobExecution jobExecution = new JobExecution(99999L);
        jobExecution.setStatus(BatchStatus.COMPLETED);
        
        Mockito.when(jobRunner.runBatchJobSync()).thenReturn(jobExecution);
        
        String response = geolocationIpController.runJobSync();
        
        Assert.assertEquals(response, "Job GeolocationIP is completed with status.COMPLETED");
    }
    
    @Test
    public void testGetByIpEmpty() throws Exception {
    	ResponseGeoIp responseError = createErrorGeolocationIp();
    	Mockito.when(geolocationIpService.getByIp(IP_TEST_DEFAULT)).thenReturn(responseError);
    	
    	ResponseEntity<?> responseEntity = geolocationIpController.getByIp(IP_TEST_DEFAULT);
    	
    	Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    	Assert.assertEquals(200, responseEntity.getStatusCodeValue());
    	Assert.assertEquals(responseError, responseEntity.getBody());
    }

    @Test
    public void testValidateWrongIpAddress() {
    	String IP_WRONG = "1223.ABC.3333.333";
    	ResponseEntity<?> responseEntity = geolocationIpController.getByIp(IP_WRONG);
    	
    	Assert.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        Assert.assertEquals("IP no tiene una estructura correcta.", responseEntity.getBody());
    }

    private static ResponseGeoIp createDefaultGeolocationIp() {
    	ResponseGeoIp responseDefault = new ResponseGeoIp(COUNTRY_CODE_DEFAULT, COUNTRY_DEFAULT, REGION_DEFAULT, CITY_DEFAULT, TIME_ZONE_DEFAULT);
    	responseDefault.setStatusCode("0");
    	responseDefault.setStatusDesc("SUCCESS");
    	return responseDefault;
    }
    private static ResponseGeoIp createErrorGeolocationIp() {
    	ResponseGeoIp responseDefault = new ResponseGeoIp();
    	responseDefault.setStatusCode("1F");
    	responseDefault.setStatusDesc("Ha ocurrido un error al buscar la IP");
    	return responseDefault;
    }

}
