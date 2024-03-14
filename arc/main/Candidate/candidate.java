public class Candidate {
    private int id;
    private String name;
    private boolean active;

    public Candidate(int id, String name) {
        this.id = id;
        this.name = name;
        this.active = true;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
