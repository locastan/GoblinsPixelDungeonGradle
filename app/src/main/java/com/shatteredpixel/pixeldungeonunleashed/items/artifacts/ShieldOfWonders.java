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
import com.shatteredpixel.pixeldungeonunleashed.ResultDescriptions;
import com.shatteredpixel.pixeldungeonunleashed.actors.Actor;
import com.shatteredpixel.pixeldungeonunleashed.actors.Char;
import com.shatteredpixel.pixeldungeonunleashed.actors.blobs.Blob;
import com.shatteredpixel.pixeldungeonunleashed.actors.blobs.ConfusionGas;
import com.shatteredpixel.pixeldungeonunleashed.actors.blobs.Fire;
import com.shatteredpixel.pixeldungeonunleashed.actors.blobs.ParalyticGas;
import com.shatteredpixel.pixeldungeonunleashed.actors.blobs.Regrowth;
import com.shatteredpixel.pixeldungeonunleashed.actors.blobs.ToxicGas;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Amok;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Barkskin;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Bleeding;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Blindness;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Buff;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Burning;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Cripple;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Drowsy;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Euphoria;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Frost;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Invisibility;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Paralysis;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Poison;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Slow;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Terror;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Vertigo;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Weakness;
import com.shatteredpixel.pixeldungeonunleashed.actors.hero.Hero;
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.Mimic;
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.Mob;
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.npcs.Sheep;
import com.shatteredpixel.pixeldungeonunleashed.effects.CellEmitter;
import com.shatteredpixel.pixeldungeonunleashed.effects.Flare;
import com.shatteredpixel.pixeldungeonunleashed.effects.MagicMissile;
import com.shatteredpixel.pixeldungeonunleashed.effects.Speck;
import com.shatteredpixel.pixeldungeonunleashed.effects.SpellSprite;
import com.shatteredpixel.pixeldungeonunleashed.effects.particles.ShadowParticle;
import com.shatteredpixel.pixeldungeonunleashed.items.Bomb;
import com.shatteredpixel.pixeldungeonunleashed.items.EquipableItem;
import com.shatteredpixel.pixeldungeonunleashed.items.Generator;
import com.shatteredpixel.pixeldungeonunleashed.items.Heap;
import com.shatteredpixel.pixeldungeonunleashed.items.Item;
import com.shatteredpixel.pixeldungeonunleashed.items.KindOfWeapon;
import com.shatteredpixel.pixeldungeonunleashed.items.KindofMisc;
import com.shatteredpixel.pixeldungeonunleashed.items.armor.Armor;
import com.shatteredpixel.pixeldungeonunleashed.items.scrolls.ScrollOfRecharging;
import com.shatteredpixel.pixeldungeonunleashed.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.pixeldungeonunleashed.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.pixeldungeonunleashed.levels.Level;
import com.shatteredpixel.pixeldungeonunleashed.levels.Terrain;
import com.shatteredpixel.pixeldungeonunleashed.levels.traps.AlarmTrap;
import com.shatteredpixel.pixeldungeonunleashed.levels.traps.GrippingTrap;
import com.shatteredpixel.pixeldungeonunleashed.levels.traps.LightningTrap;
import com.shatteredpixel.pixeldungeonunleashed.levels.traps.ParalyticTrap;
import com.shatteredpixel.pixeldungeonunleashed.levels.traps.PoisonTrap;
import com.shatteredpixel.pixeldungeonunleashed.levels.traps.SummoningTrap;
import com.shatteredpixel.pixeldungeonunleashed.levels.traps.ToxicTrap;
import com.shatteredpixel.pixeldungeonunleashed.plants.Plant;
import com.shatteredpixel.pixeldungeonunleashed.scenes.GameScene;
import com.shatteredpixel.pixeldungeonunleashed.sprites.ItemSpriteSheet;
import com.shatteredpixel.pixeldungeonunleashed.ui.HealthIndicator;
import com.shatteredpixel.pixeldungeonunleashed.utils.GLog;
import com.shatteredpixel.pixeldungeonunleashed.utils.Utils;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.ArrayList;


