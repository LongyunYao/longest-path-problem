package game;

import java.util.ArrayList;
import java.util.List;

/**
 * 主函数
 * Created by wuzhaofeng on 17/9/24.
 */
public class Main {

    public static void main(String[] args) {

        // 初始化参数
        boolean[][] simpleMap = new boolean[][] {
                {false, false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, true , true , false},
                {false, false, false, true , false, false, true , true , false},
                {false, false, true , false, false, false, false, false, false},
                {false, false, true , false, false, false, false, false, false},
                {false, false, true , false, false, false, false, false, false},
                {false, false, false, true , false, true , false, false, false},
                {false, false, false, false, true , true , false, false, false},
                {false, false, false, false, false, false, false, false, false}
        };
        boolean[][] complexMap = new boolean[][] {
                {false, true,  true,  false, false, true,  true,  false, true},
                {true,  false, false, false, true,  false, false, false, true},
                {true,  true,  false, false, true,  true,  false, false, false},
                {false, true,  true,  false,  false, true,  true,  true,  false},
                {false, true,  true,  false, false, true,  true,  false, true},
                {true,  false, false, false, true,  false, false, false, true},
                {true,  true,  false, false, true,  true,  false, false, false},
                {false, true,  true,  true,  false, true,  true,  true,  false},
                {false, true,  true,  false, false, true,  true,  false, true}
        };
        Pos[] moveOffset = new Pos[] {
                new Pos(-1,  0),    // 向左移动
                new Pos(-1, -1),    // 向左上移动
                new Pos( 0, -1),    // 向上移动
                new Pos( 1, -1),    // 向右上移动
                new Pos( 1,  0),    // 向右移动
                new Pos( 1,  1),    // 向右下移动
                new Pos( 0,  1),    // 向下移动
                new Pos(-1,  1)     // 向左下移动
        };
        Pos start = new Pos(3, 3);

        // 执行深度优先算法
        long begin = System.currentTimeMillis();
        List<Pos> longestPath = getLongestPathBySA(complexMap, start, moveOffset);
        long end = System.currentTimeMillis();

        // 打印路径
        System.out.println(end - begin + "ms");
        System.out.println(longestPath);
        printPathInMap(simpleMap, longestPath);

    }

    /**
     * 通过深度优先搜索算法获取最长路径
     * @param map 地图
     * @param start 起点
     * @param moveOffset 方向偏移量
     * @return 最长路径
     */
    public static List<Pos> getLongestPathByDFS(boolean[][] map, Pos start, Pos[] moveOffset) {

        List<Pos> longestPath = new ArrayList<>();
        dfs(start, map, new ArrayList<>(), longestPath, moveOffset);
        return longestPath;

    }

    /**
     * 递归实现深度优先搜索
     * @param pos 当前节点
     * @param map 地图
     * @param path 当前路径
     * @param result 最终结果
     * @param moveOffset 方向偏移量
     */
    private static void dfs(Pos pos, boolean[][] map, List<Pos> path, List<Pos> result, Pos[] moveOffset) {

        // 记录当前节点的周围是否经过
        List<Pos> visited = new ArrayList<>();

        // 保存当前节点的周围节点
        Pos[] neighbours = new Pos[moveOffset.length];

        // 依次向周围行走
        for (int i = 0; i < moveOffset.length; i++) {
            Pos next = new Pos(pos.getX() + moveOffset[i].getX(), pos.getY() + moveOffset[i].getY());
            neighbours[i] = next;
            if (inMap(map, next) && !path.contains(next) && map[next.getY()][next.getX()]) {
                path.add(next);
                visited.add(next);
                dfs(next, map, path, result, moveOffset);
            }
        }

        // 当前无路可走时保存最长路径
        if (visited.isEmpty()) {
            if (path.size() > result.size()) {
                result.clear();
                result.addAll(path);
            }
        }

        // 当周围节点都不能行走时回退到上一步
        for (Pos neighbour : neighbours) {
            if (canPath(map, path, neighbour, visited)) {
                return;
            }
        }
        path.remove(pos);

    }

