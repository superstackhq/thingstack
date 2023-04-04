package one.superstack.thingstack.request;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public class ThingBusEmitEventRequest implements Serializable {

    @NotNull
    private Object payload;

    public ThingBusEmitEventRequest() {

    }

    public ThingBusEmitEventRequest(Object payload) {
        this.payload = payload;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }
}
