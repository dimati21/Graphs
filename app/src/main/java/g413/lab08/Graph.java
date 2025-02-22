package g413.lab08;

import java.util.ArrayList;

import g413.lab08.sampledata.Link;
import g413.lab08.sampledata.Node;

public class Graph {
    ArrayList<Node> node = new ArrayList<Node>();
    ArrayList<Link> link = new ArrayList<Link>();

    public void add_node(float x, float y)
    {
        node.add(new Node(x, y));
    }

    public void remove_node(int index)
    {
        if (index < 0) return;
        node.remove(index);
    }

    public void add_link(int a, int b)
    {
        if (a < 0 || b < 0) return;
        for (int i = 0; i < link.size(); i++)
        {
            Link l = link.get(i);
            if (a == l.a && b == l.b) return;
            if (b == l.a && a == l.b) return;
        }
        link.add(new Link(a, b));
    }

}
