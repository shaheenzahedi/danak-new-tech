package org.danak.backend.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link org.danak.backend.domain.Facilitator} entity.
 */
public class FacilitatorDTO implements Serializable {

    private String id;

    private Instant createTimeStamp;

    private UserDTO user;

    private UserDTO referedBy;

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

    public UserDTO getReferedBy() {
        return referedBy;
    }

    public void setReferedBy(UserDTO referedBy) {
        this.referedBy = referedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FacilitatorDTO)) {
            return false;
        }

        FacilitatorDTO facilitatorDTO = (FacilitatorDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, facilitatorDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FacilitatorDTO{" +
            "id='" + getId() + "'" +
            ", createTimeStamp='" + getCreateTimeStamp() + "'" +
            ", user=" + getUser() +
            ", referedBy=" + getReferedBy() +
            "}";
    }
}
