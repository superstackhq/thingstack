package one.superstack.thingstack.response;

import java.io.Serializable;

public class SecretResponse implements Serializable {

    private String secret;

    public SecretResponse() {

    }

    public SecretResponse(String secret) {
        this.secret = secret;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
