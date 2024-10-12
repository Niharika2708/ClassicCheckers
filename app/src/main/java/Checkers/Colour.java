package Checkers;

public class Colour {

    int red;
    int green;
    int blue;

    public Colour(int r, int g, int b){

        red =r;
        green = g;
        blue = b;
    }
    public int[] rgb(){

        return new int[] {red, green, blue};
    }
    
    public Colour add(Colour b){
        int[] bRGB = b.rgb();
        int newRed = Math.min(red +bRGB[0], 255);
        int newGreen = Math.min(green +bRGB[1], 255);
        int newBlue = Math.min(blue +bRGB[2], 255);

        return new Colour(newRed, newGreen, newBlue);

        }
}