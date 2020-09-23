package com.example.activiti;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;

import org.activiti.engine.FormService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.extern.slf4j.Slf4j;
@Slf4j
//@SpringBo
public class CommonTest {
	
	@Autowired
	ActivitiRule activitiRule;
	
	@Test
    @Deployment(resources = {"my-process-form.bpmn"})
    public void testFormService() {

        FormService formService = activitiRule.getFormService();
        ProcessDefinition processDefinition = activitiRule.getRepositoryService().createProcessDefinitionQuery().singleResult();

        String startFormKey = formService.getStartFormKey(processDefinition.getId());
        log.info("startFormKey = {}", startFormKey);

        StartFormData startFormData = formService.getStartFormData(processDefinition.getId());
        List<FormProperty> formProperties = startFormData.getFormProperties();
        formProperties.forEach(form -> {
            log.info("startFormKey = {}", startFormData);
        });

        //通过form服务启动
        Map<String, String> properties =new HashMap<>();
        properties.put("message", "this is my test message");
        ProcessInstance processInstance = formService.submitStartFormData(processDefinition.getId(), properties);

        Task task = activitiRule.getTaskService().createTaskQuery().singleResult();
        //流程启动才会确定taskId
        TaskFormData taskFormData = formService.getTaskFormData(task.getId());
        List<FormProperty> formProperties1 = taskFormData.getFormProperties();
        formProperties1.forEach(formProperty -> {
            log.info("formProperty = {}", formProperty);
        });

        Map<String, String> properties1 =  new HashMap<>();
        properties1.put("yesOrNo", "yes");
        formService.submitTaskFormData(task.getId(), properties1);
        Task task1 = activitiRule.getTaskService().createTaskQuery().taskId(task.getId()).singleResult();
        log.info("task1 = {}", task1);
    }
}

