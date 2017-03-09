//package edu.cg;

import java.awt.image.BufferedImage;

public class Retargeter {
	
	//Does all necessary preprocessing, including the calculation of the seam order matrix.
	//k is the amount of seams to preprocess
	//isVertical tells whether the resizing is vertical or horizontal
	public Retargeter(BufferedImage img, int k, boolean isVertical) {
		//TODO do initialization and preprocessing here
		calculateSeamsOrderMatrix(k);
	}

	//Does the actual resizing of the image
	public BufferedImage retarget(int newSize) {
		//TODO implement this
		return null;
	}
	
	//Colors the seams pending for removal/duplication
	public BufferedImage showSeams(int newSize) {
		//TODO implement this
		return null;
	}
	
	//Calculates the order in which seams are extracted
	private void calculateSeamsOrderMatrix(int k) {
		//TODO implement this (it is just a suggestion, you can remove this function/change it however you like)
	}
	
	//Calculates the cost matrix for a given image width (w).
	//To be used inside calculateSeamsOrderMatrix().
	private void calculateCostsMatrix(int w) {
		//TODO implement this (it is just a suggestion, you can remove this function/change it however you like)
	}
	
}
