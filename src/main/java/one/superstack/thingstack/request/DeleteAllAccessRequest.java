package one.superstack.thingstack.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import one.superstack.thingstack.enums.ActorType;
import one.superstack.thingstack.enums.TargetType;

import java.io.Serializable;

public class DeleteAllAccessRequest implements Serializable {


    @NotNull
    private TargetType targetType;

    private String targetId;

    @NotNull
    private ActorType actorType;

    @NotBlank
    private String actorId;

    public DeleteAllAccessRequest() {

    }

    public DeleteAllAccessRequest(TargetType targetType, String targetId, ActorType actorType, String actorId) {
        this.targetType = targetType;
        this.targetId = targetId;
        this.actorType = actorType;
        this.actorId = actorId;
    }

    public TargetType getTargetType() {
        return targetType;
    }

    public void setTargetType(TargetType targetType) {
        this.targetType = targetType;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public ActorType getActorType() {
        return actorType;
    }

    public void setActorType(ActorType actorType) {
        this.actorType = actorType;
    }

    public String getActorId() {
        return actorId;
    }

    public void setActorId(String actorId) {
        this.actorId = actorId;
    }
}
