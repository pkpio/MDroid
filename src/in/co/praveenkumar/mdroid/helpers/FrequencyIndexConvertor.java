package in.co.praveenkumar.mdroid.helpers;

public class FrequencyIndexConvertor {
	public static int getIndex(int value) {
		int index = 0;
		switch (value) {
		case 1:
			index = 0;
			break;
		case 2:
			index = 1;
			break;
		case 4:
			index = 2;
			break;
		case 6:
			index = 3;
			break;
		case 8:
			index = 4;
			break;
		case 12:
			index = 5;
			break;
		case 24:
			index = 6;
			break;
		case 48:
			index = 7;
			break;
		case 168:
			index = 8;
			break;
		default:
			break;
		}

		return index;
	}

	public static int getValue(int index) {
		int value = 1;
		switch (index) {
		case 0:
			value = 1;
			break;
		case 1:
			value = 2;
			break;
		case 2:
			value = 4;
			break;
		case 3:
			value = 6;
			break;
		case 4:
			value = 8;
			break;
		case 5:
			value = 12;
			break;
		case 6:
			value = 24;
			break;
		case 7:
			value = 48;
			break;
		case 8:
			value = 168;
			break;
		default:
			break;
		}

		return value;
	}

}
