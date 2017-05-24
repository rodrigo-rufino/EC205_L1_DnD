package com.ec205.dnd.models;

/**
 * Created by Eduardo on 08/05/2017.
 */
public class Equipament
{
   public String name;
    public String classe;
    public String level;
    public String hp;
    public String caracteristics;
    public String xp;
    public String alignment;

    public Equipament(){}

    public Equipament(String name,
                     String classe,
                     String level,
                     String hp,
                     String caracteristics,
                     String xp,
                     String alignment){
        this.name = name;
        this.classe = classe;
        this.level = level;
        this.hp = hp;
        this.caracteristics = caracteristics;
        this.xp = xp;
        this.alignment = alignment;
    }

    public String getName() {
        return name;
    }

    public String getClasse() {
        return classe;
    }

    public String getLevel() {
        return level;
    }

    public String getHp() {
        return hp;
    }

    public String getCaracteristics() {
        return caracteristics;
    }

    public String getXp() {
        return xp;
    }

    public String getAlignment() {
        return alignment;
    }

}




