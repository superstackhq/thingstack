package one.superstack.thingstack.request;

import java.io.Serializable;

public class RuleUpdateRequest implements Serializable {

    private String name;

    private String description;

    private String topic;

    private String expression;

    public RuleUpdateRequest() {

    }

    public RuleUpdateRequest(String name, String description, String topic, String expression) {
        this.name = name;
        this.description = description;
        this.topic = topic;
        this.expression = expression;
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
}
