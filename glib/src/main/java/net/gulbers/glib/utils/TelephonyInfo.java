package net.gulbers.glib.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;

import java.lang.reflect.Method;

public final class TelephonyInfo {

    public static final String TAG = TelephonyInfo.class.getName();

    private static TelephonyInfo telephonyInfo;

    private String imeiSIM1;
    private String imeiSIM2;
    private String operatorSIM1;
    private String operatorSIM2;

    /**
     * use TelephonyInfo.getInstance() instead
     */
    private TelephonyInfo() {
    }

    /**
     * Get telephony info IMEI & Operator SIM card
     *
     * @param context application context
     * @param isDebug boolean debug value
     * @return TelephonyInfo instance
     */
    @SuppressWarnings("deprecation")
    @SuppressLint("HardwareIds")
    public static TelephonyInfo getInstance(Context context, boolean isDebug) {
        if (telephonyInfo == null) {
            telephonyInfo = new TelephonyInfo();

            TelephonyManager telephonyManager = ((TelephonyManager) context.getSystemService
                    (Context.TELEPHONY_SERVICE));
            if (telephonyManager == null) return null;

            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return null;
            }
            telephonyInfo.imeiSIM1 = telephonyManager.getDeviceId();
            telephonyInfo.imeiSIM2 = null;
            telephonyInfo.operatorSIM1 = telephonyManager.getSimOperatorName();
            telephonyInfo.operatorSIM2 = null;

            // get IMEI 1
            if (telephonyInfo.imeiSIM1 == null || telephonyInfo.imeiSIM1.length() == 0) {
                try {
                    telephonyInfo.imeiSIM1 = getDeviceIdBySlot(context, "getDeviceIdGemini", 0);
                } catch (GeminiMethodNotFoundException e) {
                    Debug.w(isDebug, TAG, "getDeviceIdGemini 0: " + e.getMessage());
                }
            }
            if (telephonyInfo.imeiSIM1 == null || telephonyInfo.imeiSIM1.length() == 0) {
                try {
                    telephonyInfo.imeiSIM1 = getDeviceIdBySlot(context, "getDeviceId", 0);
                } catch (GeminiMethodNotFoundException e) {
                    Debug.w(isDebug, TAG, "getDeviceId 0: " + e.getMessage());
                }
            }
            if (telephonyInfo.imeiSIM1 == null || telephonyInfo.imeiSIM1.length() == 0) {
                try {
                    telephonyInfo.imeiSIM1 = getDeviceIdBySlot(context, "getDeviceIdDs", 0);
                } catch (GeminiMethodNotFoundException e) {
                    Debug.w(isDebug, TAG, "getDeviceIdDs 0: " + e.getMessage());
                }
            }
            if (telephonyInfo.imeiSIM1 == null || telephonyInfo.imeiSIM1.length() == 0) {
                try {
                    telephonyInfo.imeiSIM1 = getDeviceIdBySlot(context,
                            "getSimSerialNumberGemini", 0);
                } catch (GeminiMethodNotFoundException e) {
                    Debug.w(isDebug, TAG, "getSimSerialNumberGemini 0: " + e.getMessage());
                }
            }

            // get IMEI 2
            if (telephonyInfo.imeiSIM2 == null || telephonyInfo.imeiSIM2.length() == 0) {
                try {
                    telephonyInfo.imeiSIM2 = getDeviceIdBySlot(context, "getDeviceIdGemini", 1);
                } catch (GeminiMethodNotFoundException e) {
                    Debug.w(isDebug, TAG, "getDeviceIdGemini 1: " + e.getMessage());
                }
            }
            if (telephonyInfo.imeiSIM2 == null || telephonyInfo.imeiSIM2.length() == 0) {
                try {
                    telephonyInfo.imeiSIM2 = getDeviceIdBySlot(context, "getDeviceId", 1);
                } catch (GeminiMethodNotFoundException e) {
                    Debug.w(isDebug, TAG, "getDeviceId 1: " + e.getMessage());
                }
            }
            if (telephonyInfo.imeiSIM2 == null || telephonyInfo.imeiSIM2.length() == 0) {
                try {
                    telephonyInfo.imeiSIM2 = getDeviceIdBySlot(context, "getDeviceIdDs", 1);
                } catch (GeminiMethodNotFoundException e) {
                    Debug.w(isDebug, TAG, "getDeviceIdDs 1: " + e.getMessage());
                }
            }
            if (telephonyInfo.imeiSIM2 == null || telephonyInfo.imeiSIM2.length() == 0) {
                try {
                    telephonyInfo.imeiSIM2 = getDeviceIdBySlot(context,
                            "getSimSerialNumberGemini", 1);
                } catch (GeminiMethodNotFoundException e) {
                    Debug.w(isDebug, TAG, "getSimSerialNumberGemini 1: " + e.getMessage());
                }
            }

            // get OPERATOR SIM 1
            if (telephonyInfo.operatorSIM1 == null || telephonyInfo.operatorSIM1.length() == 0) {
                try {
                    telephonyInfo.operatorSIM1 = getDeviceIdBySlot(context,
                            "getSimOperatorNameGemini", 0);
                } catch (GeminiMethodNotFoundException e) {
                    Debug.w(isDebug, TAG, "getSimOperatorNameGemini 0: " + e.getMessage());
                }
            }
            if (telephonyInfo.operatorSIM1 == null || telephonyInfo.operatorSIM1.length() == 0) {
                try {
                    telephonyInfo.operatorSIM1 = getDeviceIdBySlot(context,
                            "getNetworkOperatorNameGemini", 0);
                } catch (GeminiMethodNotFoundException e) {
                    Debug.w(isDebug, TAG, "getNetworkOperatorNameGemini 0: " + e.getMessage());
                }
            }
            if (telephonyInfo.operatorSIM1 == null || telephonyInfo.operatorSIM1.length() == 0) {
                try {
                    telephonyInfo.operatorSIM1 = getDeviceIdBySlot(context,
                            "getNetworkOperatorName", 0);
                } catch (GeminiMethodNotFoundException e) {
                    Debug.w(isDebug, TAG, "getNetworkOperatorName 0: " + e.getMessage());
                }
            }
            if (telephonyInfo.operatorSIM1 == null || telephonyInfo.operatorSIM1.length() == 0) {
                try {
                    telephonyInfo.operatorSIM1 = getDeviceIdBySlot(context, "getSimOperatorName",
                            0);
                } catch (GeminiMethodNotFoundException e) {
                    Debug.w(isDebug, TAG, "getSimOperatorName 0: " + e.getMessage());
                }
            }

            // get OPERATOR SIM 2
            if (telephonyInfo.operatorSIM2 == null || telephonyInfo.operatorSIM2.length() == 0) {
                try {
                    telephonyInfo.operatorSIM2 = getDeviceIdBySlot(context,
                            "getSimOperatorNameGemini", 1);
                } catch (GeminiMethodNotFoundException e) {
                    Debug.w(isDebug, TAG, "getSimOperatorNameGemini 1: " + e.getMessage());
                }
            }
            if (telephonyInfo.operatorSIM2 == null || telephonyInfo.operatorSIM2.length() == 0) {
                try {
                    telephonyInfo.operatorSIM2 = getDeviceIdBySlot(context,
                            "getNetworkOperatorNameGemini", 1);
                } catch (GeminiMethodNotFoundException e) {
                    Debug.w(isDebug, TAG, "getNetworkOperatorNameGemini 1: " + e.getMessage());
                }
            }
            if (telephonyInfo.operatorSIM2 == null || telephonyInfo.operatorSIM2.length() == 0) {
                try {
                    telephonyInfo.operatorSIM2 = getDeviceIdBySlot(context,
                            "getNetworkOperatorName", 1);
                } catch (GeminiMethodNotFoundException e) {
                    Debug.w(isDebug, TAG, "getNetworkOperatorName 1: " + e.getMessage());
                }
            }
            if (telephonyInfo.operatorSIM2 == null || telephonyInfo.operatorSIM2.length() == 0) {
                try {
                    telephonyInfo.operatorSIM2 = getDeviceIdBySlot(context, "getSimOperatorName",
                            1);
                } catch (GeminiMethodNotFoundException e) {
                    Debug.w(isDebug, TAG, "getSimOperatorName 1: " + e.getMessage());
                }
            }
        }

