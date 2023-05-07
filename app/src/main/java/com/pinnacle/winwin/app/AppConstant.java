package com.pinnacle.winwin.app;

import com.pinnacle.winwin.network.model.CountryCodeData;

import java.util.ArrayList;

public class AppConstant {

    public static final int SPLASH_DELAY_TIME = 2000;

    public static final int TIME_OUT_PERIOD = 2;

    /*Preference Constants*/
    public static final String KEY_IS_LOGIN = "key_is_login";
    public static final String KEY_USER_FIRST_NAME = "key_user_first_name";
    public static final String KEY_USER_LAST_NAME = "key_user_last_name";
    public static final String KEY_USER_MOBILE_NO = "key_user_mobile_no";
    public static final String KEY_USER_POINTS = "key_user_points";
    public static final String KEY_USER_CUST_ID = "key_user_cust_id";
    public static final String KEY_USER_TOKEN = "key_user_token";
    public static final String KEY_USER_ADMIN_ID = "key_user_admin_id";
    public static final String KEY_USER_ADMIN_NAME = "key_user_admin_name";
    public static final String KEY_USER_ADMIN_MOBILE_NO = "key_user_admin_mobile_no";
    public static final String KEY_IMEI_NUMBER = "key_imei_number";
    public static final String KEY_SERVER_DB_VERSION = "KEY_SERVER_DB_VERSION";
    public static final String KEY_APP_VERSION = "key_app_version";
    public static final String KEY_USER_PROFILE_IMAGE = "key_user_profile_image";
    public static final String KEY_USER_IS_VERIFIED = "key_user_is_verified";
    public static final String KEY_USER_ACCOUNT_NUMBER = "key_user_account_number";
    public static final String KEY_USER_IFSC_CODE = "key_user_ifsc_code";
    public static final String KEY_USER_BANK_NAME = "key_user_bank_name";
    public static final String KEY_USER_ACCOUNT_NAME = "key_user_account_name";
    public static final String KEY_USER_ACCOUNT_TYPE = "key_user_account_type";
    public static final String KEY_USER_BRANCH_NAME = "key_user_branch_name";
    public static final String KEY_USER_IS_KYC_COMPLETED = "key_user_is_kyc_completed";
    public static final String KEY_MIN_RECHARGE = "key_min_recharge";
    public static final String KEY_MAX_RECHARGE = "key_max_recharge";

    public static final int NO_FRAGMENT_TYPE = -1;
    public static final int FRAGMENT_TYPE_VERIFY_OTP = 1;
    public static final int FRAGMENT_TYPE_NEW_PASSWORD = 2;

    /*Type of Baazaars*/
    public static final int BAAZAAR_TYPE_KALYAN_OPEN = 1;
    public static final int BAAZAAR_TYPE_KALYAN_CLOSE = 2;
    public static final int BAAZAAR_TYPE_MAIN_OPEN = 3;
    public static final int BAAZAAR_TYPE_MAIN_CLOSE = 4;
    public static final int BAAZAAR_TYPE_TIME_OPEN = 5;
    public static final int BAAZAAR_TYPE_TIME_CLOSE = 6;
    public static final int BAAZAAR_TYPE_MILAN_DAY_OPEN = 7;
    public static final int BAAZAAR_TYPE_MILAN_DAY_CLOSE = 8;
    public static final int BAAZAAR_TYPE_MILAN_NIGHT_OPEN = 9;
    public static final int BAAZAAR_TYPE_MILAN_NIGHT_CLOSE = 10;
    public static final int BAAZAAR_TYPE_KAATA_CHAAPA = 5;

    /*Type of Games*/
    public static final int GAME_TYPE_SINGLE = 1;
    public static final int GAME_TYPE_PAANA = 2;
    public static final int GAME_TYPE_CP = 3;
    public static final int GAME_TYPE_MOTOR = 4;
    public static final int GAME_TYPE_BRACKET = 5;
    public static final int GAME_TYPE_PAANA_NUMBER = 8;
    public static final int GAME_TYPE_CHART = 6;
    public static final int GAME_TYPE_COMMON = 7;
    public static final int GAME_TYPE_ALL = -1;

