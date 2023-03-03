package org.danak.backend.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link org.danak.backend.domain.UnitConfig} entity.
 */
public class UnitConfigDTO implements Serializable {

    private String id;

    private String name;

    private String displayName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UnitConfigDTO)) {
            return false;
        }

        UnitConfigDTO unitConfigDTO = (UnitConfigDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, unitConfigDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UnitConfigDTO{" +
            "id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            ", displayName='" + getDisplayName() + "'" +
            "}";
    }
}
