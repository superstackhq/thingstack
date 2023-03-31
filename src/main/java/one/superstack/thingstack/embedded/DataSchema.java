package one.superstack.thingstack.embedded;

import one.superstack.thingstack.enums.DataType;

import java.io.Serializable;

public class DataSchema implements Serializable {

    private String title;

    private String description;

    private DataType dataType;

    private String unit;

    private Constraints constraints;

    public DataSchema() {

    }

    public DataSchema(String title, String description, DataType dataType, String unit, Constraints constraints) {
        this.title = title;
        this.description = description;
        this.dataType = dataType;
        this.unit = unit;
        this.constraints = constraints;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Constraints getConstraints() {
        return constraints;
    }

    public void setConstraints(Constraints constraints) {
        this.constraints = constraints;
    }
}
