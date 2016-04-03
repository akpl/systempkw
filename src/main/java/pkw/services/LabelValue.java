package pkw.services;

public class LabelValue {
    private String label;
    private Integer value;

    public LabelValue(String label, Integer value) {
        this.setValue(value);
        this.setLabel(label);
    }

    public String getLabel() {
        return label;
    }

    public Integer getValue() {
        return value;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
