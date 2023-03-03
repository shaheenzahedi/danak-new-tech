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
 * A Facilitator.
 */
@Document(collection = "facilitator")
public class Facilitator implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("create_time_stamp")
    private Instant createTimeStamp;

    @Field("user")
    private User user;

    @Field("child")
    @JsonIgnoreProperties(value = { "user", "progresses", "centre", "device", "facilitator" }, allowSetters = true)
    private Set<Child> children = new HashSet<>();

    @Field("referedBy")
    private User referedBy;

    @Field("centre")
    @JsonIgnoreProperties(value = { "facilitator", "centre" }, allowSetters = true)
    private Set<FacilitatorCentreAssociation> centres = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Facilitator id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Instant getCreateTimeStamp() {
        return this.createTimeStamp;
    }

    public Facilitator createTimeStamp(Instant createTimeStamp) {
        this.setCreateTimeStamp(createTimeStamp);
        return this;
    }

    public void setCreateTimeStamp(Instant createTimeStamp) {
        this.createTimeStamp = createTimeStamp;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Facilitator user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<Child> getChildren() {
        return this.children;
    }

    public void setChildren(Set<Child> children) {
        if (this.children != null) {
            this.children.forEach(i -> i.setFacilitator(null));
        }
        if (children != null) {
            children.forEach(i -> i.setFacilitator(this));
        }
        this.children = children;
    }

    public Facilitator children(Set<Child> children) {
        this.setChildren(children);
        return this;
    }

    public Facilitator addChild(Child child) {
        this.children.add(child);
        child.setFacilitator(this);
        return this;
    }

    public Facilitator removeChild(Child child) {
        this.children.remove(child);
        child.setFacilitator(null);
        return this;
    }

    public User getReferedBy() {
        return this.referedBy;
    }

    public void setReferedBy(User user) {
        this.referedBy = user;
    }

    public Facilitator referedBy(User user) {
        this.setReferedBy(user);
        return this;
    }

    public Set<FacilitatorCentreAssociation> getCentres() {
        return this.centres;
    }

    public void setCentres(Set<FacilitatorCentreAssociation> facilitatorCentreAssociations) {
        if (this.centres != null) {
            this.centres.forEach(i -> i.setFacilitator(null));
        }
        if (facilitatorCentreAssociations != null) {
            facilitatorCentreAssociations.forEach(i -> i.setFacilitator(this));
        }
        this.centres = facilitatorCentreAssociations;
    }

    public Facilitator centres(Set<FacilitatorCentreAssociation> facilitatorCentreAssociations) {
        this.setCentres(facilitatorCentreAssociations);
        return this;
    }

    public Facilitator addCentre(FacilitatorCentreAssociation facilitatorCentreAssociation) {
        this.centres.add(facilitatorCentreAssociation);
        facilitatorCentreAssociation.setFacilitator(this);
        return this;
    }

    public Facilitator removeCentre(FacilitatorCentreAssociation facilitatorCentreAssociation) {
        this.centres.remove(facilitatorCentreAssociation);
        facilitatorCentreAssociation.setFacilitator(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Facilitator)) {
            return false;
        }
        return id != null && id.equals(((Facilitator) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Facilitator{" +
            "id=" + getId() +
            ", createTimeStamp='" + getCreateTimeStamp() + "'" +
            "}";
    }
}
