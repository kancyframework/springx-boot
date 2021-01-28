package com.github.kancyframework.springx.swing.themes;

/**
 * Themes
 *
 * @author kancy
 * @date 2020/6/14 12:34
 */
public class Themes {

    public static void useRandom(){
        LookAndFeels.useRandom();
    }

    public static void useNimbus(){
        LookAndFeels.useNimbus();
    }

    public static void useMetal(){
        LookAndFeels.useMetal();
    }

    public static void useSeaGlass(){
        LookAndFeels.useSeaGlass();
    }

    public static void useDefault(){
        LookAndFeels.useDefault();
    }

    public static void useTheme(String theme){
        switch (theme){
            case "random" : LookAndFeels.useRandom();break;
            case "nimbus" : LookAndFeels.useNimbus();break;
            case "metal" : LookAndFeels.useMetal();break;
            case "seaGlass" : LookAndFeels.useSeaGlass();break;
            default:
                LookAndFeels.useDefault();
        }
    }
}
