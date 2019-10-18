package com.cg.ibs.rm.util;

public interface QueryMapper {
	public static final String UCI_LIST_FOR_ADMIN1="SELECT uci FROM TempBeneficiaryTable ";
	public static final String UCI_LIST_FOR_ADMIN2="SELECT uci FROM TempCreditCardTable ";
	public static final String UNAPP_BENEFECIARIS="SELECT Account_Number, Account_Name,Ifsc_code,bank_name, Type FROM TempBeneficiarytable";
	public static final String UNAPP_CARDS ="SELECT Card_Number, EXPIRY_DATE, Name_on_card FROM TempCreditCardTable WHERE UCI =?";
	public static final String SAVING_CARDS_INTO_FINAL ="INSERT INTO FinalCreditCardTable(Card_Number,UCI,EXPIRY_DATE,Name_on_card) VALUES(?,?,?,?)";
	public static final String SAVING_BENEFICIARIES_INTO_FINAL ="INSERT INTO FinalBeneficiaryTable(Account_Number, Account_Name , Ifsc_Code , Bank_Name , Type , UCI) VALUES(?,?,?,?,?,?)";
	
	
	public static final String GET_CARDS_DETAILS = "SELECT credit_card_num , name_on_cred_card, credit_expiry_date  FROM CREDIT_CARD WHERE UCI=?";
	public static final String GET_CARD = "SELECT credit_card_num FROM CREDIT_CARD WHERE credit_card_num =?";
	public static final String INSERT_CARD_DETAILS = "INSERT INTO TEMPCREDITCARDTABLE (Card_Number,UCI,EXPIRY_DATE,Name_on_card,Time_Stamp) VALUES(?,?,?,?,?)";
	public static final String DELETE_CARD_DETAILS = "DELETE FROM CREDIT_CARD WHERE CREDIT_CARD_NUM =? ";
	
	
	public static final String GET_ADDED_BENEFICIARIES = "SELECT Account_Number, Account_Name,Ifsc_code,bank_name, Type FROM FinalBeneficiaryTable WHERE uci=?";
	public static final String ADD_BENEFICIARY = "INSERT INTO TempBeneficiaryTable(Account_Number, Account_Name , Ifsc_Code , Bank_Name , Type , UCI) VALUES(?,?,?,?,?,?)";
	public static final String GET_BENEFICIARY = "SELECT Account_Number, Account_Name,Ifsc_code,bank_name,Type FROM FinalBeneficiaryTable WHERE Account_Number = ?";
	public static final String DELETE_BENEFICIARY = "Delete FROM FinalBeneficiaryTable WHERE UCI= ? AND Account_Number = ?";
	public static final String CHECK_BENEFICIARY = "SELECT Account_Number FROM FinalBeneficiaryTable WHERE Account_Number = ?";
	
	
	
	public static final String GET_AUTOPAYMENTS= "SELECT Amount,DateofStart,SPI FROM autopayment_table WHERE uci=?";
	public static final String ADD_AUTOPAYMENTS= "INSERT INTO autopayment_table VALUES(?,?,?)";
	public static final String DELETE_AUTOPAYMENTS = "DELETE FROM Autopayment_table WHERE uci=? AND serviceProviderId=?";
	public static final String SERVICE_LIST = "SELECT SPI, Company_Name FROM Service_Providers";
	public static final String GET_BALANCE = "SELECT Current_Balance FROM Accounts WHERE UCI = ?";
	public static final String SET_BALANCE = "UPDATE Accounts SET current_balance = ? WHERE uci =?";
}
