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
 * A Centre.
 */
@Document(collection = "centre")
public class Centre implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("create_time_stamp")
    private Instant createTimeStamp;

    @Field("name")
    private String name;

    @Field("child")
    @JsonIgnoreProperties(value = { "user", "progresses", "centre", "device", "facilitator" }, allowSetters = true)
    private Set<Child> children = new HashSet<>();

    @Field("city")
    @JsonIgnoreProperties(value = { "centres", "devices", "province" }, allowSetters = true)
    private City city;

    @Field("facilitator")
    @JsonIgnoreProperties(value = { "facilitator", "centre" }, allowSetters = true)
    private Set<FacilitatorCentreAssociation> facilitators = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Centre id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Instant getCreateTimeStamp() {
        return this.createTimeStamp;
    }

    public Centre createTimeStamp(Instant createTimeStamp) {
        this.setCreateTimeStamp(createTimeStamp);
        return this;
    }

    public void setCreateTimeStamp(Instant createTimeStamp) {
        this.createTimeStamp = createTimeStamp;
    }

    public String getName() {
        return this.name;
    }

    public Centre name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Child> getChildren() {
        return this.children;
    }

    public void setChildren(Set<Child> children) {
        if (this.children != null) {
            this.children.forEach(i -> i.setCentre(null));
        }
        if (children != null) {
            children.forEach(i -> i.setCentre(this));
        }
        this.children = children;
    }

    public Centre children(Set<Child> children) {
        this.setChildren(children);
        return this;
    }

    public Centre addChild(Child child) {
        this.children.add(child);
        child.setCentre(this);
        return this;
    }

    public Centre removeChild(Child child) {
        this.children.remove(child);
        child.setCentre(null);
        return this;
    }

    public City getCity() {
        return this.city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Centre city(City city) {
        this.setCity(city);
        return this;
    }

    public Set<FacilitatorCentreAssociation> getFacilitators() {
        return this.facilitators;
    }

    public void setFacilitators(Set<FacilitatorCentreAssociation> facilitatorCentreAssociations) {
        if (this.facilitators != null) {
            this.facilitators.forEach(i -> i.setCentre(null));
        }
        if (facilitatorCentreAssociations != null) {
            facilitatorCentreAssociations.forEach(i -> i.setCentre(this));
        }
        this.facilitators = facilitatorCentreAssociations;
    }

    public Centre facilitators(Set<FacilitatorCentreAssociation> facilitatorCentreAssociations) {
        this.setFacilitators(facilitatorCentreAssociations);
        return this;
    }

    public Centre addFacilitator(FacilitatorCentreAssociation facilitatorCentreAssociation) {
        this.facilitators.add(facilitatorCentreAssociation);
        facilitatorCentreAssociation.setCentre(this);
        return this;
    }

    public Centre removeFacilitator(FacilitatorCentreAssociation facilitatorCentreAssociation) {
        this.facilitators.remove(facilitatorCentreAssociation);
        facilitatorCentreAssociation.setCentre(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Centre)) {
            return false;
        }
        return id != null && id.equals(((Centre) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Centre{" +
            "id=" + getId() +
            ", createTimeStamp='" + getCreateTimeStamp() + "'" +
            ", name='" + getName() + "'" +
            "}";
    }
}
