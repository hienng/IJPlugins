package util.neighbourhood.collector;

import ij.process.ImageProcessor;

public class Sobel extends GreyCollector {
	private int[][] matrix;
	private int[][] pixels;
	
	public int[][] getPixels() {
		return pixels;
	}
	
	public Sobel(int[][] m) {
		matrix = m;
	}
	
	@Override
	public void putPixel(ImageProcessor ip, int x, int y) {
		if(pixels == null) {
			pixels = new int[ip.getWidth()][ip.getWidth()];
		}
		
		int sum = 0;
		for(int i = 0; i < matrix.length; i++) {
			for(int j = 0; j < matrix[i].length; j++) {
				sum += matrix[i][j] * getResultSet().remove(0);
			}
		}
		pixels[x][y] = sum;
		ip.putPixel(x,y,sum);
	}

}
