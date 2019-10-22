package com.tik.android.component.libcommon;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

/**
 * @describe :电话号码格式处理和合法性判断
 * @usage :
 * <p>
 * </p>
 * Created by baowei on 2018/11/26.
 */
public class PhoneNumberUtils {

    private static final PhoneNumberUtil PHONE_NUMBER_UTIL = PhoneNumberUtil.getInstance();
    private static final String PREFIX_COUNTRY_CODE = "+";

    private PhoneNumberUtils() {
    }

    private static Phonenumber.PhoneNumber parseCellphone(String cellphone) {
        Phonenumber.PhoneNumber phoneNumber;
        try {
            phoneNumber = PHONE_NUMBER_UTIL.parse(cellphone, null);
            if (!PHONE_NUMBER_UTIL.isValidNumber(phoneNumber)) {
                return null;
            }

            return phoneNumber;
        } catch (NumberParseException e) {
            LogUtil.i("parseCellphone error:" + e.toString() + "\t\tcellphone:" + cellphone);
            return null;
        }
    }

    /**
     * 获取国家码
     */
    public static String getNationalCode(String cellphone) {
        Phonenumber.PhoneNumber pn = parseCellphone(cellphone);
        return PREFIX_COUNTRY_CODE + pn.getCountryCode();
    }

    /**
     * 获取不加国家码的手机号
     */
    public static String getNationalNumber(String cellphone) {
        return PHONE_NUMBER_UTIL.getNationalSignificantNumber(parseCellphone(cellphone));
    }

    /**
     * 判断是否是有效手机号码
     * 单独做了+86手机号处理
     */
    public static boolean isValidPhoneNumber(String cellphone) {
        try {
            String code = getNationalCode(cellphone);
            String number = getNationalNumber(cellphone);
            boolean numberInvalid = (number == null || (number.length() != 11));
            //如果是中国手机号码，并且号码位数非11位，则为无效号码
            if ("+86".equals(code) && numberInvalid) {
                return false;
            } else {
                return PHONE_NUMBER_UTIL.isValidNumber(parseCellphone(cellphone));
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取标准手机号格式：eg. +86 18700000000 (+国家码+空格+手机号)
     *
     * @param cellphone
     * @return
     */
    public static String formatCellphone(String cellphone) {
        Phonenumber.PhoneNumber pn = parseCellphone(cellphone);
        return PREFIX_COUNTRY_CODE + pn.getCountryCode() + " " + PHONE_NUMBER_UTIL.getNationalSignificantNumber(pn);
    }

    /**
     * 根据国家代码和手机号  判断手机号是否有效
     *
     * @param phoneNumber
     * @param countryCode
     * @return
     */
    public static boolean isValidPhoneNumber(String phoneNumber, Integer countryCode) {
        try {
            long phone = Long.parseLong(phoneNumber);

            Phonenumber.PhoneNumber pn = new Phonenumber.PhoneNumber();
            pn.setCountryCode(countryCode);
            pn.setNationalNumber(phone);

            return PHONE_NUMBER_UTIL.isValidNumber(pn);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
