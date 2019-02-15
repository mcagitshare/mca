package com.mca.Model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Message extends RealmObject {

    @PrimaryKey
    private String Id;

    @Required
    private String DisplayMessage;
    private String Icon;
    private String Option;
    private Boolean ReadStatus;
    private Boolean AccRej;

    public Message() {
    }

    public Message(String id, String displayMessage, String icon, String option, Boolean readStatus, Boolean accRej) {
        Id = id;
        DisplayMessage = displayMessage;
        Icon = icon;
        Option = option;
        ReadStatus = readStatus;
        AccRej = accRej;
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

    public String getDisplayMessage() {
        return DisplayMessage;
    }

    public void setDisplayMessage(String displayMessage) {
        DisplayMessage = displayMessage;
    }

    public String getIcon() {
        return Icon;
    }

    public void setIcon(String icon) {
        Icon = icon;
    }

    public String getOption() {
        return Option;
    }

    public void setOption(String option) {
        Option = option;
    }
}
