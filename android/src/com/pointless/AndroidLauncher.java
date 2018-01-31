package com.pointless;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class AndroidLauncher extends AndroidApplication implements AdHandler {
	private static final String TAG = "AndroidLauncher";
	private final int SHOW_ADS = 1;
	private final int HIDE_ADS = 0;
	protected AdView adView;
    private InterstitialAd interstitialAd;

	Handler handler = new Handler(){



		@Override

		public void handleMessage(Message msg) {
			switch(msg.what){

				case SHOW_ADS:

					adView.setVisibility(View.VISIBLE);
					break;

				case HIDE_ADS:

					adView.setVisibility(View.GONE);
					break;

			}

		}

	};



	@Override

	protected void onCreate (Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		RelativeLayout layout = new RelativeLayout(this);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		View gameView = initializeForView(new FlappyImpact(this), config);
		layout.addView(gameView);

		adView = new AdView(this);
		adView.setBackgroundColor(Color.TRANSPARENT);
		adView.setAdListener(new AdListener() {

			@Override

			public void onAdLoaded() {

				int visibility = adView.getVisibility();
				adView.setVisibility(AdView.GONE);
				adView.setVisibility(visibility);
				Log.i(TAG, "Ad Loaded...");

			}

		});



		interstitialAd = new InterstitialAd(this);
		interstitialAd.setAdUnitId("ca-app-pub-6306276344364522/6979706827");

		AdRequest.Builder builder1 = new AdRequest.Builder();
		AdRequest ad = builder1.build();
		interstitialAd.loadAd(ad);

		adView.setAdSize(AdSize.SMART_BANNER);

		//http://www.google.com/admob

		adView.setAdUnitId("ca-app-pub-6306276344364522/8102443196");



		AdRequest.Builder builder = new AdRequest.Builder();

		//run once before uncommenting the following line. Get TEST device ID from the logcat logs.


		//builder.addTestDevice(AdId);

		RelativeLayout.LayoutParams adParams = new RelativeLayout.LayoutParams(

				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT

		);



		layout.addView(adView, adParams);

		adView.loadAd(builder.build());
		setContentView(layout);

	}

	public void showInterstitialAd(final Runnable then) {
     runOnUiThread(new Runnable() {

     public void run() {
     if (then != null) {
        interstitialAd.setAdListener(new AdListener() {

        public void onAdClosed() {

	        Gdx.app.postRunnable(then);
            AdRequest.Builder builder = new AdRequest.Builder();
            AdRequest ad = builder.build();
            interstitialAd.loadAd(ad);
}
});
}
interstitialAd.show();
}
});
}

	@Override

	public void showAds(boolean show) {

		handler.sendEmptyMessage(show ? SHOW_ADS : HIDE_ADS);

	}

	@Override
	public boolean isWifiConnected() {

		return (true);
	}

}

