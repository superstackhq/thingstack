package one.superstack.thingstack.embedded;

import one.superstack.thingstack.enums.HookType;

import java.io.Serializable;
import java.util.Objects;

public class HookReference implements Serializable {

    private HookType type;

    private String referenceId;

    public HookReference() {

    }

    public HookReference(HookType type, String referenceId) {
        this.type = type;
        this.referenceId = referenceId;
    }

    public HookType getType() {
        return type;
    }

    public void setType(HookType type) {
        this.type = type;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HookReference hookReference = (HookReference) o;
        return type == hookReference.type && Objects.equals(referenceId, hookReference.referenceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, referenceId);
    }
}
