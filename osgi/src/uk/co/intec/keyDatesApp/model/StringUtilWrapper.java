package uk.co.intec.keyDatesApp.model;

import com.ibm.commons.util.StringUtil;

public class StringUtilWrapper {

	public StringUtilWrapper() {

	}

	public static boolean isNotEmpty(String testVal) {
		return StringUtil.isNotEmpty(testVal);
	}

	public static boolean equals(String firstVal, String secondVal) {
		return StringUtil.equals(firstVal, secondVal);
	}

}
