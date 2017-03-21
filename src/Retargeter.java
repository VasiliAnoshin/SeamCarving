//package edu.cg;

import java.awt.image.BufferedImage;

import java.awt.Color;

public class Retargeter {
	private BufferedImage m_grayScaleImg;
	private BufferedImage m_originalImg;
	
	private int[][] m_originalPosMat;
	private int[][] m_grayScaleMat;
	private int[][] m_seamsOrdMat;
	private boolean m_isVertical;
	
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
		BufferedImage newImg = new BufferedImage(newSize, m_originalHeight,BufferedImage.TYPE_INT_RGB);
		int shift=0;		
		int seamCount = Math.abs(m_originalWidth-newSize);
		//If the newSize is bigger then original
		if (m_originalWidth < newSize) {
			// upscaling
			for (int y=0;y<m_originalHeight;y++) {
				shift=0;
				for (int x=0;x<m_originalWidth;x++)
					if ((m_seamsOrdMat[x][y]>0)&&(m_seamsOrdMat[x][y]<=seamCount)) {
						// duplicate seam pixels
						newImg.setRGB(x+shift, y, m_originalImg.getRGB(x, y));
						shift++;
						newImg.setRGB(x+shift, y, m_originalImg.getRGB(x, y));
					} else
						newImg.setRGB(x+shift, y, m_originalImg.getRGB(x, y));
			}
		} else {
			// downscaling
			for (int y=0;y<m_originalHeight;y++) {
				shift=0;
				for (int x=0;x<m_originalWidth;x++)
					if ((m_seamsOrdMat[x][y]>0)&&(m_seamsOrdMat[x][y]<=seamCount))
						// Ignore seam pixels
						shift++;
					else
						newImg.setRGB(x-shift, y, m_originalImg.getRGB(x, y));
			}
		}				
		if (m_isVertical) 
			return transposeImg(newImg);
		else
			return newImg;
	}
	
	//Colors the seams pending for removal/duplication
	public BufferedImage showSeams(int newSize) {
		BufferedImage seamsImg = new BufferedImage(m_originalWidth, m_originalHeight,BufferedImage.TYPE_INT_RGB);
		int seamCount = Math.abs(m_originalWidth-newSize);
		for (int y=0;y<m_originalHeight;y++) 
			for (int x=0;x<m_originalWidth;x++)
				if ((m_seamsOrdMat[x][y]>0)&&(m_seamsOrdMat[x][y]<=seamCount))
					seamsImg.setRGB(x, y, Color.RED.getRGB());
				else
					seamsImg.setRGB(x, y, m_originalImg.getRGB(x, y));
		
		if (m_isVertical) 
			return transposeImg(seamsImg);
		else
			return seamsImg;
	}
	
	//Calculates the order in which seams are extracted
	private void calculateSeamsOrderMatrix(int k) {
		//cur MartixWidth
		int i_calcCurrentWidth,y,x;
		int minVal,minX,tmpMinX;

		int[][] newOrgPosMat;
		int[][] costMat;
		m_seamsOrdMat = new int[m_originalWidth][m_originalHeight];
		
		// the img will shrink while processing..
		i_calcCurrentWidth = m_originalWidth;
		
		// add k seams (iSeam index path) to the seamsOrdMat   
		for (int iSeam=1;	iSeam<=k;	iSeam++) {
			newOrgPosMat = new int[i_calcCurrentWidth][m_originalHeight];
			costMat = calculateCostsMatrix(i_calcCurrentWidth);
			
			//first index in the last row
			minVal = costMat[0][m_originalHeight-1];
			minX = 0;
			
			//find where the minimum seam ends -> start point. From this point start compute the seam.  
			y=m_originalHeight-1;
			for (x=1;x<i_calcCurrentWidth;x++) {
				if (costMat[x][y] < minVal) {
					minVal = costMat[x][y];
					minX = x;
				}
			}
			//sets first index of seam in the seam order matrix 
			m_seamsOrdMat[m_originalPosMat[minX][y]][y]=iSeam;
			
			//shift left all pixels right to the seam, for the new original position Matrix
			for (int i=0;i<minX;i++){
				newOrgPosMat[i][y] = m_originalPosMat[i][y];
			}
			for (int i=minX;i<(i_calcCurrentWidth-1);i++){
				newOrgPosMat[i][y] = m_originalPosMat[i+1][y];
			}
			//trek the min seam up
			for (;y>=0;--y) { 
				//find where it came from  
				tmpMinX = minX;
				//vertical
				minVal = costMat[minX][y];
				//left
				if ((minX-1>0)&&(costMat[minX-1][y]<minVal)) { 
					tmpMinX = minX-1;
					minVal = costMat[tmpMinX][y];
				}
				//or right...
				if ((minX+1<i_calcCurrentWidth) && (costMat[minX+1][y]<minVal))
					tmpMinX = minX+1;
				
				minX = tmpMinX;
				
				// sets the seam index at the original pos of it's path
				m_seamsOrdMat[m_originalPosMat[minX][y]][y]=iSeam;
				
				//shift left all x pos of pixels right to the seam, for the new original position Matrix
				for (int i=0;i<minX;i++)
					newOrgPosMat[i][y] = m_originalPosMat[i][y];
				for (int i=minX;i<i_calcCurrentWidth-1;i++)
					newOrgPosMat[i][y] = m_originalPosMat[i+1][y];
			}
			i_calcCurrentWidth--;
			m_originalPosMat = newOrgPosMat;
			
		}
	}
	
	//Calculates the cost matrix for a given image width (w).
	//To be used inside calculateSeamsOrderMatrix().
	private int[][] calculateCostsMatrix(int calcCurrentWidth) {
		int cL,cR,cV,mL,mV,mR;
		int[][] costM = new int[calcCurrentWidth][m_originalHeight];
		
		//Fill help matrix with -1
		for (int x=0; x<calcCurrentWidth; x++) {
			for (int y=0; y<m_originalHeight; y++) { 
				costM[x][y] = -1;
			}
		}		
		//Set corners (At the two upper corner pixels, you can’t really calculate a cost. Put a value of 1000);
		costM[0][0] = 1000;
		costM[calcCurrentWidth-1][0] = 1000;
		
		//set upper border  |I(i-1,j ) - I(i+1,j)| compute from grayScalMat
		for (int x=1;x<calcCurrentWidth-1;x++) {
			costM[x][0] = Math.abs(m_grayScaleMat[m_originalPosMat[x-1][0]][0] - m_grayScaleMat[m_originalPosMat[x+1][0]][0]);
		}
		//forward matrix energy computing
		for (int y=1;y<m_originalHeight;y++) {
			for (int x=1;x<calcCurrentWidth-1;x++) {
				//Calc left, right and vertical path cost
			
				cV = Math.abs(m_grayScaleMat[m_originalPosMat[x-1][y]][y] - m_grayScaleMat[m_originalPosMat[x+1][y]][y]);
			
				cL = Math.abs(m_grayScaleMat[m_originalPosMat[x][y-1]][y-1] - m_grayScaleMat[m_originalPosMat[x-1][y]][y]);
			
				cR = Math.abs(m_grayScaleMat[m_originalPosMat[x][y-1]][y-1] - m_grayScaleMat[m_originalPosMat[x+1][y]][y]);
				
				mL = costM[x-1][y-1] + cL + cV;
				mV = costM[x][y-1] + cV;
				mR = costM[x+1][y-1] + cR + cV;
				
				//set the minimum
				if ((mL<mR) && (mL<mV)) {
					costM[x][y] = mL;
				} else if ((mV<mR) && (mV<mL)) {
					costM[x][y] = mV;	
				} else { 
					costM[x][y] = mR;	
				}
			}			
			//The most left edge pixel (after middle ones were set):
			mV = costM[0][y-1];

			cR = Math.abs(m_grayScaleMat[m_originalPosMat[0][y-1]][y-1] - m_grayScaleMat[m_originalPosMat[1][y]][y]);
			
			mR = costM[1][y-1] + cR;
			if (mV<mR) 
				costM[0][y] = mV + costM[1][y];
			else
				costM[0][y] = mR + costM[1][y];
			
			//the most right edge pixel (after middle ones were set):
			mV = costM[calcCurrentWidth-1][y-1];

			cL = Math.abs(m_grayScaleMat[m_originalPosMat[calcCurrentWidth-1][y-1]][y-1] - m_grayScaleMat[m_originalPosMat[calcCurrentWidth-2][y]][y]);
			mL = costM[calcCurrentWidth-2][y-1] + cL;
			if (mV<mL) 
				costM[calcCurrentWidth-1][y] = mV + costM[calcCurrentWidth-2][y];
			else
				costM[calcCurrentWidth-1][y] = mL + costM[calcCurrentWidth-2][y];
		}
		//Tool for debugging - there is no should be -1 in the matrix after construction. 
		for (int x=0;x<calcCurrentWidth;x++) 
			for (int y=0;y<m_originalHeight;y++) { 
				if (costM[x][y] < 0)
					System.out.println("cost not calc at: (" + x + "," + y +")");
			}
		
		return costM; 
	}			
}
