/*
 * This class defines some static methods of image processing.
 */

//package edu.cg;

import java.awt.image.BufferedImage;

import com.sun.prism.paint.Color;
@Author(Author = "BenFranklin", date = "19/3/2017")
public class ImageProc {

	//Creates a reduced size image from the original image by taking every <factor>-th pixel in x and y direction
	public static BufferedImage scaleDown(BufferedImage img, int factor) {
		if (factor <= 0)
			throw new IllegalArgumentException();
		int newHeight = img.getHeight()/factor;
		int newWidth = img.getWidth()/factor;
		BufferedImage out = new BufferedImage(newWidth, newHeight, img.getType());
		for (int x = 0; x < newWidth; x++)
			for (int y = 0; y < newHeight; y++)
				out.setRGB(x, y, img.getRGB(x*factor, y*factor));
		return out;
	}
	
	//Runs the seam carving algorithm to resize an image horizontally (change width)
	public static BufferedImage retargetHorizontal(BufferedImage img, int width) {
		return new Retargeter(img, Math.abs(img.getWidth()-width), false).retarget(width);
	}
	
	//Runs the seam carving algorithm to resize an image vertically (change height)
	public static BufferedImage retargetVertical(BufferedImage img, int height) {
		return new Retargeter(img, Math.abs(img.getHeight()-height), true).retarget(height);		
	}
	
	//Runs the horizontal seam carving algorithm to present the seams for removal/duplication
	public static BufferedImage showSeamsHorizontal(BufferedImage img, int width) {
		return new Retargeter(img, Math.abs(img.getWidth()-width), false).showSeams(width);
	}
	
	//Runs the vertical seam carving algorithm to present the seams for removal/duplication
	public static BufferedImage showSeamsVertical(BufferedImage img, int height) {
		return new Retargeter(img, Math.abs(img.getHeight()-height), true).showSeams(height);		
	}
	/**
	 * When I strated out, I was really confused about bit shift and hexadecimal number and binary. This is a good example how confusing it could get sometimes. Occasionally you see this in the code, for exmaple in C++:
	   byte a = ( b >>  8) & 0xff; // b is an integer
	   Okay, I know what “>> 8” means, we are shifting variable “b” to the right by 8 bits. But, what the heck is “& 0xff” doing there?
       Well, 0xff is the hexadecimal number FF which has a integer value of 255. And the binary representation of FF is 00000000000000000000000011111111 (under the 32-bit integer).
	   The & operator performs a bitwise AND operation. a & b will give you an integer with a bit pattern that has a 0 in all positions where b has a 0, while in all positions where b has a 1, the corresponding bit value
	   from a is used (this also goes the other way around). For example, the bitwise AND of 10110111 and00001101 is 00000101.
	   In a nutshell, “& 0xff” effectively masks the variable so it leaves only the value in the last 8 bits, and ignores all the rest of the bits.
	   It’s seen most in cases like when trying to transform color values from a special format to standard RGB values (which is 8 bits long).
	 * @param img
	 * @return
	 */
	//Converts an image to gray scale
	public static BufferedImage grayScale(BufferedImage img) {
		int height = img.getHeight();
		int width = img.getWidth();
		int red, green,blue,grayScale,argb;
		BufferedImage out = new BufferedImage(width, height, img.getType());
		for (int x = 0; x < width; x++){
			for (int y = 0; y < height; y++){
				//getRGB -  method returns the RGB value representing the color in the default ARGB ColorModel.
				//(Bits 24-31 are alpha, 16-23 are red, 8-15 are green, 0-7 are blue)
				argb = img.getRGB(x, y);
				red = (argb>>16) & 0xff;
				green = (argb>>8) & 0xff;
				blue = (argb) & 0xff;
				//Formula for compute the the greyLevel.
				grayScale = (int) (0.2989 * red + 0.5870 * green + 0.1140 * blue);
				//R + G + B have the same grey level. greyScale have value only for first Blue value .
				//It still need to fill all of R + G with the same grey values .				
				grayScale = (grayScale<<16) | (grayScale<<8) | grayScale;
				out.setRGB(x, y, grayScale);
			}
		}
		return out;
	}
	
	//Calculates the magnitude of gradient at each pixel
	public static BufferedImage gradientMagnitude(BufferedImage img) {
		/*First we should to get img in gray that's beacuse we should recieve picture 
		with black/white/gray colors . Gradient should show only the edge parts in white - the places with the most differences.
		and black if there is no difference .
		*/
		BufferedImage grayScaleImg = grayScale(img);
		int height = grayScaleImg.getHeight();
		int width = grayScaleImg.getWidth();
		int curPixel, upPixel,argbDY,leftPixel, argbDX;		
		BufferedImage out = new BufferedImage(width, height, grayScaleImg.getType());		
		for (int x = 1; x < width; x++){	
			for (int y = 1; y < height; y++){
				//As we working in gray should take left 8 bits and compute dx/dy				
				curPixel = grayScaleImg.getRGB(x, y)& 0xff;
				upPixel = grayScaleImg.getRGB(x, y-1)& 0xff;
				leftPixel = grayScaleImg.getRGB(x-1, y)& 0xff;
				
				argbDY = upPixel - curPixel;
				argbDX = leftPixel - curPixel;
				//gradient = sqrt(dx^2 + dy^2)/sqrt(2)
				curPixel = (int) Math.sqrt(Math.pow(argbDX, 2) + Math.pow(argbDY, 2));				
				curPixel = (curPixel<<16) | (curPixel<<8) |curPixel;
				out.setRGB(x, y, curPixel);
			}
		}
		return out;
	}
	
	
}