    public static final int TEMP_WALLET_BALANCE = 10000;

    public static final String APP_DB_NAME = "ASOnlineDB.db";

    /*API REQUEST TYPES*/
    public static final int REQ_API_TYPE_GET_MASTER_DATA = 1;
    public static final int REQ_API_TYPE_USER_LOGIN = 2;
    public static final int REQ_API_TYPE_GENERATE_OTP = 3;
    public static final int REQ_API_TYPE_VALIDATE_OTP = 4;
    public static final int REQ_API_TYPE_CHANGE_PASSWORD = 5;
    public static final int REQ_API_TYPE_ADD_NEW_BET = 6;
    public static final int REQ_API_TYPE_GET_BET_HISTORY = 7;
    public static final int REQ_API_TYPE_GET_INITIAL_DATA = 8;
    public static final int REQ_API_TYPE_GET_BAAZAAR_REMAINING_TIME = 9;
    public static final int REQ_API_TYPE_GET_WALLET_BALANCE = 10;
    public static final int REQ_API_TYPE_CLAIM_BET = 11;
    public static final int REQ_API_TYPE_FORGOT_PASSWORD = 12;
    public static final int REQ_API_TYPE_GET_CUSTOMER_TRANSACTIONS = 13;
    public static final int REQ_API_TYPE_GET_BAAZAAR_HISTORY = 14;
    public static final int REQ_API_TYPE_UPDATE_PROFILE_IMAGE = 15;
    public static final int REQ_API_TYPE_UPDATE_CUSTOMER_NAME = 16;
    public static final int REQ_API_TYPE_GET_HT_INITIAL_DATA = 17;
    public static final int REQ_API_TYPE_ADD_HT_NEW_BET = 18;
    public static final int REQ_API_TYPE_GET_HT_RESULT = 19;
    public static final int REQ_API_TYPE_CANCEL_HT_BET = 20;
    public static final int REQ_API_TYPE_GET_HT_BET_HISTORY = 21;
    public static final int REQ_API_TYPE_USER_SIGN_UP = 22;
    public static final int REQ_API_TYPE_SEND_MESSAGE = 23;
    public static final int REQ_API_TYPE_GET_CHAT_THREAD = 24;
    public static final int REQ_API_TYPE_WALLET_RECHARGE = 25;
    public static final int REQ_API_TYPE_UPDATE_KYC = 26;
    public static final int REQ_API_TYPE_GET_RECHARGE_HISTORY = 27;
    public static final int REQ_API_TYPE_GET_BANK_DETAILS = 28;
    public static final int REQ_API_TYPE_WITHDRAW_POINTS = 29;
    public static final int REQ_API_TYPE_CANCEL_WITHDRAW_POINTS = 30;
    public static final int REQ_API_TYPE_GET_WITHDRAW_HISTORY = 31;
    public static final int REQ_API_TYPE_GET_COUNTRY_CODE_LIST = 32;
    public static final int REQ_API_TYPE_SEND_UPI_PAYMENT_STATUS = 33;
    public static final int REQ_API_TYPE_GET_LAST_10_UPI_PAYMENT_TRANSACTION = 34;

