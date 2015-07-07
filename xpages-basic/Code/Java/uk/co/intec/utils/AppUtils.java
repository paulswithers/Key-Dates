package uk.co.intec.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AppUtils {

	public static String convertDate(Date passedDate) {
		final SimpleDateFormat DATE_FORMAT_DATE_ONLY = new SimpleDateFormat("dd/MM/yyyy");
		return DATE_FORMAT_DATE_ONLY.format(passedDate);

	}

}
