import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Canvas extends JPanel {
    private final static int STROKE_SIZE = 8;
    private List<List<ColorPoint>> allPaths;
    private List<ColorPoint> currentPath;
    private Color color;
    private int canvasWidth, canvaHight;
    private int x, y;

    public Canvas(int targetwidth, int targethight) {
        super();
        setPreferredSize(new Dimension(targetwidth, targethight));
        setOpaque(true);
        setBackground(Color.white);
        setBorder(BorderFactory.createLineBorder(color.black));
        allPaths = new ArrayList<>(25);
        canvasWidth = targetwidth;
        canvaHight = targethight;
        MouseAdapter ma = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                x = e.getX();
                y = e.getY();
                Graphics g = getGraphics();
                g.setColor(color);
                g.fillRect(x, y, STROKE_SIZE, STROKE_SIZE);
                g.dispose();
                currentPath = new ArrayList<>(25);
                currentPath.add((new ColorPoint(x, y, color)));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                currentPath = null;
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                x = e.getX();
                y = e.getY();
                Graphics2D g2d = (Graphics2D) getGraphics();
                g2d.setColor(color);
                if (!currentPath.isEmpty()) {
                    ColorPoint prevPoint = currentPath.get(currentPath.size() - 1);
                    g2d.setStroke(new BasicStroke(STROKE_SIZE));
                    g2d.drawLine(prevPoint.getX(), prevPoint.getY(), x, y);
                }
                g2d.dispose();
                ColorPoint nextPoint = new ColorPoint(e.getX(), e.getY(), color);
                currentPath.add(nextPoint);
            }

        };
        addMouseListener(ma);
        addMouseMotionListener(ma);
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void resetCanvas() {
        Graphics g = getGraphics();
        g.clearRect(0, 0, canvasWidth, canvaHight);
        g.dispose();
        allPaths = new ArrayList<>(25);
        currentPath = null;
        repaint();
        revalidate();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // redraws all of the paths created so far
        for (List<ColorPoint> path : allPaths) {
            ColorPoint from = null;
            for (ColorPoint point : path) {
                g2d.setColor(point.getColor());

                // edge case when a single dot is created
                if (path.size() == 1) {
                    g2d.fillRect(point.getX(), point.getY(), STROKE_SIZE, STROKE_SIZE);
                }

                if (from != null) {
                    g2d.setStroke(new BasicStroke(STROKE_SIZE));
                    g2d.drawLine(from.getX(), from.getY(), point.getX(), point.getY());
                }
                from = point;
            }
        }
    }
}