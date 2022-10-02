package edu.ktu.screenshotanalyser.utils.methods;

import java.util.List;

public final class CollectionUtils {
	private CollectionUtils() {

	}

	public static <T extends Comparable<T>> int getMaxIndex(List<T> data) {
		if (data.size() == 0)
			return -1;

		if (data.size() == 1)
			return 0;

		T mx = data.get(0);
		int index = 0;
		for (int i = 1; i < data.size(); i++) {
			T val = data.get(i);
			if (val.compareTo(mx) > 0) {
				mx = val;
				index = i;
			}
		}

		return index;
	}

	public static <T extends Comparable<T>> int getMinIndex(List<T> data) {
		if (data.size() == 0)
			return -1;

		if (data.size() == 1)
			return 0;

		T mn = data.get(0);
		int index = 0;
		for (int i = 1; i < data.size(); i++) {
			T val = data.get(i);
			if (val.compareTo(mn) < 0) {
				mn = val;
				index = i;
			}
		}

		return index;
	}
}
