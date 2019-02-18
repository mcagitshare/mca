package com.mca.Model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Group extends RealmObject {

    @PrimaryKey
    private String Id;

    @Required
    private String GroupId;
    private String ContactId;
    private String Name;
    private String Image;
    private String Phone;
    private Boolean ReadStatus;
    private Boolean AccRej;
    private String option;

    public Group() {
    }

    public Group(String id, String groupId, String contactId, String name, String image, String phone,
                 String option, Boolean readStatus, Boolean accRej) {
        Id = id;
        GroupId = groupId;
        ContactId = contactId;
        Name = name;
        Image = image;
        Phone = phone;
        ReadStatus = readStatus;
        AccRej = accRej;
        this.option = option;

    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public Boolean getAccRej() {
        return AccRej;
    }

    public void setAccRej(Boolean accRej) {
        AccRej = accRej;
    }

    public Boolean getReadStatus() {
        return ReadStatus;
    }

    public void setReadStatus(Boolean readStatus) {
        ReadStatus = readStatus;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getGroupId() {
        return GroupId;
    }

    public void setGroupId(String groupId) {
        GroupId = groupId;
    }

    public String getContactId() {
        return ContactId;
    }

    public void setContactId(String contactId) {
        ContactId = contactId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }
}
