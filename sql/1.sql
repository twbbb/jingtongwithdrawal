/*
withdrawal  提现
id，
date，	交易时间
hash，	交易hash
fee，	交易费用，井通计价
memos，	交易备注
counterparty，	交易对家
amountvalue，	交易金额
amountcurrency，	货币类型
amountissuer，	货币发行方
state	提现状态，1待提现，2正处理，3返现成功4.回退5";//返现失败回退"6";//返现异常
withdrawal_type 提现类型 1.微信 2.支付宝
datasource 数据来源 1.socket2.定时任务
*/

DROP TABLE IF EXISTS withdrawal;
    CREATE TABLE withdrawal
    (
      id int(11) AUTO_INCREMENT PRIMARY KEY, 
      date datetime NOT NULL,
      hash varchar(64) COLLATE utf8_unicode_ci NOT NULL,  
      fee float(10,4),
      memos varchar(1000) COLLATE utf8_unicode_ci ,
counterparty varchar(34) COLLATE utf8_unicode_ci ,
amountvalue  float(16,6) NOT NULL,
amountcurrency varchar(20) COLLATE utf8_unicode_ci NOT NULL,
amountissuer varchar(34) COLLATE utf8_unicode_ci ,
state varchar(2) COLLATE utf8_unicode_ci NOT NULL,
withdrawal_type varchar(2) COLLATE utf8_unicode_ci NOT NULL,
datasource varchar(2) COLLATE utf8_unicode_ci NOT NULL
    ) ENGINE = InnoDB;
   ALTER TABLE withdrawal  ADD INDEX withdrawal_state ( `state` ) ;
ALTER TABLE withdrawal ADD CONSTRAINT uc_withdrawal  UNIQUE (`hash`) ;

/*
transaction_log  交易记录表
id，
date，	交易时间
hash，	交易hash
type  ,  类型
result ，结果
fee，	交易费用，井通计价
memos，	交易备注
counterparty，	交易对家
amountvalue，	交易金额
amountcurrency，	货币类型
amountissuer，	货币发行方
checkflag 定时任务验证socket 1.待比对验证 2.验证成功3.socket没有，待添加。4.socket没有，已添加5.异常6.无需比对
checkmsg 比对信息
*/

DROP TABLE IF EXISTS transaction_log;
    CREATE TABLE transaction_log
    (
      id int(11) AUTO_INCREMENT PRIMARY KEY, 
      date datetime NOT NULL,
      hash varchar(64) COLLATE utf8_unicode_ci NOT NULL,  
type  varchar(20) COLLATE utf8_unicode_ci NOT NULL,  
result varchar(20) COLLATE utf8_unicode_ci NOT NULL,  
      fee float(10,4),
      memos varchar(1000) COLLATE utf8_unicode_ci ,
counterparty varchar(34) COLLATE utf8_unicode_ci ,
amountvalue  float(16,6) ,
amountcurrency varchar(20) COLLATE utf8_unicode_ci ,
amountissuer varchar(34) COLLATE utf8_unicode_ci ,
checkflag varchar(2) COLLATE utf8_unicode_ci NOT NULL,
checkmsg varchar(300) COLLATE utf8_unicode_ci 
    ) ENGINE = InnoDB;
   
 ALTER TABLE transaction_log ADD CONSTRAINT uc_transaction_log  UNIQUE (`hash`) ;

/*
withdrawal_deallog 提现日志
id,
withdrawal_id,
withdrawal_type,	提现通道，1支付宝，2微信

payee_account 提现账户，
amount，提现金额,单位，分
time,	提现处理时间

return_code,	处理结果 
return_msg, 处理结果msg
pay_date,支付时间
order_id, 商户订单号
order_wxid, 	微信订单号
*/

DROP TABLE IF EXISTS withdrawal_deallog ;
    CREATE TABLE withdrawal_deallog 
    (
      id int(11) AUTO_INCREMENT PRIMARY KEY, 
      withdrawal_id int(11) NOT NULL,
      withdrawal_type varchar(2) COLLATE utf8_unicode_ci NOT NULL,       
payee_account varchar(50) COLLATE utf8_unicode_ci , 
      amount   float(10,2) NOT NULL,
      time datetime,
return_code varchar(20) COLLATE utf8_unicode_ci ,  
return_msg varchar(200) COLLATE utf8_unicode_ci ,
pay_date datetime,
order_id varchar(64) COLLATE utf8_unicode_ci,
order_wxid varchar(64) COLLATE utf8_unicode_ci 

    ) ENGINE = InnoDB;
    
    
    /*
withdrawal_back 提现回退
id,
withdrawal_id,
withdrawal_deallog_id,
amountvalue,  金额
amountcurrency,	货币
amountissuer,	货币发行方
date 数据插入时间
backreason 回退原因
counterparty 交易对家
hash，	交易hash


response_state 响应状态，1待响应，2正响应，3响应异常 4.响应成功5.响应失败
response_msg 响应成功失败等信息
response_hash 响应的hash
response_data 响应完成的时间
*/

DROP TABLE IF EXISTS withdrawal_back ;
    CREATE TABLE withdrawal_back 
    (
      id int(11) AUTO_INCREMENT PRIMARY KEY, 
      withdrawal_id int(11) NOT NULL,
     withdrawal_deallog_id int(11) ,
counterparty varchar(34) COLLATE utf8_unicode_ci ,
 hash varchar(64) COLLATE utf8_unicode_ci NOT NULL,  
amountvalue  float(16,6)  NOT NULL,
amountcurrency varchar(20) COLLATE utf8_unicode_ci NOT NULL,
amountissuer varchar(34) COLLATE utf8_unicode_ci ,  
backreason varchar(300) COLLATE utf8_unicode_ci ,  
date datetime,  
response_state varchar(2) COLLATE utf8_unicode_ci NOT NULL,  
response_data datetime,   
response_msg varchar(200) COLLATE utf8_unicode_ci,  
response_hash varchar(64) COLLATE utf8_unicode_ci 
    ) ENGINE = InnoDB;

ALTER TABLE withdrawal_back ADD CONSTRAINT uc_withdrawal_back   UNIQUE (`hash`) ;

    

