package com.example.activiti.listener;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class MyCompeteistener implements TaskListener{

	@Override
	public void notify(DelegateTask delegateTask) {
		// TODO Auto-generated method stub
		List<String> userList = new ArrayList<>();
		userList.add("222");
		userList.add("333");
		userList.add("444");
//		userList.add("555");
		log.info("开始加入人员信息");
		delegateTask.setVariable("budgetUsers", userList);
		
	}

}
