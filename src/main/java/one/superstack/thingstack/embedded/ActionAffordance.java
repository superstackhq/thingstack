package one.superstack.thingstack.embedded;

import java.io.Serializable;

public class ActionAffordance implements Serializable {

    private String title;

    private String description;

    private DataSchema input;

    private DataSchema output;

    public ActionAffordance() {

    }

    public ActionAffordance(String title, String description, DataSchema input, DataSchema output) {
        this.title = title;
        this.description = description;
        this.input = input;
        this.output = output;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DataSchema getInput() {
        return input;
    }

    public void setInput(DataSchema input) {
        this.input = input;
    }

    public DataSchema getOutput() {
        return output;
    }

    public void setOutput(DataSchema output) {
        this.output = output;
    }
}
