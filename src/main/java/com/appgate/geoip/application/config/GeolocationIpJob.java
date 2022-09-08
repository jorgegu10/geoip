package com.appgate.geoip.application.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import com.appgate.geoip.domain.dto.GeolocationIpDTO;
import com.appgate.geoip.domain.mapper.GeolocationIpFileRowMapper;
import com.appgate.geoip.domain.model.GeolocationIp;
import com.appgate.geoip.domain.service.GeolocationIpProcessor;
import com.appgate.geoip.domain.service.GeolocationIpWriter;

/**
 * Configuración de JOB Spring Batch
 * 
 * @author jorge.gutierrez
 *
 */
@Configuration
public class GeolocationIpJob {
	private JobBuilderFactory jobBuilderFactory;
	private StepBuilderFactory stepBuilderFactory;
	private GeolocationIpProcessor geolocationProcessor;
	private GeolocationIpWriter geolocationIpWriter;
	private DataSource dataSource;

	@Autowired
	public GeolocationIpJob(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory,
			GeolocationIpProcessor geolocationProcessor, GeolocationIpWriter geolocationIpWrite,
			DataSource dataSource) {
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;
		this.geolocationProcessor = geolocationProcessor;
		this.geolocationIpWriter = geolocationIpWrite;
		this.dataSource = dataSource;
	}

	@Qualifier("geolocationIpJob")
	@Bean
	public Job processJob() throws Exception {
		return this.jobBuilderFactory.get("geolocationIpJob").start(this.step1GeolocationIp()).build();
	}

	@Bean
	public Step step1GeolocationIp() throws Exception {
		return this.stepBuilderFactory.get("step1")
				.<GeolocationIpDTO, GeolocationIp>chunk(10000)
				.reader(geolocationIpReader())
				.processor(geolocationProcessor)
				.writer(geolocationIpWriter)
				.taskExecutor(taskExecutor())
				.build();
	}

	@Bean
	@StepScope
	Resource inputFileResource(@Value("#{jobParameters[fileName]}") final String fileName) throws Exception {
		return new ClassPathResource(fileName);
	}

	@Bean
	@StepScope
	public FlatFileItemReader<GeolocationIpDTO> geolocationIpReader() throws Exception {
		FlatFileItemReader<GeolocationIpDTO> reader = new FlatFileItemReader<>();
		reader.setResource(this.inputFileResource((String) null));
		reader.setLineMapper(new DefaultLineMapper<GeolocationIpDTO>() {
			{
				this.setLineTokenizer(new DelimitedLineTokenizer() {
					{
						this.setNames(new String[] { "ipFrom", "ipTo", "countryCode", "country", "region", "city",
								"latitude", "longitude", "timeZone" });
						this.setDelimiter(",");
					}
				});
				this.setFieldSetMapper(new GeolocationIpFileRowMapper());
			}
		});
		return reader;
	}

	@Bean
	public TaskExecutor taskExecutor() {
		SimpleAsyncTaskExecutor simpleAsyncTaskExecutor = new SimpleAsyncTaskExecutor();
		simpleAsyncTaskExecutor.setConcurrencyLimit(8);
		return simpleAsyncTaskExecutor;
	}

	@Bean
	public JdbcBatchItemWriter<GeolocationIp> geolocationIpDBWriterDefault() {
		JdbcBatchItemWriter<GeolocationIp> itemWriter = new JdbcBatchItemWriter<GeolocationIp>();
		itemWriter.setDataSource(this.dataSource);
		itemWriter.setSql(
				"insert into geolocationip (ip_from, ip_to, country_code, country, region, city, latitude, longitude, time_zone) values (:ipId.ipFrom, :ipId.ipTo, :countryCode, :country, :region, :city, :latitude, :longitude, :timeZone)");
		itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<GeolocationIp>());
		return itemWriter;
	}
}