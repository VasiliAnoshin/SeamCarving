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
		//cur MartixWidth
		int i_calcCurrentWidth;
		int[][] newOrgPosMat;
		int[][] costMat;
		m_seamsOrdMat = new int[m_originalWidth][m_originalHeight];
		
		// the img will shrink while processing..
		i_calcCurrentWidth = m_originalWidth;
		
		// add k seams (iSeam index path) to the seamsOrdMat   
		for (int iSeam=1;	iSeam<=k;	iSeam++) {
			newOrgPosMat = new int[i_calcCurrentWidth][m_originalHeight];
			costMat = calculateCostsMatrix(i_calcCurrentWidth);
		}
	}
	
	//Calculates the cost matrix for a given image width (w).
	//To be used inside calculateSeamsOrderMatrix().
	private int[][] calculateCostsMatrix(int calcCurrentWidth) {
		int cL,cR,cV,mL,mV,mR;
		int[][] costM = new int[calcCurrentWidth][m_originalHeight];
		
		for (int x=0; x<calcCurrentWidth; x++) {
			for (int y=0; y<m_originalHeight; y++) { 
				costM[x][y] = -1;
			}
		}
		//set corners.
		costM[0][0] = 1000;
		costM[calcCurrentWidth-1][0] = 1000;
		
		for (int x=1;x<calcCurrentWidth-1;x++) {
			costM[x][0] = Math.abs(m_grayScaleMat[m_originalPosMat[x-1][0]][0] - m_grayScaleMat[m_originalPosMat[x+1][0]][0]);
		}
		
		return costM; 
	}
		
	
}
