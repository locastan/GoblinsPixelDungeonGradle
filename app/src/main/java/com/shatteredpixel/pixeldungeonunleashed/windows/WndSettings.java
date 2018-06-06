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
package com.shatteredpixel.pixeldungeonunleashed.windows;

import com.shatteredpixel.pixeldungeonunleashed.Badges;
import com.shatteredpixel.pixeldungeonunleashed.GoblinsPixelDungeon;
import com.shatteredpixel.pixeldungeonunleashed.ui.Toolbar;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.shatteredpixel.pixeldungeonunleashed.Assets;
import com.shatteredpixel.pixeldungeonunleashed.scenes.PixelScene;
import com.shatteredpixel.pixeldungeonunleashed.ui.CheckBox;
import com.shatteredpixel.pixeldungeonunleashed.ui.RedButton;
import com.shatteredpixel.pixeldungeonunleashed.ui.Window;

public class WndSettings extends Window {
	
	private static final String TXT_ZOOM_IN			= "+";
	private static final String TXT_ZOOM_OUT		= "-";
	private static final String TXT_ZOOM_DEFAULT	= "Zoom";

	private static final String TXT_EASIER          = "-";
	private static final String TXT_HARDER          = "+";

	private static final String TXT_IMMERSIVE		= "Immersive mode";
	
	private static final String TXT_MUSIC	= "Music";
	
	private static final String TXT_SOUND	= "Sound FX";
	
	private static final String TXT_BRIGHTNESS	= "Brightness";

	private static final String TXT_QUICKSLOT = "Second QuickSlot";
	
	private static final String TXT_SWITCH_PORT	= "Switch to portrait";
	private static final String TXT_SWITCH_LAND	= "Switch to landscape";
	
	private static final int WIDTH		= 112;
	private static final int BTN_HEIGHT	= 20;
	private static final int GAP 		= 2;

	private static boolean endlessUnlocked = false;
	private static boolean testUnlocked = false;

	private RedButton btnZoomOut;
	private RedButton btnZoomIn;
    private RedButton btnResetZ;

	private static int difficultLevel   = GoblinsPixelDungeon.getDifficulty();
	private RedButton btnEasier;
	private RedButton btnHarder;
	private RedButton btnDifficulty;

