package one.superstack.thingstack.request;

import jakarta.validation.constraints.NotBlank;
import one.superstack.thingstack.embedded.HookReference;

import java.io.Serializable;
import java.util.Set;

public class RuleCreationRequest implements Serializable {

    @NotBlank
    private String name;

    private String description;

    @NotBlank
    private String topic;

    @NotBlank
    private String expression;

    private Set<HookReference> hookReferences;

    public RuleCreationRequest() {

    }

    public RuleCreationRequest(String name, String description, String topic, String expression, Set<HookReference> hookReferences) {
        this.name = name;
        this.description = description;
        this.topic = topic;
        this.expression = expression;
        this.hookReferences = hookReferences;
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

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public Set<HookReference> getHooks() {
        return hookReferences;
    }

    public void setHooks(Set<HookReference> hookReferences) {
        this.hookReferences = hookReferences;
    }
}
