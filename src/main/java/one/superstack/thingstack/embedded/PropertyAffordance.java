package one.superstack.thingstack.embedded;

import one.superstack.thingstack.enums.DataType;

import java.io.Serializable;

public class PropertyAffordance extends DataSchema {

    public PropertyAffordance() {
    }

    public PropertyAffordance(String title, String description, DataType dataType, String unit, Constraints constraints) {
        super(title, description, dataType, unit, constraints);
    }
}
