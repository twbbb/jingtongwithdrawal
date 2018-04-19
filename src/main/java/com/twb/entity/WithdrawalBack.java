package com.twb.entity;


import java.util.Date;

import javax.persistence.*;

//使用JPA注解配置映射关系
@Entity //告诉JPA这是一个实体类（和数据表映射的类）
@Table(name = "withdrawal_back ") //@Table来指定和哪个数据表对应;如果省略默认表名就是user；
public class WithdrawalBack  {

	
	public static final String RESPONSE_STATE_TODO = "1";//1待响应
	public static final String RESPONSE_STATE_DOING = "2";//2正处理
	public static final String RESPONSE_STATE_EXCEPTION = "3";//3处理异常
	public static final String RESPONSE_STATE_SUCCESS = "4";//4处理成功
	public static final String RESPONSE_STATE_FAIL = "5";//5处理失败
	
	
    @Id //这是一个主键
    @GeneratedValue(strategy = GenerationType.IDENTITY)//自增主键
    private Integer id;
    
    @Column(name = "withdrawal_id")
    private Integer withdrawalId;

    @Column(name = "withdrawal_deallog_id")
    private Integer withdrawalDeallogId;
    
    private String amountvalue; //交易金额
    private String amountcurrency; //货币类型
    private String amountissuer; //货币发行方
    
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date")
    private Date date; //数据插入时间
	
	private String backreason ;//回退原因
	
	private String hash; //交易hash
	
	private String counterparty; //	交易对家
	
	@Column(name = "response_state")
	private String responseState;//响应状态，1待响应，2正响应，3响应异常 4.响应成功5.响应失败
	@Column(name = "response_msg")
	private String responseMsg;//响应成功失败等信息
	@Column(name = "response_hash")
	private String responseHash;//响应成功的hash
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "response_data")
	private Date responseData;// 响应完成的时间
	
	public Integer getId()
	{
		return id;
	}
	public void setId(Integer id)
	{
		this.id = id;
	}
	public Integer getWithdrawalId()
	{
		return withdrawalId;
	}
	public void setWithdrawalId(Integer withdrawalId)
	{
		this.withdrawalId = withdrawalId;
	}
	public Integer getWithdrawalDeallogId()
	{
		return withdrawalDeallogId;
	}
	public void setWithdrawalDeallogId(Integer withdrawalDeallogId)
	{
		this.withdrawalDeallogId = withdrawalDeallogId;
	}
	public String getAmountvalue()
	{
		return amountvalue;
	}
	public void setAmountvalue(String amountvalue)
	{
		this.amountvalue = amountvalue;
	}
	public String getAmountcurrency()
	{
		return amountcurrency;
	}
	public void setAmountcurrency(String amountcurrency)
	{
		this.amountcurrency = amountcurrency;
	}
	public String getAmountissuer()
	{
		return amountissuer;
	}
	public void setAmountissuer(String amountissuer)
	{
		this.amountissuer = amountissuer;
	}
	public Date getDate()
	{
		return date;
	}
	public void setDate(Date date)
	{
		this.date = date;
	}
	public String getHash()
	{
		return hash;
	}
	public void setHash(String hash)
	{
		this.hash = hash;
	}
	public String getCounterparty()
	{
		return counterparty;
	}
	public void setCounterparty(String counterparty)
	{
		this.counterparty = counterparty;
	}
	public String getBackreason()
	{
		return backreason;
	}
	public void setBackreason(String backreason)
	{
		this.backreason = backreason;
	}
	public String getResponseState()
	{
		return responseState;
	}
	public void setResponseState(String responseState)
	{
		this.responseState = responseState;
	}
	public String getResponseMsg()
	{
		return responseMsg;
	}
	public void setResponseMsg(String responseMsg)
	{
		this.responseMsg = responseMsg;
	}
	public String getResponseHash()
	{
		return responseHash;
	}
	public void setResponseHash(String responseHash)
	{
		this.responseHash = responseHash;
	}
	public Date getResponseData()
	{
		return responseData;
	}
	public void setResponseData(Date responseData)
	{
		this.responseData = responseData;
	}
  
	
	
    
    

}
