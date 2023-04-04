package one.superstack.thingstack.response;

import java.io.Serializable;

public class ThingBusActionInvocationResponse implements Serializable {

    private String requestId;

    public ThingBusActionInvocationResponse() {

    }

    public ThingBusActionInvocationResponse(String requestId) {
        this.requestId = requestId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
