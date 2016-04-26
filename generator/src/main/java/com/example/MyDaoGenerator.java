package com.example;


import java.util.Date;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;

/**
 * Created by wangguang on 2016/3/25.
 */
public class MyDaoGenerator {
    public static void main(String[] args)throws Exception{
        Schema schema=new Schema(1,"com.wang.masssms.model.orm");
        addEntity(schema);
        new DaoGenerator().generateAll(schema,"./app/src/main/java");
    }
    public static void addEntity(Schema schema){
//        private int id;
//        private int phonenumber;
//        private int name;
//        private int groupid;
//        private Date creattime;
//        private Date lastmodify;
        Entity contacts= schema.addEntity("Contacts");
        contacts.addIdProperty().primaryKey().autoincrement();
        contacts.addStringProperty("phonenumber");
        contacts.addStringProperty("name").unique();
        contacts.addDateProperty("creattime");
        contacts.addDateProperty("lastmodify");
//        private int id;
//        private String name;
//        private Date creatTime;
        Entity contactGroup=schema.addEntity("ContactGroup");
        contactGroup.addIdProperty().primaryKey().autoincrement();
        contactGroup.addStringProperty("name").unique();
        contactGroup.addDateProperty("creatTime");
//        private int id;
//        private int sendtime;
//        private int gid;
//        private String  text;

        Entity message=schema.addEntity("Message");
        message.addIdProperty().primaryKey().autoincrement();
        message.addDateProperty("sendtime");
        message.addStringProperty("text");
        message.addBooleanProperty("iscollect");
        message.addBooleanProperty("isdraft");

        Entity contactToGroup=schema.addEntity("ContactToGroup");
        contactToGroup.addIdProperty().autoincrement().primaryKey();
        Property cid=contactToGroup.addLongProperty("cid").getProperty();
        Property gid=contactToGroup.addLongProperty("gid").getProperty();
        contactToGroup.addToOne(contacts, cid);
        contactToGroup.addToOne(contactGroup, gid);
        contacts.addToMany(contactToGroup, cid).setName("cid");
        contactGroup.addToMany(contactToGroup, gid).setName("gid");

        Entity contactToMessage=schema.addEntity("ContactToMessage");
        contactToMessage.addIdProperty().primaryKey().autoincrement();
        Property mcid=contactToMessage.addLongProperty("cid").getProperty();
        Property mid=contactToMessage.addLongProperty("mid").getProperty();
        contactToMessage.addToOne(contacts, mcid);
        contactToMessage.addToOne(message, mid);
        contacts.addToMany(contactToMessage, mcid).setName("cmid");
        message.addToMany(contactToMessage, mid).setName("mid");

        Entity groupToMessage=schema.addEntity("GroupToMessage");
        groupToMessage.addIdProperty().primaryKey().autoincrement();
        Property mgid=groupToMessage.addLongProperty("gid").getProperty();
        Property mmid=groupToMessage.addLongProperty("mid").getProperty();
        groupToMessage.addToOne(contactGroup, mgid);
        groupToMessage.addToOne(message, mmid);
        contactGroup.addToMany(groupToMessage, mgid);
        message.addToMany(groupToMessage, mmid);

    }

}
