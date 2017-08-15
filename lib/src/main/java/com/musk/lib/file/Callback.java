package com.musk.lib.file;


public interface Callback {
	public void OnSuccess(DownInfo info);

	public void OnDownloading(DownInfo info);

	public void OnFailed(DownInfo info, int error_code);
}
