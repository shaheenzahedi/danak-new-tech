package org.danak.backend.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link org.danak.backend.domain.Device} entity.
 */
public class DeviceDTO implements Serializable {

    private String id;

    private Instant createTimeStamp;

    private UUID universalId;

    private String globalNum;

    private String model;

    private String yearBuilt;

    private String androidId;

    private CityDTO city;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Instant getCreateTimeStamp() {
        return createTimeStamp;
    }

    public void setCreateTimeStamp(Instant createTimeStamp) {
        this.createTimeStamp = createTimeStamp;
    }

    public UUID getUniversalId() {
        return universalId;
    }

    public void setUniversalId(UUID universalId) {
        this.universalId = universalId;
    }

    public String getGlobalNum() {
        return globalNum;
    }

    public void setGlobalNum(String globalNum) {
        this.globalNum = globalNum;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getYearBuilt() {
        return yearBuilt;
    }

    public void setYearBuilt(String yearBuilt) {
        this.yearBuilt = yearBuilt;
    }

    public String getAndroidId() {
        return androidId;
    }

    public void setAndroidId(String androidId) {
        this.androidId = androidId;
    }

    public CityDTO getCity() {
        return city;
    }

    public void setCity(CityDTO city) {
        this.city = city;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DeviceDTO)) {
            return false;
        }

        DeviceDTO deviceDTO = (DeviceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, deviceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DeviceDTO{" +
            "id='" + getId() + "'" +
            ", createTimeStamp='" + getCreateTimeStamp() + "'" +
            ", universalId='" + getUniversalId() + "'" +
            ", globalNum='" + getGlobalNum() + "'" +
            ", model='" + getModel() + "'" +
            ", yearBuilt='" + getYearBuilt() + "'" +
            ", androidId='" + getAndroidId() + "'" +
            ", city=" + getCity() +
            "}";
    }
}
