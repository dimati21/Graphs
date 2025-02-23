package g413.lab08.sampledata;

public class Node {
    public float x, y;
    public String name;
    public int id;
    public Node(float x, float y, int id)
    {
        this.x = x;
        this.y = y;
        this.id = id;
        this.name = "Node " + id;
    }

    public int getId(){
        return this.id;
    }
}
