package org.danak.backend.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import org.danak.backend.domain.enumeration.UnitListType;

/**
 * A DTO for the {@link org.danak.backend.domain.UnitList} entity.
 */
public class UnitListDTO implements Serializable {

    private String id;

    private Instant createTimeStamp;

    private Integer num;

    private String nickName;

    private UnitListType type;

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

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public UnitListType getType() {
        return type;
    }

    public void setType(UnitListType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UnitListDTO)) {
            return false;
        }

        UnitListDTO unitListDTO = (UnitListDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, unitListDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UnitListDTO{" +
            "id='" + getId() + "'" +
            ", createTimeStamp='" + getCreateTimeStamp() + "'" +
            ", num=" + getNum() +
            ", nickName='" + getNickName() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