    /**
     * 判断当前节点是否可以行走
     */
    private static boolean canPath(boolean[][] map, List<Pos> path, Pos pos, List<Pos> visited) {

        // 不在地图里，不能行走
        if (!inMap(map, pos)) {
            return false;
        }

        // 空白格子，不能行走
        if (!map[pos.getY()][pos.getX()]) {
            return false;
        }

        // 已经在路径中或经过，不能行走
        if (path.contains(pos) || visited.contains(pos)) {
            return false;
        }

        return true;
    }

    /**
     * 通过贪心算法获取最长路径
     * @param map 地图
     * @param start 起点
     * @param moveOffset 方向偏移量
     * @return 最长路径
     */
    public static List<Pos> getLongestPathByChain(boolean[][] map, Pos start, Pos[] moveOffset) {

        List<Pos> longestPath = new ArrayList<>();
        chain(start, map, new ArrayList<>(), longestPath, moveOffset);
        return longestPath;

    }


    /**
     * 递归实现贪心算法
     * @param pos 当前节点
     * @param map 地图
     * @param path 当前路径
     * @param result 最终结果
     * @param moveOffset 方向偏移量
     */
    private static void chain(Pos pos, boolean[][] map, List<Pos> path, List<Pos> result, Pos[] moveOffset) {

        // 获取出路最小的节点
        Pos minWayPos = getMinWayPos(pos, map, moveOffset);

        if (minWayPos != null) {
            // 递归搜寻路径
            path.add(minWayPos);
            map[minWayPos.getY()][minWayPos.getX()] = false;
            chain(minWayPos, map, path, result, moveOffset);
        } else {
            // 当前无路可走时保存最长路径
            if (path.size() > result.size()) {
                result.clear();
                result.addAll(path);
            }
        }

    }

    /**
     * 获取当前节点周围最小出路的节点
     */
    private static Pos getMinWayPos(Pos pos, boolean[][] map, Pos[] moveOffset) {

        int minWayCost = Integer.MAX_VALUE;
        List<Pos> minWayPoss = new ArrayList<>();

        for (int i = 0; i < moveOffset.length; i++) {
            Pos next = new Pos(pos.getX() + moveOffset[i].getX(), pos.getY() + moveOffset[i].getY());
            if (inMap(map, next) && map[next.getY()][next.getX()]) {
                int w = -1;
                for (int j = 0; j < moveOffset.length; j++) {
                    Pos nextNext = new Pos(next.getX() + moveOffset[j].getX(), next.getY() + moveOffset[j].getY());
                    if (inMap(map, nextNext) && map[nextNext.getY()][nextNext.getX()]) {
                        w++;
                    }
                }
                if (minWayCost > w) {
                    minWayCost = w;
                    minWayPoss.clear();
                    minWayPoss.add(next);
                } else if (minWayCost == w) {
                    minWayPoss.add(next);
                }
            }
        }

        if (minWayPoss.size() != 0) {
            // 随机返回一个最小出路的节点
            return minWayPoss.get((int) (Math.random() * minWayPoss.size()));
        } else {
            return null;
        }

    }


    /**
     * 通过模拟退火算法获取最长路径
     * @param map 地图
     * @param start 起点
     * @param moveOffset 方向偏移量
     * @return 最长路径
     */
    public static List<Pos> getLongestPathBySA(boolean[][] map, Pos start, Pos[] moveOffset) {

        // 初始化参数
        double temperature = 100.0;
        double endTemperature = 1e-8;
        double descentRate = 0.98;
        double count = 0;
        double total = Math.log(endTemperature / temperature) / Math.log(descentRate);
        int iterations = map.length * map[0].length;
        List<Pos> longestPath = new ArrayList<>();
        List<List<Pos>> paths = new ArrayList<>();
        for (int i = 0; i < iterations; i++) {
            boolean[][] cloneMap = deepCloneMap(map);
            List<Pos> path = initPath(cloneMap, start, moveOffset);
            paths.add(path);
        }

        // 降温过程
        while (temperature > endTemperature) {

            // 迭代过程
            for (int i = 0; i < iterations; i++) {

                // 取出当前解，并计算函数结果
                List<Pos> path = paths.get(i);
                int result = caculateResult(path);

                // 在邻域内产生新的解，并计算函数结果
                boolean[][] cloneMap = deepCloneMap(map);
                List<Pos> newPath = getNewPath(cloneMap, path, moveOffset, count / total);
                int newResult = caculateResult(newPath);

                // 判断是否替换解
                if (newResult - result < 0) {
                    // 替换
                    path.clear();
                    path.addAll(newPath);
                } else {
                    // 以概率替换
                    double p = 1 / (1 + Math.exp(-(newResult - result) / temperature));
                    if (Math.random() < p) {
                        path.clear();
                        path.addAll(newPath);
                    }
                }

            }

            count++;
            temperature = temperature * descentRate;

        }

        // 返回一条最长的路径
        for (int i = 0; i < paths.size(); i++) {
            if (paths.get(i).size() > longestPath.size()) {
                longestPath = paths.get(i);
            }
        }
        return longestPath;

    }

