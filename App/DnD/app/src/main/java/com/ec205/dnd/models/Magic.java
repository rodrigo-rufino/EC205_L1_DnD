package com.ec205.dnd.models;

/**
 * Created by rodri on 01-May-17.
 */

public class Magic {

    public String name;
    public String damage;
    public String distance;
    public String components;
    public String level;

    public Magic(){}

    public Magic(String name,
                 String damage,
                 String distance,
                 String components,
                 String level){
        this.name = name;
        this.damage = damage;
        this.distance = distance;
        this.components = components;
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public String getDamage() {
        return damage;
    }

    public String getDistance() {
        return distance;
    }

    public String getComponents() {
        return components;
    }

    public String getLevel() {
        return level;
    }
}
