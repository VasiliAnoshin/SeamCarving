/*
 * This class defines some static methods of image processing.
 */

//package edu.cg;

import java.awt.image.BufferedImage;

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
	
	//Converts an image to gray scale
	public static BufferedImage grayScale(BufferedImage img) {
		//TODO implement this (choose any implementation that was learnt in class)
		return null;
	}
	
	//Calculates the magnitude of gradient at each pixel
	public static BufferedImage gradientMagnitude(BufferedImage img) {
		//TODO implement this
		return null;
	}
	
	
}
