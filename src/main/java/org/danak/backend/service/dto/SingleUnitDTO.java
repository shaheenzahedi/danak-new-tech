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

    private String icon;

    private String target;

    private String params;

    private String words;

    private UnitListDTO unitList;

    private UnitConfigDTO config;

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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }

    public UnitListDTO getUnitList() {
        return unitList;
    }

    public void setUnitList(UnitListDTO unitList) {
        this.unitList = unitList;
    }

    public UnitConfigDTO getConfig() {
        return config;
    }

    public void setConfig(UnitConfigDTO config) {
        this.config = config;
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
            ", icon='" + getIcon() + "'" +
            ", target='" + getTarget() + "'" +
            ", params='" + getParams() + "'" +
            ", words='" + getWords() + "'" +
            ", unitList=" + getUnitList() +
            ", config=" + getConfig() +
            "}";
    }
}
