package com.example.activiti.listener;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component("myExecutionListener")
public class MyExecutionListener implements ExecutionListener,TaskListener{

	@Override
	public void notify(DelegateExecution execution) {
		String eventName = execution.getEventName();
		if("start".equals(eventName)) {
			log.info("流程开始");
			// TODO Auto-generated method stub
			List<String> userList = new ArrayList<>();
			userList.add("222");
			userList.add("333");
			userList.add("444");
//			userList.add("555");
			log.info("开始加入人员信息");
			execution.setVariable("budgetUsers", userList);
		}else if("end".equals(eventName)) {
			log.info("流程结束");
		}
	}

	@Override
	public void notify(DelegateTask delegateTask) {
		// TODO Auto-generated method stub
		String en = delegateTask.getEventName();
		switch (en) {
			case "create":
				log.info("节点创建");break;
			case "assignment":
				log.info("节点assignment");break;
				
			case "complete":
				log.info("节点complete");break;
			case "delete":
				log.info("节点delete");break;
			default:
				log.info("en={}",en);
		}
		String as = delegateTask.getAssignee();
		log.info("as = {}",as);
		Object var = delegateTask.getVariable("budgetUsers");
		log.info("var={}",var);
	}

}
