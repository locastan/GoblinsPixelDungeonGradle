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
package com.shatteredpixel.pixeldungeonunleashed.items.food;

import com.shatteredpixel.pixeldungeonunleashed.Assets;
import com.shatteredpixel.pixeldungeonunleashed.Badges;
import com.shatteredpixel.pixeldungeonunleashed.Challenges;
import com.shatteredpixel.pixeldungeonunleashed.Dungeon;
import com.shatteredpixel.pixeldungeonunleashed.Statistics;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Barkskin;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Bleeding;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Buff;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Cripple;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.EarthImbue;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.FireImbue;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Hunger;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Invisibility;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Poison;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.ToxicImbue;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Weakness;
import com.shatteredpixel.pixeldungeonunleashed.actors.hero.Hero;
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.npcs.Wandmaker;
import com.shatteredpixel.pixeldungeonunleashed.effects.Speck;
import com.shatteredpixel.pixeldungeonunleashed.effects.SpellSprite;
import com.shatteredpixel.pixeldungeonunleashed.items.Item;
import com.shatteredpixel.pixeldungeonunleashed.items.Recipe;
import com.shatteredpixel.pixeldungeonunleashed.items.potions.*;
import com.shatteredpixel.pixeldungeonunleashed.items.scrolls.ScrollOfRecharging;
import com.shatteredpixel.pixeldungeonunleashed.plants.Blindweed;
import com.shatteredpixel.pixeldungeonunleashed.plants.Dreamfoil;
import com.shatteredpixel.pixeldungeonunleashed.plants.Earthroot;
import com.shatteredpixel.pixeldungeonunleashed.plants.Fadeleaf;
import com.shatteredpixel.pixeldungeonunleashed.plants.Firebloom;
import com.shatteredpixel.pixeldungeonunleashed.plants.Icecap;
import com.shatteredpixel.pixeldungeonunleashed.plants.Plant.Seed;
import com.shatteredpixel.pixeldungeonunleashed.plants.Sorrowmoss;
import com.shatteredpixel.pixeldungeonunleashed.plants.Stormvine;
import com.shatteredpixel.pixeldungeonunleashed.plants.Sungrass;
import com.shatteredpixel.pixeldungeonunleashed.sprites.ItemSprite;
import com.shatteredpixel.pixeldungeonunleashed.sprites.ItemSpriteSheet;
import com.shatteredpixel.pixeldungeonunleashed.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Yumyuck extends Food {

    public String message = "You eat the yumyuck moss. It's the best thing you ever tast...bleugh!";
    public String info = "A moist fluffy bit of moss. Yummy taste...followed by SONOVACRAP horrible!";

    public Potion potionAttrib = null;
    public static ItemSprite.Glowing potionGlow = null;

    {
        name = "Yumyuck Moss";
        stackable = true;
        image = ItemSpriteSheet.BLANDFRUIT;
        energy = Hunger.STARVING;
        hornValue = 6; //only applies when blandfruit is cooked

        bones = true;
    }

    @Override
    public boolean isSimilar(Item item) {
        if (item instanceof Yumyuck) {
            if (potionAttrib == null) {
                if (((Yumyuck) item).potionAttrib == null)
                    return true;
            } else if (((Yumyuck) item).potionAttrib != null) {
                if (((Yumyuck) item).potionAttrib.getClass() == potionAttrib.getClass())
                    return true;
            }
        }
        return false;
    }

    @Override
    public void execute(Hero hero, String action) {
        if (action.equals(AC_EAT)) {

            if (potionAttrib == null) {

                detach(hero.belongings.backpack);

                ((Hunger) hero.buff(Hunger.class)).satisfy(energy);
                GLog.i(message);

                hero.sprite.operate(hero.pos);
                hero.busy();
                SpellSprite.show(hero, SpellSprite.FOOD);
                Sample.INSTANCE.play(Assets.SND_EAT);

                hero.spend(1f);

                Statistics.foodEaten++;
                Badges.validateFoodEaten();
            } else {

                ((Hunger) hero.buff(Hunger.class)).satisfy(Hunger.HUNGRY);

                detach(hero.belongings.backpack);

                hero.spend(1f);
                hero.busy();

                if (potionAttrib instanceof PotionOfFrost) {
                    GLog.i("the Ice-yuck tastes a bit like Frozen Carpaccio turned bad.");
                    switch (Random.Int(5)) {
                        case 0:
                            GLog.i("You see your hands turn invisible!");
                            Buff.affect(hero, Invisibility.class, Invisibility.DURATION);
                            break;
                        case 1:
                            GLog.i("You feel your skin harden!");
                            Buff.affect(hero, Barkskin.class).level(hero.HT / 4);
                            break;
                        case 2:
                            GLog.i("Refreshing!");
                            Buff.detach(hero, Poison.class);
                            Buff.detach(hero, Cripple.class);
                            Buff.detach(hero, Weakness.class);
                            Buff.detach(hero, Bleeding.class);
                            break;
                        case 3:
                            GLog.i("You feel better!");
                            if (hero.HP < hero.HT) {
                                hero.HP = Math.min(hero.HP + hero.HT / 4, hero.HT);
                                hero.sprite.emitter().burst(Speck.factory(Speck.HEALING), 1);
                            }
                            break;
                    }
                } else if (potionAttrib instanceof PotionOfLiquidFlame) {
                    GLog.i("You feel a great fire burning within you!");
                    Buff.affect(hero, FireImbue.class).set(FireImbue.DURATION);
                } else if (potionAttrib instanceof PotionOfToxicGas) {
                    GLog.i("You are imbued with vile toxic power!");
                    Buff.affect(hero, ToxicImbue.class).set(ToxicImbue.DURATION);
                } else if (potionAttrib instanceof PotionOfParalyticGas) {
                    GLog.i("You feel the power of the earth coursing through you!");
                    Buff.affect(hero, EarthImbue.class, EarthImbue.DURATION);
                } else
                    potionAttrib.apply(hero);

                Sample.INSTANCE.play(Assets.SND_EAT);
                SpellSprite.show(hero, SpellSprite.FOOD);
                hero.sprite.operate(hero.pos);

                switch (hero.heroClass) {
                    case COMPLAINS:
                        if (hero.HP < hero.HT) {
                            hero.HP = Math.min(hero.HP + 5, hero.HT);
                            hero.sprite.emitter().burst(Speck.factory(Speck.HEALING), 1);
                        }
                        break;
                    case CHIEF:
                        //1 charge
                        Buff.affect(hero, ScrollOfRecharging.Recharging.class, 4f);
                        ScrollOfRecharging.charge(hero);
                        break;
                    case FUMBLES:
                    case THACO:
                        break;
                }
            }
        } else {
            super.execute(hero, action);
        }
    }

    @Override
    public String info() {
        return info;
    }

    @Override
    public int price() {
        return 20 * quantity;
    }

    public Item cook(Seed seed) {

        try {
            return imbuePotion((Potion) seed.alchemyClass.newInstance());
        } catch (Exception e) {
            return null;
        }

    }

    public Item imbuePotion(Potion potion) {

        potionAttrib = potion;
        potionAttrib.ownedByFruit = true;

        potionAttrib.image = ItemSpriteSheet.BLANDFRUIT;


        info = "The moss has absorbed the properties of the seed it was cooked with. " +
                "Unfortunately the \"Yum\" has been replaced with the magic effect and the \"Yuck\" remained.\n\n";

        if (potionAttrib instanceof PotionOfHealing) {

            name = "Health-Yuck";
            potionGlow = new ItemSprite.Glowing(0x2EE62E);
            info += "It looks nutritious and healthy, ready to be eaten!";

        } else if (potionAttrib instanceof PotionOfStrength) {

            name = "Power-Yuck";
            potionGlow = new ItemSprite.Glowing(0xCC0022);
            info += "It looks quite powerful, ready to be eaten!";

        } else if (potionAttrib instanceof PotionOfParalyticGas) {

            name = "Root-Yuck";
            potionGlow = new ItemSprite.Glowing(0x67583D);
            info += "It looks firm and hardening, ready to be eaten!";

        } else if (potionAttrib instanceof PotionOfInvisibility) {

            name = "Invisi-Yuck";
            potionGlow = new ItemSprite.Glowing(0xE5D273);
            info += "It looks translucent and shiny, ready to be eaten!";

        } else if (potionAttrib instanceof PotionOfLiquidFlame) {

            name = "Flame-Yuck";
            potionGlow = new ItemSprite.Glowing(0xFF7F00);
            info += "It smells hot and spicy, ready to be eaten!";

        } else if (potionAttrib instanceof PotionOfFrost) {

            name = "Ice-Yuck";
            potionGlow = new ItemSprite.Glowing(0x66B3FF);
            info += "It looks cool and refreshing, ready to be eaten!";

        } else if (potionAttrib instanceof PotionOfMindVision) {

            name = "Vision-Yuck";
            potionGlow = new ItemSprite.Glowing(0xB8E6CF);
            info += "It looks weird and shadowy, ready to be eaten!";

        } else if (potionAttrib instanceof PotionOfToxicGas) {

            name = "Toxic-Yuck";
            potionGlow = new ItemSprite.Glowing(0xA15CE5);
            info += "It looks fresh and crisp, ready to be eaten!";

        } else if (potionAttrib instanceof PotionOfLevitation) {

            name = "Float-Yuck";
            potionGlow = new ItemSprite.Glowing(0x1C3A57);
            info += "It looks lofty and lightweight, ready to be eaten!";

        } else if (potionAttrib instanceof PotionOfPurity) {

            name = "Pure-Yuck";
            potionGlow = new ItemSprite.Glowing(0x8E2975);
            info += "It looks pure and clean, ready to be eaten!";

        } else if (potionAttrib instanceof PotionOfExperience) {

            name = "Star-Yuck";
            potionGlow = new ItemSprite.Glowing(0xA79400);
            info += "It looks exciting and glorious, ready to be eaten!";

        }

        return this;
    }

    public static final String POTIONATTRIB = "potionattrib";

    @Override
    public void cast(final Hero user, int dst) {
        if (potionAttrib instanceof PotionOfLiquidFlame ||
                potionAttrib instanceof PotionOfToxicGas ||
                potionAttrib instanceof PotionOfParalyticGas ||
                potionAttrib instanceof PotionOfFrost ||
                potionAttrib instanceof PotionOfLevitation ||
                potionAttrib instanceof PotionOfPurity) {
            potionAttrib.cast(user, dst);
            detach(user.belongings.backpack);
        } else {
            super.cast(user, dst);
        }

    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(POTIONATTRIB, potionAttrib);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        if (bundle.contains(POTIONATTRIB)) {
            imbuePotion((Potion) bundle.get(POTIONATTRIB));

            //TODO: legacy code for pre-v0.2.3, remove when saves from that version are no longer supported.
        } else if (bundle.contains("name")) {
            name = bundle.getString("name");

            if (name.equals("Health-Yuck"))
                cook(new Sungrass.Seed());
            else if (name.equals("Power-Yuck"))
                cook(new Wandmaker.Rotberry.Seed());
            else if (name.equals("Root-Yuck"))
                cook(new Earthroot.Seed());
            else if (name.equals("Invisi-Yuck"))
                cook(new Blindweed.Seed());
            else if (name.equals("Flame-Yuck"))
                cook(new Firebloom.Seed());
            else if (name.equals("Ice-Yuck"))
                cook(new Icecap.Seed());
            else if (name.equals("Vision-Yuck"))
                cook(new Fadeleaf.Seed());
            else if (name.equals("Toxic-Yuck"))
                cook(new Sorrowmoss.Seed());
            else if (name.equals("Float-Yuck"))
                cook(new Stormvine.Seed());
            else if (name.equals("Pure-Yuck"))
                cook(new Dreamfoil.Seed());
        }

    }

    @Override
    public ItemSprite.Glowing glowing() {
        return potionGlow;
    }

    public static class CookFruit extends Recipe {

        @Override
        //also sorts ingredients if it can
        public boolean testIngredients(ArrayList<Item> ingredients) {
            if (ingredients.size() != 2) {
                return false;
            }
            if (ingredients.get(0) instanceof Yumyuck) {
                if (!(ingredients.get(1) instanceof Seed)) {
                    return false;
                }
            } else if (ingredients.get(0) instanceof Seed) {
                if (ingredients.get(1) instanceof Yumyuck) {
                    Item temp = ingredients.get(0);
                    ingredients.set(0, ingredients.get(1));
                    ingredients.set(1, temp);
                } else {
                    return false;
                }
            } else {
                return false;
            }

            Yumyuck fruit = (Yumyuck) ingredients.get(0);
            Seed seed = (Seed) ingredients.get(1);

            if (fruit.quantity() >= 1 && fruit.potionAttrib == null
                    && seed.quantity() >= 1) {

                if (Dungeon.isChallenged(Challenges.NO_HEALING)
                        && seed instanceof Sungrass.Seed) {
                    return false;
                }

                return true;
            }

            return false;
        }

        @Override
        public int cost(ArrayList<Item> ingredients) {
            return 2;
        }

        @Override
        public Item brew(ArrayList<Item> ingredients) {
            if (!testIngredients(ingredients)) return null;

            ingredients.get(0).quantity(ingredients.get(0).quantity() - 1);
            ingredients.get(1).quantity(ingredients.get(1).quantity() - 1);


            return new Yumyuck().cook((Seed) ingredients.get(1));
        }

        @Override
        public Item sampleOutput(ArrayList<Item> ingredients) {
            if (!testIngredients(ingredients)) return null;

            return new Yumyuck().cook((Seed) ingredients.get(1));
        }

    }


}


