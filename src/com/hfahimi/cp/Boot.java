package com.hfahimi.cp;

import java.util.HashSet;
import java.util.Set;

public class Boot {
    public static void main(String[] args) throws InterruptedException {
        Universe universe = new Universe(5, 5);
        LifeGame lg = new LifeGame(universe);
        lg.play();
    }
}
