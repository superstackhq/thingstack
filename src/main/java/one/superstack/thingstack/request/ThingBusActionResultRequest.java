package one.superstack.thingstack.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public class ThingBusActionResultRequest implements Serializable {

    @NotBlank
    private String requestId;

    @NotNull
    private Object output;

    public ThingBusActionResultRequest() {

    }

    public ThingBusActionResultRequest(String requestId, Object output) {
        this.requestId = requestId;
        this.output = output;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Object getOutput() {
        return output;
    }

    public void setOutput(Object output) {
        this.output = output;
    }
}
