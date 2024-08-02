import javax.swing.*;
import java.awt.*;
import java.util.List;          
import java.util.ArrayList;     
import java.util.Set;           
import java.util.HashSet;       
import java.util.Queue;         
import java.util.LinkedList;    
import java.util.Collections;   
import java.util.Arrays;

public class FlightPathDrawer extends JPanel {
    private List<List<Point>> flightPaths;
    private static final int SCALE = 100;
    private static final int OFFSET = 50;
    private static final int GRID_SIZE = 10;

    public FlightPathDrawer(List<List<Point>> flightPaths) {
        this.flightPaths = flightPaths;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setStroke(new BasicStroke(2));

        for (int i = 0; i < flightPaths.size(); i++) {
            List<Point> path = flightPaths.get(i);
            g2d.setColor(getColor(i));

            for (int j = 0; j < path.size() - 1; j++) {
                Point p1 = path.get(j);
                Point p2 = path.get(j + 1);
                g2d.drawLine(
                    p1.x * SCALE + OFFSET, p1.y * SCALE + OFFSET,
                    p2.x * SCALE + OFFSET, p2.y * SCALE + OFFSET
                );
            }

            
            for (Point p : path) {
                g2d.fillOval(p.x * SCALE + OFFSET - 3, p.y * SCALE + OFFSET - 3, 6, 6);
            }
        }
    }

    private Color getColor(int index) {
        Color[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE, Color.MAGENTA};
        return colors[index % colors.length];
    }

    
    private static List<Point> findPath(Point start, Point end, Set<Point> occupiedPoints) {
        Queue<List<Point>> queue = new LinkedList<>();
        Set<Point> visited = new HashSet<>();
        queue.add(Collections.singletonList(start));

        while (!queue.isEmpty()) {
            List<Point> path = queue.poll();
            Point current = path.get(path.size() - 1);

            if (current.equals(end)) {
                return path;
            }

            visited.add(current);

            for (Point neighbor : getNeighbors(current, occupiedPoints)) {
                if (!visited.contains(neighbor)) {
                    List<Point> newPath = new ArrayList<>(path);
                    newPath.add(neighbor);
                    queue.add(newPath);
                    visited.add(neighbor);
                }
            }
        }

        
        return null;
    }

    private static List<Point> getNeighbors(Point current, Set<Point> occupiedPoints) {
        List<Point> neighbors = new ArrayList<>();

        int[] dx = {-1, 1, 0, 0};
        int[] dy = {0, 0, -1, 1};

        for (int i = 0; i < 4; i++) {
            int newX = current.x + dx[i];
            int newY = current.y + dy[i];
            Point newPoint = new Point(newX, newY);

            if (newX >= 0 && newX < GRID_SIZE && newY >= 0 && newY < GRID_SIZE && !occupiedPoints.contains(newPoint)) {
                neighbors.add(newPoint);
            }
        }

        return neighbors;
    }

    public static void main(String[] args) {
        List<List<Point>> flightPaths = new ArrayList<>();
        Set<Point> occupiedPoints = new HashSet<>();

        List<List<Point>> inputs = new ArrayList<>();
        inputs.add(Arrays.asList(new Point(1, 1), new Point(2, 2), new Point(3, 3)));
        inputs.add(Arrays.asList(new Point(1, 1), new Point(2, 4), new Point(3, 2)));
        inputs.add(Arrays.asList(new Point(1, 1), new Point(4, 2), new Point(3, 4)));

        for (List<Point> input : inputs) {
            List<Point> path = new ArrayList<>();
            for (int i = 0; i < input.size() - 1; i++) {
                List<Point> segment = findPath(input.get(i), input.get(i + 1), occupiedPoints);
                if (segment == null) {
                    System.out.println("Unable to find a non-intersecting path for all flights.");
                    return;
                }
                path.addAll(segment.subList(0, segment.size() - 1));
            }
            path.add(input.get(input.size() - 1));
            flightPaths.add(path);
            occupiedPoints.addAll(path);
        }

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Flight Paths");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new FlightPathDrawer(flightPaths));
            frame.setSize(500, 500);
            frame.setVisible(true);
        });
    }
}
