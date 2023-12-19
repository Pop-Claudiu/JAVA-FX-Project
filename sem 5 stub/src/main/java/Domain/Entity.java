package Domain;

import java.io.Serializable;

public abstract class Entity implements Serializable {
    protected int ID;

    public Entity(int ID) {
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }
}
