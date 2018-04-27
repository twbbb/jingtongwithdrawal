
这个主要是做对应的业务处理。
微信提现。



GetWithdrawalBackTask，取出数据库数据，然后放入队列，DoWithdrawalTask，会从队列中取出数据，然后做提现处理。提现记录放入withdrawal_deallog。提现结果，失败回退，成功，数据响应，都放入withdrawal_back。

表 withdrawal_back ，hash唯一约束
