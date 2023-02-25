package org.danak.backend.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link org.danak.backend.domain.SingleUnit} entity.
 */
public class SingleUnitDTO implements Serializable {

    private String id;

    private Instant createTimeStamp;

    private String globalNum;

    private UnitListDTO unitList;

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

    public String getGlobalNum() {
        return globalNum;
    }

    public void setGlobalNum(String globalNum) {
        this.globalNum = globalNum;
    }

    public UnitListDTO getUnitList() {
        return unitList;
    }

    public void setUnitList(UnitListDTO unitList) {
        this.unitList = unitList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SingleUnitDTO)) {
            return false;
        }

        SingleUnitDTO singleUnitDTO = (SingleUnitDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, singleUnitDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SingleUnitDTO{" +
            "id='" + getId() + "'" +
            ", createTimeStamp='" + getCreateTimeStamp() + "'" +
            ", globalNum='" + getGlobalNum() + "'" +
            ", unitList=" + getUnitList() +
            "}";
    }
}
