package one.superstack.thingstack.request;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public class ThingBusSetPropertyRequest implements Serializable {

    @NotNull
    private Object value;

    public ThingBusSetPropertyRequest() {

    }

    public ThingBusSetPropertyRequest(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
