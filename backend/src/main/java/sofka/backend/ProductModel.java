package sofka.backend;

public class ProductModel {
    
    private String name;
    private Integer inInventary;
    private Boolean enabled;
    private Integer min;
    private Integer max;
    private String collection;
    
    public Integer getInInventary() {
        return inInventary;
    }

    public void setInInventary(Integer inInventary) {
        this.inInventary = inInventary;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Integer getMin() {
        return min;
    }

    public void setMin(Integer min) {
        this.min = min;
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

   

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}