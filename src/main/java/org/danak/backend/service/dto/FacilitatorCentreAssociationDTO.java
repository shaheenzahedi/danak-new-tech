package org.danak.backend.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link org.danak.backend.domain.FacilitatorCentreAssociation} entity.
 */
public class FacilitatorCentreAssociationDTO implements Serializable {

    private String id;

    private Instant createTimeStamp;

    private Instant joinDate;

    private FacilitatorDTO facilitator;

    private CentreDTO centre;

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

    public Instant getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Instant joinDate) {
        this.joinDate = joinDate;
    }

    public FacilitatorDTO getFacilitator() {
        return facilitator;
    }

    public void setFacilitator(FacilitatorDTO facilitator) {
        this.facilitator = facilitator;
    }

    public CentreDTO getCentre() {
        return centre;
    }

    public void setCentre(CentreDTO centre) {
        this.centre = centre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FacilitatorCentreAssociationDTO)) {
            return false;
        }

        FacilitatorCentreAssociationDTO facilitatorCentreAssociationDTO = (FacilitatorCentreAssociationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, facilitatorCentreAssociationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FacilitatorCentreAssociationDTO{" +
            "id='" + getId() + "'" +
            ", createTimeStamp='" + getCreateTimeStamp() + "'" +
            ", joinDate='" + getJoinDate() + "'" +
            ", facilitator=" + getFacilitator() +
            ", centre=" + getCentre() +
            "}";
    }
}
