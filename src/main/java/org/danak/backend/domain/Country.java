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
 * A Country.
 */
@Document(collection = "country")
public class Country implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("create_time_stamp")
    private Instant createTimeStamp;

    @Field("name")
    private String name;

    @Field("province")
    @JsonIgnoreProperties(value = { "cities", "country" }, allowSetters = true)
    private Set<Province> provinces = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Country id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Instant getCreateTimeStamp() {
        return this.createTimeStamp;
    }

    public Country createTimeStamp(Instant createTimeStamp) {
        this.setCreateTimeStamp(createTimeStamp);
        return this;
    }

    public void setCreateTimeStamp(Instant createTimeStamp) {
        this.createTimeStamp = createTimeStamp;
    }

    public String getName() {
        return this.name;
    }

    public Country name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Province> getProvinces() {
        return this.provinces;
    }

    public void setProvinces(Set<Province> provinces) {
        if (this.provinces != null) {
            this.provinces.forEach(i -> i.setCountry(null));
        }
        if (provinces != null) {
            provinces.forEach(i -> i.setCountry(this));
        }
        this.provinces = provinces;
    }

    public Country provinces(Set<Province> provinces) {
        this.setProvinces(provinces);
        return this;
    }

    public Country addProvince(Province province) {
        this.provinces.add(province);
        province.setCountry(this);
        return this;
    }

    public Country removeProvince(Province province) {
        this.provinces.remove(province);
        province.setCountry(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Country)) {
            return false;
        }
        return id != null && id.equals(((Country) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Country{" +
            "id=" + getId() +
            ", createTimeStamp='" + getCreateTimeStamp() + "'" +
            ", name='" + getName() + "'" +
            "}";
    }
}
