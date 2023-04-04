package one.superstack.thingstack.response;

import java.io.Serializable;

public class ThingBusPropertyChangeAckResponse implements Serializable {

    private String requestId;

    public ThingBusPropertyChangeAckResponse() {

    }

    public ThingBusPropertyChangeAckResponse(String requestId) {
        this.requestId = requestId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
