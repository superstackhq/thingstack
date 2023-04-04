package one.superstack.thingstack.pojo;

import one.superstack.thingstack.enums.AffordanceType;
import one.superstack.thingstack.enums.TopicType;

import java.io.Serializable;
import java.util.List;

public class Topic implements Serializable {

    private String path;

    private TopicType type;

    private AffordanceType affordanceType;

    private String affordanceKey;

    private String thingId;

    private String thingType;

    private String thingTypeVersion;

    private List<String> namespace;

    private String organizationId;

    private Boolean custom; // Custom or not custom - depends on if the thing of type, version and namespace actually exists in db

    public Topic() {

    }

    public Topic(String path, TopicType type, AffordanceType affordanceType, String affordanceKey, String thingId, String thingType, String thingTypeVersion, List<String> namespace, String organizationId, Boolean custom) {
        this.path = path;
        this.type = type;
        this.affordanceType = affordanceType;
        this.affordanceKey = affordanceKey;
        this.thingId = thingId;
        this.thingType = thingType;
        this.thingTypeVersion = thingTypeVersion;
        this.namespace = namespace;
        this.organizationId = organizationId;
        this.custom = custom;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public TopicType getType() {
        return type;
    }

    public void setType(TopicType type) {
        this.type = type;
    }

    public AffordanceType getAffordanceType() {
        return affordanceType;
    }

    public void setAffordanceType(AffordanceType affordanceType) {
        this.affordanceType = affordanceType;
    }

    public String getAffordanceKey() {
        return affordanceKey;
    }

    public void setAffordanceKey(String affordanceKey) {
        this.affordanceKey = affordanceKey;
    }

    public String getThingId() {
        return thingId;
    }

    public void setThingId(String thingId) {
        this.thingId = thingId;
    }

    public String getThingType() {
        return thingType;
    }

    public void setThingType(String thingType) {
        this.thingType = thingType;
    }

    public String getThingTypeVersion() {
        return thingTypeVersion;
    }

    public void setThingTypeVersion(String thingTypeVersion) {
        this.thingTypeVersion = thingTypeVersion;
    }

    public List<String> getNamespace() {
        return namespace;
    }

    public void setNamespace(List<String> namespace) {
        this.namespace = namespace;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public Boolean getCustom() {
        return custom;
    }

    public void setCustom(Boolean custom) {
        this.custom = custom;
    }
}
