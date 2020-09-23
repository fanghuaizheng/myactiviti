package com.example.activiti.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.activiti.dao.UserDao;
import com.example.activiti.entity.User;
import com.example.activiti.service.MyService;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@RestController
@RequestMapping("test")
public class TestController {
	
	@Autowired
	TaskService taskService;
	
	@Autowired
	UserDao userDao;
	
	@RequestMapping("addUser")
	public Object addUser() {
		User user = new User(Double.valueOf(Math.random()*1000).intValue()+"","fd",Math.round(45.444F),"df");
		log.info("保存用户={}",user);
		userDao.save(user);
		
		return "1";
		
	}
	@Autowired
	private RepositoryService repositoryService;
	
	@Autowired
	private RuntimeService runtimeService;
	
	@Autowired
	MyService myService;
	
	 @RequestMapping(value="/process")
	    public Object startProcessInstance(String id) {
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
//	      return  myService.startProcess(id);
	    }
	 
	
	 	
	 
	 	
	 	
	 	 static class TaskRepresentation {

	         private String id;
	         private String name;

	         public TaskRepresentation(String id, String name) {
	             this.id = id;
	             this.name = name;
	         }

	          public String getId() {
	             return id;
	         }
	         public void setId(String id) {
	             this.id = id;
	         }
	         public String getName() {
	             return name;
	         }
	         public void setName(String name) {
	             this.name = name;
	         }

	     }
	
}
