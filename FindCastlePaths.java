import java.util.*;

public class FindCastlePaths {
    private static final int SIZE = 10;
    private static int[][] board = new int[SIZE][SIZE];
    private static int startX, startY;

    private static final int[] DX = {0, 1, 0, -1};
    private static final int[] DY = {1, 0, -1, 0};

    private static List<List<String>> paths = new ArrayList<>();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of soldiers: ");
        int n = sc.nextInt();
        sc.nextLine();

        for (int i = 1; i <= n; i++) {
            System.out.print("Enter coordinates for soldier " + i + ": ");
            String[] coords = sc.nextLine().split(",");
            int x = Integer.parseInt(coords[0].trim());
            int y = Integer.parseInt(coords[1].trim());
            board[x][y] = 1;
        }

        System.out.print("Enter the coordinates for your castle: ");
        String[] startCoords = sc.nextLine().split(",");
        startX = Integer.parseInt(startCoords[0].trim());
        startY = Integer.parseInt(startCoords[1].trim());

        findPaths(startX, startY, 0, new ArrayList<>(), new boolean[SIZE][SIZE]);

        printResults();
    }

    private static void findPaths(int x, int y, int dir, List<String> path, boolean[][] visited) {
        visited[x][y] = true;

        if (allKilled() && x == startX && y == startY) {
            paths.add(new ArrayList<>(path));
            visited[x][y] = false;
            return;
        }

        for (int i = 0; i < 4; i++) {
            int newDir = (dir + i) % 4;
            int nx = x + DX[newDir];
            int ny = y + DY[newDir];

            if (isValid(nx, ny, visited)) {
                if (board[nx][ny] == 1) {
                    path.add("Kill (" + nx + "," + ny + "). Turn Left");
                    board[nx][ny] = 0;
                    findPaths(nx, ny, (newDir + 1) % 4, path, visited);
                    board[nx][ny] = 1;
                    path.remove(path.size() - 1);
                } else {
                    path.add("Jump to (" + nx + "," + ny + ")");
                    findPaths(nx, ny, newDir, path, visited);
                    path.remove(path.size() - 1);
                }
            }
        }

        visited[x][y] = false;
    }

    private static boolean isValid(int x, int y, boolean[][] visited) {
        return x >= 0 && y >= 0 && x < SIZE && y < SIZE && !visited[x][y];
    }

    private static boolean allKilled() {
        for (int[] row : board) {
            for (int cell : row) {
                if (cell == 1) return false;
            }
        }
        return true;
    }

    private static void printResults() {
        if (paths.isEmpty()) {
            System.out.println("No paths found for your castle");
        } else {
            System.out.println("\nThanks. There are " + paths.size() + " unique paths for your castle");

            int count = 1;
            for (List<String> path : paths) {
                System.out.println("\nPath " + count + ":");
                System.out.println("Start (" + startX + "," + startY + ")");
                for (String step : path) {
                    System.out.println(step);
                }
                System.out.println("Arrive (" + startX + "," + startY + ")");
                count++;
            }
        }
    }
}
