package com.kancy;

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

	private String getPrefixArgument(String paramName, String s) {
		String booleanValue = null;
		if (p.containsKey(paramName)) {
			booleanValue = this.p.getProperty(paramName, s);
		} else {
			booleanValue = this.p.getProperty(PREFIX + paramName, s);
		}
		return booleanValue;
	}

	private void init() {
		this.p = new Properties();
		if ((this.args == null) || (this.args.length == 0)){
			return;
		}
		for (String param : this.args){
			if (!check(param)) {
				String[] kvs = param.split("=", 2);
				this.p.put(kvs[0], kvs[1]);
			}
		}

	}

	private boolean check(String param) {
		if ((param == null) || ("".equals(param))) {
			return true;
		}
		return !param.contains("=");
	}
}
