package com.sanri.test.mongodb;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.Test;
import org.springframework.data.mongodb.core.convert.MongoConverter;

import java.util.Iterator;
import java.util.List;

/**
 * 不结合 spring 使用 mongodb 的增删改查
 */
public class MongoCRUD {

    private String collectionName = "normal";

    @Test
    public void first(){
        MongoClient mongoClient = new MongoClient("localhost",27017);
        MongoDatabase database = mongoClient.getDatabase("test");
        MongoCollection<Document> collection = database.getCollection(collectionName);
        Document doc = new Document();
        doc.append("name","张三");
        doc.append("age",123);
        collection.insertOne(doc);
    }

    @Test
    public void testFullConstructor(){
        MongoClient mongoClient = new MongoClient("",27017);
        List<ServerAddress> serverAddressList = mongoClient.getServerAddressList();
    }

    @Test
    public void cursor(){
        MongoClient mongoClient = new MongoClient("localhost",27017);
        MongoDatabase database = mongoClient.getDatabase("test");
        MongoCollection<Document> collection = database.getCollection(collectionName);
    }

    @Test
    public void oldApi(){
        Mongo mongo = new Mongo("localhost",27017);
        DB db = mongo.getDB("test");
        DBCollection collection = db.getCollection(collectionName);

        DBCursor dbCursor = collection.find();
        Iterator<DBObject> iterator = dbCursor.iterator();
        while (iterator.hasNext()){
            DBObject next = iterator.next();
            Object age = next.get("age");
            System.out.println(age);
            System.out.println(next);
        }

        DBObject one = collection.findOne("5ee03fccf5c4001b6cadcab1");
        System.out.println(one);
    }
}
