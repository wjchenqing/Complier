package Frontend.Entity;

abstract public class Entity{
    private final String name;

    public Entity(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
