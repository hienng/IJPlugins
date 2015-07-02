package util;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

import ij.IJ;
import ij.ImagePlus;
import ij.process.ImageProcessor;

public class Util {
	public static ImagePlus toGreyScale(ImageProcessor input) {
		ImagePlus grey = IJ.createImage("greyscale", "8-bit", input.getWidth(), input.getHeight(), 1);
		for(int x = 0; x < grey.getWidth(); x++) {
			for(int y = 0; y < grey.getHeight(); y++) {
				int[] rgb = new int[3];
				input.getPixel(x, y, rgb);
				int p = (rgb[0] + rgb[1] + rgb[2])/3;
				grey.getProcessor().putPixel(x, y, p);
			}
		}
		return grey;
	}
	
	public static int getMedian(List<Integer> list) {
		Collections.sort(list);
		return list.size() == 0 ? 0 : list.get(list.size()/2);
	}
}
