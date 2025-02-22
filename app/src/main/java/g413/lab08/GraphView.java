package g413.lab08;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceView;

import g413.lab08.sampledata.Link;
import g413.lab08.sampledata.Node;

public class GraphView extends SurfaceView {
    Graph g = new Graph();
    Paint p;

    int selected1 = -1;
    int selected2 = -1;
    int lastHit = -1;

    float rad = 10.0f;
    float halfside = 5.0f;

    float last_x;
    float last_y;

    public void add_node() {
        g.add_node(100.0f, 100.0f);
        invalidate();
    }

    public void remove_selected_node() {
        if (selected1 < 0) return;
        g.remove_node(selected1);
        selected1 = -1;
        invalidate();
    }

    public void link_selected_nodes() {
        if (selected1 < 0) return;
        if (selected2 < 0) return;
        g.add_link(selected1, selected2);
        invalidate();
    }

    public GraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
        p = new Paint();
        p.setAntiAlias(true);
        setWillNotDraw(false);
    }
    public int get_link_at_xy(float x, float y) {
        for (int i = 0; i < g.link.size(); i++) {
            Link l = g.link.get(i);
            Node na = g.node.get(l.a);
            Node nb = g.node.get(l.b);
            float bx = (na.x + nb.x) * 0.5f;
            float by = (na.y + nb.y) * 0.5f;
            if (x >= bx - halfside && x <= bx + halfside && y >= by - halfside && y <= by + halfside) return i;
        }
        return -1;
    }

    public int get_node_at_xy(float x, float y) {
        for (int i = g.node.size() - 1; i >= 0; i--) {
            Node n = g.node.get(i);
            float dx = x - n.x;
            float dy = y - n.y;
            if (dx * dx + dy * dy <= rad * rad) return i;
        }
        return -1;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float x = event.getX();
        float y = event.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                int i = get_node_at_xy(x, y);
                lastHit = i;
                if (i < 0) {
                    selected1 = -1;
                    selected2 = -1;
                } else {
                    if (selected1 >= 0) selected2 = i;
                    else selected1 = i;
                }
                last_x = x;
                last_y = y;
                invalidate();
                return true;

            case MotionEvent.ACTION_UP:
                break;

            case MotionEvent.ACTION_MOVE:
                if (lastHit >= 0) {
                    Node n = g.node.get(lastHit);
                    n.x += x - last_x;
                    n.y += y - last_y;
                    invalidate();
                }
                last_x = x;
                last_y = y;
                return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.rgb(255, 255, 255));

        for (int i = 0; i < g.link.size(); i++) {
            Link l = g.link.get(i);
            Node na = g.node.get(l.a);
            Node nb = g.node.get(l.b);
            p.setColor(Color.argb(127, 0, 0, 0));
            canvas.drawLine(na.x, na.y, nb.x, nb.y, p);
            float bx = (na.x + nb.x) * 0.5f;
            float by = (na.y + nb.y) * 0.5f;
            float x0 = bx - halfside;
            float y0 = by - halfside;
            float x1 = bx + halfside;
            float y1 = by + halfside;
            canvas.drawRect(x0, y0, x1, y1, p);
        }

        for (int i = 0; i < g.node.size(); i++) {
            Node n = g.node.get(i);

            p.setStyle(Paint.Style.FILL);

            if (i == selected1) p.setColor(Color.argb(50, 127, 0, 255));
        else if (i == selected2) p.setColor(Color.argb( 50, 255, 0, 50));
        else p.setColor(Color.argb(50, 0, 127, 255));

            canvas.drawCircle(n.x, n.y, rad, p);

            p.setStyle(Paint.Style.STROKE);

            if (i == selected1) p.setColor(Color.rgb(127, 0, 255));
        else if (i == selected2) p.setColor(Color.rgb(255, 0, 50));
        else p.setColor(Color.rgb(0, 127, 255));

            canvas.drawCircle(n.x, n.y, rad, p);
        }
    }
}
