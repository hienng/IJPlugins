import java.util.ArrayList;
import java.util.List;

import util.Util;
import filter.SobelFilter;
import ij.IJ;
import ij.ImagePlus;
import ij.process.ImageProcessor;
import ij.plugin.filter.PlugInFilter;

/*
 * Blatt 6, Aufgabe 1 
 */

public class Sobel_Cut_Detection_Plugin implements PlugInFilter {
	private ImagePlus imp;
	
	private static final float ECR_THRESHOLD = 0.97f;
	private static final int EDGE_THRESHOLD = 100;
	
	public int setup(String arg, ImagePlus imp) {
		this.imp = imp;
		return DOES_ALL;
	}
	
	public void run(ImageProcessor ip) {
		List<Integer> cuts = new ArrayList<Integer>();
		ImageProcessor[] sobeled = new ImageProcessor[imp.getStackSize()];
		Edges[] edges = new Edges[imp.getStackSize()];
		SobelFilter sobel = new SobelFilter();
		
		// grey-conversion and edge-detection
		for(int i = 0; i < imp.getStackSize(); i++) {
			ImageProcessor nip = imp.getStack().getProcessor(i+1);
			ImagePlus grey = Util.toGreyScale(nip);
			sobeled[i] = sobel.apply(grey.getProcessor());
		}
		
		edges[0] = new Edges(sobeled[0]);
		for(int i = 1; i < sobeled.length; i++) {
			edges[i] = new Edges(sobeled[i], edges[i-1]);
		}
		
		// compute ECR and store Index if ECR > 1 
		for(int i = 1; i < edges.length; i++) {
			double ecr = Math.max((float) edges[i].ein/edges[i-1].total, (float) edges[i].eout/edges[i].total);
			if(ecr > ECR_THRESHOLD)
				cuts.add(i);
//			System.out.println(i + ":" + ecr);
		}
		IJ.showMessage("Cuts after frames: " + cuts);
	}
		
	private class Edges {
		public int ein = 0, eout = 0, total = 0;
		public final ImageProcessor image;
		
		/*
		 * Constructor for first frame, which has no predecessor. ==> All edges in this frame 
		 * are E_in and E_out
		 */
		
		public Edges(ImageProcessor ip) {
			image = ip;
			for(int x = 0; x < ip.getWidth(); x++) {
				for(int y = 0; y < ip.getHeight(); y++) {
					if(ip.getPixel(x,y) > EDGE_THRESHOLD)
						ein++;
				}
			}
			total = ein;
		}
		
		/*
		 * Constructor for consecutive Frames
		 * taking predecessor frames as parameter and computes E_in and E_out
		 */
		
		public Edges(ImageProcessor ip, Edges pred) {
			image = ip;
			for(int x = 0; x < ip.getWidth(); x++) {
				for(int y = 0; y < ip.getHeight(); y++) {
					int predPixel = pred.image.getPixel(x, y);
					int currentPixel = image.getPixel(x, y);
					if(currentPixel > EDGE_THRESHOLD) {
						if(predPixel < EDGE_THRESHOLD) {
							ein++;
						}
						total++;
					} else {
						if(predPixel > EDGE_THRESHOLD) {
							eout++;
						}
					}
				}
			}
		}
	}
	
}
