import java.util.ArrayList;
import java.util.List;

import ij.IJ;
import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;
import util.ColourHistogram;

/*
 * Blatt 6, Aufgabe 2
 * Antwort: Erkennung eines Schnitts bei Frame 44 zu 45 und Frame 159 zu 160
 */

public class Histrogram_Cut_Detection_Plugin implements PlugInFilter {
	private ImagePlus imp;
	
	public int setup(String arg, ImagePlus imp) {
		this.imp = imp;
		return DOES_ALL;
	}
	// 20% Tolerance T
	private static final float CUT_TOLERANCE = 0.2f;
	
	public void run(ImageProcessor ip) {
		ColourHistogram[] histogram = new ColourHistogram[imp.getStackSize()];
		List<Integer> cuts = new ArrayList<Integer>();
		for(int i = 0; i < imp.getStackSize(); i++) {
			ImageProcessor nip = imp.getImageStack().getProcessor(i+1);
			histogram[i] = new ColourHistogram(nip);
		}
		
		for(int i = 0; i < histogram.length - 2; i++) {
			// f1 -- f2
			int d1 = histogram[i].difference(histogram[i+1]);
			// f2 -- f3
			int d2 = histogram[i+1].difference(histogram[i+2]);
			if(d2 > d1*(1+CUT_TOLERANCE)) {
				cuts.add(i+2);
			}
		}
		
		IJ.showMessage("Cuts after frames: " + cuts);
	}
	
}
