package one.superstack.thingstack.response;

import java.io.Serializable;

public class BusCustomTopicMessageResponse implements Serializable {

    private Boolean success;

    public BusCustomTopicMessageResponse() {

    }

    public BusCustomTopicMessageResponse(Boolean success) {
        this.success = success;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
