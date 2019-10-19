package com.cg.ibs.rm.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.SQLException;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.cg.ibs.rm.bean.Beneficiary;
import com.cg.ibs.rm.dao.BeneficiaryDAOImpl;
import com.cg.ibs.rm.exception.IBSExceptions;
import com.cg.ibs.rm.ui.Type;

class BeneficiaryAccountServiceImplTest {
	BeneficiaryAccountService object = new BeneficiaryAccountServiceImpl();
	BankRepresentativeService object1 = new BankRepresentativeServiceImpl();
	BeneficiaryDAOImpl object3 = new BeneficiaryDAOImpl();
	Beneficiary beneficiary = new Beneficiary();

	@Disabled
	@Test
	public void testShowBeneficiaryAccount1() {
		try {
			assertEquals(1, object.showBeneficiaryAccount("123456").size());
		} catch (IBSExceptions e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testShowBeneficiaryAccount2() {
		try {
			assertNotNull(object.showBeneficiaryAccount("123456"));
		} catch (IBSExceptions e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Disabled
	@Test
	public void testShowBeneficiaryAccount3() {
		beneficiary.setAccountNumber(new BigInteger("123456789013"));
		beneficiary.setAccountName("John Trump");
		beneficiary.setBankName("HDFC");
		beneficiary.setIfscCode("HDFC5623487");
		beneficiary.setType(Type.OTHERSINOTHERS);
		try {
			object.showBeneficiaryAccount("123456").add(beneficiary);
		
		assertEquals(2, object.showBeneficiaryAccount("123456").size());
		} catch (IBSExceptions e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testValidateBeneficiaryAccountNumber1() {
		assertTrue(object.validateBeneficiaryAccountNumber("123456789012"));
	}

	@Test
	public void testValidateBeneficiaryAccountNumber2() {
		assertFalse(object.validateBeneficiaryAccountNumber("123456789012345"));
	}

	@Test
	public void testValidateBeneficiaryAccountNumber3() {
		assertEquals(true, (object.validateBeneficiaryAccountNumber("123456789012")));
	}

	@Test
	public void testValidateBeneficiaryAccountNameOrBankName1() {
		assertTrue(object.validateBeneficiaryAccountNameOrBankName("Shivani"));
	}

	@Test
	public void testValidateBeneficiaryAccountNameOrBankName2() {
		assertFalse(object.validateBeneficiaryAccountNameOrBankName("123456345srsf"));
	}

	@Test
	public void testValidateBeneficiaryAccountNameOrBankName3() {
		assertEquals(true, (object.validateBeneficiaryAccountNameOrBankName("HDFC")));
	}

	@Test
	public void testDeleteBeneficiaryAccountDetails1() {
		try {
			object.deleteBeneficiaryAccountDetails("12345", new BigInteger("349825671589"));
			assertEquals(0, object.showBeneficiaryAccount("12345").size());
		} catch (IBSExceptions exception) {
			Throwable throwable = assertThrows(IBSExceptions.class, () -> {
				throw new IBSExceptions(exception.getMessage());
			});

			assertSame("Beneficiary doesn't exist", throwable.getMessage());
		}
	}

	@Test
	public void testDeleteBeneficiaryAccountDetails2() {
		try {
			object.deleteBeneficiaryAccountDetails("12345", new BigInteger("349825671589"));
			assertNotNull(object.showBeneficiaryAccount("12345"));
		} catch (IBSExceptions exception) {
			Throwable throwable = assertThrows(IBSExceptions.class, () -> {
				throw new IBSExceptions(exception.getMessage());
			});
			assertSame("Beneficiary doesn't exist", throwable.getMessage());
		}
	}

	@Test
	public void testSaveBeneficiaryAccountDetails1() {
		beneficiary.setAccountNumber(new BigInteger("123456789013"));
		beneficiary.setAccountName("Ayush Handsome");
		beneficiary.setBankName("SBSC");
		beneficiary.setIfscCode("SBSC5623487");
		beneficiary.setType(Type.OTHERSINOTHERS);
		try {
			object.saveBeneficiaryAccountDetails("123456", beneficiary);
			assertEquals(2, object1.showUnapprovedBeneficiaries("123456").size());
		} catch (IBSExceptions exception) {
			Throwable throwable = assertThrows(IBSExceptions.class, () -> {
				throw new IBSExceptions(exception.getMessage());
			});
			assertSame("Beneficiary already added", throwable.getMessage());
		}

	}

	@Test
	public void testModifyBeneficiaryAccountDetails1() {
		Beneficiary beneficiary3 = new Beneficiary();
		try {
			System.out.println(object3.getBeneficiary("5555111151513301", (new BigInteger("349825671589"))));
			beneficiary3 = object3.getBeneficiary("5555111151513301", (new BigInteger("349825671589")));
			Beneficiary beneficiary2 = new Beneficiary();
			beneficiary2.setAccountName("Shivani Dasssss");
			beneficiary2.setAccountNumber(beneficiary3.getAccountNumber());
			beneficiary2.setIfscCode(beneficiary3.getIfscCode());
			beneficiary2.setBankName(beneficiary3.getBankName());
			beneficiary2.setType(beneficiary3.getType());
			assertTrue(object.modifyBeneficiaryAccountDetails("5555111151513301", beneficiary3.getAccountNumber(), beneficiary2));
		} catch (IBSExceptions | SQLException | IOException exception) {
			Throwable throwable = assertThrows(IBSExceptions.class, () -> {
				throw new IBSExceptions(exception.getMessage());
			});
			assertSame("Beneficiary doesn't exist", throwable.getMessage());
		}
	}

}
