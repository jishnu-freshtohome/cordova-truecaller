package org.apache.cordova.truecaller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.truecaller.android.sdk.ITrueCallback;
import com.truecaller.android.sdk.TrueError;
import com.truecaller.android.sdk.TrueProfile;
import com.truecaller.android.sdk.TruecallerSDK;
import com.truecaller.android.sdk.TruecallerSdkScope;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * This class echoes a string called from JavaScript.
 */
public class TruecallerPlugin extends CordovaPlugin {

    private TrueProfile profile;
    private CallbackContext callbackContext;
    private PluginResult result;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("checkTruecaller")) {
            this.callbackContext = callbackContext;
            try {
                TruecallerPlugin.this.cordova.getActivity().getApplicationContext().getPackageManager()
                        .getPackageInfo("com.truecaller", PackageManager.GET_ACTIVITIES);
                this.sendResponse("success", "OK");
            } catch (PackageManager.NameNotFoundException e) {
                this.sendResponse("error", "Not installed");
            }
            return true;
        }

        if (action.equals("truecallerInit")) {
            this.callbackContext = callbackContext;
            this.cordova.setActivityResultCallback(this);
            JSONObject options = args.getJSONObject(0);
            this.initTruecaller(options);
            return true;
        }

        if (action.equals("clearSDK")) {
            TruecallerSDK.clear();
            return true;
        }

        return false;
    }

    private void initTruecaller(JSONObject options) {
        this.createTruescope(options);
        try {
            if (TruecallerSDK.getInstance().isUsable()) {
                TruecallerSDK.getInstance().getUserProfile((AppCompatActivity) this.cordova.getActivity());
            } else {
                this.sendResponse("error", "Truecaller is not available on device");
            }
        } catch (Exception e) {
            this.sendResponse("error", e.getMessage());
        }
    }

    private void sendResponse(String status, JSONObject response) {
        if (status.equals("success")) result = new PluginResult(PluginResult.Status.OK, response);
        else if (status.equals("error"))
            result = new PluginResult(PluginResult.Status.ERROR, response);
        else return;
        result.setKeepCallback(true);
        callbackContext.sendPluginResult(result);
    }

    private void sendResponse(String status, String response) {
        if (status.equals("success")) result = new PluginResult(PluginResult.Status.OK, response);
        else if (status.equals("error"))
            result = new PluginResult(PluginResult.Status.ERROR, response);
        else return;
        result.setKeepCallback(true);
        callbackContext.sendPluginResult(result);
    }

    private void createTruescope(JSONObject options) {
        try {
            if (TruecallerSDK.getInstance().isUsable()) return;
        } catch (Exception ignored) {}

        Context context = this.cordova.getContext();
        try {
            TruecallerSdkScope truescope = new TruecallerSdkScope.Builder(context, new sdkCallback())
                    .consentMode(options.getInt("consentMode"))
                    .buttonColor(Color.parseColor(options.getString("buttonColor")))
                    .buttonTextColor(Color.parseColor(options.getString("buttonTextColor")))
                    .loginTextPrefix(options.getInt("loginTextPrefix"))
                    .loginTextSuffix(options.getInt("loginTextSuffix"))
                    .ctaTextPrefix(options.getInt("ctaTextPrefix"))
                    .buttonShapeOptions(options.getInt("buttonShapeOptions"))
                    .privacyPolicyUrl(options.getString("privacyPolicyUrl"))
                    .termsOfServiceUrl(options.getString("termsOfServiceUrl"))
                    .footerType(options.getInt("footerType"))
                    .consentTitleOption(options.getInt("consentTitleOption"))
                    .sdkOptions(options.getInt("sdkOptions"))
                    .build();
            TruecallerSDK.init(truescope);
        } catch (JSONException e) {
            e.printStackTrace();
            TruecallerPlugin.this.sendResponse("error", "createTruescopeFailed: " + e.getMessage());
        }

    }

    private JSONObject getProfile(TrueProfile trueprofile) throws JSONException {
        JSONObject response = new JSONObject();

        response.put("firstName", trueprofile.firstName);
        response.put("lastName", trueprofile.lastName);
        response.put("phoneNumber", trueprofile.phoneNumber);
        response.put("gender", trueprofile.gender);
        response.put("street", trueprofile.street);
        response.put("city", trueprofile.city);
        response.put("zipcode", trueprofile.zipcode);
        response.put("countryCode", trueprofile.countryCode);
        response.put("facebookId", trueprofile.facebookId);
        response.put("twitterId", trueprofile.twitterId);
        response.put("email", trueprofile.email);
        response.put("url", trueprofile.url);
        response.put("avatarUrl", trueprofile.avatarUrl);
        response.put("isTruename", trueprofile.isTrueName);
        response.put("isAmbassador", trueprofile.isAmbassador);
        response.put("companyName", trueprofile.companyName);
        response.put("jobTitle", trueprofile.jobTitle);
        response.put("payload", trueprofile.payload);
        response.put("signature", trueprofile.signature);
        response.put("signatureAlgorithm", trueprofile.signatureAlgorithm);
        response.put("requestNonce", trueprofile.requestNonce);
        response.put("isSimChanged", trueprofile.isSimChanged);
        response.put("verificationMode", trueprofile.verificationMode);
        response.put("verificationTimestamp", trueprofile.verificationTimestamp);
        response.put("userLocale", trueprofile.userLocale);
        response.put("accessToken", trueprofile.accessToken);

        return response;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TruecallerSDK.SHARE_PROFILE_REQUEST_CODE) {
            TruecallerSDK.getInstance().onActivityResultObtained((AppCompatActivity) this.cordova.getActivity(), requestCode, resultCode, data);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        TruecallerSDK.clear();
    }

    private class sdkCallback implements ITrueCallback {
        @Override
        public void onSuccessProfileShared(@NonNull TrueProfile trueProfile) {
            try {
                JSONObject response = TruecallerPlugin.this.getProfile(trueProfile);
                TruecallerPlugin.this.sendResponse("success", response);
            } catch (JSONException e) {
                e.printStackTrace();
                TruecallerPlugin.this.sendResponse("error", e.getMessage());
            }
        }

        @SuppressLint("LongLogTag")
        @Override
        public void onFailureProfileShared(@NonNull TrueError trueError) {
            try {
                Log.e("onFailureProfileShared: ", String.valueOf(trueError.getErrorType()));
                TruecallerPlugin.this.sendResponse("error", String.valueOf(trueError.getErrorType()));
            } catch (Exception e) {
                TruecallerPlugin.this.sendResponse("error", "onFailureProfileShared: " + e.getMessage());
            }
        }

        @SuppressLint("LongLogTag")
        @Override
        public void onVerificationRequired(TrueError trueError) {
            try {
                Log.e("onVerificationRequired: ", String.valueOf(trueError.getErrorType()));
                TruecallerPlugin.this.sendResponse("error", String.valueOf(trueError.getErrorType()));
            } catch (Exception e) {
                TruecallerPlugin.this.sendResponse("error", "onVerificationRequired: " + e.getMessage());
            }
        }
    }
}
