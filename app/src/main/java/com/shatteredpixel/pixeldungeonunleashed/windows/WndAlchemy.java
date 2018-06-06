/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2018 Evan Debenham
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

package com.shatteredpixel.pixeldungeonunleashed.windows;

import com.shatteredpixel.pixeldungeonunleashed.Assets;
import com.shatteredpixel.pixeldungeonunleashed.Dungeon;
import com.shatteredpixel.pixeldungeonunleashed.actors.hero.Hero;
import com.shatteredpixel.pixeldungeonunleashed.effects.Speck;
import com.shatteredpixel.pixeldungeonunleashed.items.Item;
import com.shatteredpixel.pixeldungeonunleashed.items.Recipe;
import com.shatteredpixel.pixeldungeonunleashed.items.weapon.missiles.darts.Dart;
import com.shatteredpixel.pixeldungeonunleashed.scenes.GameScene;
import com.shatteredpixel.pixeldungeonunleashed.scenes.PixelScene;
import com.shatteredpixel.pixeldungeonunleashed.sprites.ItemSpriteSheet;
import com.shatteredpixel.pixeldungeonunleashed.ui.Icons;
import com.shatteredpixel.pixeldungeonunleashed.ui.ItemSlot;
import com.shatteredpixel.pixeldungeonunleashed.ui.RedButton;
import com.shatteredpixel.pixeldungeonunleashed.utils.GLog;
import com.watabou.noosa.BitmapTextMultiline;
import com.shatteredpixel.pixeldungeonunleashed.ui.Window;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class WndAlchemy extends Window {
	
	private static WndBlacksmith.ItemButton[] inputs = new WndBlacksmith.ItemButton[3];
	private ItemSlot output;
	
	private Emitter smokeEmitter;
	private Emitter bubbleEmitter;
	
	private RedButton btnCombine;

	private static final String TXT_PROMPT =	"Alchemy for fun and profit.";

	private static final float GAP		= 2;
	private static final int WIDTH		= 116;
	private static final int BTN_SIZE	= 28;
	
	public WndAlchemy(){
		
		int w = WIDTH;
		
		int h = 0;
		
		IconTitle titlebar = new IconTitle();
		titlebar.icon(Icons.get(Icons.ALCHEMY));
		titlebar.label( "Alchemy" );
		titlebar.setRect( 0, 0, w, 0 );
		add( titlebar );
		
		h += titlebar.height() + 2;

		BitmapTextMultiline desc = PixelScene.createMultiline(TXT_PROMPT,6,false);
		desc.maxWidth = WIDTH;
		desc.measure();
		desc.y = titlebar.bottom() + GAP;
		add( desc );
		
		h += desc.height() + 6;

		synchronized (inputs) {
			for (int i = 0; i < inputs.length; i++) {
				inputs[i] = new WndBlacksmith.ItemButton() {
					@Override
					protected void onClick() {
						super.onClick();
						if (item != null) {
							if (!item.collect()) {
								Dungeon.level.drop(item, Dungeon.hero.pos);
							}
							item = null;
							slot.item(new WndBag.Placeholder(ItemSpriteSheet.SMTH));
						}
						GameScene.selectItem(itemSelector, WndBag.Mode.ALCHEMY, "select");
					}
				};
				inputs[i].setRect(10, h, BTN_SIZE, BTN_SIZE);
				add(inputs[i]);
				h += BTN_SIZE + 2;
			}
		}
		
		btnCombine = new RedButton(""){
			Image arrow;
			
			@Override
			protected void createChildren() {
				super.createChildren();
				arrow = Icons.get(Icons.RESUME);
				add(arrow);
			}
			
			@Override
			protected void layout() {
				super.layout();
				arrow.x = PixelScene.align( PixelScene.uiCamera, x+1 + (width - arrow.width) / 2 );
				arrow.y = PixelScene.align( PixelScene.uiCamera, y + (height - arrow.height) / 2 );
			}
			
			@Override
			public void enable(boolean value) {
				super.enable(value);
				if (value){
					arrow.tint(1, 1, 0, 1);
					arrow.alpha(1f);
					bg.alpha(1f);
				} else {
					arrow.color(0, 0, 0);
					arrow.alpha(0.6f);
					bg.alpha(0.6f);
				}
			}
			
			@Override
			protected void onClick() {
				super.onClick();
				combine();
			}
		};
		btnCombine.enable(false);
		btnCombine.setRect((w-30)/2, inputs[1].top()+5, 30, inputs[1].height()-10);
		add(btnCombine);
		
		output = new ItemSlot(){
			@Override
			protected void onClick() {
				super.onClick();
			}
		};
		output.setRect(w - BTN_SIZE - 10, inputs[1].top(), BTN_SIZE, BTN_SIZE);
		
		ColorBlock outputBG = new ColorBlock(output.width(), output.height(), 0x9991938C);
		outputBG.x = output.left();
		outputBG.y = output.top();
		add(outputBG);
		
		add(output);
		output.visible = false;
		
		bubbleEmitter = new Emitter();
		smokeEmitter = new Emitter();
		bubbleEmitter.pos(outputBG.x + (BTN_SIZE-16)/2, outputBG.y + (BTN_SIZE-16)/2, 16, 16);
		smokeEmitter.pos(bubbleEmitter.x, bubbleEmitter.y, bubbleEmitter.width, bubbleEmitter.height);
		bubbleEmitter.autoKill = false;
		smokeEmitter.autoKill = false;
		add(bubbleEmitter);
		add(smokeEmitter);
		
		h += 4;
		
		float btnWidth = (w-14)/2;
		
		RedButton btnRecipes = new RedButton("recipes"){
			@Override
			protected void onClick() {
				super.onClick();
				GameScene.show(new WndMessage("Random Potion:\n\nMix three seeds of any type to create a random potion. The potion is more likely to relate to one of the seeds used.\n\nCooked Moss:\nMix yumyuck-moss with one seed to imbue the moss with that seed's properties.\n\nTipped Darts:\nMix two regular darts with a seed to create two tipped darts!"));
			}
		};
		btnRecipes.setRect(5, h, btnWidth, 18);
		PixelScene.align(btnRecipes);
		add(btnRecipes);
		
		RedButton btnClose = new RedButton("close"){
			@Override
			protected void onClick() {
				super.onClick();
				onBackPressed();
			}
		};
		btnClose.setRect(w - 5 - btnWidth, h, btnWidth, 18);
		PixelScene.align(btnClose);
		add(btnClose);
		
		h += btnClose.height();
		
		resize(w, h);
	}
	
	protected WndBag.Listener itemSelector = new WndBag.Listener() {
		@Override
		public void onSelect( Item item ) {
			synchronized (inputs) {
				if (item != null && inputs[0] != null) {
					for (int i = 0; i < inputs.length; i++) {
						if (inputs[i].item == null) {
							if (item.name().equals("dart")) {
								inputs[i].item(item.detachAll(Dungeon.hero.belongings.backpack));
							} else {
								inputs[i].item(item.detach(Dungeon.hero.belongings.backpack));
							}
							break;
						}
					}
					updateState();
				}
			}
		}
	};
	
	private<T extends Item> ArrayList<T> filterInput(Class<? extends T> itemClass){
		ArrayList<T> filtered = new ArrayList<>();
		for (int i = 0; i < inputs.length; i++){
			Item item = inputs[i].item;
			if (item != null && itemClass.isInstance(item)){
				filtered.add((T)item);
			}
		}
		return filtered;
	}
	
	private void updateState(){
		
		ArrayList<Item> ingredients = filterInput(Item.class);
		Recipe recipe = Recipe.findRecipe(ingredients);
		
		if (recipe != null){
			output.item(recipe.sampleOutput(ingredients));
			output.visible = true;
			btnCombine.enable(true);
			
		} else {
			btnCombine.enable(false);
			output.visible = false;
		}
		
	}

	private void combine(){
		
		ArrayList<Item> ingredients = filterInput(Item.class);
		Recipe recipe = Recipe.findRecipe(ingredients);
		
		Item result = null;
		
		if (recipe != null){
			result = recipe.brew(ingredients);
		}
		
		if (result != null){
			bubbleEmitter.start(Speck.factory( Speck.BUBBLE ), 0.2f, 10 );
			smokeEmitter.burst(Speck.factory( Speck.WOOL ), 10 );
			Sample.INSTANCE.play( Assets.SND_PUFF );
			
			output.item(result);
			if (!result.collect()){
				Dungeon.level.drop(result, Dungeon.hero.pos);
			}

			synchronized (inputs) {
				for (int i = 0; i < inputs.length; i++) {
					if (inputs[i] != null && inputs[i].item != null) {
						if (inputs[i].item.quantity() <= 0) {
							inputs[i].slot.item(new WndBag.Placeholder(ItemSpriteSheet.SMTH));
							inputs[i].item = null;
						} else {
							inputs[i].slot.item(inputs[i].item);
						}
					}
				}
			}
			
			btnCombine.enable(false);
		}
		
	}
	
	@Override
	public void destroy() {
		synchronized ( inputs ) {
			for (int i = 0; i < inputs.length; i++) {
				if (inputs[i] != null && inputs[i].item != null) {
					if (!inputs[i].item.collect()) {
						Dungeon.level.drop(inputs[i].item, Dungeon.hero.pos);
					}
				}
				inputs[i] = null;
			}
		}
		super.destroy();
	}

	private static final String ALCHEMY_INPUTS = "alchemy_inputs";

	public static void storeInBundle( Bundle b ){
		synchronized ( inputs ){
			ArrayList<Item> items = new ArrayList<>();
			for (WndBlacksmith.ItemButton i : inputs){
				if (i != null && i.item != null){
					items.add(i.item);
				}
			}
			if (!items.isEmpty()){
				b.put( ALCHEMY_INPUTS, items );
			}
		}
	}

	public static void restoreFromBundle( Bundle b, Hero h ){

		if (b.contains(ALCHEMY_INPUTS)){
			for (Bundlable item : b.getCollection(ALCHEMY_INPUTS)){

				//try to add normally, force-add otherwise.
				if (!((Item)item).collect(h.belongings.backpack)){
					h.belongings.backpack.items.add((Item)item);
				}
			}
		}

	}
}
