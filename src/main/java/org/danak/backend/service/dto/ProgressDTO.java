package org.danak.backend.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link org.danak.backend.domain.Progress} entity.
 */
public class ProgressDTO implements Serializable {

    private String id;

    private Instant createTimeStamp;

    private Long spentTime;

    private ChildDTO child;

    private DeviceDTO createdByDevice;

    private SingleUnitDTO singleUnit;

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

    public Long getSpentTime() {
        return spentTime;
    }

    public void setSpentTime(Long spentTime) {
        this.spentTime = spentTime;
    }

    public ChildDTO getChild() {
        return child;
    }

    public void setChild(ChildDTO child) {
        this.child = child;
    }

    public DeviceDTO getCreatedByDevice() {
        return createdByDevice;
    }

    public void setCreatedByDevice(DeviceDTO createdByDevice) {
        this.createdByDevice = createdByDevice;
    }

    public SingleUnitDTO getSingleUnit() {
        return singleUnit;
    }

    public void setSingleUnit(SingleUnitDTO singleUnit) {
        this.singleUnit = singleUnit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProgressDTO)) {
            return false;
        }

        ProgressDTO progressDTO = (ProgressDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, progressDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProgressDTO{" +
            "id='" + getId() + "'" +
            ", createTimeStamp='" + getCreateTimeStamp() + "'" +
            ", spentTime=" + getSpentTime() +
            ", child=" + getChild() +
            ", createdByDevice=" + getCreatedByDevice() +
            ", singleUnit=" + getSingleUnit() +
            "}";
    }
}
