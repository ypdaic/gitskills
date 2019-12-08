package com.daiyanping.cms.mongodb;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Sorts.*;
import static com.mongodb.client.model.Aggregates.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.bson.BSON;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runner.manipulation.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.PushOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.operation.OrderBy;

public class JavaAuthTest {

	private static final Logger logger = LoggerFactory
			.getLogger(JavaAuthTest.class);

	private MongoDatabase db;

	private MongoCollection<Document> collection;


	private MongoClient client;

	@Before
	public void init() {
		MongoCredential createCredential = 
				MongoCredential.createCredential("lison", "lison", "lison".toCharArray());
		MongoClientOptions mco = MongoClientOptions.builder()
				.writeConcern(WriteConcern.JOURNALED)
				.connectionsPerHost(100)
				.readPreference(ReadPreference.secondary())
				.threadsAllowedToBlockForConnectionMultiplier(5)
				.maxWaitTime(120000).connectTimeout(10000).build();
		List<ServerAddress> asList = Arrays.asList(
				new ServerAddress("192.168.111.128",27017));
		this.client = new MongoClient(asList, createCredential,mco);
		db = client.getDatabase("lison");
		collection = db.getCollection("users");
	}

	// -----------------------------操作符使用实例------------------------------------------

	// db.users.find({"username":{"$in":["lison", "mark", "james"]}}).pretty()
	// 查询姓名为lison、mark和james这个范围的人
	@Test
	public void testInOper() {
		Bson in = in("username", "lison", "mark", "james");
		FindIterable<Document> find = collection.find(in);
		printOperation(find);
	}
	
	// ---------------------------------------------------------------------------

	//返回对象的处理器，打印每一行数据
	private Block<Document> getBlock(final List<Document> ret) {
		Block<Document> printBlock = new Block<Document>() {
			@Override
			public void apply(Document t) {
				logger.info("---------------------");
				logger.info(t.toJson());
				logger.info("---------------------");
				ret.add(t);
			}
		};
		return printBlock;
	}

	//打印查询出来的数据和查询的数据量
	private void printOperation( FindIterable<Document> find) {
		final List<Document> ret = new ArrayList<Document>();
		Block<Document> printBlock = getBlock(ret);
		find.forEach(printBlock);
		System.out.println(ret.size());
		ret.removeAll(ret);
	}
}