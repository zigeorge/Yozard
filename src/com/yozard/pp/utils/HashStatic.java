package com.yozard.pp.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Vector;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import com.yozard.pp.model.Coupon;

public class HashStatic {

//	public static int sortByPos = -1;
	public static boolean isDrawerOpen = false;

	// ///for coupon of product details
	public static String counter_details = "counterDetails";
	public static String shouldStopShowingDetails = "stopDialog";

	public static boolean signUp_byEmail = false;

	public static boolean firstTimeLogin = false;
	public static boolean isFavactionOn = false;

	public static boolean isSortingMode_on = false;
	// /SHARED PREFERENCE
	public static final String PREF_NAME_REG = "registration";
	public static final String PROMOI_REG_TOKEN = "PromoRegToken";
	public static final String ACCESS_TOKEN_FB = "token";
	public static final String CUSTOMER_ID = "customer_id";
	public static final String CUSTOMER_STATUS = "customer_status";
	public static final String CUSTOMER_EMAIL = "customer_email";

	public static final String CAMPAIGN_ID = "campaign_id";
	public static final String COMPANY_ID = "company_id";

	public static final String ID = "id";
	public static final String FACEBOOK_PRO_LINK = "image_url";
	public static final String FACEBOOK_auth_LINK = "link";

	public static final String HASH_firstNAME = "firstname ";
	public static final String HASH_LastNAME = "lastname ";
	public static final String HASH_email = "email";
	public static final String HASH_date = "birthday";
	public static final String HASH_gender = "gender";
	public static final String HASH_lastname = "lastname";
	public static final String HASH_password = "password";
	public static final String HASH_country = "country";
	public static final String HASH_baseUrl = "baseUrl";
	public static final String HASH_auth = "auth_type";
	public static final String HASH_currency = "currency";
	public static final String HASH_image_url = "image_url";

	public static final String HASH_isValueset_already = "value_already";
	public static final String HASH_wait_for_network_BR = "network_BR";

	// ////////////Internet////////////////////////////////////
	public static int TYPE_WIFI = 1;
	public static int TYPE_MOBILE = 2;
	public static int TYPE_NOT_CONNECTED = 0;

	// ///////////////PROMO Variable//////////////////////////
	public static final String HASH_PROD_NAME = "prod_name";
	public static final String HASH_PROD_IMAGE = "prod_image";
	public static final String HASH_PROD_PRICE = "prod_price";
	public static final String HASH_PROD_CURRENCY = "prod_currency";
	public static final String HASH_PROD_CASHBACK_SHARE = "prod_cashback_share";
	public static final String HASH_COMP_NAME = "company_name";
	public static final String HASH_ProdId = "prod_id";
	public static final String HASH_EVT_TYPE = "evt_type";

	// //////***Calcualtion related fields***/////////
	public static final String HASH_EVT_PRIZE = "evt_prize";
	public static final String HASH_EVT_WINNER_POT = "evt_winner_pot";
	public static final String HASH_EVT_PROD_PRICE = "evt_prod_price";
	public static final String HASH_OFFER = "offer";
	public static final String HASH_CASHBACK_SHARE = "custom_cashback_share";
	public static final String HASH_WINNER_SHARE = "custom_winner_share";
	public static final String HASH_CSR_SAHRE = "custom_csr_share";
	public static final String HASH_EVT_CSR_POT = "evt_csr_pot";
	public static final String HASH_EVT_WINNERS = "evt_winners";
	public static final String HASH_EVT_END = "evt_end";
	public static final String HASH_EVT_END_TIME = "evt_end_time";

	public static final String HASH_Cat_Id_products = "prod_cat_id";// //this is
																	// in all
	// products
	public static final String HASH_Sub_Cat_Id_products = "prod_sub_cat";

	// ////////////////cashback VAriable//////////////////////
	public static final String HASH_CAMPAIGN_PROD_PRICE_CASHBACK = "evt_prod_price";// hash
																					// been
																					// changed
																					// to
																					// evt
	public static final String HASH_CAMPAIGN_OFFER_CASHBACK = "offer";// hash
																		// been
																		// changed
																		// to
																		// offer
	public static final String HASH_CASHBACK_SHARE_CASHBACK = "cashback_share";
	public static final String HASH_END_DATE_CASHBACK = "end_date";

	// //////////////favourite required fields///////////
	public static final String HASH_campaign_ID = "campaign_id";// used for
																// cashback
																// products
	public static final String HASH_campaign_type = "campaign_type";
	public static final String HASH_favorite_type = "favorite_type";
	public static final String HASH_customer_ID_fav = "customer_id";

	public static final String HASH_default_campaign_ID = "campaign"; // this is
																		// the
																		// name
																		// for
																		// both
																		// cashback
																		// and
																		// promo
	public static final String HASH_company_ID = "company_id";
	public static final String HASH_evt_ID = "evt_id"; // used for promo,brand
														// promo products

