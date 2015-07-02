package util.neighbourhood.collector;

import java.util.ArrayList;
import java.util.List;
import util.Util;
import ij.process.ImageProcessor;

public class GreyCollector extends NeighbourCollector<List<Integer>> {
	private List<Integer> result;
	
	@Override
	public List<Integer> getResultSet() {
		return result;
	}
	
	@Override
	public void putPixel(ImageProcessor ip, int x, int y) {
		int g = Util.getMedian(getResultSet());
		ip.putPixel(x, y, g);
	}

	@Override
	public void initResultSet(int nsize) {
		result = new ArrayList<Integer>(2*nsize+1);
		
	}

	@Override
	public void addDefault(ImageProcessor ip, int x, int y, int nx, int ny) {
		result.add(0);
	}

	@Override
	public void addToResult(ImageProcessor ip, int x, int y, int nx, int ny) {
		result.add(ip.getPixel(x,y));
	}
}
