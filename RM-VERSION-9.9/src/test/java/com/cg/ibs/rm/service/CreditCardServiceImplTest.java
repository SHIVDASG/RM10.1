package com.cg.ibs.rm.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigInteger;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.cg.ibs.rm.bean.CreditCard;
import com.cg.ibs.rm.exception.IBSExceptions;

class CreditCardServiceImplTest {
	CreditCardServiceImpl obj = new CreditCardServiceImpl();
	BankRepresentativeServiceImpl obj1 = new BankRepresentativeServiceImpl();

	@Test
	public void test1ValidateNameOnCard() {
		assertTrue(obj.validateNameOnCard("qwertyysdhdbhdsbsdijh"));

	}

	@Test
	public void test2ValidateNameOnCard() {
		assertFalse(obj.validateNameOnCard("123564564"));

	}

	@Test
	public void test3ValidateNameOnCard() {
		assertEquals(false, obj.validateNameOnCard("!@##$@#$%#%^$^&%"));

	}

	@Test
	final void test1DeleteCardDetails() {
		try {
			assertTrue(obj.deleteCardDetails("123456", new BigInteger("9281672789250000")));
		} catch (IBSExceptions exception) {
			Throwable throwable = assertThrows(IBSExceptions.class, () -> {
				throw new IBSExceptions(exception.getMessage());
			});

			assertSame("Credit card doesn't exist", throwable.getMessage());

		}
	}

	@Test
	final void test2DeleteCardDetails() {
		try {
			assertFalse(obj.deleteCardDetails("123456", new BigInteger("1231672789250000")));
		} catch (IBSExceptions exception) {
			Throwable throwable = assertThrows(IBSExceptions.class, () -> {
				throw new IBSExceptions(exception.getMessage());
			});

			assertSame("Credit card doesn't exist", throwable.getMessage());

		}
	}

	@Test
	final void test3DeleteCardDetails() {
		try {
			assertEquals(true, obj.deleteCardDetails("123456", new BigInteger("9281672789250000")));
		} catch (IBSExceptions exception) {
			Throwable throwable = assertThrows(IBSExceptions.class, () -> {
				throw new IBSExceptions(exception.getMessage());
			});

			assertSame("Credit card doesn't exist", throwable.getMessage());

		}
	}

	@Disabled
	@Test
	final void test1SaveCardDetails() {
		CreditCard card = new CreditCard();
		card.setcreditCardNumber(new BigInteger("1234567890123456"));
		card.setcreditDateOfExpiry("17/12/2020");
		card.setnameOnCreditCard("Saima");
		try {
			obj.saveCardDetails("123456", card);
			assertEquals(2, obj1.showUnapprovedCreditCards("123456").size());
		} catch (IBSExceptions exception) {
			Throwable throwable = assertThrows(IBSExceptions.class, () -> {
				throw new IBSExceptions(exception.getMessage());
			});
			assertSame("Credit card already added", throwable.getMessage());
		}
	}

	@Test
	final void test2SaveCardDetails() {
		CreditCard card = new CreditCard();
		card.setcreditCardNumber(new BigInteger("1234567890123456"));
		card.setcreditDateOfExpiry("17/12/2020");
		card.setnameOnCreditCard("saima");
		try {
			obj.saveCardDetails("123456", card);
			assertNotNull(obj1.showUnapprovedBeneficiaries("123456").size());
		} catch (IBSExceptions exception) {
			Throwable throwable = assertThrows(IBSExceptions.class, () -> {
				throw new IBSExceptions(exception.getMessage());
			});
			assertSame("Credit card already added", throwable.getMessage());
		}

	}

}
