package uk.co.intec.keyDatesApp.model;

import com.ibm.commons.util.StringUtil;

/**
 * @author Paul Withers<br/>
 *         <br/>
 *         String Utils, a wrapper to allow us to easily replace this class
 *         (which uses com.ibm.commons.util.StringUtil) with a non-proprietary
 *         alternative (e.g. Apache Commons StringUtils).
 */
public class StringUtilWrapper {

	/**
	 * Constructor
	 */
	public StringUtilWrapper() {

	}

	/**
	 * Checks whether a valule is an empty string
	 *
	 * @param testVal
	 *            String value to test
	 * @return boolean whether the value is empty or not
	 */
	public static boolean isNotEmpty(String testVal) {
		return StringUtil.isNotEmpty(testVal);
	}

	/**
	 * Checks whether two strings are equal, handling potential nulls
	 *
	 * @param firstVal
	 *            String first value to compare
	 * @param secondVal
	 *            String second value to compare
	 * @return boolean whether the two strings are the same text
	 */
	public static boolean equals(String firstVal, String secondVal) {
		return StringUtil.equals(firstVal, secondVal);
	}

}
