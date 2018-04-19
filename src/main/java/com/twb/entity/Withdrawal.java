package com.twb.entity;


import java.util.Date;

import javax.persistence.*;

//使用JPA注解配置映射关系
@Entity //告诉JPA这是一个实体类（和数据表映射的类）
@Table(name = "withdrawal") //@Table来指定和哪个数据表对应;如果省略默认表名就是user；
public class Withdrawal {

	public static final String STATE_TOBE_WITHDRAWAL = "1";//1待提现
	public static final String STATE_DOING_WITHDRAWAL = "2";//2正处理
	public static final String STATE_WITHDRAWAL_SUCCESS = "3";//3返现成功
	public static final String STATE_WITHDRAWAL_BACK = "4";//回退
	public static final String STATE_WITHDRAWAL_FAIL_BACK = "5";//返现失败回退
	public static final String STATE_WITHDRAWAL_EXCEPTION = "6";//返现异常
	
	public static final String WITHDRAWALTYPE_WECHAT = "1";//1微信
	public static final String WITHDRAWALTYPE_ALIPAY = "2";//2支付宝

	public static final String DATASOURCE_SOCKET = "1";//1socket监听 
	public static final String DATASOURCE_SCHEDULING = "2";//2定时任务
	
	public static final String CHECKFLAG_TOBE = "1";//1.待验证
	public static final String CHECKFLAG_SUCCESS = "2";// 2.验证成功
	public static final String CHECKFLAG_FAIL = "3";// 2.验证失败
	
    @Id //这是一个主键
    @GeneratedValue(strategy = GenerationType.IDENTITY)//自增主键
    private Integer id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date")
    private Date date; //交易时间
	
    private String hash; //交易hash
    private String fee; //交易费用，井通计价
    private String memos; //交易备注
    private String counterparty; //	交易对家
    private String amountvalue; //交易金额
    private String amountcurrency; //货币类型
    private String amountissuer; //货币发行方
    private String state; //提现状态，1待提现，2已处理
    @Column(name = "withdrawal_type")
    private String withdrawalType=WITHDRAWALTYPE_WECHAT ; //提现类型 1.微信 2.支付宝
    private String datasource  ; //数据来源 1.socket监听 2.定时任务
    
	public Integer getId()
	{
		return id;
	}
	public void setId(Integer id)
	{
		this.id = id;
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
	public String getFee()
	{
		return fee;
	}
	public void setFee(String fee)
	{
		this.fee = fee;
	}
	public String getMemos()
	{
		return memos;
	}
	public void setMemos(String memos)
	{
	
		this.memos = memos;
	}
	public String getCounterparty()
	{
		return counterparty;
	}
	public void setCounterparty(String counterparty)
	{
		this.counterparty = counterparty;
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
	public String getState()
	{
		return state;
	}
	public void setState(String state)
	{
		this.state = state;
	}
	public String getWithdrawalType()
	{
		return withdrawalType;
	}
	public void setWithdrawalType(String withdrawalType)
	{
		this.withdrawalType = withdrawalType;
	}
	public String getDatasource()
	{
		return datasource;
	}
	public void setDatasource(String datasource)
	{
		this.datasource = datasource;
	}
    

    
    
    

}
