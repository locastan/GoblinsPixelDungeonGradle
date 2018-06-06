/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2015 Evan Debenham
 *
 * Goblins Pixel Dungeon
 * Copyright (C) 2016 Mario Braun
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.shatteredpixel.pixeldungeonunleashed.actors.mobs;

import com.shatteredpixel.pixeldungeonunleashed.Dungeon;
import com.shatteredpixel.pixeldungeonunleashed.actors.Char;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Buff;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Infested;
import com.shatteredpixel.pixeldungeonunleashed.items.Item;
import com.shatteredpixel.pixeldungeonunleashed.items.potions.PotionOfPurity;
import com.shatteredpixel.pixeldungeonunleashed.levels.Level;
import com.shatteredpixel.pixeldungeonunleashed.sprites.BodFlySprite;
import com.shatteredpixel.pixeldungeonunleashed.sprites.CharSprite;
import com.shatteredpixel.pixeldungeonunleashed.utils.GLog;


public class BodFly extends Mob {

    {
        name = "Bod Fly";
        spriteClass = BodFlySprite.class;

        HP = HT = 60;
        defenseSkill = 9;
        atkSkill = 12;
        dmgMin = 3;
        dmgMax = 10;
        EXP = 5;

        maxLvl = 12;
        TYPE_ANIMAL = true;

        flying = true;

        loot = new PotionOfPurity();
        lootChance = 0.05f;
    }

    @Override
    public boolean act() {

        if ((enemy != null) && (Level.adjacent(enemy.pos, this.pos))) {
            stealth(false);
        } else {
            stealth(true);
        }
        return super.act();
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        if (damage > 6) {
            GLog.n("You are Infested!");
            Buff.affect(enemy, Infested.class).set(5f, (enemy.HP/20));
        }
        return damage;
    }

    @Override
    public void onAttackComplete() {
        stealth(false);
        super.onAttackComplete();
    }

    @Override
    public String defenseVerb() {
        return "evaded";
    }

    @Override
    public void die( Object cause ){
        //sets drop chance
        stealth(false);
        lootChance = 1f/((8 + Dungeon.limitedDrops.swarmHP.count ));
        super.die( cause );
    }

    @Override
    protected Item createLoot(){
        Dungeon.limitedDrops.swarmHP.count++;
        return super.createLoot();
    }

    public void stealth(boolean on) {
        if (on) this.sprite.add( CharSprite.State.INVISIBLE );
        else if (this.invisible == 0) this.sprite.remove( CharSprite.State.INVISIBLE );
    }

    @Override
    public String description() {
        return
                "These dangerous flies are stealthy and sneak up on people to lay eggs in their skin. Ewww!";
    }
}