    public static final String KEY_LOGIN_REQUEST = "key_login_request";
    public static final String KEY_GENERATE_OTP_REQUEST = "key_generate_otp_request";
    public static final String KEY_VALIDATE_OTP_REQUEST = "key_validate_otp_request";
    public static final String KEY_OTP_DATA = "key_otp_data";
    public static final String KEY_OLD_PASSWORD = "key_old_password";
    public static final String KEY_MOBILE_NUMBER = "key_mobile_number";
    public static final String KEY_CHANGE_PASSWORD_REQUEST = "key_change_password_request";
    public static final String KEY_CHANGE_PASSWORD_TYPE = "key_change_password_type";
    public static final String KEY_ADD_NEW_BET_REQUEST = "key_change_password_request";
    public static final String KEY_BAAZAAR_ID = "key_baazaar_id";
    public static final String KEY_GAME_ID = "key_game_id";
    public static final String KEY_GET_BET_HISTORY_REQUEST = "key_get_bet_history_request";
    public static final String KEY_SELECTED_PAANA_NUMBER = "key_selected_paana_number";
    public static final String KEY_SCREEN_TYPE = "key_screen_type";
    public static final String KEY_FINAL_NUMBER = "key_final_number";
    public static final String KEY_CLAIM_BET_REQUEST = "key_claim_bet_request";
    public static final String KEY_DUAL_BTN_DIALOG_MSG = "key_dual_btn_dialog_msg";
    public static final String KEY_SINGLE_BTN_DIALOG_MSG = "key_single_btn_dialog_msg";
    public static final String KEY_DIALOG_TITLE = "key_dialog_title";
    public static final String KEY_DIALOG_TYPE = "key_dialog_type";
    public static final String KEY_GAME_MAP = "key_game_map";
    public static final String KEY_FORGOT_PASSWORD_REQUEST = "key_forgot_password_request";
    public static final String KEY_GET_CUSTOMER_TRANSACTIONS_REQUEST = "key_get_customer_transactions_request";
    public static final String KEY_BET_HISTORY_DATA = "KEY_BET_HISTORY_DATA";
    public static final String KEY_LAST_RESULT = "key_last_result";
    public static final String KEY_DIALOG_LIST = "key_dialog_list";
    public static final String KEY_UPDATE_PROFILE_IMAGE_REQUEST = "key_update_profile_image_request";
    public static final String KEY_BET_HISTORY_CLAIM_DATA = "key_bet_history_claim_data";
    public static final String KEY_UPDATE_CUSTOMER_NAME_REQUEST = "key_update_customer_name_request";
    public static final String KEY_MATKA_INITIAL_REQUEST = "key_matka_initial_request";
    public static final String KEY_BAAZAAR_REMAINING_TIME_REQUEST = "key_baazaar_remaining_time_request";
    public static final String KEY_GET_WALLET_BALANCE_REQUEST = "key_get_wallet_balance_request";
    public static final String KEY_CHART_TAB_NUMBER = "key_chart_tab_number";
    public static final String KEY_CHART_ITEM_CLICKABLE = "key_chart_item_clickable";
    public static final String KEY_PAANA_NUMBER_LIST = "key_paana_number_list";
    public static final String KEY_PAANA_TYPE_GROUP_LIST = "key_paana_type_group_list";
    public static final String KEY_HT_INITIAL_REQUEST = "key_ht_initial_request";
    public static final String KEY_ADD_HT_NEW_BET_REQUEST = "key_add_ht_new_bet_request";
    public static final String KEY_HT_RESULT_REQUEST = "key_ht_result_request";
    public static final String KEY_CANCEL_HT_BET_REQUEST = "key_cancel_ht_bet_request";
    public static final String KEY_GET_HT_BET_HISTORY_REQUEST = "key_get_ht_bet_history_request";
    public static final String KEY_GET_BAAZAAR_HISTORY_REQUEST = "key_get_baazaar_history_request";
    public static final String KEY_HT_RESULT_DATA = "key_ht_result_data";
    public static final String KEY_BONUS_AMOUNT = "key_bonus_amount";
    public static final String KEY_SIGN_UP_REQUEST = "key_sign_up_request";
    public static final String KEY_SEND_MESSAGE_REQUEST = "key_send_message_request";
    public static final String KEY_GET_CHAT_THREAD_REQUEST = "key_get_chat_thread_request";
    public static final String KEY_WALLET_RECHARGE_REQUEST = "key_wallet_recharge_request";
    public static final String KEY_BILL_ID = "key_bill_id";
    public static final String KEY_UPDATE_KYC_REQUEST = "key_update_kyc_request";
    public static final String KEY_GET_RECHARGE_HISTORY_REQUEST = "key_get_recharge_history_request";
    public static final String KEY_GET_BANK_DETAILS_URL = "key_get_bank_details_url";
    public static final String KEY_WITHDRAW_POINTS_REQUEST = "key_withdraw_points_request";
    public static final String KEY_CANCEL_WITHDRAW_POINTS_REQUEST = "key_cancel_withdraw_points_request";
    public static final String KEY_GET_WITHDRAW_HISTORY_REQUEST = "key_cancel_withdraw_points_request";
    public static final String KEY_WITHDRAW_POINTS_STATUS = "key_withdraw_points_status";
    public static final String KEY_RECHARGE_INFO_STATUS = "key_recharge_info_status";
    public static final String KEY_UPI_PAYMENT_INFO_STATUS = "key_upi_payment_info_status";
    public static final String KEY_LAST_10_UPI_PAYMENT_INFO = "key_last_10_upi_payment_info";

