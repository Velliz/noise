package org.uiieditt.noise;

/**
 *
 */
public interface ProgressHandler {

	/**
	 *
	 * @param tot
     */
	public void setTotal(int tot);

	/**
	 *
	 * @param inc
     */
	public void increment(int inc);

	/**
	 *
	 */
	public void finished();

}
