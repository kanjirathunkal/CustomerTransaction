package com.java.transaction;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class Transaction {

	private final int customerAccountNumber ;
	private final double transactionAmount;
}
