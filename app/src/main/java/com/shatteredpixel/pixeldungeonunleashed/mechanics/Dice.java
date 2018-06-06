package com.shatteredpixel.pixeldungeonunleashed.mechanics;

import com.watabou.utils.Random;

public class Dice {

    public int roll(int count, int faces) {
        int result = 0;
        int i = 0;
        while (i  < count){
            result += Random.Int(1, faces);
            i++;
        }
        return result;
    }

}
