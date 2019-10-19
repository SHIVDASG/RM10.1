package com.cg.ibs.rm.dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

import com.cg.ibs.rm.bean.CreditCard;
import com.cg.ibs.rm.exception.ExceptionMessages;
import com.cg.ibs.rm.exception.IBSExceptions;
import com.cg.ibs.rm.util.ConnectionProvider;
import com.cg.ibs.rm.util.QueryMapper;

public class CreditCardDAOImpl implements CreditCardDAO {

	public Set<CreditCard> getDetails(String uci) throws IBSExceptions {
		Set<CreditCard> getCreditCards = new HashSet<>();
		Connection connection = ConnectionProvider.getConnection();
		try (PreparedStatement preparedStatement = connection.prepareStatement(QueryMapper.GET_CARDS_DETAILS);) {
			preparedStatement.setBigDecimal(1, new BigDecimal(uci));
			try (ResultSet resultSet = preparedStatement.executeQuery();) {
				while (resultSet.next()) {
					getCreditCards.add(new CreditCard(new BigInteger(resultSet.getBigDecimal("credit_card_num").toString()),
									resultSet.getString("name_on_cred_card"),
									resultSet.getDate("credit_expiry_date").toString()));
				}
			}
		} catch (SQLException exception) {
			throw new IBSExceptions(ExceptionMessages.ERROR11);
		}
		return getCreditCards;
	}

	public boolean copyDetails(String uci, CreditCard card) throws IBSExceptions {
		boolean result =false;
		if (card != null) {
			Connection connection = ConnectionProvider.getConnection();
			try (PreparedStatement preparedStatement = connection.prepareStatement(QueryMapper.GET_CARD);) {
				preparedStatement.setBigDecimal(1, new BigDecimal(card.getcreditCardNumber()));
				try (ResultSet getCard = preparedStatement.executeQuery();) {
					if (getCard.next()) {
						throw new IBSExceptions(ExceptionMessages.ERROR1);
					} else {
						try (PreparedStatement preparedStatement2 = connection.prepareStatement(QueryMapper.INSERT_CARD_DETAILS)) {
							preparedStatement2.setBigDecimal(1, new BigDecimal(card.getcreditCardNumber()));
							preparedStatement2.setBigDecimal(2, new BigDecimal(uci));
							DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern("dd/MM/yyyy");
							LocalDate datebow=LocalDate.parse(card.getcreditDateOfExpiry(), dateTimeFormatter);
							preparedStatement2.setDate(3,Date.valueOf(datebow) );
							preparedStatement2.setString(4, card.getnameOnCreditCard());
							LocalDateTime dateTime = LocalDateTime.now();
							preparedStatement2.setTimestamp(5, Timestamp.valueOf(dateTime));
							if(preparedStatement2.executeUpdate()>0) {
								result = true;
							}
						}
					}
				}
			} catch (SQLException exception) {
				throw new IBSExceptions(ExceptionMessages.ERROR12);
			}
		}
		return result;
	}

	@Override
	public boolean deleteDetails(String uci, BigInteger cardNumber) throws IBSExceptions {
		boolean result = false;
		Connection connection = ConnectionProvider.getConnection();
		try (PreparedStatement preparedStatement = connection.prepareStatement(QueryMapper.GET_CARD);) {
			preparedStatement.setBigDecimal(1, new BigDecimal(cardNumber));
			try (ResultSet getCard = preparedStatement.executeQuery();) {
				if (getCard.next()) {
					try (PreparedStatement preparedStatement2 = connection
							.prepareStatement(QueryMapper.DELETE_CARD_DETAILS);) {
						int check = preparedStatement2.executeUpdate();
						if (check > 0) {
							result = true;
						}
					}
				} else {
					throw new IBSExceptions(ExceptionMessages.ERROR2);
				}

			}
		} catch (SQLException exception) {
			throw new IBSExceptions(ExceptionMessages.ERROR11);
		}
		return result;
	}
}