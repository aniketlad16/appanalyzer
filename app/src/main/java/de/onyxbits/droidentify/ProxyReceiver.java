package de.onyxbits.droidentify;

import java.io.File;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.webkit.WebView;

import com.appanalyzer.R;

class ProxyReceiver extends BroadcastReceiver {

	private Result resultObj ;

	public ProxyReceiver(Context m) {
		this.resultObj = (Result) m;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		WebView wv = (WebView) resultObj.findViewById(R.id.webview);
		File target = new File(new File(resultObj.getFilesDir(), MainService.HTDOCS),
				"index.html");
		wv.loadUrl(target.toURI().toString());
	}
}
