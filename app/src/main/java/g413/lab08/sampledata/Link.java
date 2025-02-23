package g413.lab08.sampledata;

public class Link {

    public int source, target, value, id;
    public Link(int a, int b, int id)
    {
        this.source = a;
        this.target= b;
        this.id = id;
        this.value = 1;
    }

    public int getId()
    {
        return this.id;
    }
}
