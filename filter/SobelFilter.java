//package filter;
//
//import ij.process.ImageProcessor;
//import util.neighbourhood.collector.Sobel;
//
//public class SobelFilter implements IFilter {
//	private static final int[][] horiz = {
//		{-1, 0, 1},
//		{-2, 0, 2},
//		{-1, 0, 1}
//	};
//	
//	private static final int[][] vert = {
//		{1, 2, 1},
//		{0, 0, 0},
//		{-1, -2, -1}
//	};
//	
//	public ImageProcessor apply(ImageProcessor ip) {
//		Sobel sobelh = new Sobel(horiz);
//		Sobel sobelv = new Sobel(vert);
//		
//		sobelh.apply(ip.duplicate());
//		sobelv.apply(ip.duplicate());
//		
//		for(int i = 0; i < ip.getWidth(); i++) {
//			for(int j = 0; j < ip.getHeight(); j++) {
//				int sobh = sobelh.getPixels()[i][j];
//				int sobv = sobelv.getPixels()[i][j];
//				ip.putPixel(i, j, (int) Math.sqrt(sobh*sobh + sobv*sobv));
//			}
//		}
//	}
//	
//}
