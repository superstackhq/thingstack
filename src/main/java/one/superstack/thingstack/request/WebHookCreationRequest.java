package one.superstack.thingstack.request;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

public class WebHookCreationRequest implements Serializable {

    @NotBlank
    private String name;

    private String description;

    @NotBlank
    private String endpoint;

    public WebHookCreationRequest() {

    }

    public WebHookCreationRequest(String name, String description, String endpoint) {
        this.name = name;
        this.description = description;
        this.endpoint = endpoint;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }
}
