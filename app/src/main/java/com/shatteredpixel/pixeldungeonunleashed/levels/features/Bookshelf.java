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
package com.shatteredpixel.pixeldungeonunleashed.levels.features;

import com.shatteredpixel.pixeldungeonunleashed.Assets;
import com.shatteredpixel.pixeldungeonunleashed.Dungeon;
import com.shatteredpixel.pixeldungeonunleashed.items.Generator;
import com.shatteredpixel.pixeldungeonunleashed.levels.Level;
import com.shatteredpixel.pixeldungeonunleashed.levels.Terrain;
import com.shatteredpixel.pixeldungeonunleashed.scenes.GameScene;
import com.shatteredpixel.pixeldungeonunleashed.utils.GLog;
import com.shatteredpixel.pixeldungeonunleashed.windows.WndStory;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class Bookshelf {

    private static final String TXT_FOUND_SCROLL	= "You found a scroll!";
    private static final String TXT_FOUND_NOTHING	= "You found nothing interesting.";

    private static final String[] BOOKS = {

            // LORE

            "As the last of the chosen greyhill paladins to roam this realm the burden falls to me. Great care must be taken to destroy all evil in this world,"+
            " or the Sacred might still be freed from the cursed axe.\n\n - Kore.",

            "Once every 500 years, maglubiyet chooses one goblin of the clan viper to be born with wings, capable of flight. This is the one destined to lead all goblinkind to do great things.\n\n - From the book of Viper.",

            "They really tried to stop me. These vile creatures that sat in our holy council! It only shows how corrupted they already were, they let enough evil into their minds to try to stop ME! Oh, but I showed them...\n\n - Kore.",

            "The Tarrock Babarians have a strict patriarchaic view on the world. Their entire society is based around profound respect for their fathers. It is said, the quickest way to die is to insult a Tarrock's father.\n\n - Uncommon Concepts of Society - Vol. 3 Issue 24.",

            "Over a 1000 years ago, a demon of furious power ripped through the 7th layer of hell into this plane. It sought to scrape the life from this realm and claim it as a pocket of hell.\n\n"+
            "A group of high level holy guardians, 81 paladins and 22 war clerics, fought the demon in what is known today as the war crater."+
            "At the end of the day, the demon still stood, but only 9 paladins and one cleric remained. Unable to destroy the demon, they forged a powerful magic axe as a prison to contain it."+
            "All but one paladin were slain in the attempt to trap the abomination, but the demon was successfully captured.\n\n"+
            "And so shall the evil not be destroyed, but contained. Weakened by courage, imprisoned by valor.\n\n"+
            "- Thus was born the axe of prissan.",

            // DENIZENS

            "Goblins are savage beasts with heathen rituals. Luckily for the world of men, they pose no real threat whatsoever...at best we should treat and dispose of them like the vermin they are.\n\n - Internal treatise on Goblins by Dellyn Goblinslayer.",

            "The common cave troll (Magnimata sillicata stupidus) is intellectually challenged at best, but once in a while a troll comes to be intelligent enough to be a trader, blacksmith, or suited for some other lowly profession."+
            " These creatures are as reclusive in their daily life as they are ferocious when threatened. I examined the brain of one of these \"smart\" trolls, and found impurities in his sillcon brain as well as a advanced cooling system. Such abominations should not be allowed to live!\n\n - Allaster Spagirus (Wizard, Dr.Dct.Magn.Thau.)",

            "The dwarfs of greyhill achieved glorious technological advances in blacksmithing and cunning artifices. It is a shame it all literally went to hell upon that wretched Kore business. \n\n - Sephira Crumbell, Historian.",

            "Horrible demonic creatures inhabit the bowels of this world, one of the most powerful of which is only referred to by other demons as \"The Sacred\"."+
            "It's essence is rumored to be contained in some kind of holy weapon, but other rumors say some bodyparts left behind when the Sacred was contained still corrupt the realm around them today.\n\n"+
            "I call poppycock on all this nonsense of course! It's no more true than the bedtime stories told by our ancient dribbling elders of the rat king and his treasure room.\n\n - Allaster Spagirus (Wizard, Dr.Dct.Magn.Thau.)",

            "*crumbling parchment* ... and verily I say, you can trap such an entity. Not with gilt or steel or magic, but with your steely MinD!!! *blotches* I have DONE SO!!!"+
            "And to all you other Idiots hammering at MY DOOR right now because I killed a few worthless SOULS in THE PrOCESSsss: I say to the BLAZES with you...ARGlsbrbbllblub...*handwriting stops in a scrawly line*",

            // CHARACTERS

            "Mysterious Organisms are the Kliks. They resemble spheres with arms and mouth and seem to inherit properties of materials they are made of. Each of them has a substance that is good for them, and one that is very, very fatal."+
            "The positive and negative are often linked in a specific manner, but since there is apparently an unlimited number of Klik subspecies, it is very hard to find out what can be done to eradicate"+
            "troublesome Kliks that got too powerful by consuming their positive substance. \n\n - Eargreave Drumroll, master trapper.",

            "The outer planes are an assortment of sheer agony paired with normal worlds and paradise ones. I'd say the paradise worlds are the most dangerous, because it is so easy to become lost in there to never return.",

            // CREDITS

            "This is a tome which was written by Evan the Shattered, a great sage who worked tirelessly to improve his world. His deeds inspired many, and were adored by even more.",

            // MISC

            "This appears to be a book of jokes...heavily worn and ripped, but some words can be read: \"..hat sound does a dwarven god make falling down the stairs? - CLANGGEDIN clanggedin clangg\"...",

            "This is a list of bizarre prophecies, which mentions dead men blowing into a horns, arrival of demons in blood-red raiments, and giant crabs in formal dresses.",

    };

    public static void rummage( int cell ) {

        GLog.p( "You rummage around." );
        Integer Roll = Random.Int(99);
        if (Roll < 15 ) {

            WndStory.showChapter( BOOKS[ Random.Int( BOOKS.length ) ] );

        } else if (Roll < ( 20 + Dungeon.depth ) ) {

            Dungeon.level.drop( Generator.random( Generator.Category.SCROLL ), Dungeon.hero.pos ).sprite.drop();
            GLog.p( TXT_FOUND_SCROLL );

        } else {

            GLog.i( TXT_FOUND_NOTHING );

        }

        Level.set( cell, Terrain.EMPTY_SHELF );
        Dungeon.observe();

        if (Dungeon.visible[cell]) {
            GameScene.updateMap( cell );
            Sample.INSTANCE.play(Assets.SND_OPEN);
        }
    }
}
