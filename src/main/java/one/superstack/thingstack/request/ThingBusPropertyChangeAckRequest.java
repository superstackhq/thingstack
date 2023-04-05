package one.superstack.thingstack.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public class ThingBusPropertyChangeAckRequest implements Serializable {

    @NotBlank
    private String requestId;

    @NotNull
    private Object value;

    public ThingBusPropertyChangeAckRequest() {

    }

    public ThingBusPropertyChangeAckRequest(String requestId, Object value) {
        this.requestId = requestId;
        this.value = value;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
