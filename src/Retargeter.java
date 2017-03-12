//package edu.cg;

import java.awt.image.BufferedImage;

public class Retargeter {
	private BufferedImage m_grayScaleImg;
	private BufferedImage m_originalImg;
	
	private int[][] m_originalPosMat;
	private int[][] m_grayScaleMat;
	private int[][] m_seamsOrdMat;
	
	private int m_originalHeight;
	private boolean isVertical;
	private int m_originalWidth;
	//Does all necessary preprocessing, including the calculation of the seam order matrix.
	//k is the amount of seams to preprocess
	//isVertical tells whether the resizing is vertical or horizontal
	public Retargeter(BufferedImage img, int k, boolean _isVertical) {					
		this.isVertical = _isVertical;
		//Always will work in horizontal mode, for this purpose we transpose the
		//original matrix if we get vertical mode and at the end change it back.
		if(_isVertical){
			m_originalImg = transposeImg(img);
		}else{
			m_originalImg = img;
		}
		this.m_grayScaleImg = ImageProc.grayScale(m_originalImg);
		this.m_originalHeight = m_originalImg.getHeight();
		this.m_originalWidth = m_originalImg.getWidth();
		
		m_originalPosMat = new int[m_originalWidth][m_originalHeight];
		m_grayScaleMat = new int[m_originalWidth][m_originalHeight];
		
		for (int y=0;y<m_originalHeight;y++) {
			for (int x=0;x<m_originalWidth;x++) {
				// init original position matrix for the img.
				m_originalPosMat[x][y] = x;
				// create a m_grayScaleMat for calc convenience -> &0xFF for recieving gray between 0-255
				m_grayScaleMat[x][y] = m_grayScaleImg.getRGB(x, y) & 0xFF;
			}
		}
		
		calculateSeamsOrderMatrix(k);
	}
	//Transpose
	private BufferedImage transposeImg(BufferedImage img) {
		int width = img.getWidth();
		int height = img.getHeight();
		//In Transpose mode the height will in width place and the width will in height mode.
		BufferedImage tImg = new BufferedImage(height,width, img.getType());		
		for (int y=0;y<height;y++) {
			for (int x=0;x<width;x++) {
				tImg.setRGB(y,x,img.getRGB(x, y));
			}
		}
		return tImg;
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
