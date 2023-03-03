package org.danak.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Child.
 */
@Document(collection = "child")
public class Child implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("create_time_stamp")
    private Instant createTimeStamp;

    @Field("user")
    private User user;

    @Field("progress")
    @JsonIgnoreProperties(value = { "child", "createdByDevice", "singleUnit" }, allowSetters = true)
    private Set<Progress> progresses = new HashSet<>();

    @Field("centre")
    @JsonIgnoreProperties(value = { "children", "city", "facilitators" }, allowSetters = true)
    private Centre centre;

    @Field("device")
    @JsonIgnoreProperties(value = { "children", "progresses", "city" }, allowSetters = true)
    private Device device;

    @Field("facilitator")
    @JsonIgnoreProperties(value = { "user", "children", "referedBy", "centres" }, allowSetters = true)
    private Facilitator facilitator;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Child id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Instant getCreateTimeStamp() {
        return this.createTimeStamp;
    }

    public Child createTimeStamp(Instant createTimeStamp) {
        this.setCreateTimeStamp(createTimeStamp);
        return this;
    }

    public void setCreateTimeStamp(Instant createTimeStamp) {
        this.createTimeStamp = createTimeStamp;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Child user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<Progress> getProgresses() {
        return this.progresses;
    }

    public void setProgresses(Set<Progress> progresses) {
        if (this.progresses != null) {
            this.progresses.forEach(i -> i.setChild(null));
        }
        if (progresses != null) {
            progresses.forEach(i -> i.setChild(this));
        }
        this.progresses = progresses;
    }

    public Child progresses(Set<Progress> progresses) {
        this.setProgresses(progresses);
        return this;
    }

    public Child addProgress(Progress progress) {
        this.progresses.add(progress);
        progress.setChild(this);
        return this;
    }

    public Child removeProgress(Progress progress) {
        this.progresses.remove(progress);
        progress.setChild(null);
        return this;
    }

    public Centre getCentre() {
        return this.centre;
    }

    public void setCentre(Centre centre) {
        this.centre = centre;
    }

    public Child centre(Centre centre) {
        this.setCentre(centre);
        return this;
    }

    public Device getDevice() {
        return this.device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public Child device(Device device) {
        this.setDevice(device);
        return this;
    }

    public Facilitator getFacilitator() {
        return this.facilitator;
    }

    public void setFacilitator(Facilitator facilitator) {
        this.facilitator = facilitator;
    }

    public Child facilitator(Facilitator facilitator) {
        this.setFacilitator(facilitator);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Child)) {
            return false;
        }
        return id != null && id.equals(((Child) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Child{" +
            "id=" + getId() +
            ", createTimeStamp='" + getCreateTimeStamp() + "'" +
            "}";
    }
}
