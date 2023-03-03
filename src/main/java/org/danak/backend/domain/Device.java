package org.danak.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Device.
 */
@Document(collection = "device")
public class Device implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("create_time_stamp")
    private Instant createTimeStamp;

    @Field("universal_id")
    private UUID universalId;

    @Field("global_num")
    private String globalNum;

    @Field("model")
    private String model;

    @Field("year_built")
    private String yearBuilt;

    @Field("android_id")
    private String androidId;

    @Field("child")
    @JsonIgnoreProperties(value = { "user", "progresses", "centre", "device", "facilitator" }, allowSetters = true)
    private Set<Child> children = new HashSet<>();

    @Field("progress")
    @JsonIgnoreProperties(value = { "child", "createdByDevice", "singleUnit" }, allowSetters = true)
    private Set<Progress> progresses = new HashSet<>();

    @Field("city")
    @JsonIgnoreProperties(value = { "centres", "devices", "province" }, allowSetters = true)
    private City city;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Device id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Instant getCreateTimeStamp() {
        return this.createTimeStamp;
    }

    public Device createTimeStamp(Instant createTimeStamp) {
        this.setCreateTimeStamp(createTimeStamp);
        return this;
    }

    public void setCreateTimeStamp(Instant createTimeStamp) {
        this.createTimeStamp = createTimeStamp;
    }

    public UUID getUniversalId() {
        return this.universalId;
    }

    public Device universalId(UUID universalId) {
        this.setUniversalId(universalId);
        return this;
    }

    public void setUniversalId(UUID universalId) {
        this.universalId = universalId;
    }

    public String getGlobalNum() {
        return this.globalNum;
    }

    public Device globalNum(String globalNum) {
        this.setGlobalNum(globalNum);
        return this;
    }

    public void setGlobalNum(String globalNum) {
        this.globalNum = globalNum;
    }

    public String getModel() {
        return this.model;
    }

    public Device model(String model) {
        this.setModel(model);
        return this;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getYearBuilt() {
        return this.yearBuilt;
    }

    public Device yearBuilt(String yearBuilt) {
        this.setYearBuilt(yearBuilt);
        return this;
    }

    public void setYearBuilt(String yearBuilt) {
        this.yearBuilt = yearBuilt;
    }

    public String getAndroidId() {
        return this.androidId;
    }

    public Device androidId(String androidId) {
        this.setAndroidId(androidId);
        return this;
    }

    public void setAndroidId(String androidId) {
        this.androidId = androidId;
    }

    public Set<Child> getChildren() {
        return this.children;
    }

    public void setChildren(Set<Child> children) {
        if (this.children != null) {
            this.children.forEach(i -> i.setDevice(null));
        }
        if (children != null) {
            children.forEach(i -> i.setDevice(this));
        }
        this.children = children;
    }

    public Device children(Set<Child> children) {
        this.setChildren(children);
        return this;
    }

    public Device addChild(Child child) {
        this.children.add(child);
        child.setDevice(this);
        return this;
    }

    public Device removeChild(Child child) {
        this.children.remove(child);
        child.setDevice(null);
        return this;
    }

    public Set<Progress> getProgresses() {
        return this.progresses;
    }

    public void setProgresses(Set<Progress> progresses) {
        if (this.progresses != null) {
            this.progresses.forEach(i -> i.setCreatedByDevice(null));
        }
        if (progresses != null) {
            progresses.forEach(i -> i.setCreatedByDevice(this));
        }
        this.progresses = progresses;
    }

    public Device progresses(Set<Progress> progresses) {
        this.setProgresses(progresses);
        return this;
    }

    public Device addProgress(Progress progress) {
        this.progresses.add(progress);
        progress.setCreatedByDevice(this);
        return this;
    }

    public Device removeProgress(Progress progress) {
        this.progresses.remove(progress);
        progress.setCreatedByDevice(null);
        return this;
    }

    public City getCity() {
        return this.city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Device city(City city) {
        this.setCity(city);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Device)) {
            return false;
        }
        return id != null && id.equals(((Device) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Device{" +
            "id=" + getId() +
            ", createTimeStamp='" + getCreateTimeStamp() + "'" +
            ", universalId='" + getUniversalId() + "'" +
            ", globalNum='" + getGlobalNum() + "'" +
            ", model='" + getModel() + "'" +
            ", yearBuilt='" + getYearBuilt() + "'" +
            ", androidId='" + getAndroidId() + "'" +
            "}";
    }
}
