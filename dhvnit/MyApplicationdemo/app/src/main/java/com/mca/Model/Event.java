package com.mca.Model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Event extends RealmObject {

    @PrimaryKey
    private String Id;

    @Required
    private String EventName;
    private String Icon;
    private String DateFrom;
    private String DateTo;
    private String Option;
    private Boolean ReadStatus;

    public Event(String id, String eventName, String icon, String dateFrom, String dateTo, String option, Boolean readStatus) {
        Id = id;
        EventName = eventName;
        Icon = icon;
        DateFrom = dateFrom;
        DateTo = dateTo;
        Option = option;
        ReadStatus = readStatus;
    }

    public Event() {
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

    public String getEventName() {
        return EventName;
    }

    public void setEventName(String eventName) {
        EventName = eventName;
    }

    public String getIcon() {
        return Icon;
    }

    public void setIcon(String icon) {
        Icon = icon;
    }

    public String getDateFrom() {
        return DateFrom;
    }

    public void setDateFrom(String dateFrom) {
        DateFrom = dateFrom;
    }

    public String getDateTo() {
        return DateTo;
    }

    public void setDateTo(String dateTo) {
        DateTo = dateTo;
    }

    public String getOption() {
        return Option;
    }

    public void setOption(String option) {
        Option = option;
    }
}
