package util;

import ij.process.ImageProcessor;

public class Histogram {
	private int[] h;
	
	public int[] getDistribution() {
		return h;
	}
	
	public Histogram(ImageProcessor ip) {
		h = new int[256];
		for(int x = 0; x < ip.getWidth(); x++) {
			for(int y = 0; y < ip.getHeight(); y++) {
				int i = ip.getPixel(x, y);
				h[i]++;
			}
		}
	}
}