	public WndSettings( boolean inGame ) {
		super();
		Badges.loadGlobal();

		if (Badges.isUnlocked(Badges.Badge.BOSS_SLAIN_4) || Badges.isUnlocked(Badges.Badge.BETA_TESTER)) {
			endlessUnlocked = true;
		}
		if (Badges.isUnlocked(Badges.Badge.HAPPY_END) || Badges.isUnlocked(Badges.Badge.BETA_TESTER)) {
			testUnlocked = true;
		}
		CheckBox btnImmersive = null;

		if (inGame) {
			int w = BTN_HEIGHT;
			
			// Zoom out
			btnZoomOut = new RedButton( TXT_ZOOM_OUT ) {
				@Override
				protected void onClick() {
					zoom( Camera.main.zoom - 0.5f );
                    btnResetZ.text(TXT_ZOOM_DEFAULT + " (" + Camera.main.zoom + ")");
				}
			};
			add( btnZoomOut.setRect( 0, 0, w, BTN_HEIGHT) );
			
			// Zoom in
			btnZoomIn = new RedButton( TXT_ZOOM_IN ) {
				@Override
				protected void onClick() {
					zoom( Camera.main.zoom + 0.5f );
                    btnResetZ.text(TXT_ZOOM_DEFAULT + " (" + Camera.main.zoom + ")");
				}
			};
			add( btnZoomIn.setRect( WIDTH - w, 0, w, BTN_HEIGHT) );
			
			// Default zoom
			btnResetZ = new RedButton(TXT_ZOOM_DEFAULT + " (" + Camera.main.zoom + ")") {
				@Override
				protected void onClick() {
					zoom(PixelScene.defaultZoom);
					this.text(TXT_ZOOM_DEFAULT + " (" + Camera.main.zoom + ")");
				}
			};
            add( btnResetZ.setRect(btnZoomOut.right(), 0, WIDTH - btnZoomIn.width() - btnZoomOut.width(), BTN_HEIGHT));
			
		} else {
			int w = BTN_HEIGHT;

			// Difficulty Levels
			btnEasier = new RedButton( TXT_EASIER ) {
				@Override
				protected void onClick() {
					if ((testUnlocked && (difficultLevel <= 9)) || (!testUnlocked && (difficultLevel <= 10))) {
						if (endlessUnlocked) {
							difficultLevel = 15;
						} else {
							difficultLevel = 14;
						}
					} else {
						difficultLevel -= 1;
					}

					GoblinsPixelDungeon.setDifficulty(difficultLevel);
					btnDifficulty.text( difficultyText() );
				}
			};
			add( btnEasier.setRect( 0, 0, w, BTN_HEIGHT) );

			btnHarder = new RedButton( TXT_HARDER ) {
				@Override
				protected void onClick() {
					if ((endlessUnlocked && (difficultLevel >= 15)) || (!endlessUnlocked && (difficultLevel >= 14))) {
						if (testUnlocked) {
							difficultLevel = 9;
						} else {
							difficultLevel = 10;
						}
					} else {
						difficultLevel += 1;
					}

					GoblinsPixelDungeon.setDifficulty(difficultLevel);
					btnDifficulty.text( difficultyText() );
				}
			};
			add (btnHarder.setRect( WIDTH - w, 0, w, BTN_HEIGHT) );

			btnDifficulty = new RedButton( difficultyText() ) {
				@Override
				protected void onClick() {
				}
			};
			add(btnDifficulty.setRect(btnEasier.right(), 0, WIDTH - btnEasier.width() - btnHarder.width(), BTN_HEIGHT) );

			btnImmersive = new CheckBox( TXT_IMMERSIVE ) {
				@Override
				protected void onClick() {
					super.onClick();
					GoblinsPixelDungeon.immerse( checked() );
				}
			};
			btnImmersive.setRect( 0, btnEasier.bottom() + GAP, WIDTH, BTN_HEIGHT );
			btnImmersive.checked( GoblinsPixelDungeon.immersed() );
			btnImmersive.enable( android.os.Build.VERSION.SDK_INT >= 19 );
			add( btnImmersive );

		}
		
		CheckBox btnMusic = new CheckBox( TXT_MUSIC ) {
			@Override
			protected void onClick() {
				super.onClick();
				GoblinsPixelDungeon.music(checked());
			}
		};
		btnMusic.setRect(0, (btnImmersive != null ? btnImmersive.bottom() : BTN_HEIGHT) + GAP, WIDTH, BTN_HEIGHT );
		btnMusic.checked(GoblinsPixelDungeon.music() );
			add(btnMusic );
		
		CheckBox btnSound = new CheckBox( TXT_SOUND ) {
			@Override
			protected void onClick() {
				super.onClick();
				GoblinsPixelDungeon.soundFx(checked());
				Sample.INSTANCE.play( Assets.SND_CLICK );
			}
		};
		btnSound.setRect(0, btnMusic.bottom() + GAP, WIDTH, BTN_HEIGHT );
		btnSound.checked(GoblinsPixelDungeon.soundFx() );
			add(btnSound);
		
		if (!inGame) {
			
			RedButton btnOrientation = new RedButton( orientationText() ) {
				@Override
				protected void onClick() {
					GoblinsPixelDungeon.landscape(!GoblinsPixelDungeon.landscape());
				}
			};
			btnOrientation.setRect( 0, btnSound.bottom() + GAP, WIDTH, BTN_HEIGHT );
			add( btnOrientation );
			
			resize(WIDTH, (int) btnOrientation.bottom());
			
		} else {
		
			CheckBox btnBrightness = new CheckBox( TXT_BRIGHTNESS ) {
				@Override
				protected void onClick() {
					super.onClick();
					GoblinsPixelDungeon.brightness(checked());
				}
			};
			btnBrightness.setRect(0, btnSound.bottom() + GAP, WIDTH, BTN_HEIGHT);
			btnBrightness.checked(GoblinsPixelDungeon.brightness());
			add(btnBrightness);

			CheckBox btnQuickSlot = new CheckBox( TXT_QUICKSLOT ) {
				@Override
				protected void onClick() {
					super.onClick();
					GoblinsPixelDungeon.quickSlots(checked() ? 2 : 1);
					Toolbar.QuickSlots = checked() ? 2 : 1;
				}
			};
			btnQuickSlot.setRect( 0, btnBrightness.bottom() + GAP, WIDTH, BTN_HEIGHT );
			btnQuickSlot.checked(GoblinsPixelDungeon.quickSlots() == 2);
			add(btnQuickSlot);
			
			resize(WIDTH, (int) btnQuickSlot.bottom());
			
		}
	}
	
	private void zoom( float value ) {

		Camera.main.zoom( value );
		GoblinsPixelDungeon.zoom((int) (value - PixelScene.defaultZoom));

		updateEnabled();
	}
	
	private void updateEnabled() {
		float zoom = Camera.main.zoom;
		btnZoomIn.enable( zoom < PixelScene.maxZoom );
		btnZoomOut.enable( zoom > PixelScene.minZoom );
	}
	
	private String orientationText() {
		return GoblinsPixelDungeon.landscape() ? TXT_SWITCH_PORT : TXT_SWITCH_LAND;
	}

	private String difficultyText() {
		difficultLevel = GoblinsPixelDungeon.getDifficulty();
		switch (difficultLevel) {
			case 15:
				return "Endless";
			case 14:
				return "Nightmare";
			case 13:
				return "Hard";
			case 12:
				return "Normal";
			case 11:
				return "Easy";
			case 10:
				return "Tutorial";
			case 9:
				return "Test";
			default:
				difficultLevel = 12;
			    return "Normal";
		}
	}
}
