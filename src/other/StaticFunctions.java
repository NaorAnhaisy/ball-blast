package other;

public class StaticFunctions {
	
	public static String returnBigNumbersInShortcat(int num) {
		if (num >= 1000 && num < 1000000) {
			return String.valueOf(num / 1000) + "." + String.valueOf(num % 1000 / 100) + "K";
		}
		if (num >= 1000000 && num < 1000000000) {
			return String.valueOf(num / 1000000) + "." + String.valueOf(num % 1000000 / 100000) + "M";
		}
		if (num >= 1000000000) {
			return String.valueOf(num / 1000000000) + "." + String.valueOf(num % 1000000000 / 100000000) + "B";
		}
		return String.valueOf(num);
	}
}
