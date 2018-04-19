package com.twb.thread;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.twb.entity.Withdrawal;
import com.twb.service.WithdrawalService;
import com.twb.utils.WithdrawalQueue;

public class WithdrawalRunnable implements Runnable
{

	private static final Logger logger = LoggerFactory.getLogger(WithdrawalRunnable.class);

	private WithdrawalService withdrawalServiceImp;

	@Override
	public void run()
	{
		logger.info("线程:" + Thread.currentThread().getName() + "运行中.....");
		while (true)
		{
			try
			{
				Withdrawal withdrawal = WithdrawalQueue.get();

				withdrawalServiceImp.doingWithdrawal(withdrawal);

			}
			catch (Exception e)
			{
				logger.error("error.." + e.toString() + "," + Arrays.toString(e.getStackTrace()));
				e.printStackTrace();
			}
		}

	}

	public WithdrawalService getWithdrawalServiceImp()
	{
		return withdrawalServiceImp;
	}

	public void setWithdrawalServiceImp(WithdrawalService withdrawalServiceImp)
	{
		this.withdrawalServiceImp = withdrawalServiceImp;
	}
	
	

}
