package util;

import ij.process.ImageProcessor;

public class ColourHistogram {
	public final int[] r,g,b;
	
	public int difference(ColourHistogram other) {
		int distance = 0;
		int[][] H = {r,g,b};
		int[][] RGB_triple = {other.r, other.g, other.b};
		for(int i = 0; i < H.length; i++) {
			for(int j = 0; j < H[i].length; j++) {
				distance += Math.abs(H[i][j] - RGB_triple[i][j]);
			}
		}
		return distance;
	}
	
	/*
	 * Constructor
	 */
	
	public ColourHistogram(ImageProcessor ip) {
		r = new int[256];
		g = new int[256];
		b = new int[256];
		
		for(int x = 0; x < ip.getWidth(); x++) {
			for(int y = 0; y < ip.getHeight(); y++) {
				int[] p = new int[3];
				ip.getPixel(x, y, p);
				r[p[0]]++;
				g[p[1]]++;
				b[p[2]]++;
			}
		}
	}
}
