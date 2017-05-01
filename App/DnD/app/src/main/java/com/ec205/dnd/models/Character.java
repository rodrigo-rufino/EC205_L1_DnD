package com.ec205.dnd.models;

public class Character {
    public String name;
    public String xp;
    public String characterClass;
    public String hitDice;
    public String caracteristics;
    public String level;
    public String alignment;

    public  Character(){}

    public Character(String name,
                     String xp,
                     String characterClass,
                     String hitDice,
                     String caracteristics,
                     String level,
                     String alignment){
        this.name = name;
        this.xp = xp;
        this.characterClass = characterClass;
        this.hitDice = hitDice;
        this.caracteristics = caracteristics;
        this.level = level;
        this.alignment = alignment;
    }

    public String getName() {
        return name;
    }

    public String getXp() {
        return xp;
    }

    public String getCharacterClass() {
        return characterClass;
    }

    public String getHitDice() {
        return hitDice;
    }

    public String getCaracteristics() {
        return caracteristics;
    }

    public String getLevel() {
        return level;
    }

    public String getAlignment() {
        return alignment;
    }
}
