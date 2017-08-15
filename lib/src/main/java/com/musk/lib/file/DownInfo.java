package com.musk.lib.file;

import java.io.Serializable;

public class DownInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Object pointer;
	public String folder;
	public String filename;
	public String url;

	public int AllSize = 0;
	public int DownloadSize = 0;
	public double percent = 0.0;
	public boolean isFinish = false;
}