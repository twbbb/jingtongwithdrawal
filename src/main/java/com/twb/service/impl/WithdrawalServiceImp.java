package com.twb.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.twb.entity.Withdrawal;
import com.twb.entity.WithdrawalBack;
import com.twb.entity.WithdrawalDeallog;
import com.twb.repository.WithdrawalBackRepository;
import com.twb.repository.WithdrawalDeallogRepository;
import com.twb.repository.WithdrawalRepository;
import com.twb.service.WithdrawalService;
import com.twb.utils.TransferUtil;
import com.twb.utils.WithdrawalQueue;

@Service
public class WithdrawalServiceImp implements WithdrawalService
{

	private static final Logger logger = LoggerFactory.getLogger(WithdrawalServiceImp.class);

	@Autowired
	private WithdrawalRepository withdrawalRepository;
	
	@Autowired
	private WithdrawalBackRepository withdrawalBackRepository;
	
	@Autowired
	private WithdrawalDeallogRepository withdrawalDeallogRepository;
	
	@Value("${cny_min}")
	private String cny_min;
	
	@Value("${cny_max}")
	private String cny_max;
	
	@Value("${cny_issuer}")
	private String cny_issuer;
	
	@Value("${wxorder_pre}")
	private String wxorder_pre;
	
	@Value("${memos_support}")
	private String memos_support;
	
	

	private static final  SimpleDateFormat SDF=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //加上时间

	@Transactional(rollbackFor = Exception.class)
	public List<Withdrawal> getTodoWithdrawal() throws Exception
	{
		List<Withdrawal> list = withdrawalRepository.getAllWithdrawalByState(Withdrawal.STATE_TOBE_WITHDRAWAL);
		if(list!=null&&!list.isEmpty())
		{
			for(Withdrawal w:list)
			{
				w.setState(Withdrawal.STATE_DOING_WITHDRAWAL);
				withdrawalRepository.save(w);
				
			}
		}
		if(list==null)
		{
			list = new ArrayList();
		}
		return list;
	}
	
	public List<Withdrawal> getDoingWithdrawal() throws Exception
	{
		List<Withdrawal> list = withdrawalRepository.getAllWithdrawalByState(Withdrawal.STATE_DOING_WITHDRAWAL);
		if(list==null)
		{
			list = new ArrayList();
		}
		return list;
	}

	@Transactional(rollbackFor = Exception.class)
	public void handlerTodoWithdrawal(List<Withdrawal> list) throws Exception
	{
		for(Withdrawal withdrawal:list)
		{
			String msg = checkWithdrawal(withdrawal);
			if(!StringUtils.isEmpty(msg))
			{
				logger.info("验证失败,"+msg+",hash:"+withdrawal.getHash()+",放入回退表");
				saveWithdrawBack(withdrawal,msg,Withdrawal.STATE_WITHDRAWAL_BACK);
			}
			else
			{
				logger.info("验证成功,"+msg+",hash:"+withdrawal.getHash()+",放入支付队列");
				WithdrawalQueue.add(withdrawal);
			}
			
			
		}
		
	}

	private String checkWithdrawal(Withdrawal withdrawal)
	{
		String currency=withdrawal.getAmountcurrency();
		String issuer = withdrawal.getAmountissuer();
		String value = withdrawal.getAmountvalue();
		
		String memos = withdrawal.getMemos();
		
		BigDecimal bd = new BigDecimal(value);
		BigDecimal bdMin = new BigDecimal(cny_min);
		BigDecimal bdMax = new BigDecimal(cny_max);
		
		StringBuffer errSb = new StringBuffer();
		if(memos_support.equals(memos))
		{
			errSb.append(memos_support);
		}
		else
		{
			if("CNY".equals(currency)&&cny_issuer.equals(issuer))
			{
				
				if(bd.compareTo(bdMin)<0){
					errSb.append("转入CNY小于最小提取金额："+cny_min+"元");
				}
				
				if(bd.compareTo(bdMax)>0){
					errSb.append("转入CNY大于最大提取金额："+cny_max+"元");
				}
				
				if(StringUtils.isEmpty(memos)||memos.length()>32)
				{
					errSb.append("转账时候请输入正确的备注");
				}
				
			}
			else
			{
				errSb.append("转入不是CNY,暂不支持非CNY提取");
			}
		}
		
		return errSb.toString();
		
	}

