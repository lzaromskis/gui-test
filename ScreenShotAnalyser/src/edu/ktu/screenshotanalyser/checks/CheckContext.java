package edu.ktu.screenshotanalyser.checks;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CheckContext {

	private final Map<String, List<ResourceText>> resources = new HashMap<String, List<ResourceText>>();

	public boolean isPlaceholder(String text) {
		if (text == null) {
			return false;
		}
		return this.resources.containsKey(text);
	}

	public static class ResourceText {
		String lang;
		String key;
		String value;
	}
}
