package org.danak.backend.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link org.danak.backend.domain.City} entity.
 */
public class CityDTO implements Serializable {

    private String id;

    private Instant createTimeStamp;

    private String name;

    private Boolean isVillage;

    private ProvinceDTO province;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsVillage() {
        return isVillage;
    }

    public void setIsVillage(Boolean isVillage) {
        this.isVillage = isVillage;
    }

    public ProvinceDTO getProvince() {
        return province;
    }

    public void setProvince(ProvinceDTO province) {
        this.province = province;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CityDTO)) {
            return false;
        }

        CityDTO cityDTO = (CityDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, cityDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CityDTO{" +
            "id='" + getId() + "'" +
            ", createTimeStamp='" + getCreateTimeStamp() + "'" +
            ", name='" + getName() + "'" +
            ", isVillage='" + getIsVillage() + "'" +
            ", province=" + getProvince() +
            "}";
    }
}
