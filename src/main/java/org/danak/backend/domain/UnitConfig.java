package org.danak.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A UnitConfig.
 */
@Document(collection = "unit_config")
public class UnitConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("name")
    private String name;

    @Field("display_name")
    private String displayName;

    @Field("singleUnit")
    @JsonIgnoreProperties(value = { "progresses", "unitList", "config" }, allowSetters = true)
    private Set<SingleUnit> singleUnits = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public UnitConfig id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public UnitConfig name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public UnitConfig displayName(String displayName) {
        this.setDisplayName(displayName);
        return this;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Set<SingleUnit> getSingleUnits() {
        return this.singleUnits;
    }

    public void setSingleUnits(Set<SingleUnit> singleUnits) {
        if (this.singleUnits != null) {
            this.singleUnits.forEach(i -> i.setConfig(null));
        }
        if (singleUnits != null) {
            singleUnits.forEach(i -> i.setConfig(this));
        }
        this.singleUnits = singleUnits;
    }

    public UnitConfig singleUnits(Set<SingleUnit> singleUnits) {
        this.setSingleUnits(singleUnits);
        return this;
    }

    public UnitConfig addSingleUnit(SingleUnit singleUnit) {
        this.singleUnits.add(singleUnit);
        singleUnit.setConfig(this);
        return this;
    }

    public UnitConfig removeSingleUnit(SingleUnit singleUnit) {
        this.singleUnits.remove(singleUnit);
        singleUnit.setConfig(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UnitConfig)) {
            return false;
        }
        return id != null && id.equals(((UnitConfig) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UnitConfig{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", displayName='" + getDisplayName() + "'" +
            "}";
    }
}