	// ///////////////myaccount related fields 1st page/////////
	public static final String HASH_BALANCE_OBJECT = "balance";
	public static final String HASH_AVAILABLE_BALANCE = "available_balance";
	public static final String HASH_PENDING_PROMO_BALANCE = "pending_promo_balance";
	public static final String HASH_PENDING_CASHBACK_BALANCE = "pending_cahback_balance";
	public static final String HASH_PENDING_Total_BALANCE = "pending_total";

	public static final String HASH_TOTAL_PROMO_BALANCE = "total_promo_balance";
	public static final String HASH_TOTAL_CASHBACK_BALANCE = "total_cashback_balance";
	public static final String HASH_TOTAL_SAVED = "total_saved";

	public static final String HASH_TOTAL_REDEEMED = "total_redeemed";
	public static final String HASH_REDEEMED_BALANCE = "redeem_balance";
	public static final String HASH_CURRENTLY_WON = "currently_won";

	// ///////////myaccount related fields 2nd page/////////
	public static final String HASH_History_m2 = "history";
	public static final String HASH_balanceTag_m2 = "available_balance";
	public static final String HASH_transactions_m2 = "transactions";
	public static final String HASH_prodName_m2 = "prod_name";
	public static final String HASH_compName_m2 = "company_name";
	public static final String HASH_effectiveDate_m2 = "effective_date";
	public static final String HASH_amount_m2 = "amount";
	public static final String HASH_currentBalance_m2 = "current_balance";
	public static final String HASH_type_m2 = "type";
	public static final String HASH_gc_code_m2 = "gc_code_serial";

	// ///////////myaccount related fields 3rd page(pending)/////////
	public static final String HASH_History_m3 = "history";
	public static final String HASH_balanceTag_m3 = "pending_balance";
	public static final String HASH_transactions_m3 = "transactions";
	public static final String HASH_prodName_m3 = "prod_name";
	public static final String HASH_compName_m3 = "company_name";
	public static final String HASH_UC_DATE_ADDED_m3 = "uc_date_added";
	public static final String HASH_UC_AVAILABLE_ON_m3 = "uc_available_on";
	public static final String HASH_code_m3 = "code_serial";
	public static final String HASH_UC_AMOUNT_m3 = "uc_amount";

	// ///////////myaccount related fields 4th page(redeem)/////////
	public static final String HASH_History_m4 = "history";
	public static final String HASH_available_balance_m4 = "available_balance";
	public static final String HASH_balanceTag_m4 = "redeem_balance";
	public static final String HASH_transactions_m4 = "transactions";
	public static final String HASH_transaction_id_m4 = "transaction_id";
	public static final String HASH_method_name_m4 = "method_name";
	public static final String HASH_transaction_status_m4 = "transaction_status";
	public static final String HASH_transaction_amount_m4 = "transaction_amount";
	public static final String HASH_balance_after_redeem_m4 = "balance_after_redeem";
	public static final String HASH_date_submitted_m4 = "date_submitted";

	// ///////////myaccount related fields 5th page(Won event)/////////
	public static final String HASH_codes_m5 = "codes";
	public static final String HASH_won_m5 = "won";
	public static final String HASH_currently_won_m5 = "currently_won";
	public static final String HASH_unreachable_m5 = "unreachable";
	public static final String HASH_balance_m5 = "balance";
	public static final String HASH_code_status_m5 = "code_status";
	public static final String HASH_evt_name_m5 = "evt_name";
	public static final String HASH_evt_type_m5 = "evt_type";
	public static final String HASH_evt_prize_m5 = "evt_prize";
	public static final String HASH_evt_winners_m5 = "evt_winners";
	public static final String HASH_company_name_m5 = "company_name";
	public static final String HASH_prod_name_m5 = "prod_name";
	public static final String HASH_currently_won_amount_m5 = "currently_won_amount";
	public static final String HASH_unreachable_amount_m5 = "unreachable_amount";
	public static final String HASH_total_won_amount_m5 = "total_won_amount";
	public static final String HASH_created_date_m5 = "created_date";
	public static final String HASH_prize_date_m5 = "prize_date";

	// ///////////myaccount related fields 6th page(prize event)/////////
	public static final String HASH_participating_events_m6 = "participating_events";
	public static final String HASH_total_count_m6 = "total_count";
	public static final String HASH_event_m6 = "event";
	public static final String HASH_evt_name_m6 = "evt_name";
	public static final String HASH_evt_prize_m6 = "evt_prize";
	public static final String HASH_evt_winner_pot_m6 = "evt_winner_pot";
	public static final String HASH_evt_csr_pot_m6 = "evt_csr_pot";
	public static final String HASH_evt_id_m6 = "evt_id";
	public static final String HASH_count_m6 = "count";

	// ////////////scanner Activity////////////////////
	public static final String HASH_message_sc = "message";
	public static final String HASH_success_sc = "success";
	public static final String HASH_amount_sc = "amount";

	public static final String HASH_PRIZE_sc = "prize";

