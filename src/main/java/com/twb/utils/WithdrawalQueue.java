package com.twb.utils;

import java.util.concurrent.LinkedBlockingQueue;

import com.twb.entity.Withdrawal;

public class WithdrawalQueue
{
	private static LinkedBlockingQueue<Withdrawal> WITHDRAWL_QUEUE = new LinkedBlockingQueue<Withdrawal>();
	
	public static void add(Withdrawal withdrawal) throws InterruptedException
	{
		WITHDRAWL_QUEUE.put(withdrawal);
	}
	
	public static Withdrawal get() throws InterruptedException
	{
		return WITHDRAWL_QUEUE.take();
	}
}
