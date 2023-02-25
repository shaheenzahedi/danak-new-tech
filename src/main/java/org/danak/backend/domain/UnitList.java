package org.danak.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.danak.backend.domain.enumeration.UnitListType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A UnitList.
 */
@Document(collection = "unit_list")
public class UnitList implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("create_time_stamp")
    private Instant createTimeStamp;

    @Field("num")
    private Integer num;

    @Field("nick_name")
    private String nickName;

    @Field("type")
    private UnitListType type;

    @Field("singleUnit")
    @JsonIgnoreProperties(value = { "progresses", "unitList" }, allowSetters = true)
    private Set<SingleUnit> singleUnits = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public UnitList id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Instant getCreateTimeStamp() {
        return this.createTimeStamp;
    }

    public UnitList createTimeStamp(Instant createTimeStamp) {
        this.setCreateTimeStamp(createTimeStamp);
        return this;
    }

    public void setCreateTimeStamp(Instant createTimeStamp) {
        this.createTimeStamp = createTimeStamp;
    }

    public Integer getNum() {
        return this.num;
    }

    public UnitList num(Integer num) {
        this.setNum(num);
        return this;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getNickName() {
        return this.nickName;
    }

    public UnitList nickName(String nickName) {
        this.setNickName(nickName);
        return this;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public UnitListType getType() {
        return this.type;
    }

    public UnitList type(UnitListType type) {
        this.setType(type);
        return this;
    }

    public void setType(UnitListType type) {
        this.type = type;
    }

    public Set<SingleUnit> getSingleUnits() {
        return this.singleUnits;
    }

    public void setSingleUnits(Set<SingleUnit> singleUnits) {
        if (this.singleUnits != null) {
            this.singleUnits.forEach(i -> i.setUnitList(null));
        }
        if (singleUnits != null) {
            singleUnits.forEach(i -> i.setUnitList(this));
        }
        this.singleUnits = singleUnits;
    }

    public UnitList singleUnits(Set<SingleUnit> singleUnits) {
        this.setSingleUnits(singleUnits);
        return this;
    }

    public UnitList addSingleUnit(SingleUnit singleUnit) {
        this.singleUnits.add(singleUnit);
        singleUnit.setUnitList(this);
        return this;
    }

    public UnitList removeSingleUnit(SingleUnit singleUnit) {
        this.singleUnits.remove(singleUnit);
        singleUnit.setUnitList(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UnitList)) {
            return false;
        }
        return id != null && id.equals(((UnitList) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UnitList{" +
            "id=" + getId() +
            ", createTimeStamp='" + getCreateTimeStamp() + "'" +
            ", num=" + getNum() +
            ", nickName='" + getNickName() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