public class ShieldOfWonders extends Artifact{

	{
		name = "Shield of Wonder";
		image = ItemSpriteSheet.ARTIFACT_SHIELD;

		level = 0;
		levelCap = 10;

		charge = 0;
		chargeCap = 100;

		defaultAction = "NONE";
	}

    private static final float TIME_TO_EQUIP = 1f;

    @Override
    public String desc() {
        String desc = "This shield is decorated with multicolored gems and looks quite nice and " +
                "innocent. The metal is solid and sturdy. It seems to slightly vibrate to a deep energy, " +
                "who knows what it will do when used?";
        if (isEquipped( Dungeon.hero )) {
            desc += "\n\n";
            desc += "The shield feels reassuringly heavy on your arm. You're sure that you can block " +
                    "some damage in a fight, but not so much about who the chaotic magic might end up hurting.";
            desc += "\n\n";
            desc += "Your current chance to block physical damage is: " + Integer.toString(charge) + "%.";
        }

        return desc;
    }

	public void causeEffect(final Hero user, final Char enemy){
        float VERY_RARE_CHANCE = 0.01f;
        float RARE_CHANCE = 0.09f;
        float UNCOMMON_CHANCE = 0.3f;
        float COMMON_CHANCE = 0.6f;
        if (enemy != null) {
            switch (Random.chances(new float[]{COMMON_CHANCE, UNCOMMON_CHANCE, RARE_CHANCE, VERY_RARE_CHANCE})){
                case 0:
                default:
                    commonEffect(user, enemy.pos);
                    break;
                case 1:
                    uncommonEffect(user, enemy.pos);
                    break;
                case 2:
                    rareEffect(user, enemy.pos);
                    break;
                case 3:
                    veryRareEffect(user, enemy.pos);
                    break;
            }
        }
	}

