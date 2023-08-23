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
import com.shatteredpixel.pixeldungeonunleashed.Dungeon;
import com.shatteredpixel.pixeldungeonunleashed.actors.hero.Hero;
import com.shatteredpixel.pixeldungeonunleashed.items.Item;
import com.shatteredpixel.pixeldungeonunleashed.items.keys.GoldenKey;
import com.shatteredpixel.pixeldungeonunleashed.items.keys.IronKey;
import com.shatteredpixel.pixeldungeonunleashed.scenes.GameScene;
import com.shatteredpixel.pixeldungeonunleashed.sprites.ItemSpriteSheet;
import com.shatteredpixel.pixeldungeonunleashed.utils.GLog;
import com.shatteredpixel.pixeldungeonunleashed.windows.WndBag;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class HummingTool extends Artifact {

	{
		name = "Hummingtool";
		image = ItemSpriteSheet.ARTIFACT_HUMMING;

		level = 0;
		levelCap = 3;

		charge = 0;

		defaultAction = AC_FEED;
	}

	public static final String AC_FEED = "FEED";
    private Integer usecount = 0;

	protected String inventoryTitle = "Select key:";
	protected WndBag.Mode mode = WndBag.Mode.KEY;


	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		if (!cursed) {
            actions.add(AC_FEED);
        }
        if (cursed) {
            actions.remove(AC_DROP);
            actions.remove(AC_THROW);
        }
        actions.remove(AC_EQUIP);
		return actions;
	}

	@Override
	public void execute( Hero hero, String action ) {
		super.execute(hero, action);
		if (action.equals(AC_FEED)) {
			GameScene.selectItem(itemSelector, mode, inventoryTitle);
		}
	}

    @Override
    public boolean doEquip( Hero hero ) {
        GLog.w("The Hummingtool refuses to be worn. It comes when needed.");
        return false;
    }

    @Override
    public boolean doPickUp( Hero hero ) {
        if (cursed) {
            GLog.w("The cursed Hummingtool digs itself into your backpack.");
        }
        return super.doPickUp(hero);
    }

    @Override
    public int visiblyUpgraded() {
        return level;
    }


	@Override
	public String desc() {
		String desc = "";
		if (level == 0)
			desc += "A cute little automaton bird with various odd shaped tools sticking out of it." +
				  "It seems a bit weak and only moves slowly. Maybe I can feed it something?";
		else if (level == 1)
			desc += "The little thing seems more agile after eating that golden key. " +
				"It zips around fast and now it takes less charge to pick a lock.";
		else if (level == 2)
			desc += "The Hummingtool is getting quite fast. Picking locks with it is requiring " +
					"less and less effort now. I wonder how fast it can go when fully improved?";
		else
			desc += "The Thing is a blur of tools and zipping through locks without much effort." +
					"Sometimes it even unlocks complicated contraptions without any help at all!";
        if (cursed)
            desc += "\r\n\r\n This Hummingtool is cursed and refuses to leave your backpack.";


		return desc;
	}

	@Override
	public Item upgrade() {
		if (level < levelCap) {
			return super.upgrade();
		} else {
			return this;
		}
	}

	public Integer charge() {
		return charge;
	}

    public void useCharge() {
        // Logic to have the tool use less charges per gear click as it levels.
        if (level == 0) {
            charge -= 1;
        } else if (level == 1) {
            usecount += 1;
            if (usecount >= 2){
                charge -= 1;
                usecount = 0;
            }
        } else if (level == 2) {
            usecount += 1;
            if (usecount >= 3){
                charge -= 1;
                usecount = 0;
            }
        } else {
            usecount += 1;
            if (usecount >= 4){
                charge -= 1;
                usecount = 0;
            }
        }
    }

    public void usedAutosolve() {
        charge -= 20;
    }


	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle(bundle);
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);
	}


	protected WndBag.Listener itemSelector = new WndBag.Listener() {
		@Override
		public void onSelect( Item item ) {
			if (item != null && item instanceof IronKey || item instanceof GoldenKey) {
					Hero hero = Dungeon.hero;
					hero.sprite.operate( hero.pos );
					Sample.INSTANCE.play( Assets.SND_PLANT );
					hero.busy();
					hero.spend( 2f );
					if (item instanceof GoldenKey){
                        charge += 25;
						upgrade();
						if (level >= 1 && level <= 3) {
							GLog.p("The Hummingtool it is now level " +level+ "!");
						}

					} else {
                        charge += 10;
						GLog.i("Your " + name + " absorbed the " +item.name()+ ", it seems to enjoy it.");
					}
					item.detach(hero.belongings.backpack);

			}
		}
	};

}