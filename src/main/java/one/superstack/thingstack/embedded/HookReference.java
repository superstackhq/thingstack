package one.superstack.thingstack.embedded;

import one.superstack.thingstack.enums.HookType;

import java.io.Serializable;

public class HookReference implements Serializable {

    private HookType type;

    private String id;

    public HookReference() {

    }

    public HookReference(HookType type, String id) {
        this.type = type;
        this.id = id;
    }

    public HookType getType() {
        return type;
    }

    public void setType(HookType type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
