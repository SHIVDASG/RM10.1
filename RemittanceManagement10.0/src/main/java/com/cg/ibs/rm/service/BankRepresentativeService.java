package com.cg.ibs.rm.service;


import java.math.BigInteger;
import java.util.Set;

import com.cg.ibs.rm.bean.Beneficiary;
import com.cg.ibs.rm.bean.CreditCard;
import com.cg.ibs.rm.exception.IBSExceptions;

public interface BankRepresentativeService {
	public Set<String> showRequests() throws IBSExceptions;
	
	public Set<CreditCard> showUnapprovedCreditCards(String uci) throws IBSExceptions;

	public Set<Beneficiary> showUnapprovedBeneficiaries(String uci) throws IBSExceptions;

	public boolean saveCreditCardDetails(String uci, CreditCard card) throws IBSExceptions;

	public void saveBeneficiaryDetails(String uci, Beneficiary beneficiary) throws IBSExceptions;
	
	public boolean checkUciList(String uci) throws IBSExceptions;
	
	public boolean deleteBenficiary(BigInteger accountNumber) throws IBSExceptions;
	
	public boolean deleteCreditCard(BigInteger creditCardNumber)throws IBSExceptions;

}

