package org.apache.cordova.plugin;

import android.content.Context;

import android.provider.Settings;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrengthCdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.TelephonyManager;
import android.util.ArrayMap;
import android.util.JsonWriter;
import android.util.Log;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public class SignalStrength extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

        if (action.equals("dbm")) {
            TelephonyManager tm = (TelephonyManager) cordova.getActivity().getSystemService(Context.TELEPHONY_SERVICE);
            List<CellInfo> cellInfoList = tm.getAllCellInfo();
            Map<String, Integer> allDbm = new ArrayMap();
            if (cellInfoList != null) {
                for (final CellInfo info : cellInfoList) {
                    if (info instanceof CellInfoCdma) {
                        final CellSignalStrengthCdma cdma = ((CellInfoCdma) info).getCellSignalStrength();
                        if (info.isRegistered()) {
                            allDbm.put("cdma", cdma.getDbm());
                        }
                    } else if (info instanceof CellInfoGsm) {
                        final CellSignalStrengthGsm gsm = ((CellInfoGsm) info).getCellSignalStrength();
                        if (info.isRegistered()) {
                            allDbm.put("gsm", gsm.getDbm());
                        }
                    } else if (info instanceof CellInfoWcdma) {
                        final CellSignalStrengthWcdma wcdma = ((CellInfoWcdma) info).getCellSignalStrength();
                        if (info.isRegistered()) {
                            allDbm.put("wcdma", wcdma.getDbm());
                        }
                    } else if (info instanceof CellInfoLte) {
                        final CellSignalStrengthLte lte = ((CellInfoLte) info).getCellSignalStrength();
                        if (info.isRegistered()) {
                            allDbm.put("lte", lte.getDbm());
                        }
                    }
                }
            }
            if (allDbm.size() > 0) {
                JSONObject allDbmJson = new JSONObject(allDbm);
                callbackContext.success(allDbmJson);
                return true;
            } else if (isAirplaneModeOn(cordova.getContext())) {
                callbackContext.success(255);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private static boolean isAirplaneModeOn(Context context) {
        return Settings.System.getInt(context.getContentResolver(),
                Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
    }

}
