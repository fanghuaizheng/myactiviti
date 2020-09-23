package com.example.activiti.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

/**
 *  节点创建监听器

 * 为节点设置处理人
 * @author JVC20190082
 *
 */
public class MutiGroupsListener implements TaskListener{

	@Override
	public void notify(DelegateTask arg0) {
		// TODO Auto-generated method stub
		//将通过人数，未通过人数，总数，重新置为0，退回的时候才能重新计算
		arg0.setVariable("passCount", "0");
		arg0.setVariable("totalCount", "0");
		arg0.setVariable("noPassCount", "0");
		//为每一个任务设置处理人
		 String party = (String)arg0.getVariable("party");
		 arg0.setAssignee(party);
		
	}

}