    /**
     * 深拷贝地图
     */
    private static boolean[][] deepCloneMap(boolean[][] map) {
        boolean[][] cloneMap = new boolean[map.length][];
        for (int i = 0; i < map.length; i++) {
            cloneMap[i] = map[i].clone();
        }
        return cloneMap;
    }

    /**
     * 初始化路径
     */
    private static List<Pos> initPath(boolean[][] map, Pos start, Pos[] moveOffset) {

        List<Pos> path = new ArrayList<>();
        getPath(map, start, path, moveOffset);
        return path;

    }

    /**
     * 根据当前路径继续行走到底，采用随机行走策略
     */
    private static void getPath(boolean[][] map, Pos current, List<Pos> path, Pos[] moveOffset) {

        boolean end = true;
        List<Pos> neighbours = new ArrayList<>();
        for (int i = 0; i < moveOffset.length; i++) {
            Pos neighbour = new Pos(current.getX() + moveOffset[i].getX(), current.getY() + moveOffset[i].getY());
            if (inMap(map, neighbour) && map[neighbour.getY()][neighbour.getX()]) {
                end = false;
                neighbours.add(neighbour);
            }
        }
        if (end) {
            return;
        } else {
            Pos random = neighbours.get((int) (Math.random() * neighbours.size()));
            map[random.getY()][random.getX()] = false;
            path.add(random);
            getPath(map, random, path, moveOffset);
        }

    }

    /**
     * 计算函数结果，函数结果为路径负长度
     */
    private static int caculateResult(List<Pos> path) {
        return -path.size();
    }


    /**
     * 根据当前路径和降温率，生成一条新路径
     */
    private static List<Pos> getNewPath(boolean[][] map, List<Pos> path, Pos[] moveOffset, double ratio) {

        int size = (int) (path.size() * ratio);
        if (size == 0) {
            size = 1;
        }
        if (size > path.size()) {
            size = path.size();
        }

        List<Pos> newPath = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Pos pos = path.get(i);
            newPath.add(pos);
            map[pos.getY()][pos.getX()] = false;
        }

        getPath(map, newPath.get(newPath.size() - 1), newPath, moveOffset);
        return newPath;

    }


    /**
     * 判断当前节点是否在地图内
     */
    private static boolean inMap(boolean[][] map, Pos pos) {

        if (pos.getY() < 0 || pos.getY() >= map.length) {
            return false;
        }

        if (pos.getX() < 0 || pos.getX() >= map[0].length) {
            return false;
        }

        return true;

    }

    /**
     * 在地图中打印行走路径
     */
    private static void printPathInMap(boolean[][] map, List<Pos> result) {

        int[][] printMap = new int[map.length][map[0].length];

        for (int i = 0; i < result.size(); i++) {
            Pos pos = result.get(i);
            printMap[pos.getY()][pos.getX()] = i + 1;
        }

        for (int i = 0; i < printMap.length; i++) {
            for (int j = 0; j < printMap[0].length; j++) {
                int value = printMap[i][j];
                if (value < 10) {
                    System.out.print(" " + value + " ");
                } else {
                    System.out.print(value + " ");
                }
            }
            System.out.println();
        }

    }

}
