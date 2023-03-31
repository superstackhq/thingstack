package one.superstack.thingstack.pojo;

import one.superstack.thingstack.enums.ActorType;

import java.io.Serializable;

public class ActorReference implements Serializable {

    private ActorType type;

    private String id;

    public ActorReference() {

    }

    public ActorReference(ActorType type, String id) {
        this.type = type;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ActorType getType() {
        return type;
    }

    public void setType(ActorType type) {
        this.type = type;
    }
}
