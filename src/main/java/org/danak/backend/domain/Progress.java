package org.danak.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Progress.
 */
@Document(collection = "progress")
public class Progress implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("create_time_stamp")
    private Instant createTimeStamp;

    @Field("spent_time")
    private Long spentTime;

    @Field("child")
    @JsonIgnoreProperties(value = { "user", "progresses", "centre", "device", "facilitator" }, allowSetters = true)
    private Child child;

    @Field("singleUnit")
    @JsonIgnoreProperties(value = { "progresses", "unitList" }, allowSetters = true)
    private SingleUnit singleUnit;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Progress id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Instant getCreateTimeStamp() {
        return this.createTimeStamp;
    }

    public Progress createTimeStamp(Instant createTimeStamp) {
        this.setCreateTimeStamp(createTimeStamp);
        return this;
    }

    public void setCreateTimeStamp(Instant createTimeStamp) {
        this.createTimeStamp = createTimeStamp;
    }

    public Long getSpentTime() {
        return this.spentTime;
    }

    public Progress spentTime(Long spentTime) {
        this.setSpentTime(spentTime);
        return this;
    }

    public void setSpentTime(Long spentTime) {
        this.spentTime = spentTime;
    }

    public Child getChild() {
        return this.child;
    }

    public void setChild(Child child) {
        this.child = child;
    }

    public Progress child(Child child) {
        this.setChild(child);
        return this;
    }

    public SingleUnit getSingleUnit() {
        return this.singleUnit;
    }

    public void setSingleUnit(SingleUnit singleUnit) {
        this.singleUnit = singleUnit;
    }

    public Progress singleUnit(SingleUnit singleUnit) {
        this.setSingleUnit(singleUnit);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Progress)) {
            return false;
        }
        return id != null && id.equals(((Progress) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Progress{" +
            "id=" + getId() +
            ", createTimeStamp='" + getCreateTimeStamp() + "'" +
            ", spentTime=" + getSpentTime() +
            "}";
    }
}
