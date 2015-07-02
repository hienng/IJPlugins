import ij.ImagePlus;
import ij.io.OpenDialog;
import ij.io.Opener;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;

import java.awt.Point;

public class FaceDetection_plugin implements PlugInFilter {

	@Override
	public void run(ImageProcessor ip) {
		OpenDialog diag = new OpenDialog("Bitte Template waehlen:", "");
		ImagePlus impTemp = new Opener().openImage(diag.getPath());
		DTW dtw = new DTW();
		Point p = dtw.analyse(ip, impTemp.getProcessor());
		ip.drawRect(p.x, p.y, impTemp.getWidth(), impTemp.getHeight());
	}

	@Override
	public int setup(String arg, ImagePlus imp) {
		return DOES_ALL;
	}
	
	/*
	 * A fingerprint is a histogram
	 */
	public class Fingerprint extends Histogram {
		private Histogram h;
		
		public Fingerprint(ImageProcessor ip) {
			super(ip);
			Histogram fp = new Histogram(ip);
			h = fp;
		}
		
		public Fingerprint(ImageProcessor ip, int i, int j, int width, int heigth) {
			super(ip);
			Histogram fp = new Histogram(ip, i, j, width, heigth);
			h = fp;
		}
		
		public int[] getValues() {
			return h.getDistribution();
		}
	}
	
	public class Histogram {
		private int[] values;
		
		/**
		 * @return extract the partition of the image
		 */
		private int[] getDistribution() {
			return this.values;
		}
		
		/**
		 * Constructor 
		 * @param ip
		 */
		public Histogram(ImageProcessor ip) {
			this(ip, 0, 0, ip.getWidth(), ip.getHeight());
		}
		
		/**
		 * extended Constructor - partitioning of the image
		 * @param ip 		ImageProcessor,  the image
		 * @param i			position x used for partition 
		 * @param j			position y used for partition
		 * @param width		width of area
		 * @param height	height of area
		 */
		public Histogram(ImageProcessor ip, int i, int j, int width, int height) {
			values = new int[width + height];
			
			for(int x = 0; x < width; x++) {
				int colSum = 0;
				for(int y = 0; y < height; y++) {
					colSum += ip.getPixel(x+i, y+j);
				}
				values[x] = colSum;
			}
			
			for(int y = 0; y < height; y++) {
				int rowSum = 0;
				for(int x = 0; x < width; x++) {
					rowSum += ip.getPixel(x+i,  y+j);
				}
				values[y+width] = rowSum;
			}	
		}	
	}
	
	/*
	 * DTW: Dynamic Time Warping 
	 */
	public class DTW {
		public Point p;
		
		/**
		 * distance of template t and image b, which should be analysed
		 * @param x 		
		 * @param y
		 * @param t			fingerprint of template image t	
		 * @param b			fingerprint of input image b
		 * @return
		 */
		public int calcDistance(int x, int y, Fingerprint t, Fingerprint b) {
			return (int) Math.floor(Math.sqrt(Math.pow(t.getValues()[x] - b.getValues()[y], 2)) / 10 ) * 10;
		}
		
		/**
		 * M x N of fingerprint-partition 
		 * @param a			fingerprint - partition
		 * @param b			fingerprint - partition
		 * @return 			global distance matrix or accumulative distance 
		 */
		public int getDistance(Fingerprint a, Fingerprint b) {
			int[][] D = new int[a.getValues().length][b.getValues().length];
			for(int x = 0; x < D.length; x++) {
				for(int y = 0; y < D[x].length; y++) {
					D[x][y] = calcDistance(x, y, a, b) + Math.min(get(x-1, y, D), Math.min(get(x-1, y-1, D), get(x, y-1, D)));
				}
			}
			return D[a.getValues().length-1][b.getValues().length - 1];
		}
		
		/**
		 * fill the matrix of D
		 * @param x
		 * @param y
		 * @param D
		 * @return either D[x][y] or Infinity
		 */
		public int get(int x, int y, int[][] D) {
			return x >= 0 && y >= 0 && x < D.length && y < D[y].length ? D[x][y] : Integer.MAX_VALUE;
		}
		
		/**
		 * analyses two images
		 * @param ip			input image
		 * @param template		input template image
		 * @return				area which matches
		 */
		public Point analyse(ImageProcessor ip, ImageProcessor template) {
			Fingerprint templateFp = new Fingerprint(template);
			int posX = 0;
			int posY = 0;
			int minDistance = Integer.MAX_VALUE;
			for(int x = 0; x < ip.getWidth() - template.getWidth(); x++) {
				for(int y = 0; y < ip.getHeight() - template.getHeight(); y++) {
					Fingerprint n = new Fingerprint(ip, x, y, template.getWidth(), template.getHeight());
					int globalDistance = getDistance(templateFp, n);
					if(globalDistance < minDistance) {
						minDistance = globalDistance;
						posX = x; 
						posY = y;
					}
				}
			}
			return new Point(posX, posY);
		}
	}
	
}
