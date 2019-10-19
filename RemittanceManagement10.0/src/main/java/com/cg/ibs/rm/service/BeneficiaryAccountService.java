package com.cg.ibs.rm.service;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.Set;

import com.cg.ibs.rm.bean.Beneficiary;
import com.cg.ibs.rm.exception.IBSExceptions;

public interface BeneficiaryAccountService {
	public Set<Beneficiary> showBeneficiaryAccount(String uci) throws IBSExceptions;

	public boolean validateBeneficiaryAccountNumber(String accountNumber);

	public boolean validateBeneficiaryAccountNameOrBankName(String name);

	public boolean validateBeneficiaryIfscCode(String Ifsc);

	public boolean modifyBeneficiaryAccountDetails( String uci, BigInteger accountNumber, Beneficiary beneficiary)
			throws IBSExceptions, SQLException, IOException;

	public boolean deleteBeneficiaryAccountDetails(String uci, BigInteger accountNumber) throws IBSExceptions;

	public boolean saveBeneficiaryAccountDetails(String uci, Beneficiary beneficiary) throws IBSExceptions;
}
