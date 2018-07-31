package encyclopedia;

import javafx.beans.property.SimpleStringProperty;

public class Animal {

    private SimpleStringProperty name;
    private SimpleStringProperty lifespan;
    private SimpleStringProperty color;
    private SimpleStringProperty sound;
    private SimpleStringProperty ID;

    public Animal() {
        this.name = new SimpleStringProperty("");
        this.lifespan = new SimpleStringProperty("");
        this.color = new SimpleStringProperty("");
        this.sound = new SimpleStringProperty("");
        this.ID = new SimpleStringProperty("");
    }

    public Animal(String name, String lifespan, String color, String sound) {
        this.name = new SimpleStringProperty(name);
        this.lifespan = new SimpleStringProperty(lifespan);
        this.color = new SimpleStringProperty(color);
        this.sound = new SimpleStringProperty(sound);
        this.ID = new SimpleStringProperty("");
    }

    public Animal(String name, String lifespan, String color, String sound, String ID) {
        this.name = new SimpleStringProperty(name);
        this.lifespan = new SimpleStringProperty(lifespan);
        this.color = new SimpleStringProperty(color);
        this.sound = new SimpleStringProperty(sound);
        this.ID = new SimpleStringProperty(ID);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getLifespan() {
        return lifespan.get();
    }

    public void setLifespan(String lifespan) {
        this.lifespan = new SimpleStringProperty(lifespan);
    }

    public String getColor() {
        return color.get();
    }

    public void setColor(String color) {
        this.color = new SimpleStringProperty(color);
    }

    public String getSound() {
        return sound.get();
    }

    public void setSound(String sound) {
        this.sound = new SimpleStringProperty(sound);
    }

    public String getID() {
        return ID.get();
    }

    public void setID(String ID) {
        this.ID.set(ID);
    }

}
