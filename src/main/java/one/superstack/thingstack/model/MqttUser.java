package one.superstack.thingstack.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

@Document(collection = "mqtt_users")
public class MqttUser implements Serializable {

    @Id
    private String id;

    private String username;

    private String password;

    private Date createdOn;

    private Date modifiedOn;

    public MqttUser() {
    }

    public MqttUser(String id, String username, String password, Date createdOn, Date modifiedOn) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.createdOn = createdOn;
        this.modifiedOn = modifiedOn;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
