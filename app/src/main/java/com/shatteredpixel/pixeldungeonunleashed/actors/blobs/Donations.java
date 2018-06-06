/*
 * Unleashed Pixel Dungeon
 * Copyright (C) 2015  David Mitchell
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
package com.shatteredpixel.pixeldungeonunleashed.actors.blobs;

import com.shatteredpixel.pixeldungeonunleashed.Dungeon;
import com.shatteredpixel.pixeldungeonunleashed.Journal;
import com.shatteredpixel.pixeldungeonunleashed.actors.Actor;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Awareness;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Bless;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Buff;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Burning;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Paralysis;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Vertigo;
import com.shatteredpixel.pixeldungeonunleashed.actors.hero.Hero;
import com.shatteredpixel.pixeldungeonunleashed.effects.BlobEmitter;
import com.shatteredpixel.pixeldungeonunleashed.effects.Speck;
import com.shatteredpixel.pixeldungeonunleashed.items.Generator;
import com.shatteredpixel.pixeldungeonunleashed.items.Heap;
import com.shatteredpixel.pixeldungeonunleashed.items.Item;
import com.shatteredpixel.pixeldungeonunleashed.items.artifacts.Artifact;
import com.shatteredpixel.pixeldungeonunleashed.items.potions.PotionOfHealing;
import com.shatteredpixel.pixeldungeonunleashed.items.scrolls.Scroll;
import com.shatteredpixel.pixeldungeonunleashed.items.weapon.Weapon;
import com.shatteredpixel.pixeldungeonunleashed.items.weapon.enchantments.*;
import com.shatteredpixel.pixeldungeonunleashed.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.pixeldungeonunleashed.levels.Level;
import com.shatteredpixel.pixeldungeonunleashed.scenes.GameScene;
import com.shatteredpixel.pixeldungeonunleashed.utils.GLog;
import com.shatteredpixel.pixeldungeonunleashed.windows.WndMessage;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.Arrays;

public class Donations extends Blob {
    protected int pos;

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );

        for (int i=0; i < LENGTH; i++) {
            if (cur[i] > 0) {
                pos = i;
                break;
            }
        }
    }

    @Override
    protected void evolve() {
        volume = off[pos] = cur[pos];

        if (Dungeon.visible[pos]) {
            Journal.add( Journal.Feature.ALTAR );
            if (Dungeon.difficultyLevel == Dungeon.DIFF_TUTOR && !Dungeon.tutorial_altar_seen) {
                Dungeon.tutorial_altar_seen = true;
                GameScene.show(new WndMessage("An Altar room allows you to donate items to the DM in the hopes of " +
                    "divine intervention.  More expensive donations provide better rewards, including some difficult " +
                    "to obtain enchanted weapons, artifacts and instant level-ups.  Your donated loot is cumulative."));
            }
        }
    }

    @Override
    public void seed( int cell, int amount ) {
        cur[pos] = 0;
        pos = cell;
        volume = cur[pos] = amount;
    }

    private static boolean throwItem(int cell, Item thrownItem) {
        // throw the item off of the Altar to avoid a redonation loop
        int newPlace;
        do {
            newPlace = cell + Level.NEIGHBOURS8[Random.Int(8)];
        } while (!Level.passable[newPlace] && !Level.avoid[newPlace] && Actor.findChar(newPlace) == null);
        Dungeon.level.drop(thrownItem, newPlace).sprite.drop(cell);

        return true;
    }

    public static void donate( int cell ) {
        Hero hero = Dungeon.hero;
        Heap heap = Dungeon.level.heaps.get( cell );

        if (heap != null && hero.donatedLoot != -1) {
            // don't allow accidental donations of important items such as keys or quest items
            // and don't insult the gods with insignificant donations
            if (heap.peek().unique || heap.peek().price() < 5 || heap.peek() instanceof MissileWeapon) {
                GLog.p("Herbert refuses your offering.");
                // throw the item off of the Altar to avoid a redonation loop

                throwItem(cell, heap.pickUp());
                return;
            }

            if (heap.peek() instanceof Scroll) {
                hero.donatedLoot += (heap.peek().price() * heap.peek().quantity() * 2);
                //GLog.p("Current donation value: " + Integer.toString(hero.donatedLoot));
            } else {
                hero.donatedLoot += (heap.peek().price() * heap.peek().quantity());
                //GLog.p("Current donation value: " + Integer.toString(hero.donatedLoot));
            }
            if (heap.peek().cursed) {
                // the Gods do not like to receive cursed goods and will punish the hero for this
                Buff.affect(hero, Burning.class).reignite(hero);
                Buff.affect(hero, Paralysis.class);
                Buff.prolong(hero, Vertigo.class, 5f);
                GLog.w("Herbert is displeased with your donation!");

                // the hero may not use an altar again during this run until amended
                hero.donatedLoot = -1;
            } else if (hero.donatedLoot >= 350) {
                // in order to get here you either have to donate a lot of goods all at once,
                // or you have been doing a lot of donations and collecting the lower rewards
                GLog.p("Herbert is very pleased and rewards you!");
                if (Random.Int(3) == 0) {
                    GLog.p("Herbert floods your mind with visions of battles past.");
                    hero.earnExp( hero.maxExp() );
                    hero.donatedLoot = 0;
                } else {
                    try {
                        GLog.p("You are rewarded with an ancient Artifact!");
                        Item item = Generator.randomArtifact();
                        //if we're out of artifacts, return a ring instead.
                        if (item == null) {
                            GLog.p("Oops..there are none left of those...here have a ring!");
                            item : Generator.random(Generator.Category.RING);
                        }
                        GLog.p("You are rewarded with: " + item.name());
                        throwItem(cell, item);
                        hero.donatedLoot = 0;
                    } catch (Exception ex) {
                        GLog.n(Arrays.toString(ex.getStackTrace()));
                    }
                }
            } else if (hero.donatedLoot >= 150) {
                if (Random.Int(3) == 0) {
                    // upgrade an item
                    Weapon wpn = (Weapon) Generator.random(Generator.Category.MELEE);
                    try {
                        switch (Random.Int(13)) {
                            case 0:
                                wpn.enchant(Glowing.class.newInstance());
                                break;
                            case 1:
                                wpn.enchant(Ancient.class.newInstance());
                                break;
                            case 2:
                                wpn.enchant(Holy.class.newInstance());
                                break;
                            case 3:
                                wpn.enchant(Luck.class.newInstance());
                                break;
                            case 4:
                                wpn.enchant(Horror.class.newInstance());
                                break;
                            case 5:
                                wpn.enchant(Death.class.newInstance());
                                break;
                            case 6:
                                wpn.enchant(Midas.class.newInstance());
                                break;
                            case 7:
                                wpn.enchant(Leech.class.newInstance());
                                break;
                            case 8:
                                wpn.enchant(Hunting.class.newInstance());
                                break;
                            case 9:
                                wpn.enchant(com.shatteredpixel.pixeldungeonunleashed.items.weapon.enchantments.Paralysis.class.newInstance());
                                break;
                            case 10:
                                wpn.enchant(Slow.class.newInstance());
                                break;
                            case 11:
                                wpn.enchant(com.shatteredpixel.pixeldungeonunleashed.items.weapon.enchantments.Fire.class.newInstance());
                                break;
                            case 12:
                                wpn.enchant(Instability.class.newInstance());
                                break;
                        }
                        throwItem(cell, wpn);
                        hero.donatedLoot -= 150;
                        GLog.p("you are rewarded with a magical weapon.");
                    } catch (NullPointerException e) {
                    } catch (InstantiationException e) {
                    } catch (IllegalAccessException e) {
                    }
                    GLog.p("you feel very close to Herbert.");
                } else {
                    GLog.p("Herbert smiles upon you.");
                }
            } else if (hero.donatedLoot >= 50) {
                // some type of reward may be given to the hero, if so reduce the total donation value
                if (hero.HT < hero.HP) {
                    GLog.p("Herbert heals your wounds.");
                    PotionOfHealing.heal(hero);
                    hero.donatedLoot -= 40;
                } else if ((! hero.buffs().contains(Awareness.class)) && (Random.Int(6) == 0)) {
                    GLog.p("Herbert reveals secrets of your surroundings.");
                    Buff.affect(hero, Awareness.class, Awareness.DURATION * 2);
                    Dungeon.observe();
                    hero.donatedLoot -= 35;
                } else if ((! hero.buffs().contains(Bless.class)) && (Random.Int(6) == 0)){
                    GLog.p("Herbert blesses you.");
                    hero.belongings.uncurseEquipped();
                    Buff.affect(hero, Bless.class);
                    Buff.prolong(hero, Bless.class, 120f);
                    hero.donatedLoot -= 45;
                } else {
                    GLog.p("Herbert seems happy...maybe its laundry day...");
                }
            } else {
                GLog.p("Herbert accepts your donation...");
            }
            heap.donate();
            return;
        } else if (heap != null) {
            if ((heap.peek() instanceof Artifact || heap.peek().level >= 5)) {
                GLog.p("Herbert grumbles, but accepts your donation and allows you to donate more.");
                hero.donatedLoot = 0;
                heap.donate();
                return;
            } else {
                GLog.n("Herbert holds a grudge. You need to donate a very powerful item to please him now...");
                throwItem(cell, heap.pickUp());
                return;
            }
        }
    }

    @Override
    public void use(BlobEmitter emitter) {
        super.use(emitter);
        emitter.start(Speck.factory(Speck.LIGHT), 0.2f, 0);
    }
}