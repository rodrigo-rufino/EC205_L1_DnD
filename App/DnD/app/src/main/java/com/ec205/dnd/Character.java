package com.ec205.dnd;

public class Character {
    private String Name;
    private String Xp;
    private String CharacterClass;
    private String HitDice;
    private String Caracteristics;
    private String Level;
    private String Alignment;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getXp() {
        return Xp;
    }

    public void setXp(String xp) {
        Xp = xp;
    }

    public String getCharacterClass() {
        return CharacterClass;
    }

    public void setCharacterClass(String characterClass) {
        CharacterClass = characterClass;
    }

    public String getHitDice() {
        return HitDice;
    }

    public void setHitDice(String hitDice) {
        HitDice = hitDice;
    }

    public String getCaracteristics() {
        return Caracteristics;
    }

    public void setCaracteristics(String caracteristics) {
        Caracteristics = caracteristics;
    }

    public String getLevel() {
        return Level;
    }

    public void setLevel(String level) {
        Level = level;
    }

    public String getAlignment() {
        return Alignment;
    }

    public void setAlignment(String alignment) {
        Alignment = alignment;
    }
}
