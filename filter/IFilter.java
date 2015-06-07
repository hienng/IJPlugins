package filter;

import ij.process.ImageProcessor;

/*
 * Apply filter to the input.
 * @param input, the input image
 * @param return, result of filtering
 */

public interface IFilter {
	ImageProcessor apply(ImageProcessor input);
}
