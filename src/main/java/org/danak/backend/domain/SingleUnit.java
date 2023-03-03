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

    @Field("icon")
    private String icon;

    @Field("target")
    private String target;

    @Field("params")
    private String params;

    @Field("words")
    private String words;

    @Field("progress")
    @JsonIgnoreProperties(value = { "child", "createdByDevice", "singleUnit" }, allowSetters = true)
    private Set<Progress> progresses = new HashSet<>();

    @Field("unitList")
    @JsonIgnoreProperties(value = { "singleUnits" }, allowSetters = true)
    private UnitList unitList;

    @Field("config")
    @JsonIgnoreProperties(value = { "singleUnits" }, allowSetters = true)
    private UnitConfig config;

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

    public String getIcon() {
        return this.icon;
    }

    public SingleUnit icon(String icon) {
        this.setIcon(icon);
        return this;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTarget() {
        return this.target;
    }

    public SingleUnit target(String target) {
        this.setTarget(target);
        return this;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getParams() {
        return this.params;
    }

    public SingleUnit params(String params) {
        this.setParams(params);
        return this;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getWords() {
        return this.words;
    }

    public SingleUnit words(String words) {
        this.setWords(words);
        return this;
    }

    public void setWords(String words) {
        this.words = words;
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

    public UnitConfig getConfig() {
        return this.config;
    }

    public void setConfig(UnitConfig unitConfig) {
        this.config = unitConfig;
    }

    public SingleUnit config(UnitConfig unitConfig) {
        this.setConfig(unitConfig);
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
            ", icon='" + getIcon() + "'" +
            ", target='" + getTarget() + "'" +
            ", params='" + getParams() + "'" +
            ", words='" + getWords() + "'" +
            "}";
    }
}
