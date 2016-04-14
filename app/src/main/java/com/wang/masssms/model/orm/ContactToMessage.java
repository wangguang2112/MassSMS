package com.wang.masssms.model.orm;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "CONTACT_TO_MESSAGE".
 */
public class ContactToMessage {

    private Long id;
    private Integer cid;
    private Integer mid;

    public ContactToMessage() {
    }

    public ContactToMessage(Long id) {
        this.id = id;
    }

    public ContactToMessage(Long id, Integer cid, Integer mid) {
        this.id = id;
        this.cid = cid;
        this.mid = mid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public Integer getMid() {
        return mid;
    }

    public void setMid(Integer mid) {
        this.mid = mid;
    }

}