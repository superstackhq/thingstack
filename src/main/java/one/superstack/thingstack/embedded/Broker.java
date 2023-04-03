package one.superstack.thingstack.embedded;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

public class Broker implements Serializable {

    private String endpoint;

    private String username;

    @JsonIgnore
    private String password;

    public Broker() {

    }

    public Broker(String endpoint, String username, String password) {
        this.endpoint = endpoint;
        this.username = username;
        this.password = password;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
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
}
