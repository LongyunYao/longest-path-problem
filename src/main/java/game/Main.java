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
        List<Pos> longestPath = getLongestPathByDFS(complexMap, start, moveOffset);

        // 打印路径
        System.out.println(longestPath);
        printPathInMap(simpleMap, longestPath);

    }

    /**
     * 通过深度优先搜索获取最长路径
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