        Debug.i(isDebug, TAG, "IMEI 1: " + telephonyInfo.imeiSIM1 + ", IMEI 2: " + telephonyInfo.imeiSIM2);

        return telephonyInfo;
    }

    private static String getDeviceIdBySlot(Context context, String predictedMethodName, int
            slotID) throws GeminiMethodNotFoundException {

        String imei = null;

        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context
                .TELEPHONY_SERVICE);

        try {
            assert telephony != null;
            Class<?> telephonyClass = Class.forName(telephony.getClass().getName());

            // Class noparams[] = {};
            Class<?>[] parameter = new Class[1];
            parameter[0] = int.class;
            Method getSimID = telephonyClass.getMethod(predictedMethodName, parameter);

            Object[] obParameter = new Object[1];
            obParameter[0] = slotID;
            Object ob_phone = getSimID.invoke(telephony, obParameter);

            if (ob_phone != null) {
                imei = ob_phone.toString();
            }
        } catch (Exception e) {
            throw new GeminiMethodNotFoundException(e.getMessage());
        }

        return imei;
    }

    /**
     * @return IMEI sim 1
     */
    public String getImeiSIM1() {
        return imeiSIM1;
    }

    /**
     * @return IMEI sim 2
     */
    public String getImeiSIM2() {
        return imeiSIM2;
    }

    /**
     * @return dual sim or single
     */
    public boolean isDualSIM() {
        return imeiSIM2 != null;
    }

    /**
     * @return operator name Sim 1
     */
    public String getOperatorSim1() {
        return operatorSIM1;
    }

    /**
     * @return operator name Sim 2
     */
    public String getOperatorSim2() {
        return operatorSIM2;
    }

    private static class GeminiMethodNotFoundException extends Exception {

        private static final long serialVersionUID = -996812356902545308L;

        public GeminiMethodNotFoundException(String info) {
            super(info);
        }
    }
}