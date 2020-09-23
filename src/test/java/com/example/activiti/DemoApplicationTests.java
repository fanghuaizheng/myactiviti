//package com.example.activiti;
//
<<<<<<< HEAD
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.activiti.engine.FormService;
//import org.activiti.engine.form.FormProperty;
//import org.activiti.engine.form.StartFormData;
//import org.activiti.engine.form.TaskFormData;
//import org.activiti.engine.repository.ProcessDefinition;
//import org.activiti.engine.runtime.ProcessInstance;
//import org.activiti.engine.task.Task;
//import org.activiti.engine.test.ActivitiRule;
//import org.activiti.engine.test.Deployment;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import lombok.extern.slf4j.Slf4j;
//@Slf4j
=======
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
>>>>>>> 6b5f00b783b940a8a7d8952004aaa395719ffcb0
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class DemoApplicationTests {
//
//	@Test
//	public void contextLoads() {
//	}
<<<<<<< HEAD
//	
//	@Autowired
//	ActivitiRule activitiRule;
//	
//	@Test
//    @Deployment(resources = {"my-process-form.bpmn"})
//    public void testFormService() {
//
//        FormService formService = activitiRule.getFormService();
//        ProcessDefinition processDefinition = activitiRule.getRepositoryService().createProcessDefinitionQuery().singleResult();
//
//        String startFormKey = formService.getStartFormKey(processDefinition.getId());
//        log.info("startFormKey = {}", startFormKey);
//
//        StartFormData startFormData = formService.getStartFormData(processDefinition.getId());
//        List<FormProperty> formProperties = startFormData.getFormProperties();
//        formProperties.forEach(form -> {
//            log.info("startFormKey = {}", startFormData);
//        });
//
//        //通过form服务启动
//        Map<String, String> properties =new HashMap<>();
//        properties.put("message", "this is my test message");
//        ProcessInstance processInstance = formService.submitStartFormData(processDefinition.getId(), properties);
//
//        Task task = activitiRule.getTaskService().createTaskQuery().singleResult();
//        //流程启动才会确定taskId
//        TaskFormData taskFormData = formService.getTaskFormData(task.getId());
//        List<FormProperty> formProperties1 = taskFormData.getFormProperties();
//        formProperties1.forEach(formProperty -> {
//            log.info("formProperty = {}", formProperty);
//        });
//
//        Map<String, String> properties1 =  new HashMap<>();
//        properties1.put("yesOrNo", "yes");
//        formService.submitTaskFormData(task.getId(), properties1);
//        Task task1 = activitiRule.getTaskService().createTaskQuery().taskId(task.getId()).singleResult();
//        log.info("task1 = {}", task1);
//    }
=======
>>>>>>> 6b5f00b783b940a8a7d8952004aaa395719ffcb0
//
//}
