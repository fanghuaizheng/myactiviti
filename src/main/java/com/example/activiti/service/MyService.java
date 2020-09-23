package com.example.activiti.service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;
import org.activiti.engine.task.Task;
import org.activiti.engine.FormService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.activiti.dao.UserDao;
import com.example.activiti.entity.User;


@Service
public class MyService {
	
	@Autowired
	RuntimeService runtimeService;
	
	@Autowired
	TaskService taskService;
	
	@Autowired
	RepositoryService repositoryService;
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	FormService formService;
	
	public ProcessInstance startProcess(String assignee) {
		
		 //根据bpmn文件部署流程
        Deployment deployment = repositoryService.createDeployment().addClasspathResource("processes/demo2.bpmn").deploy();
        //获取流程定义
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
        //启动流程定义，返回流程实例
      //启动流程定义，返回流程实例
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("id", "123");
        
        ProcessInstance pi = runtimeService.startProcessInstanceById(processDefinition.getId(),variables);
        return pi;
//        return runtimeService.startProcessInstanceById(processDefinition.getId(),variables);
	}
	
	@Transactional
    public List<Task> getTasks(String assignee) {
		return taskService.createTaskQuery().taskAssignee(assignee).list();
    }

}
