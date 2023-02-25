package org.danak.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A FacilitatorCentreAssociation.
 */
@Document(collection = "facilitator_centre_association")
public class FacilitatorCentreAssociation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("create_time_stamp")
    private Instant createTimeStamp;

    @Field("join_date")
    private Instant joinDate;

    @Field("facilitator")
    @JsonIgnoreProperties(value = { "user", "children", "centres" }, allowSetters = true)
    private Facilitator facilitator;

    @Field("centre")
    @JsonIgnoreProperties(value = { "children", "city", "facilitators" }, allowSetters = true)
    private Centre centre;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public FacilitatorCentreAssociation id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Instant getCreateTimeStamp() {
        return this.createTimeStamp;
    }

    public FacilitatorCentreAssociation createTimeStamp(Instant createTimeStamp) {
        this.setCreateTimeStamp(createTimeStamp);
        return this;
    }

    public void setCreateTimeStamp(Instant createTimeStamp) {
        this.createTimeStamp = createTimeStamp;
    }

    public Instant getJoinDate() {
        return this.joinDate;
    }

    public FacilitatorCentreAssociation joinDate(Instant joinDate) {
        this.setJoinDate(joinDate);
        return this;
    }

    public void setJoinDate(Instant joinDate) {
        this.joinDate = joinDate;
    }

    public Facilitator getFacilitator() {
        return this.facilitator;
    }

    public void setFacilitator(Facilitator facilitator) {
        this.facilitator = facilitator;
    }

    public FacilitatorCentreAssociation facilitator(Facilitator facilitator) {
        this.setFacilitator(facilitator);
        return this;
    }

    public Centre getCentre() {
        return this.centre;
    }

    public void setCentre(Centre centre) {
        this.centre = centre;
    }

    public FacilitatorCentreAssociation centre(Centre centre) {
        this.setCentre(centre);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FacilitatorCentreAssociation)) {
            return false;
        }
        return id != null && id.equals(((FacilitatorCentreAssociation) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FacilitatorCentreAssociation{" +
            "id=" + getId() +
            ", createTimeStamp='" + getCreateTimeStamp() + "'" +
            ", joinDate='" + getJoinDate() + "'" +
            "}";
    }
}
