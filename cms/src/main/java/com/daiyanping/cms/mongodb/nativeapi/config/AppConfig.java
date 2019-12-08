package com.daiyanping.cms.mongodb.nativeapi.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;

/**
 * 配置类的形式定义MongoClient
 */
@Configuration
public class AppConfig {

  /*
   * Use the standard Mongo driver API to create a com.mongodb.MongoClient instance.
   */
	@Bean(name="mongo")
   public MongoClient mongoClient() {
	   
//		MongoCredential createCredential = 
//				MongoCredential.createCredential("lison", "lison", "lison".toCharArray());
		
		WriteConcern wc = WriteConcern.W1.withJournal(true);
		MongoClientOptions mco = MongoClientOptions.builder()
				.writeConcern(wc)
				.connectionsPerHost(100)
				.readPreference(ReadPreference.secondary())
				.threadsAllowedToBlockForConnectionMultiplier(5)
				.maxWaitTime(120000).connectTimeout(10000).build();
		List<ServerAddress> asList = Arrays.asList(
				new ServerAddress("192.168.244.123", 27017));

	   MongoClient client = new MongoClient(asList, mco);
       return client;
   }
}