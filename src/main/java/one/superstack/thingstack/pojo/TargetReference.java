package one.superstack.thingstack.pojo;

import one.superstack.thingstack.enums.TargetType;

import java.io.Serializable;

public class TargetReference implements Serializable {

    private TargetType type;

    private String id;

    public TargetReference() {

    }

    public TargetReference(TargetType type, String id) {
        this.type = type;
        this.id = id;
    }

    public TargetType getType() {
        return type;
    }

    public void setType(TargetType type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