	public static final String HASH_prize_type_sc = "prize_type";
	public static final String HASH_prize_name_sc = "prize_name";
	public static final String HASH_prize_image_sc = "prize_image";
	public static final String HASH_prize_position_sc = "prize_position";
	public static final String HASH_company_name_sc = "company_name";
	public static final String HASH_prize_amount_sc = "prize_amount";

	public static final String HASH_promo_cashback_sc = "promo_cashback";
	public static final String HASH_cashback_sc = "cashback";
	public static final String HASH_donation_sc = "donation";
	public static final String HASH_winner_pot_sc = "winner_pot";
	public static final String HASH_currentPrizeToWin = "current_prize_to_win";

	// ////////////reedem method////////////////////
	public static final String HASH_payment_method_m4 = "payment_method";
	public static final String HASH_method_id_m4 = "method_id";
	public static final String HASH_method_namesp_m4 = "method_name";
	public static final String HASH_minimum_amount_m4 = "minimum_amount";
	public static final String HASH_method_charges_m4 = "method_charge";
	public static final String HASH_method_description_m4 = "method_description";
	public static final String HASH_charge_type_m4 = "charge_type";
	public static final String HASH_method_status_m4 = "method_status";

	public static final String HASH_percentile_m4 = "percentile";
	public static final String HASH_fixed_m4 = "fixed";

	public static final String HASH_payment_info_fields = "payment_info_fields";
	public static final String HASH_info = "info";
	public static final String HASH_mandatory_fields = "mandatory_fields";

	public static final String HASH_mandatory_fields_m4 = "mandatory_fields";
	public static final String HASH_id_m4 = "id";
	public static final String HASH_field_name_m4 = "field_name";

	// /////////////EDIT PROFILE get///////////////////
	public static final String HASH_customer_m6 = "customer";
	public static final String HASH_cust_fname_m6 = "cust_fname";
	public static final String HASH_cust_lname_m6 = "cust_lname";
	public static final String HASH_cust_email_m6 = "cust_email";
	public static final String HASH_cust_city_m6 = "cust_city";
	public static final String HASH_cust_image_m6 = "cust_image";
	public static final String HASH_cust_mobile_m6 = "cust_mobile";
	public static final String HASH_cust_gender_m6 = "cust_gender";
	public static final String HASH_cust_birth_date_m6 = "cust_birth_date";
	public static final String HASH_cust_address_m6 = "cust_address";

	// ////////LIVE COUPON & HAPPY HOURS///////////////////
	public static final String HASH_LIVECOUPONS = "live_coupon";
	public static final String HASH_HAPPYHOUR = "happy_hour";
	public static final String HASH_LIVE = "isLive";
	public static final String HASH_SAVEDCOUPONS = "saved_coupon";
	public static final String HASH_KEY = "keyword";
	public static boolean HASH_SEARCH_LIVE = true;
	public static boolean HASH_SEARCH_SAVED = false;
	public static Vector<Coupon> hintsList = null;

	// /get from ip address
	public static final String hash_jsonCountry = "country";

	// productDetails
	public static final String Hash_description = "descriptionText";
	public static final String Hash_Campaigndescription = "campaigndescT";

	// extended store information
	public static final String Hash_adv_store_id = "adv_id";

	// new types included
	public static final String HASH_OFFER_TYPE = "offer_type";
	public static final String ORDER_AMOUNT = "order_amount";

	// /fixed and instant related
	public static final String HASH_MINIMUM_PURCHASE_TARGET = "minimum_purchase_target";

	public static final String HASH_MINIMUM_PURCHASE_AMOUNT = "minimum_purchase_amount";

	public static final String HASH_prize = "prize";
	public static final String HASH_prize_position = "prize_position";
	public static final String HASH_prize_type = "prize_type";
	public static final String HASH_prize_name = "prize_name";
	public static final String HASH_prize_amount = "prize_amount";
	public static final String HASH_campaign_name = "campaign_name";

	public static final String HASH_ipc_id = "ipc_id";
	public static final String HASH_fpc_id = "fpc_id";

	public static final String Hash_GCMID = "gcmId";
	public static final String Hash_AppUpdate = "app_update";
	public static final String Hash_UpdateInfo = "update_info";
	public static final String Hash_UpdateTitle = "update_title";
	public static final String Hash_FirstTimeLogin = "first_time_log";
	public static final String Hash_AuthToken = "auth_token";
	
	public static final String HASH_Restaurant_Name = "restaurantName";
	public static final String HASH_Restaurant_Id = "restaurantId";

	// //////unique id for FIXEDD****************///////////////////

	public static void showHashKey(Context context) {
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(
					"com.promo.payout.main", PackageManager.GET_SIGNATURES); // Your
																				// package
																				// name
																				// here
			for (Signature signature : info.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				Log.i("KeyHash:",
						Base64.encodeToString(md.digest(), Base64.DEFAULT));
			}
		} catch (NameNotFoundException e) {
		} catch (NoSuchAlgorithmException e) {
		}
	}

}