	private void saveWithdrawBack(Withdrawal withdrawal,String reason,String state)
	{
		WithdrawalBack wb = new WithdrawalBack();
		wb.setAmountcurrency(withdrawal.getAmountcurrency());
		wb.setAmountissuer(withdrawal.getAmountissuer());
		wb.setAmountvalue(withdrawal.getAmountvalue());
		wb.setCounterparty(withdrawal.getCounterparty());
		wb.setDate(new Date());
		wb.setHash(withdrawal.getHash());
		wb.setWithdrawalId(withdrawal.getId());
		wb.setBackreason(reason);
		wb.setResponseState(WithdrawalBack.RESPONSE_STATE_TODO);
		withdrawalBackRepository.save(wb);
		withdrawal.setState(state);
		withdrawalRepository.save(withdrawal);
	}        

	@Transactional(rollbackFor = Exception.class)
	public void doingWithdrawal(Withdrawal withdrawal)
	{
		
		
		String msg = checkWithdrawal(withdrawal);
		if(!StringUtils.isEmpty(msg))
		{
			logger.info("验证失败,"+msg+",hash:"+withdrawal.getHash()+",放入回退表");
			saveWithdrawBack(withdrawal,msg,Withdrawal.STATE_WITHDRAWAL_BACK);
		}

		String openid = withdrawal.getMemos().trim() ;
		String amount = withdrawal.getAmountvalue();

		String partner_trade_no = wxorder_pre+withdrawal.getId();
		
		
		Map map = new HashMap();
		try
		{
			logger.info("开始转账1：订单"+partner_trade_no+",openid："+openid+"转账金额:"+amount);
			map = TransferUtil.transfer( partner_trade_no, openid,amount,"红包"+withdrawal.getHash());
			logger.info("转账结果1："+map);
			String return_code=(String) map.get("return_code");
			
			if("SUCCESS".equals(return_code))
			{
				String result_code = (String) map.get("result_code");
				if("SUCCESS".equals(result_code))
				{
					successSave(withdrawal, map);
				}
				else
				{
					logger.info("开始转账2：订单"+partner_trade_no+",openid："+openid+"转账金额:"+amount);
					//注意：当状态为FAIL时，存在业务结果未明确的情况，所以如果状态FAIL，请务必再请求一次查询接口[请务必关注错误代码（err_code字段），通过查询查询接口确认此次付款的结果。]，以确认此次付款的结果。
					map = TransferUtil.transfer( partner_trade_no, openid,amount,"红包"+withdrawal.getHash());
					logger.info("转账结果2："+map);
					String return_code2=(String) map.get("return_code");
					if("SUCCESS".equals(return_code2))
					{
						String result_code2 = (String) map.get("result_code");
						if("SUCCESS".equals(result_code2))
						{
							successSave(withdrawal, map);
						}
						else
						{
							failSave(withdrawal, map);
						}
					}
					else
					{
						saveWithdrawBack(withdrawal,"转账通讯失败",Withdrawal.STATE_WITHDRAWAL_FAIL_BACK);
					}
				}
			}
			else
			{
				saveWithdrawBack(withdrawal,"转账通讯失败",Withdrawal.STATE_WITHDRAWAL_FAIL_BACK);
			}
			
			
			
			
		}
		catch (Exception e)
		{
			saveWithdrawBack(withdrawal,"转账异常："+e.toString(),Withdrawal.STATE_WITHDRAWAL_EXCEPTION);
			logger.error(withdrawal.getId()+","+withdrawal.getHash()+",error.." + e.toString() + "," + Arrays.toString(e.getStackTrace()));
			e.printStackTrace();
		}
		
		

		
		
		
		
		
	}

