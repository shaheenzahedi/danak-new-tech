package org.danak.backend.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link org.danak.backend.domain.Child} entity.
 */
public class ChildDTO implements Serializable {

    private String id;

    private Instant createTimeStamp;

    private UserDTO user;

    private CentreDTO centre;

    private DeviceDTO device;

    private FacilitatorDTO facilitator;

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

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public CentreDTO getCentre() {
        return centre;
    }

    public void setCentre(CentreDTO centre) {
        this.centre = centre;
    }

    public DeviceDTO getDevice() {
        return device;
    }

    public void setDevice(DeviceDTO device) {
        this.device = device;
    }

    public FacilitatorDTO getFacilitator() {
        return facilitator;
    }

    public void setFacilitator(FacilitatorDTO facilitator) {
        this.facilitator = facilitator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChildDTO)) {
            return false;
        }

        ChildDTO childDTO = (ChildDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, childDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ChildDTO{" +
            "id='" + getId() + "'" +
            ", createTimeStamp='" + getCreateTimeStamp() + "'" +
            ", user=" + getUser() +
            ", centre=" + getCentre() +
            ", device=" + getDevice() +
            ", facilitator=" + getFacilitator() +
            "}";
    }
}