	private void commonEffect(final Hero user, final int pos){
		switch(Random.Int(5)){

			//anti-entropy
			case 0:
				cursedFX(user, pos, new Callback() {
						public void call() {
							Char target = Actor.findChar(pos);
							switch (Random.Int(2)){
								case 0:
									if (target != null)
										Buff.affect(target, Burning.class).reignite(target);
									Buff.affect(user, Frost.class, Frost.duration(user) * Random.Float(3f, (float)level+3f));
									break;
								case 1:
									Buff.affect(user, Burning.class).reignite(user);
									if (target != null)
										Buff.affect(target, Frost.class, Frost.duration(target) * Random.Float(3f, (float)level+3f));
									break;
							}
						}
					});
				GLog.s("Your shield transferred some heat.");
				break;

			//spawns some regrowth
			case 1:
				cursedFX(user, pos, new Callback() {
					public void call() {
						int c = Dungeon.level.map[pos];
						if (c == Terrain.EMPTY ||
								c == Terrain.EMBERS ||
								c == Terrain.EMPTY_DECO ||
								c == Terrain.GRASS ||
								c == Terrain.HIGH_GRASS) {
							GameScene.add( Blob.seed(pos, 30+level, Regrowth.class));
						}
					}
				});
                GLog.s("Look at all that grass...");
				break;

			//random teleportation
			case 2:
				switch(Random.Int(2)){
					case 0:
                        int count = 10;
                        int tpos;
                        do {
                            tpos = Dungeon.level.randomRespawnCell();
                            if (count-- <= 0) {
                                break;
                            }
                        } while (tpos == -1);

                        if (tpos == -1) {

                            GLog.s("A strong magic aura prevents teleporting.");

                        } else {

                            ScrollOfTeleportation.appear( user, tpos );
                            Dungeon.level.press( tpos, user );
                            Dungeon.observe();

                            GLog.s("Do you like this new place?");

                        }
						break;
					case 1:
						cursedFX(user, pos, new Callback() {
							public void call() {
								Char ch = Actor.findChar( pos );
								if (ch != null) {
									int count = 10;
									int rpos;
									do {
										rpos = Dungeon.level.randomRespawnCell();
										if (count-- <= 0) {
											break;
										}
									} while (rpos == -1);
									if (rpos == -1) {
										GLog.s(ScrollOfTeleportation.TXT_NO_TELEPORT);
									} else {
										ch.pos = rpos;
										ch.sprite.place(ch.pos);
										ch.sprite.visible = Dungeon.visible[rpos];
                                        GLog.s("An enemy has been teleported somwhere else.");
									}
								}
							}
						});
						break;
				}
				break;

			//random gas at location
			case 3:
				cursedFX(user, pos, new Callback() {
					public void call() {
                        if (level > 0) {
                            GLog.s("Your shield emits gas.");
                            switch (Random.Int(3)) {
                                case 0:
                                    GameScene.add(Blob.seed(pos, 80 * level, ConfusionGas.class));
                                    break;
                                case 1:
                                    GameScene.add(Blob.seed(pos, 50 * level, ToxicGas.class));
                                    break;
                                case 2:
                                    GameScene.add(Blob.seed(pos, 20 * level, ParalyticGas.class));
                                    break;
                            }
                        } else {
                            GLog.s("Your shield makes flatulent farting noises.");
                        }
					}
				});
				break;

			case 4:
                cursedFX(user, pos, new Callback() {
                    public void call() {
                        switch (Random.Int(4)) {
                            // rage scroll copy
                            case 0:
                                for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] )) {
                                    mob.beckon( curUser.pos );
                                    if (Level.fieldOfView[mob.pos]) {
                                        Buff.prolong(mob, Amok.class, 5f);
                                    }
                                }

                                for (Heap heap : Dungeon.level.heaps.values()) {
                                    if (heap.type == Heap.Type.MIMIC) {
                                        Mimic m = Mimic.spawnAt( heap.pos, heap.items );
                                        if (m != null) {
                                            m.beckon( user.pos );
                                            heap.destroy();
                                        }
                                    }
                                }

                                GLog.s( "Your shield emits an enraging roar that echoes throughout the dungeon!" );

                                user.sprite.centerEmitter().start( Speck.factory( Speck.SCREAM ), 0.3f, 3 );
                                Sample.INSTANCE.play( Assets.SND_CHALLENGE );
                                break;
                            case 1:
                                // lullaby scroll copy
                                user.sprite.centerEmitter().start( Speck.factory( Speck.NOTE ), 0.3f, 5 );
                                Sample.INSTANCE.play( Assets.SND_LULLABY );

                                for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] )) {
                                    if (Level.fieldOfView[mob.pos]) {
                                        Buff.affect(mob, Drowsy.class);
                                        if (mob.sprite != null) {
                                            mob.sprite.centerEmitter().start(Speck.factory(Speck.NOTE), 0.3f, 5);
                                        }
                                    }
                                }

                                Buff.affect( user, Drowsy.class );

                                GLog.s( "Your shield sings a soothing melody. You feel very sleepy." );
                                break;
                            case 2:
                                // terror scroll copy
                                new Flare( 5, 32 ).color( 0xFF0000, true ).show( user.sprite, 2f );
                                Sample.INSTANCE.play( Assets.SND_READ );
                                Invisibility.dispel();

