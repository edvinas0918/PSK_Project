package models;

/**
 * Created by Gintautas on 4/30/2016.
 */
public class SettingsDto {
    private int id;
    private String name;
    private String value;
    private String type;

    public SettingsDto(){

    }

    public SettingsDto(int id, String name, String value, String type){
        this.id = id;
        this.name = name;
        this.value = value;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
