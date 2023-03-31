package one.superstack.thingstack.model;

import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

public class MqttAcl implements Serializable {

    @Id
    private String id;

    private String username;

    private Set<String> publish;

    private Set<String> subscribe;

    private Set<String> pubsub;

    private Date createdOn;

    private Date modifiedOn;

    public MqttAcl() {

    }

    public MqttAcl(String username, Set<String> publish, Set<String> subscribe, Set<String> pubsub) {
        this.username = username;
        this.publish = publish;
        this.subscribe = subscribe;
        this.pubsub = pubsub;
        this.createdOn = new Date();
        this.modifiedOn = new Date();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<String> getPublish() {
        return publish;
    }

    public void setPublish(Set<String> publish) {
        this.publish = publish;
    }

    public Set<String> getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(Set<String> subscribe) {
        this.subscribe = subscribe;
    }

    public Set<String> getPubsub() {
        return pubsub;
    }

    public void setPubsub(Set<String> pubsub) {
        this.pubsub = pubsub;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }
}
