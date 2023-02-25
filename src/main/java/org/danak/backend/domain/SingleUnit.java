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
 * A SingleUnit.
 */
@Document(collection = "single_unit")
public class SingleUnit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("create_time_stamp")
    private Instant createTimeStamp;

    @Field("global_num")
    private String globalNum;

    @Field("progress")
    @JsonIgnoreProperties(value = { "child", "singleUnit" }, allowSetters = true)
    private Set<Progress> progresses = new HashSet<>();

    @Field("unitList")
    @JsonIgnoreProperties(value = { "singleUnits" }, allowSetters = true)
    private UnitList unitList;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public SingleUnit id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Instant getCreateTimeStamp() {
        return this.createTimeStamp;
    }

    public SingleUnit createTimeStamp(Instant createTimeStamp) {
        this.setCreateTimeStamp(createTimeStamp);
        return this;
    }

    public void setCreateTimeStamp(Instant createTimeStamp) {
        this.createTimeStamp = createTimeStamp;
    }

    public String getGlobalNum() {
        return this.globalNum;
    }

    public SingleUnit globalNum(String globalNum) {
        this.setGlobalNum(globalNum);
        return this;
    }

    public void setGlobalNum(String globalNum) {
        this.globalNum = globalNum;
    }

    public Set<Progress> getProgresses() {
        return this.progresses;
    }

    public void setProgresses(Set<Progress> progresses) {
        if (this.progresses != null) {
            this.progresses.forEach(i -> i.setSingleUnit(null));
        }
        if (progresses != null) {
            progresses.forEach(i -> i.setSingleUnit(this));
        }
        this.progresses = progresses;
    }

    public SingleUnit progresses(Set<Progress> progresses) {
        this.setProgresses(progresses);
        return this;
    }

    public SingleUnit addProgress(Progress progress) {
        this.progresses.add(progress);
        progress.setSingleUnit(this);
        return this;
    }

    public SingleUnit removeProgress(Progress progress) {
        this.progresses.remove(progress);
        progress.setSingleUnit(null);
        return this;
    }

    public UnitList getUnitList() {
        return this.unitList;
    }

    public void setUnitList(UnitList unitList) {
        this.unitList = unitList;
    }

    public SingleUnit unitList(UnitList unitList) {
        this.setUnitList(unitList);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SingleUnit)) {
            return false;
        }
        return id != null && id.equals(((SingleUnit) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SingleUnit{" +
            "id=" + getId() +
            ", createTimeStamp='" + getCreateTimeStamp() + "'" +
            ", globalNum='" + getGlobalNum() + "'" +
            "}";
    }
}
