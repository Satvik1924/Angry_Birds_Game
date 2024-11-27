package org.angry.Model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import org.angry.view.*;

public class ControllerLogic {
    public  static boolean  isBird=true;
    public  static boolean  RECORDING=false;
    public static Vector2 vel=new Vector2();
    public static Vector2 POS=new Vector2();
    public static boolean ISRERUN=false;
    public static int CIRCLEMASS=8;
    public static int BOXMASS=4;
    public static boolean charging=false;
    public  static float SLINGWIDTH=7;
    public  static Body EARTH;
    public static Array<Box> boxArray=new Array<Box>();
    public static Array<Ice_Box> IceboxArray= new Array<Ice_Box>();
    public static Array<Stone_Box> StoneboxArray= new Array<Stone_Box>();
    public static Array<Pig> circleArray=new Array<Pig>();
    public static Array<LargePig> LargecircleArray=new Array<LargePig>();
    public static Array<MediumPig> MediumcircleArray=new Array<MediumPig>();
}