                                int count = 0;
                                Mob affected = null;
                                for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] )) {
                                    if (Level.fieldOfView[mob.pos]) {
                                        Buff.affect( mob, Terror.class, Terror.DURATION ).object = user.id();

                                        if (mob.buff(Terror.class) != null){
                                            count++;
                                            affected = mob;
                                        }
                                    }
                                }

                                switch (count) {
                                    case 0:
                                        GLog.s( "Your shield emits a brilliant flash of red light" );
                                        break;
                                    case 1:
                                        GLog.s( "Your shield smites " + affected.name + " with terror!" );
                                        break;
                                    default:
                                        GLog.s( "Your shield emits a brilliant flash of red light and the monsters flee!" );
                                }
                                break;
                            case 3:
                                GameScene.flash( 0xFFFFFF );

                                Sample.INSTANCE.play( Assets.SND_BLAST );

                                for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] )) {
                                    if (! mob.TYPE_MINDLESS) {
                                        if (Level.fieldOfView[mob.pos]) {
                                            mob.damage(mob.HT, this);
                                        }
                                    }
                                }

                                user.damage(2 * level + Math.max(user.HT/5, user.HP/2), this);
                                Buff.prolong( user, Paralysis.class, Random.Int( 4, (4+level) ) );
                                Buff.prolong( user, Blindness.class, Random.Int( 6, (6+level) ) );
                                Dungeon.observe();
                                GLog.s( "Your shield emits a psionic blast!" );

                                user.spendAndNext( 1f );

                                if (!user.isAlive()) {
                                    Dungeon.fail( Utils.format(ResultDescriptions.ITEM, name ));
                                    GLog.n("Your shield tears your mind apart...");
                                    Badges.validateDeathBySoW();
                                }
                                break;
                        }
                    }
                });
                break;
		}

	}

	private void uncommonEffect(final Hero user, final int pos){
		switch(Random.Int(5)){

			//Random plant
			case 0:
				cursedFX(user, pos, new Callback() {
					public void call() {
						//place the plants around an enemy so they might walk into it.
						for (int n : Level.NEIGHBOURS4) {
							int cell = pos + n;
							if (Level.passable[cell] && Actor.findChar(cell) == null) {
								Dungeon.level.plant((Plant.Seed) Generator.random(Generator.Category.SEED), cell);
							}
						}
                        GLog.s( "Various interesting plants sprout around your enemy!" );
					}
				});
				break;

			//Health transfer
			case 1:
				final Char target = Actor.findChar( pos );
				if (target != null) {
					cursedFX(user, pos, new Callback() {
						public void call() {
							int damage = user.lvl * 2 * level;
							switch (Random.Int(2)) {
								case 0:
									user.HP = Math.min(user.HT, user.HP + damage);
									user.sprite.emitter().burst(Speck.factory(Speck.HEALING), 3);
									target.damage(damage, this);
									target.sprite.emitter().start(ShadowParticle.UP, 0.05f, 10);
                                    GLog.s("Your shield transferred some health to you.");
									break;
								case 1:
									user.damage( damage, this );
									user.sprite.emitter().start(ShadowParticle.UP, 0.05f, 10);
									target.HP = Math.min(target.HT, target.HP + damage);
									target.sprite.emitter().burst(Speck.factory(Speck.HEALING), 3);
									Sample.INSTANCE.play(Assets.SND_CURSED);
									if (!user.isAlive()) {
										Dungeon.fail(Utils.format(ResultDescriptions.ITEM, name));
										GLog.n("You were killed by your Shield of Wonder.");
                                        Badges.validateDeathBySoW();
									} else {
                                        GLog.s("Your shield transferred some of your health away.");
                                    }
									break;
							}
						}
					});
				} else {
					GLog.s("Your shield smells like flowers...amazing!");
				}
				break;

			//Traps and bombs for hero or enemy
			case 2:
                final int targetpos = (Random.Int(2) == 0 ? user.pos : pos);
                GLog.s("Your shield summoned a trap.");
                switch(Random.Int(7)) {
                    case 0:
                        cursedFX(user, pos, new Callback() {
                            public void call() {
                                new Bomb().explode(targetpos);
                            }
                        });
                        break;
                    case 1:
                        new LightningTrap().set( targetpos ).activate();
                        break;
                    case 2:
                        new AlarmTrap().set( targetpos ).activate();
                        break;
                    case 3:
                        new PoisonTrap().set( targetpos ).activate();
                        break;
                    case 4:
                        new GrippingTrap().set( targetpos ).activate();
                        break;
                    case 5:
                        new ToxicTrap().set( targetpos ).activate();
                        break;
                    case 6:
                        new ParalyticTrap().set( targetpos ).activate();
                        break;
                }
                break;
			//shock and recharge
			case 3:
				new LightningTrap().set( user.pos ).activate();
				Buff.prolong(user, ScrollOfRecharging.Recharging.class, 20f);
				ScrollOfRecharging.charge(user);
				SpellSprite.show(user, SpellSprite.CHARGE);
                GLog.s("Was that too much juice?");
				break;
            // shield level up!
            case 4:
                if (level < levelCap) {
                    level++;
                    charge = (int) Math.round((0.1f + (level/2*0.06f)+(1/(level+1)*0.04f))*100);
                    GLog.s("Your shield grows in strength and danger!");
                } else {
                    GLog.s("Your shield instills knowledge of a most useless factoid...");
                }
                break;
		}

	}

	private void rareEffect(final Hero user, final int pos){
		switch(Random.Int(4)){

			//sheep transformation
			case 0:
				cursedFX(user, pos, new Callback() {
					public void call() {
						Char ch = Actor.findChar( pos );
						if (ch != null && ch != user && !Dungeon.bossLevel() ){
							Sheep sheep = new Sheep();
							sheep.lifespan = 10;
							sheep.pos = ch.pos;
							ch.sprite.killAndErase();
							Actor.remove(ch);
							Dungeon.level.mobs.remove(ch);
							HealthIndicator.instance.target(null);
							GameScene.add(sheep);
							CellEmitter.get(sheep.pos).burst(Speck.factory(Speck.WOOL), 4);
						} else {
							GLog.s("Your shield talks...well mainly it says \"BAAH\" a lot...");
						}
					}
				});
				break;

			//curses!
			case 1:
				KindOfWeapon weapon = user.belongings.weapon;
				Armor armor = user.belongings.armor;
				KindofMisc misc1 = user.belongings.misc1;
				KindofMisc misc2 = user.belongings.misc2;
				if (weapon != null) weapon.cursed = weapon.cursedKnown = true;
				if (armor != null)  armor.cursed = armor.cursedKnown = true;
				if (misc1 != null)  misc1.cursed = misc1.cursedKnown = true;
				if (misc2 != null)  misc2.cursed = misc2.cursedKnown = true;
				EquipableItem.equipCursed(user);
				GLog.s("Your worn equipment becomes cursed!");
				break;

			//Frozen Carpaccio copy
			case 2:
                switch (Random.Int( 4 )) {
                    case 0:
                        GLog.s( "You see your hands turn invisible!" );
                        Buff.affect( user, Invisibility.class, Invisibility.DURATION );
                        break;
                    case 1:
                        GLog.s( "You feel your skin harden!" );
                        Buff.affect( user, Barkskin.class ).level( user.HT / 4 );
                        break;
                    case 2:
                        GLog.s( "Your shield emits refreshing energy!" );
                        Buff.detach( user, Poison.class );
                        Buff.detach( user, Cripple.class );
                        Buff.detach( user, Weakness.class );
                        Buff.detach( user, Bleeding.class );
                        Buff.detach( user, Drowsy.class );
                        Buff.detach( user, Slow.class );
                        Buff.detach( user, Vertigo.class);
                        Buff.detach( user, Euphoria.class);
                        break;
                    case 3:
                        GLog.s( "Your shield heals you!" );
                        if (user.HP < user.HT) {
                            user.HP = Math.min( user.HT * (int)(0.1f * (float)level), user.HT );
                            user.sprite.emitter().burst( Speck.factory( Speck.HEALING ), 1 );
                        }
                        break;
                }

			//summon monsters
			case 3:
                GLog.s("Your shield found you some friends!");
				new SummoningTrap().set( user.pos ).activate();
				break;
		}
	}

	private void veryRareEffect(final Hero user, final int pos){
		switch(Random.Int(3)){

			//great forest fire!
			case 0:
				for (int i = 0; i < Level.LENGTH; i++){
					int c = Dungeon.level.map[i];
					if (c == Terrain.EMPTY ||
							c == Terrain.EMBERS ||
							c == Terrain.EMPTY_DECO ||
							c == Terrain.GRASS ||
							c == Terrain.HIGH_GRASS) {
						GameScene.add( Blob.seed(i, 15, Regrowth.class));
					}
				}
				do {
					GameScene.add(Blob.seed(Dungeon.level.randomDestination(), 10, Fire.class));
				} while (Random.Int(6) != 0);
				new Flare(8, 32).color(0xFFFF66, true).show(user.sprite, 2f);
				Sample.INSTANCE.play(Assets.SND_TELEPORT);
				GLog.s("Grass explodes around you!");
				GLog.s("You smell burning...");
				break;

			//superpowered mimic
			case 1:
				cursedFX(user, pos, new Callback() {
					public void call() {
						Mimic mimic = Mimic.spawnAt(pos, new ArrayList<Item>());
						mimic.adjustStats(Dungeon.depth + level);
						mimic.HP = mimic.HT;
						Item reward;
						do {
							reward = Generator.random(Random.oneOf(Generator.Category.WEAPON, Generator.Category.ARMOR,
									Generator.Category.RING, Generator.Category.WAND));
						} while (reward.level < 2 && !(reward instanceof MissileWeapon));
						Sample.INSTANCE.play(Assets.SND_MIMIC, 1, 1, 0.5f);
						mimic.items.clear();
						mimic.items.add(reward);
					}
				});
                GLog.s("Come get some treasure!");
				break;

			//random transmogrification
			case 2:
				Item result;
				do {
					result = Generator.random(Random.oneOf(Generator.Category.WEAPON, Generator.Category.ARMOR,
							Generator.Category.RING, Generator.Category.ARTIFACT));
				} while (result.level < 0 && !(result instanceof MissileWeapon) && (Generator.spawnedArtifacts.contains(result.getClass().getSimpleName())));
				if (result.isUpgradable()) result.upgrade(Random.Int(level)+1);
				result.cursed = result.cursedKnown = true;
                GLog.s("Your shield spat out a random item!");
                Dungeon.level.drop(result, user.pos).sprite.drop();
				break;
		}
	}

	private static void cursedFX(final Hero user, final int pos, final Callback callback){
		MagicMissile.rainbow(user.sprite.parent, user.pos, pos, callback);
		Sample.INSTANCE.play( Assets.SND_ZAP );
	}

    @Override
    public boolean doEquip( Hero hero ) {

        if (hero.belongings.misc1 != null && hero.belongings.misc2 != null) {

            GLog.s( "You can only wear 2 misc items at a time" );
            return false;

        } else {

            if (hero.belongings.misc1 == null) {
                hero.belongings.misc1 = this;
            } else {
                hero.belongings.misc2 = this;
            }

            detach( hero.belongings.backpack );

            activate( hero );

            cursedKnown = true;
            if (cursed) {
                equipCursed( hero );
                GLog.s( "The " + this + " tightens around your arm painfully" );
            }

            hero.spendAndNext( TIME_TO_EQUIP );
            return true;

        }

    }

    public void activate( Char ch ) {
        charge = (int) (Math.round((0.1f + (level/2*0.06f)+(1/(level+1)*0.04))*100));
    }

    @Override
    public boolean doUnequip( Hero hero, boolean collect, boolean single ) {
        if (cursed) {
            GLog.s("You can't remove cursed %s!", name());
            return false;
        }

        if (single) {
            hero.spendAndNext( time2equip( hero ) );
        } else {
            hero.spend( time2equip( hero ) );
        }

        if (collect && !collect( hero.belongings.backpack )) {
            onDetach();
            Dungeon.quickslot.clearItem(this);
            updateQuickslot();
            Dungeon.level.drop(this, hero.pos);
        }

        if (hero.belongings.misc1 == this) {
            hero.belongings.misc1 = null;
        } else {
            hero.belongings.misc2 = null;
        }
        return true;
    }

    @Override
    public boolean isEquipped( Hero hero ) {
        return hero.belongings.misc1 == this || hero.belongings.misc2 == this;
    }


}
