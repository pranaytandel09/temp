package com.purplebits.emrd2.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonUtils {

	public static Gson createGson() {
		return new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
	}
}