	private void failSave(Withdrawal withdrawal, Map map)
	{
		String openid = withdrawal.getMemos().trim() ;
		String backAmount = withdrawal.getAmountvalue();
		String partner_trade_no = wxorder_pre+withdrawal.getId();
		
		String err_code_des = (String) map.get("err_code_des");
		String return_msg = (String) map.get("return_msg");
		String msg = "";
		if(!StringUtils.isEmpty(err_code_des))
		{
			msg = err_code_des;			
		}
		else
		{
			msg = return_msg;
		}
		if(!StringUtils.isEmpty(msg)&&msg.contains("openid"))
		{
			msg = "转账时请输入正确的备注";
		}
		
		WithdrawalDeallog wd = new WithdrawalDeallog();
		wd.setAmount(backAmount);
		wd.setOrderId(partner_trade_no);
		wd.setPayeeAccount(openid);
		wd.setReturnCode(WithdrawalDeallog.RETURNCODE_FAIL);
		wd.setReturnMsg(msg);
		wd.setTime(new Date());
		wd.setWithdrawalId(withdrawal.getId());
		wd.setWithdrawalType(WithdrawalDeallog.WITHDRAWALTYPE_WECHAT);
		withdrawalDeallogRepository.save(wd);
		
		WithdrawalBack wb = new WithdrawalBack();
		wb.setAmountcurrency(withdrawal.getAmountcurrency());
		wb.setAmountissuer(withdrawal.getAmountissuer());
		wb.setAmountvalue(withdrawal.getAmountvalue());
		wb.setCounterparty(withdrawal.getCounterparty());
		wb.setDate(new Date());
		wb.setHash(withdrawal.getHash());
		wb.setWithdrawalId(withdrawal.getId());
		wb.setBackreason(msg);
		wb.setResponseState(WithdrawalBack.RESPONSE_STATE_TODO);
		withdrawalBackRepository.save(wb);
		
		withdrawal.setState(Withdrawal.STATE_WITHDRAWAL_FAIL_BACK);
		withdrawalRepository.save(withdrawal);
		
	}

	private void successSave(Withdrawal withdrawal,  Map map)
	{
		String openid = withdrawal.getMemos().trim() ;
		String backAmount = withdrawal.getAmountvalue();
		String partner_trade_no = wxorder_pre+withdrawal.getId();
		String payment_no = (String) map.get("payment_no");
		String payment_time = (String) map.get("payment_time");
		Date date= new Date();
		if(!StringUtils.isEmpty(payment_time))
		{
			  //必须捕获异常
		    try {
		        date=SDF.parse(payment_time);
		    } catch(Exception px) {
		        px.printStackTrace();
		        date = new Date();
		    }
		}
		
		WithdrawalDeallog wd = new WithdrawalDeallog();
		wd.setAmount(backAmount);
		wd.setOrderId(partner_trade_no);
		wd.setOrderWxid(payment_no);
		wd.setPayDate(date);
		wd.setPayeeAccount(openid);
		wd.setReturnCode(WithdrawalDeallog.RETURNCODE_SUCCESS);
		wd.setReturnMsg((String) map.get("return_msg"));
		wd.setTime(new Date());
		wd.setWithdrawalId(withdrawal.getId());
		wd.setWithdrawalType(WithdrawalDeallog.WITHDRAWALTYPE_WECHAT);
		withdrawalDeallogRepository.save(wd);
		withdrawal.setState(Withdrawal.STATE_WITHDRAWAL_SUCCESS);
		withdrawalRepository.save(withdrawal);
		
		WithdrawalBack wb = new WithdrawalBack();
		wb.setAmountcurrency("SWT");
		wb.setAmountissuer("");
		wb.setAmountvalue("0.000010");
		wb.setCounterparty(withdrawal.getCounterparty());
		wb.setDate(new Date());
		wb.setHash(withdrawal.getHash());
		wb.setWithdrawalId(withdrawal.getId());
		wb.setBackreason("于"+payment_time+"提取成功，微信支付金额："+backAmount+"，微信订单号："+payment_no);
		wb.setResponseState(WithdrawalBack.RESPONSE_STATE_TODO);
		withdrawalBackRepository.save(wb);
		
	}




	

}
