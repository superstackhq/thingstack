package one.superstack.thingstack.response;

import java.io.Serializable;

public class PasswordResponse implements Serializable {

    private String password;

    public PasswordResponse() {

    }

    public PasswordResponse(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
