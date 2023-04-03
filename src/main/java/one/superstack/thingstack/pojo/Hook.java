package one.superstack.thingstack.pojo;

import one.superstack.thingstack.enums.HookType;

import java.io.Serializable;

public class Hook implements Serializable {

    private HookType type;

    private String referenceId;

    private Object data;

    public Hook() {

    }

    public Hook(HookType type, String referenceId, Object data) {
        this.type = type;
        this.referenceId = referenceId;
        this.data = data;
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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
