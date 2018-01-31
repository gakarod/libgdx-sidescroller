package com.pointless;

/**
 * Created by vaibh on 8/2/2017.
 */

public interface AdHandler {
    public void showAds(boolean show) ;
    public boolean isWifiConnected();
    public void showInterstitialAd (Runnable then);
}
