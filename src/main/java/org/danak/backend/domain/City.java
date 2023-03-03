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
 * A City.
 */
@Document(collection = "city")
public class City implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("create_time_stamp")
    private Instant createTimeStamp;

    @Field("name")
    private String name;

    @Field("is_village")
    private Boolean isVillage;

    @Field("centre")
    @JsonIgnoreProperties(value = { "children", "city", "facilitators" }, allowSetters = true)
    private Set<Centre> centres = new HashSet<>();

    @Field("device")
    @JsonIgnoreProperties(value = { "children", "progresses", "city" }, allowSetters = true)
    private Set<Device> devices = new HashSet<>();

    @Field("province")
    @JsonIgnoreProperties(value = { "cities", "country" }, allowSetters = true)
    private Province province;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public City id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Instant getCreateTimeStamp() {
        return this.createTimeStamp;
    }

    public City createTimeStamp(Instant createTimeStamp) {
        this.setCreateTimeStamp(createTimeStamp);
        return this;
    }

    public void setCreateTimeStamp(Instant createTimeStamp) {
        this.createTimeStamp = createTimeStamp;
    }

    public String getName() {
        return this.name;
    }

    public City name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsVillage() {
        return this.isVillage;
    }

    public City isVillage(Boolean isVillage) {
        this.setIsVillage(isVillage);
        return this;
    }

    public void setIsVillage(Boolean isVillage) {
        this.isVillage = isVillage;
    }

    public Set<Centre> getCentres() {
        return this.centres;
    }

    public void setCentres(Set<Centre> centres) {
        if (this.centres != null) {
            this.centres.forEach(i -> i.setCity(null));
        }
        if (centres != null) {
            centres.forEach(i -> i.setCity(this));
        }
        this.centres = centres;
    }

    public City centres(Set<Centre> centres) {
        this.setCentres(centres);
        return this;
    }

    public City addCentre(Centre centre) {
        this.centres.add(centre);
        centre.setCity(this);
        return this;
    }

    public City removeCentre(Centre centre) {
        this.centres.remove(centre);
        centre.setCity(null);
        return this;
    }

    public Set<Device> getDevices() {
        return this.devices;
    }

    public void setDevices(Set<Device> devices) {
        if (this.devices != null) {
            this.devices.forEach(i -> i.setCity(null));
        }
        if (devices != null) {
            devices.forEach(i -> i.setCity(this));
        }
        this.devices = devices;
    }

    public City devices(Set<Device> devices) {
        this.setDevices(devices);
        return this;
    }

    public City addDevice(Device device) {
        this.devices.add(device);
        device.setCity(this);
        return this;
    }

    public City removeDevice(Device device) {
        this.devices.remove(device);
        device.setCity(null);
        return this;
    }

    public Province getProvince() {
        return this.province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    public City province(Province province) {
        this.setProvince(province);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof City)) {
            return false;
        }
        return id != null && id.equals(((City) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "City{" +
            "id=" + getId() +
            ", createTimeStamp='" + getCreateTimeStamp() + "'" +
            ", name='" + getName() + "'" +
            ", isVillage='" + getIsVillage() + "'" +
            "}";
    }
}
