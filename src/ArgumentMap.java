import java.util.HashMap;
import java.util.Map;

/**
 * Parses command-line arguments into flag/value pairs, and stores those pairs
 * in a map for easy access.
 */
public class ArgumentMap {

	private final Map<String, String> map;

	/**
	 * Initializes the argument map.
	 */
	public ArgumentMap() {
		map = new HashMap<>();
	}

	/**
	 * Initializes the argument map and parses the specified arguments into
	 * key/value pairs.
	 *
	 * @param args
	 *            command line arguments
	 *
	 * @see #parse(String[])
	 */
	public ArgumentMap(String[] args) {
		this();
		parse(args);
	}

	/**
	 * Parses the specified arguments into key/value pairs and adds them to the
	 * argument map.
	 *
	 * @param args
	 *            command line arguments
	 */
	public void parse(String[] args) {
		boolean flag = false;
		boolean value = false;
		for(int i = 0 ; i < args.length ; i++) {
			flag = isFlag(args[i]);
			if((i + 1) != args.length) {
				value = isValue(args[i + 1]);
			}
			if(flag == true && value == true) {
				map.put(args[i], args[i + 1]);
			}
			if(flag == true && value == false) { // TODO else if
				map.put(args[i], null);
			}
		}
	}

	/**
	 *
	 * @param arg
	 * @return
	 */
	public static boolean isFlag(String arg) {
		if(arg == null || arg.length() == 0) {
			return false;
		}
		if(arg.charAt(0) == '-' && arg.length() >= 2 && arg.charAt(1) != ' ') {
			return true;
		} else {
			return false;
		}
		
		// TODO 
//		if (arg == null) {
//			return false;
//		}
//		
//		arg = arg.trim();
//		return arg.length() > 1 && arg.startsWith("-");
	}

	/**
	 *
	 * @param arg
	 * @return
	 */
	public static boolean isValue(String arg) {
		// TODO Same as isflag
		
		if(arg == null || arg.length() == 0) {
			return false;
		}
		if(arg.charAt(0) == '-' || arg.equals(" ") || arg.charAt(0) == '\t') {
			return false;
		}
		return true;
	}

	/**
	 * Returns the number of unique flags stored in the argument map.
	 *
	 * @return number of flags
	 */
	public int numFlags() {
		return map.size();
	}

	/**
	 * Determines whether the specified flag is stored in the argument map.
	 *
	 * @param flag
	 *            flag to test
	 *
	 * @return true if the flag is in the argument map
	 */
	public boolean hasFlag(String flag) {
		if(map.containsKey(flag)) {
			return true;
		} else {
			return false;
		}
		
		// TODO return map.containsKey(flag);
	}

	/**
	 * Determines whether the specified flag is stored in the argument map and
	 * has a non-null value stored with it.
	 *
	 * @param flag
	 *            flag to test
	 *
	 * @return true if the flag is in the argument map and has a non-null value
	 */
	public boolean hasValue(String flag) {
		// TODO Return the condition
		if(map.containsKey(flag) && map.get(flag) != null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Returns the value for the specified flag as a String object.
	 *
	 * @param flag
	 *            flag to get value for
	 *
	 * @return value as a String or null if flag or value was not found
	 */
	public String getString(String flag) {
		// TODO map.get(flag);
		
		if(map.containsKey(flag)) {
			String value = map.get(flag);
			if(value == null) {
				return null;
			}
			return value;
		} else {
			return null;
		}
	}

	/**
	 * Returns the value for the specified flag as a String object. If the flag
	 * is missing or the flag does not have a value, returns the specified
	 * default value instead.
	 *
	 * @param flag
	 *            flag to get value for
	 * @param defaultValue
	 *            value to return if flag or value is missing
	 * @return value of flag as a String, or the default value if the flag or
	 *         value is missing
	 */
	public String getString(String flag, String defaultValue) {
		if(map.containsKey(flag)) {
			String value = map.get(flag);
			if(value == null) {
				return defaultValue;
			}
			return value;
		} else {
			return defaultValue;
		}

		// TODO 
//		if ( map.get(flag) == null) {
//			return defaultValue;
//		}
//		
//		return map.get(flag);
	}

	/**
	 * Returns the value for the specified flag as an int value. If the flag is
	 * missing or the flag does not have a value, returns the specified default
	 * value instead.
	 *
	 * @param flag
	 *            flag to get value for
	 * @param defaultValue
	 *            value to return if the flag or value is missing
	 * @return value of flag as an int, or the default value if the flag or
	 *         value is missing
	 */
	public int getInteger(String flag, int defaultValue) {
		int int_value = defaultValue;
		if(map.containsKey(flag)) {
			String value = map.get(flag);
			if(value == null) {
				return defaultValue;
			} else {
				try {
					int_value = Integer.parseInt(value);
				}
				catch (NumberFormatException e)
				{}
				return int_value;
			}
		}
		return defaultValue;

		// TODO 
//		try {
//			return Integer.parseInt(map.get(flag));
//		}
//		catch (NumberFormatException|NullPointerException e) {
//			return defaultValue;
//		}
	}

	@Override
	public String toString() {
		return map.toString();
	}
}
