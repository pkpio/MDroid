package in.co.praveenkumar.mdroid.helper;

import android.graphics.Color;

public class LetterColor {
	/**
	 * Check the colors visually here,
	 * http://www.neowin.net/images/uploaded/newaccents.png
	 */
	static final String AMBER = "#F0A30A";
	static final String BROWN = "#825A2C";
	static final String COBALT = "#0050EF";
	static final String CRIMSON = "#A20025";
	static final String CYAN = "#1BA1E2";
	static final String MAGENTA = "#F0A30A";
	static final String LIME = "#A4C400";
	static final String INDIGO = "#6A00FF";
	static final String GREEN = "#60A917";
	static final String EMERALD = "#008A00";
	static final String MAUVE = "#76608A";
	static final String OLIVE = "#6D8764";
	static final String ORANGE = "#FA6800";
	static final String PINK = "#F472D0";
	static final String RED = "#E51400";
	static final String SIENNA = "#7A3B3F"; // --
	static final String STEEL = "#647687";
	static final String TEAL = "#00ABA9";
	static final String VIOLET = "#AA00FF";
	static final String YELLOW = "#D8C100";

	/**
	 * These are take from colors.xml
	 */
	static final String BEAUTIFUL_GREEN = "#3db39e";
	static final String BEAUTIFUL_BLUE = "#43adf1";
	static final String BEAUTIFUL_RED = "#e2574c";
	static final String BEAUTIFUL_YELLOW = "#f4b459";
	static final String BEAUTIFUL_GREY = "#777575";
	static final String BEAUTIFUL_DARK_YELLOW = "#d07c40";
	static final String MOODLE_COLOR = "#ff8800";

	public static int ofFirstLetterIn(String name) {
		char firstChar = 0;
		if (name != null)
			if (name.length() != 0)
				firstChar = name.charAt(0);
		return LetterColor.of(firstChar);
	}

	public static int of(char letter) {
		letter = Character.toUpperCase(letter);
		switch (letter) {
		case 'A':
			return Color.parseColor(AMBER);
		case 'B':
			return Color.parseColor(BROWN);
		case 'C':
			return Color.parseColor(COBALT);
		case 'D':
			return Color.parseColor(CRIMSON);
		case 'E':
			return Color.parseColor(CYAN);
		case 'F':
			return Color.parseColor(MAGENTA);
		case 'G':
			return Color.parseColor(LIME);
		case 'H':
			return Color.parseColor(INDIGO);
		case 'I':
			return Color.parseColor(GREEN);
		case 'J':
			return Color.parseColor(BEAUTIFUL_GREEN);
		case 'K':
			return Color.parseColor(MAUVE);
		case 'L':
			return Color.parseColor(OLIVE);
		case 'M':
			return Color.parseColor(ORANGE);
		case 'N':
			return Color.parseColor(PINK);
		case 'O':
			return Color.parseColor(RED);
		case 'P':
			return Color.parseColor(EMERALD);
		case 'Q':
			return Color.parseColor(SIENNA);
		case 'R':
			return Color.parseColor(BEAUTIFUL_BLUE);
		case 'S':
			return Color.parseColor(BEAUTIFUL_RED);
		case 'T':
			return Color.parseColor(BEAUTIFUL_GREY);
		case 'U':
			return Color.parseColor(STEEL);
		case 'V':
			return Color.parseColor(TEAL);
		case 'W':
			return Color.parseColor(VIOLET);
		case 'X':
			return Color.parseColor(YELLOW);
		case 'Y':
			return Color.parseColor(BEAUTIFUL_YELLOW);
		case 'Z':
			return Color.parseColor(BEAUTIFUL_DARK_YELLOW);
		default:
			return Color.parseColor(MOODLE_COLOR);
		}
	}

}
