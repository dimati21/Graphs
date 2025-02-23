package g413.lab08;

import java.util.ArrayList;

import g413.lab08.sampledata.Link;
import g413.lab08.sampledata.Node;

public class Graph {
    ArrayList<Node> node = new ArrayList<Node>();
    ArrayList<Link> link = new ArrayList<Link>();

    public void add_node(float x, float y, int id)
    {
        node.add(new Node(x, y, id));
    }

    public void remove_node(int index)
    {
        if (index < 0) return;
        for (int i = link.size() - 1; i >= 0; i--) {
            Link l = link.get(i);
            Node n = node.get(index);
            if (l.source == n.getId() || l.target == n.getId()) {
                link.remove(i);
            }
        }
        node.remove(index);
    }

    public void add_link(int a, int b, int id)
    {
        if (a < 0 || b < 0) return;
        for (int i = 0; i < link.size(); i++)
        {
            Link l = link.get(i);
            if (a == l.source && b == l.source) return;
            if (b == l.target && a == l.target) return;
        }
        link.add(new Link(a, b, id));
    }

    public void remove_link(int a, int b)
    {
        if (a < 0 || b < 0) return;
        for (int i = 0; i < link.size(); i++) {
            Link l = link.get(i);
            if (a == l.source && b == l.target) {
                link.remove(i);
                return;
            }
            if (b == l.source && a == l.target) {
                link.remove(i);
                return;
            }
        }
    }


}
