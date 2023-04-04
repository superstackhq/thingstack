package one.superstack.thingstack.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public class ThingBusPropertyChangeAckRequest implements Serializable {

    @NotBlank
    private String requestId;

    @NotNull
    private Object data;

    public ThingBusPropertyChangeAckRequest() {

    }

    public ThingBusPropertyChangeAckRequest(String requestId, Object data) {
        this.requestId = requestId;
        this.data = data;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
