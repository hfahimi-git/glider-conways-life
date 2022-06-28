package com.hfahimi.cp;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class LifeGame {

    private final Universe universe;

    public Set<Coordinates> liveList;
    private static final Set<Coordinates> neighborsFormula;
    private int tickCount = 1;

    static {
        neighborsFormula = new HashSet<>();
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (i != 0 || j != 0)
                    neighborsFormula.add(new Coordinates(i, j));
            }
        }
    }

    public LifeGame(Universe universe) {
        this.universe = universe;
        liveList = new HashSet<>();
        //initiate pattern
        liveList.add(new Coordinates(1, 2));
        liveList.add(new Coordinates(2, 3));
        liveList.add(new Coordinates(3, 1));
        liveList.add(new Coordinates(3, 2));
        liveList.add(new Coordinates(3, 3));
    }

    //add one row and one column to universe, in fact relocate live cell by decrease x and y
    public void extendUniverse(){
        Set<Coordinates> relocateLiveList = new HashSet<>();
        for(Coordinates point: liveList) {
            relocateLiveList.add(new Coordinates(point.x()-1, point.y()-1));
        }
        this.liveList = relocateLiveList;
    }

    public void tick() {
        int xMax = universe.maxX();
        int yMax = universe.maxY();
        Set<Coordinates> newLiveList = new HashSet<>();
        for (int x = 0; x < xMax; x++) {
            for (int y = 0; y < yMax; y++) {
                Coordinates point = new Coordinates(x, y);
                Set<Coordinates> neighbours = getNeighborsCoordinates(point);
                newLiveList.add(checkRules(point, neighbours));
            }
        }
        liveList.clear();
        newLiveList.remove(null);
        liveList.addAll(newLiveList);
        tickCount++;
    }

    private @Nullable Coordinates checkRules(Coordinates point, @NotNull Set<Coordinates> neighbours) {
        boolean isPointLive = liveList.contains(point);
        int liveNeighborsCount = 0;
        for (Coordinates neighbour: neighbours) {
            if(liveList.contains(neighbour)) {
                liveNeighborsCount++;
            }
        }
        if(isPointLive) {
            if(liveNeighborsCount == 2 || liveNeighborsCount == 3) {
                return point;
            }
        }
        else {
            if(liveNeighborsCount == 3) {
                return point;
            }
        }
        return null;
    }

    private Set<Coordinates> getNeighborsCoordinates(Coordinates point) {
        Set<Coordinates> neighborsCoordinate = new HashSet<>();
        for (Coordinates c : neighborsFormula) {
            int x = c.x() + point.x();
            int y = c.y() + point.y();
            if (x >= 0 && x < universe.maxX() && y >= 0 && y < universe.maxY()) {
                neighborsCoordinate.add(new Coordinates(x, y));
            }
        }
        return neighborsCoordinate;
    }

    public void draw() {
        StringBuilder result = new StringBuilder();
        int xMax = universe.maxX();
        int yMax = universe.maxY();
        for (int x = 0; x < xMax; x++) {
            for (int y = 0; y < yMax; y++) {
                if (liveList.contains(new Coordinates(x, y))) {
                    result.append("X ");
                } else {
                    result.append("O ");
                }
            }
            result.append("\n");
        }
        System.out.println(result);
    }

    public int getTickCount() {
        return tickCount;
    }

    public void play() throws InterruptedException {

        System.out.println("very first generation -------------------------------");
        draw();

        while (true) {
            if(tickCount%4 == 0) {
                System.out.println(tickCount/4 + " -------------------------------");
                extendUniverse();
            }
            tick();
            draw();
            Thread.sleep(1000);
        }
    }
}
