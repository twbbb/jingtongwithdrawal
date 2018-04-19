package com.twb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.twb.entity.WithdrawalBack;


//继承JpaRepository来完成对数据库的操作
public interface WithdrawalBackRepository extends JpaRepository<WithdrawalBack,Integer>{
	
	
	
	
	  
}
