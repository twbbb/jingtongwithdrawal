package com.twb.service;

import java.util.List;

import com.twb.entity.Withdrawal;

public interface WithdrawalService {
	
	/**
	 * 
	 * @Title: getTodoWithdrawal   
	 * @Description: 取出待提现数据
	 * @param: @return
	 * @param: @throws Exception      
	 * @return: List<Withdrawal>      
	 * @throws
	 */
	List<Withdrawal> getTodoWithdrawal() throws Exception;
	
	/**
	 * 
	 * @Title: getDoingWithdrawal   
	 * @Description: 取出正在提现数据
	 * @param: @return
	 * @param: @throws Exception      
	 * @return: List<Withdrawal>      
	 * @throws
	 */
	List<Withdrawal> getDoingWithdrawal() throws Exception;
	/**
	 * 
	 * @Title: handlerTodoWithdrawal   
	 * @Description: 处理待提现
	 * @param: @return
	 * @param: @throws Exception      
	 * @return: void     
	 * @throws
	 */
	void handlerTodoWithdrawal(List<Withdrawal> list) throws Exception;
	
	
	
	/**
	 * 
	 * @Title: doingWithdrawal   
	 * @Description: 处理提现
	 * @param: @param Withdrawal
	 * @param: @throws Exception      
	 * @return: void      
	 * @throws
	 */
	void doingWithdrawal(Withdrawal withdrawal) throws Exception;

}
