

package controller;

public class CheckTF {

	public static boolean isEmpty(String input) {
		boolean isValid = false;
		if (input.isEmpty() || input.isBlank() || input == null) {
			isValid = true;

		}
		return isValid;
	}

	public static boolean isInteger(String input) {
		boolean isValid = false;
		try {
			Integer i = Integer.parseInt(input);
			isValid = true;
		} catch (Exception ex) {
			isValid = false;
		}
		return isValid;
	}

	public static boolean isDouble(String input) {
		boolean isValid = false;
		try {
			double d = Double.parseDouble(input);
			isValid = true;
		} catch (Exception ex) {
			isValid = false;
		}
		return isValid;
	}
	
}
