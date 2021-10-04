package com.github.kancyframework.springx.boot;

import com.github.kancyframework.springx.utils.ObjectUtils;
import com.github.kancyframework.springx.utils.StringUtils;

import java.util.Objects;
import java.util.Properties;

/**
 * CommandLineArgument
 *
 * @author kancy
 * @date 2020/2/16 5:07
 */
public class CommandLineArgument {
	private static final String PREFIX = "--";
	private String[] args;
	private Properties p;

	public CommandLineArgument(String[] args) {
		this.args = args;
		init();
	}
	public <T> T getArgument(String paramName, Class<T> clazz) {
		return ObjectUtils.cast(getArgument(paramName), clazz);
	}

	public <T> T getArgument(String paramName, Class<T> clazz, T def) {
		return ObjectUtils.cast(getArgument(paramName), clazz, def);
	}

	public String getArgument(String paramName) {
        return getPrefixArgument(paramName, "");
    }

    public String getArgument(String paramName, String def) {
        return getPrefixArgument(paramName, def);
    }

    public boolean getArgument(String paramName, boolean def) {
        String booleanValue = getPrefixArgument(paramName, String.valueOf(def));
        return Boolean.parseBoolean(booleanValue);
    }

	public int getArgument(String paramName, int def) {
		String intValue = getPrefixArgument(paramName, String.valueOf(def));
		return Integer.parseInt(intValue);
	}

	public Properties getArguments(){
		return p;
	}

	private String getPrefixArgument(String paramName, String defaultValue) {
		return this.p.getProperty(paramName, this.p.getProperty(PREFIX + paramName, System.getProperty(paramName, defaultValue)));
	}

	private void init() {
		this.p = new Properties();
		if ((this.args == null) || (this.args.length == 0)){
			return;
		}
		for (String param : this.args){
			if (!check(param)) {
				String[] kvs = param.split("=", 2);
				Object key = removePrefix(kvs[0]);
				if (Objects.nonNull(key)){
					this.p.put(key, kvs[1]);
				}
			}
		}
	}

	private Object removePrefix(String key) {
		if (StringUtils.isBlank(key)){
			return null;
		}
		if (key.startsWith(PREFIX) || key.startsWith("-D")){
			return key.substring(2);
		}
		return key;
	}

	private boolean check(String param) {
		if ((param == null) || ("".equals(param))) {
			return true;
		}
		return !param.contains("=");
	}

	@Override
	public String toString() {
		return p.toString();
	}
}
