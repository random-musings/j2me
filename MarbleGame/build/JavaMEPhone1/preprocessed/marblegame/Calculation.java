/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marblegame;

/**
 * Code for this section has been copied from http://manlyignition.blogspot.ca/2008/11/arctan-in-j2me.html
 *  All credit belongs to  
 * Stephen Zimmerman
 */


public class Calculation {
    // calculation functions


// Because J2ME has no floating point numbers,
// some sort of fixed point math is required.
// My implementation is simply to shift 10 places.
// for example, 1024 (>> 10) = 1
// and 512 (>> 10) = 0.5
public static final int[] CosTable90 = {
1024, 1023, 1023, 1022, 1021, 1020, 1018,
1016, 1014, 1011, 1008, 1005, 1001, 997,
993, 989, 984, 979, 973, 968, 962,
955, 949, 942, 935, 928, 920, 912,
904, 895, 886, 877, 868, 858, 848,
838, 828, 817, 806, 795, 784, 772,
760, 748, 736, 724, 711, 698, 685,
671, 658, 644, 630, 616, 601, 587,
572, 557, 542, 527, 512, 496, 480,
464, 448, 432, 416, 400, 383, 366,
350, 333, 316, 299, 282, 265, 247,
230, 212, 195, 177, 160, 142, 124,
107, 89, 71, 53, 35, 17, 0
};

public static final int[] AtanTable = {
0, 1, 2, 3, 5, 6, 7, 8, 10, 11,
12, 13, 14, 16, 17, 18, 19, 20, 21, 22,
23, 25, 26, 27, 28, 29, 30, 30, 31, 32,
33, 34, 35, 36, 37, 37, 38, 39, 40, 40,
41, 42, 43, 43, 44, 45
};


// angle calculation function based on x = x1 - x2, y = y1 - y2
public static boolean ArcTan(int x, int y, AngleArray sincos)
{
int angle = atan( y, x );
if (angle == -300)
return false;

int rotangle = Math.abs(angle) % 90;

int cos = CosTable90[rotangle];
int sin = CosTable90[90-rotangle];

if (angle >= 270) // quadrant IV
{
int tCos = cos;
cos = sin;
sin = -tCos;
}
else if (angle >= 180) // quadrant III
{
cos = -cos;
sin = -sin;
}
else if (angle >= 90) // quadrant II
{
int tCos = cos;
cos = -sin;
sin = tCos;
}


sincos.sin = sin;
sincos.cos = cos;

return true;
}


/// returns angle 0->359 in degrees
public static int atan( int Y, int X )
{
boolean swap = false;

int top = Math.abs(Y);
int bottom = Math.abs(X);
if (top > bottom)
{
int btemp = bottom;
bottom = top;
top = btemp;
swap = true;
}
else if (bottom == 0)
return -300;

// this should keep index inbounds [0, 45]
int index = (top * 45) / bottom;
int angle = AtanTable[index];

if (swap)
angle = 90 - angle;

// X & Y += 180
// X & !Y = ...90
// !X & Y = ... 270
if ( (X < 0) && (Y < 0) )
angle += 180;
else if (Y < 0)
{
angle = 90 - angle;
angle += 270;
}
else if ( X < 0)
{
angle = 90 - angle;
angle += 90;
}

if (angle == 360)
angle = 0;

return angle;
}

// for good measure, sin and cos functions
public static int sin( int angle )
{
int rotangle = Math.abs(angle);
while (rotangle > 89)
rotangle -= 90;

int cos = CosTable90[rotangle];
int sin = CosTable90[90-rotangle];

if (angle >= 270) // quadrant IV
sin = -cos;

else if (angle >= 180) // quadrant III
sin = -sin;

else if (angle >= 90) // quadrant II
sin = cos;

return sin;
}

public static int cos( int angle )
{
int rotangle = Math.abs(angle);
while (rotangle > 89)
rotangle -= 90;

int cos = CosTable90[rotangle];
int sin = CosTable90[90-rotangle];

if (angle >= 270) // quadrant IV
cos = sin;

else if (angle >= 180) // quadrant III
cos = -cos;

else if (angle >= 90) // quadrant II
cos = -sin;

return cos;
}


}
