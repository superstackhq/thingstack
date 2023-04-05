package one.superstack.thingstack.response;

public class ThingBusActionResultResponse {

    private String requestId;

    public ThingBusActionResultResponse() {

    }

    public ThingBusActionResultResponse(String requestId) {
        this.requestId = requestId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
