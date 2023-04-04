package one.superstack.thingstack.response;

import java.io.Serializable;

public class ThingBusEmitEventResponse implements Serializable {

    private String eventId;

    public ThingBusEmitEventResponse() {

    }

    public ThingBusEmitEventResponse(String eventId) {
        this.eventId = eventId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}
