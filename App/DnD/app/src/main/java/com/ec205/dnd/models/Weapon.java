package com.ec205.dnd.models;

/**
 * Created by Eduardo on 08/05/2017.
 */
public class Weapon
{
   public String name;
    public String price;
    public String attack;
    public String type;
    public String magic;
    public String bonus;

    public Weapon(){}

    public Weapon(String name,
                  String price,
                  String damage,
                  String type,
                  String magic,
                  String bonus){
        this.name = name;
        this.price = price;
        this.attack = attack;
        this.type = type;
        this.magic = magic;
        this.bonus = bonus;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getDamage() {
        return attack;
    }

    public String getType() {
        return type;
    }

    public String getMagic() {
        return magic;
    }

    public String getBonus() {
        return bonus;
    }
}




