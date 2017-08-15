package com.musk.business.filedown;

public interface DownCallback {
	public void finish(String downloadUrl, String path);
	public void error(String downloadUrl);
	public void downloading(String downloadUrl, int size);
}
