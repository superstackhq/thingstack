package one.superstack.thingstack.response;

import java.io.Serializable;

public class ThingBusSetPropertyResponse implements Serializable {

    private String requestId;

    public ThingBusSetPropertyResponse() {

    }

    public ThingBusSetPropertyResponse(String requestId) {
        this.requestId = requestId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
