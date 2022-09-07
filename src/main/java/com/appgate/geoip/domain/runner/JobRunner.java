package com.appgate.geoip.domain.runner;

import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class JobRunner {
   private static final Logger logger = LoggerFactory.getLogger(JobRunner.class);
   public static final String FILE_NAME_CONTEXT_KEY = "fileName";
   private JobLauncher simpleJobLauncher;
   private Job geolocationIpJob;

   @Autowired
   public JobRunner(Job geolocationIpJob, JobLauncher jobLauncher) {
      this.simpleJobLauncher = jobLauncher;
      this.geolocationIpJob = geolocationIpJob;
   }

   @Async
   public JobExecution runBatchJob() {
      JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
      jobParametersBuilder.addString("fileName", "ipgeo.csv");
      jobParametersBuilder.addDate("date", new Date(), true);
      return this.runJob(this.geolocationIpJob, jobParametersBuilder.toJobParameters());
   }

   public JobExecution runBatchJobSync() {
      JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
      jobParametersBuilder.addString("fileName", "ipgeo.csv");
      jobParametersBuilder.addDate("date", new Date(), true);
      return this.runJob(this.geolocationIpJob, jobParametersBuilder.toJobParameters());
   }

   public JobExecution runJob(Job job, JobParameters parameters) {
      JobExecution jobExecution = null;

      try {
         System.out.println("inicia");
         jobExecution = this.simpleJobLauncher.run(job, parameters);
         System.out.println("finaliza");
      } catch (JobExecutionAlreadyRunningException var5) {
         logger.info("Job with fileName={} is already running.", parameters.getParameters().get("fileName"));
      } catch (JobRestartException var6) {
         logger.info("Job with fileName={} was not restarted.", parameters.getParameters().get("fileName"));
      } catch (JobInstanceAlreadyCompleteException var7) {
         logger.info("Job with fileName={} already completed.", parameters.getParameters().get("fileName"));
      } catch (JobParametersInvalidException var8) {
         logger.info("Invalid job parameters.", parameters.getParameters().get("fileName"));
      }

      return jobExecution;
   }
}