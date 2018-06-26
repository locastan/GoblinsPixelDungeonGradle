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
package com.shatteredpixel.pixeldungeonunleashed.items.artifacts;

import com.shatteredpixel.pixeldungeonunleashed.Assets;
import com.shatteredpixel.pixeldungeonunleashed.Badges;
import com.shatteredpixel.pixeldungeonunleashed.Dungeon;
import com.shatteredpixel.pixeldungeonunleashed.Statistics;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Buff;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Hunger;
import com.shatteredpixel.pixeldungeonunleashed.actors.hero.Hero;
import com.shatteredpixel.pixeldungeonunleashed.effects.Speck;
import com.shatteredpixel.pixeldungeonunleashed.effects.SpellSprite;
import com.shatteredpixel.pixeldungeonunleashed.items.Item;
import com.shatteredpixel.pixeldungeonunleashed.items.food.Yumyuck;
import com.shatteredpixel.pixeldungeonunleashed.items.food.Food;
import com.shatteredpixel.pixeldungeonunleashed.items.scrolls.ScrollOfRecharging;
import com.shatteredpixel.pixeldungeonunleashed.scenes.GameScene;
import com.shatteredpixel.pixeldungeonunleashed.sprites.ItemSpriteSheet;
import com.shatteredpixel.pixeldungeonunleashed.utils.GLog;
import com.shatteredpixel.pixeldungeonunleashed.windows.WndBag;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

public class HornOfPlenty extends Artifact {


	{
		name = "Anypot";
		image = ItemSpriteSheet.ARTIFACT_HORN1;

		level = 0;
		levelCap = 30;

		charge = 0;
		partialCharge = 0;
		chargeCap = 10;

		defaultAction = AC_EAT;
	}

	private static final float TIME_TO_EAT	= 3f;

    private float energy = 45f;

	public static final String AC_EAT = "EAT";
	public static final String AC_STORE = "STORE";

	protected String inventoryTitle = "Select food:";
	protected WndBag.Mode mode = WndBag.Mode.FOOD;

	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		if (isEquipped( hero ) && charge > 0)
			actions.add(AC_EAT);
		if (isEquipped( hero ) && level < 30 && !cursed)
			actions.add(AC_STORE);
		return actions;
	}

	@Override
	public void execute( Hero hero, String action ) {
		super.execute(hero, action);

		if (action.equals(AC_EAT)){

			if (!isEquipped(hero)) GLog.i("You need to equip your pot to do that.");
			else if (charge == 0)  GLog.i("Your pot has no food in it to eat!");
			else {
				(hero.buff(Hunger.class)).satisfy(energy * charge);

				//if you get at least 100 food energy from the horn
				if (charge >= 3) {
					switch (hero.heroClass) {
						case COMPLAINS:
							if (hero.HP < hero.HT) {
								hero.HP = Math.min(hero.HP + 7, hero.HT);
								hero.sprite.emitter().burst(Speck.factory(Speck.HEALING), 1);
							}
							break;
						case CHIEF:
							//1 charge
							Buff.affect( hero, ScrollOfRecharging.Recharging.class, 4f );
							ScrollOfRecharging.charge(hero);
                            if (hero.HP < hero.HT) {
                                hero.HP = Math.min(hero.HP + 2, hero.HT);
                                hero.sprite.emitter().burst(Speck.factory(Speck.HEALING), 1);
                            }
							break;
						case FUMBLES:
						case THACO:
                            if (hero.HP < hero.HT) {
                                hero.HP = Math.min(hero.HP + 2, hero.HT);
                                hero.sprite.emitter().burst(Speck.factory(Speck.HEALING), 1);
                            }
							break;
					}

					Statistics.foodEaten++;
				}
				charge = 0;

				hero.sprite.operate(hero.pos);
				hero.busy();
				SpellSprite.show(hero, SpellSprite.FOOD);
				Sample.INSTANCE.play(Assets.SND_EAT);
				GLog.i("You eat from the pot.");

				hero.spend(TIME_TO_EAT);

				Badges.validateFoodEaten();

				image = ItemSpriteSheet.ARTIFACT_HORN1;

				updateQuickslot();
			}

		} else if (action.equals(AC_STORE)){

			GameScene.selectItem(itemSelector, mode, inventoryTitle);
		}
	}

	@Override
	protected ArtifactBuff passiveBuff() {
		return new hornRecharge();
	}

	@Override
	public String desc() {
		String desc = "A sophisticated dwarven adventuring tool. Similar to the famous Anymug, this pot creates delicious food over a period of time.\n\n";

		if (charge == 0)
			desc += "The pot is completely empty.";
		else if (charge < 3)
			desc += "The pot is almost empty, a few ounces of vegetable stew swirl around.";
		else if (charge < 7)
			desc += "The pot is partially filled, you can see several pieces of meat & vegetables in the stew.";
		else if (charge < 10)
			desc += "The pot is getting quite full, bigger pieces of vegetables and a larger bit of meat is visible.";
		else
			desc += "The horn is overflowing! Large pieces of vegetables and a bit of meat are poking through the surface of the hearty stew.";

		if ( isEquipped( Dungeon.hero ) ){
			if (!cursed) {
				desc += "\n\nThe pot rests at your side and is surprisingly lightweight, even with food in it.";

				if (level < 15)
					desc += " Perhaps there is a way to increase the pot's power by giving it food energy.";
			} else {
				desc += "\n\nThe cursed pot has bound itself to your side, " +
						"it seems to be eager to take food rather than produce it.";
			}
		}

		return desc;
	}

	public class hornRecharge extends ArtifactBuff{

		@Override
		public boolean act() {
			if (charge < chargeCap && !cursed) {

				//generates 0.25 food value every round, +0.015 value per level
				//to a max of 0.70 food value per round (0.25+0.5, at level 30)
				partialCharge += 0.25f + (0.015f*level);

				//charge is in increments of 36 food value.
				if (partialCharge >= 36) {
					charge++;
					partialCharge -= 36;

					if (charge == chargeCap)
						image = ItemSpriteSheet.ARTIFACT_HORN4;
					else if (charge >= 7)
						image = ItemSpriteSheet.ARTIFACT_HORN3;
					else if (charge >= 3)
						image = ItemSpriteSheet.ARTIFACT_HORN2;
					else
						image = ItemSpriteSheet.ARTIFACT_HORN1;

					if (charge == chargeCap){
						GLog.p("Your anypot is full of food!");
						partialCharge = 0;
					}

					updateQuickslot();
				}
			} else
				partialCharge = 0;

			spend( TICK );

			return true;
		}

	}

	protected static WndBag.Listener itemSelector = new WndBag.Listener() {
		@Override
		public void onSelect( Item item ) {
			if (item != null && item instanceof Food) {
				if (item instanceof Yumyuck && ((Yumyuck) item).potionAttrib == null){
					GLog.w("your pot rejects the unprepared blandfruit.");
				} else {
					Hero hero = Dungeon.hero;
					hero.sprite.operate( hero.pos );
					hero.busy();
					hero.spend( TIME_TO_EAT );

                    for (int i = 0; i < ((Food)item).hornValue; i++) {
					    curItem.upgrade();
                    }
					if (curItem.level >= 30){
						curItem.level = 30;
						GLog.p("your pot has consumed all the food it can!");
					} else
						GLog.p("the pot consumes your food offering and grows in strength!");
					item.detach(hero.belongings.backpack);
				}

			}
		}
	};

}
