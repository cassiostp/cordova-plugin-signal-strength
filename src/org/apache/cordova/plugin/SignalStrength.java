package org.apache.cordova.signalStrength;

import android.content.Context;

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
import android.util.Log;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import org.json.JSONException;

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
                        allDbm.put("cdma", cdma.getDbm());
                    } else if (info instanceof CellInfoGsm) {
                        final CellSignalStrengthGsm gsm = ((CellInfoGsm) info).getCellSignalStrength();
                        allDbm.put("gsm", gsm.getDbm());
                    } else if (info instanceof CellInfoWcdma) {
                        final CellSignalStrengthWcdma wcdma = ((CellInfoWcdma) info).getCellSignalStrength();
                        allDbm.put("wcdma", wcdma.getDbm());
                    } else if (info instanceof CellInfoLte) {
                        final CellSignalStrengthLte lte = ((CellInfoLte) info).getCellSignalStrength();
                        allDbm.put("lte", lte.getDbm());
                    }
                }
            }
            if (allDbm.size() > 0) {
                Log.d("SIGNAL-STRENGTHS", allDbm.toString());
                callbackContext.success((JSONArray) allDbm);
                return true;
            }
            return false;
        }
        return false;
    }

}
