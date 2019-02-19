package com.mca.Model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;
import io.realm.annotations.Required;

@RealmClass
public class Item extends RealmObject {

    @PrimaryKey
    private String id;

    @Required
    private String name;
    private String phone;
    private String image;
    private String group_id;

    public Item() {
    }

    public Item(String id, String name, String phone, String image, String group_id) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.image = image;
        this.group_id = group_id;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}