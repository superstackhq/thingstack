package one.superstack.thingstack.request;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public class ThingBusActionInvocationRequest implements Serializable {

    @NotNull
    private Object input;

    public ThingBusActionInvocationRequest() {

    }

    public ThingBusActionInvocationRequest(Object input) {
        this.input = input;
    }

    public Object getInput() {
        return input;
    }

    public void setInput(Object input) {
        this.input = input;
    }
}