    public static final int CHANGE_PASSWORD_IS_FIRST_TIME = 1;
    public static final int CHANGE_PASSWORD_WITHOUT_LOGIN = 2;

    public static final String PAANA_TYPE_SP = "SP";
    public static final String PAANA_TYPE_DP = "DP";
    public static final String PAANA_TYPE_TP = "TP";

    /*BET STATUS*/
    public static final String BET_STATUS_BOOKING_DONE = "Booking_Done";
    public static final String BET_STATUS_NOT_WON = "NOT_WON";
    public static final String BET_STATUS_WON_UNCLAIMED = "WON_UNCLAIMED";
    public static final String BET_STATUS_WON_CLAIMED = "WON_CLAIMED";

    /*NAVIGATION MENU INTENT TYPE*/
    public static final int NAVIGATION_INTENT_PERSONAL_INFO = 1;
    public static final int NAVIGATION_INTENT_TYPE_GAME_HISTORY = 2;
    public static final int NAVIGATION_INTENT_TYPE_BAAZAR_HISTORY = 3;
    public static final int NAVIGATION_INTENT_TYPE_WALLET_BALANCE = 4;
    public static final int NAVIGATION_INTENT_TYPE_SINGLE_GAME_HISTORY = 5;
    public static final int NAVIGATION_INTENT_TYPE_PAANA_GAME_HISTORY = 6;
    public static final int NAVIGATION_INTENT_TYPE_CP_GAME_HISTORY = 7;
    public static final int NAVIGATION_INTENT_TYPE_MOTOR_GAME_HISTORY = 8;
    /*public static final int NAVIGATION_INTENT_TYPE_KAATA_CHAAPA_GAME_HISTORY = 9;*/
    public static final int NAVIGATION_INTENT_TYPE_BRACKET_GAME_HISTORY = 9;
    public static final int NAVIGATION_INTENT_TYPE_ALL_GAMES_HISTORY = 10;
    public static final int NAVIGATION_INTENT_TYPE_ENQUIRY = 11;
    public static final int NAVIGATION_INTENT_TYPE_RECHARGE = 12;
    public static final int NAVIGATION_INTENT_TYPE_WITHDRAWAL = 13;

    /*Type of Table*/
    public static final int TABLE_TYPE_BAAZAAR_HISTORY = 1;
    public static final int TABLE_TYPE_WALLET_TRANSACTION = 2;

    /*ACTIVITY RESULT REQUEST CODES*/
    public static final int REQ_CODE_APP_SETTINGS = 100;

    /*ACCESS TOKEN*/
    public static final String ACCESS_TOKEN = "U2FsdGVkX1+0Xz8jXqfb6iNn";

