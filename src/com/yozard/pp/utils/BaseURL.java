package com.yozard.pp.utils;

public class BaseURL {// signup.php?authtoken=api/v1/
	public static final String COUNTRY_BD = "https://www.yozard.com.bd/";
	public static final String COUNTRY_IE = "https://www.yozard.ie/";
	public static final String COUNTRY_KE = "https://www.yozard.co.ke/";
	public static final String COUNTRY_NG = "http://www.promopayout.com.ng/";
	public static final String COUNTRY_UK = "http://www.promopayout.co.uk/";//

	public static final String Auth_Token = "01058f2a172e65ac869b98b94b4863e9b0ddc50a";

	public static final String API = "api/v1/";

	public static String selectBaseUrl(String countryName) {
		String baseUrl = "";
		if (countryName != null && !countryName.equals("")) {
			if (countryName.contains("Bangladesh")) {
//				baseUrl = COUNTRY_BD;
				 baseUrl = COUNTRY_UK;
				// baseUrl = COUNTRY_KE;
			} else if (countryName.contains("United Kingdom")) {
				baseUrl = COUNTRY_UK;
//				 baseUrl = COUNTRY_KE;
			} else if (countryName.contains("Nigeria")) {
//				baseUrl = COUNTRY_NG;
//				 baseUrl = COUNTRY_KE;
			} else if (countryName.contains("Ireland")) {
//				baseUrl = COUNTRY_IE;
				// baseUrl = COUNTRY_KE;
			} else if (countryName.contains("Kenya")) {
//				baseUrl = COUNTRY_KE;
			}
		}
		return baseUrl;
	}

	public static String selectCurrency(String countryName) {
		String currency = "";
		if (countryName != null && !countryName.equals("")) {
			if (countryName.contains("Bangladesh")) {
				currency = "৳";
				// currency = "£";
			} else if (countryName.contains("United Kingdom")) {
				currency = "£";
			} else if (countryName.contains("Nigeria")) {
				currency = "₦";
			} else if (countryName.contains("Ireland")) {
				currency = "€";
			} else if (countryName.contains("Kenya")) {
				currency = "KSh";
			}
		}
		return currency;
	}
}
