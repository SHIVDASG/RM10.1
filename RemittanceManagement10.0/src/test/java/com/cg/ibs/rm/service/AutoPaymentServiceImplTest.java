
package com.cg.ibs.rm.service;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.jupiter.api.Test;

import com.cg.ibs.rm.bean.AutoPayment;
import com.cg.ibs.rm.exception.IBSExceptions;

class AutoPaymentServiceImplTest {
	AutoPaymentServiceImpl autoPaymentServiceImpl = new AutoPaymentServiceImpl();

	@Test
	public void autoDeductionTest1() {
		AutoPayment autoPayment = new AutoPayment(new BigDecimal("90"), "12/12/2019", new BigInteger("2"));
		try {
			assertEquals(true, autoPaymentServiceImpl.autoDeduction("123456", autoPayment));
		} catch (IBSExceptions exception) {
			Throwable throwable = assertThrows(IBSExceptions.class, () -> {
				throw new IBSExceptions(exception.getMessage());
			});
			assertSame("Enter a valid date format", throwable.getMessage());
		}

	}

	@Test
	public void autoDeductionTest2() {
		AutoPayment autoPayment = new AutoPayment(new BigDecimal("10000000000"), "12/12/202020202",
				new BigInteger("2"));
		try {
			assertFalse(autoPaymentServiceImpl.autoDeduction("123456", autoPayment));
		} catch (IBSExceptions exception) {
			Throwable throwable = assertThrows(IBSExceptions.class, () -> {
				throw new IBSExceptions(exception.getMessage());
			});
			assertSame("Enter a valid date format", throwable.getMessage());
		}

	}

	@Test
	public void autoDeductionTest3() {
		AutoPayment autoPayment = new AutoPayment(new BigDecimal("90"), "12/12/2023", new BigInteger("2"));
		try {
			assertTrue((autoPaymentServiceImpl.autoDeduction("123456", autoPayment)));
		} catch (IBSExceptions exception) {
			Throwable throwable = assertThrows(IBSExceptions.class, () -> {
				throw new IBSExceptions(exception.getMessage());
			});
			assertSame("Enter a valid date format", throwable.getMessage());
		}

	}

	@Test
	public void updateRequirementsTest1() {
		try {
			assertTrue(autoPaymentServiceImpl.updateRequirements("12345", new BigInteger("2")));
		} catch (IBSExceptions exception) {
			Throwable throwable = assertThrows(IBSExceptions.class, () -> {
				throw new IBSExceptions(exception.getMessage());
			});
			assertSame("Auto payment service doesn't exist", throwable.getMessage());
		}
	}

	@Test
	public void updateRequirementsTest2() {
		try {
			assertFalse((autoPaymentServiceImpl.updateRequirements("123456", new BigInteger("3"))));
		} catch (IBSExceptions exception) {
			Throwable throwable = assertThrows(IBSExceptions.class, () -> {
				throw new IBSExceptions(exception.getMessage());
			});
			assertSame("Auto payment service doesn't exist", throwable.getMessage());
		}
	}

	@Test
	public void updateRequirementsTest3() {
		try {
			assertEquals(true, (autoPaymentServiceImpl.updateRequirements("123456", new BigInteger("3"))));
		} catch (IBSExceptions exception) {
			Throwable throwable = assertThrows(IBSExceptions.class, () -> {
				throw new IBSExceptions(exception.getMessage());
			});
			assertSame("Auto payment service doesn't exist", throwable.getMessage());
		}
	}

	@Test
	public void showAutopaymentDetailsTest() {

		try {
			assertEquals(2, autoPaymentServiceImpl.showAutopaymentDetails("12345").size());
		} catch (IBSExceptions exception) {
			Throwable throwable = assertThrows(IBSExceptions.class, () -> {
				throw new IBSExceptions(exception.getMessage());
			});
			assertSame("Server Error", throwable.getMessage());
		}
	}

	@Test
	public void showIBSServiceProvidersTest() {
		try {
			assertEquals(4, autoPaymentServiceImpl.showIBSServiceProviders().size());
		} catch (IBSExceptions exception) {
			Throwable throwable = assertThrows(IBSExceptions.class, () -> {
				throw new IBSExceptions(exception.getMessage());
			});
			assertSame("Server Error", throwable.getMessage());
		}
	}

}