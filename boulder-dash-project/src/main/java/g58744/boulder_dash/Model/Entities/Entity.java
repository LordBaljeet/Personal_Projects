package g58744.boulder_dash.Model.Entities;

import java.util.Objects;

public class Entity {

    private final EntityType type;
    private String representation;

    public Entity(EntityType type) {
        this.type = type;
        representation = type.toString();
    }

    /**
     *
     * @return the type of the component. see ComponentType class
     */
    public EntityType getType() {
        return type;
    }

    /**
     * Changer the string representation of the component to the given one
     * @param representation the desired representation
     */
    public void setRepresentation(String representation) {
        this.representation = representation;
    }

    @Override
    public String toString() {
        return representation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Entity entity)) return false;
        return type == entity.type && Objects.equals(representation, entity.representation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, representation);
    }

}