    /*DIALOG TAGS*/
    public static final String DIALOG_TAG_APP_EXIT = "AppExit";
    public static final String DIALOG_TAG_LOG_OUT = "LogOut";
    public static final String DIALOG_TAG_BET_TIME_EXPIRED = "BetTimeExpired";
    public static final String DIALOG_TAG_EDIT_NAME = "EditName";
    public static final String DIALOG_TAG_SELECT_IMAGE = "SelectImage";
    public static final String DIALOG_TAG_DOWNLOAD_APP_UPDATE = "AppUpdate";
    public static final String DIALOG_TAG_APP_UPDATE_INFO = "AppUpdateInfo";
    public static final String DIALOG_TAG_APP_INFO = "AppInfo";
    public static final String DIALOG_TAG_UNAUTHORIZED = "Unauthorized";
    public static final String DIALOG_TAG_CLEAR_BET = "ClearBet";
    public static final String DIALOG_TAG_HT_CONGRATULATIONS = "HTCongratulations";
    public static final String DIALOG_TAG_BONUS = "Bonus";
    public static final String DIALOG_TAG_DOB = "DOB";
    public static final String DIALOG_TAG_KYC_INFO = "KycInfo";
    public static final String DIALOG_TAG_KYC_UPDATED = "KycUpdated";
    public static final String DIALOG_TAG_WITHDRAW_REQUEST = "WithdrawRequest";
    public static final String DIALOG_TAG_RECHARGE_INFO = "RechargeInfo";

    /*Select Image Type*/
    public static final int IMAGE_TYPE_CAMERA = 1;
    public static final int IMAGE_TYPE_GALLERY = 2;

    //ACCOUNT TYPE
    public static final int ACCOUNT_TYPE_SAVINGS = 1;
    public static final int ACCOUNT_TYPE_CURRENT = 2;

    /*DIALOG TYPES*/
    public static final int SELECT_IMAGE_TYPE_DIALOG = 1;
    public static final int SELECT_ACCOUNT_TYPE_DIALOG = 2;

    public static long downloadID;

    /*Maxium Bet Points for Each Game on Each Number*/
    public static final int MAX_SINGLE_GAME_BET_POINTS = 50000;
    public static final int MAX_PAANA_GAME_BET_POINTS = 5000;
    public static final int MAX_CP_GAME_BET_POINTS = 5000;
    public static final int MAX_MOTOR_GAME_BET_POINTS = 5000;
    public static final int MAX_BRACKET_GAME_BET_POINTS = 5000;
    public static final int MAX_HEAD_TAIL_BET_POINTS = 10000;

    /*Common Game Paana Combinations*/
    public static final int COMMON_GAME_SP_COMBINATIONS = 36;
    public static final int COMMON_GAME_DP_COMBINATIONS = 18;
    public static final int COMMON_GAME_TP_COMBINATIONS = 1;
    public static final int COMMON_GAME_SP_DP_TP_COMBINATIONS = 55;
    public static final int COMMON_GAME_SP_DP_COMBINATIONS = 54;
    public static final int COMMON_GAME_SP_TP_COMBINATIONS = 37;
    public static final int COMMON_GAME_DP_TP_COMBINATIONS = 19;

    /*Chart Game Paana Combinations*/
    public static final int CHART_GAME_20_PAANA = 2;
    public static final int CHART_GAME_30_PAANA = 3;
    public static final int CHART_GAME_40_PAANA = 4;
    public static final int CHART_GAME_50_PAANA = 5;
    public static final int CHART_GAME_60_PAANA = 6;
    public static final int CHART_GAME_70_PAANA = 7;

    /*Paana Game Tab Names*/
    public static String PAANA_GAME_INDIVIDUAL_TAB = "PAANA";
    public static String PAANA_GAME_GROUP_TAB = "SP & DP";

    //Generic Types
    public static final int GENERIC_TYPE_GAMES = 1;
    public static final int GENERIC_TYPE_BAAZAAR = 2;

    //HT Bet Type
    public static final String HT_BET_TYPE_HEAD = "H";
    public static final String HT_BET_TYPE_TAIL = "T";

    public static final String IFSC_INFO_URL = "https://ifsc.razorpay.com/";

    //Country Code List
    public static ArrayList<CountryCodeData> countryCodeList;

    public static final String DEFAULT_COUNTRY_CODE = "+91";
}
