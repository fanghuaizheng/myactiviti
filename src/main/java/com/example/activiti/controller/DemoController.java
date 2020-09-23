<<<<<<< HEAD
package com.example.activiti.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.SubProcess;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.activiti.controller.TestController.TaskRepresentation;
import com.example.activiti.dto.FlowData;
import com.example.activiti.service.MyService;
import com.example.activiti.utils.ProcessUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/demo")
public class DemoController {
    @Autowired
    //Activiti 中每一个不同版本的业务流程的定义都需要使用一些定义文件，部署文件和支持数据 ( 例如 BPMN2.0 XML 文件，表单定义文件，流程定义图像文件等 )，
//    这些文件都存储在 Activiti 内建的 Repository 中。Repository Service 提供了对 repository 的存取服务。
    private RepositoryService repositoryService;
    @Autowired
    //在 Activiti 中，每当一个流程定义被启动一次之后，都会生成一个相应的流程对象实例。
//    Runtime Service 提供了启动流程、查询流程实例、设置获取流程实例变量等功能。此外它还提供了对流程部署，流程定义和流程实例的存取服务。
    private RuntimeService runtimeService;
    @Autowired
    //在 Activiti 中业务流程定义中的每一个执行节点被称为一个 Task，对流程中的数据存取，状态变更等操作均需要在 Task 中完成。
//    Task Service 提供了对用户 Task 和 Form 相关的操作。它提供了运行时任务查询、领取、完成、删除以及变量设置等功能
    private TaskService taskService;
    
    
    @Autowired
//    Activiti 中内置了用户以及组管理的功能，必须使用这些用户和组的信息才能获取到相应的 Task。
//    Identity Service 提供了对 Activiti 系统中的用户和组的管理功能。
    IdentityService identityService;
    
//    @Autowired
//    //通过使用 Form Service 可以存取启动和完成任务所需的表单数据并且根据需要来渲染表单
//    FormService fromService;
    
    @Autowired
//    Management Service 提供了对 Activiti 流程引擎的管理和维护功能
//    ，这些功能不在工作流驱动的应用程序中使用，主要用于 Activiti 系统的日常维护。
    ManagementService managementService;
    
    @Autowired
//    History Service 用于获取正在运行或已经完成的流程实例的信息，与 Runtime Service 
//    中获取的流程信息不同，历史信息包含已经持久化存储的永久信息，并已经被针对查询优化。
    HistoryService historyService;
    
    @Autowired
    FormService formService;
    
    @Autowired
    MyService myService;
    
    /**
     * 获取项目部署的全部流程
     * @return
     */
    @RequestMapping("getAllProcess")
    public Object getAllProcess() {
    	List<Deployment> list = repositoryService.createDeploymentQuery().orderByDeploymenTime().desc().list();
    	log.info("获取部署流程长度={}",list.size());
    	if(list!=null&&list.size()>0) {
    		//获取最新的数据返回
    		List<ProcessDefinition> pd = repositoryService.createProcessDefinitionQuery().deploymentId(list.get(0).getId()).list();
    		pd.forEach(x->{
				log.info("流程={}，id={}",x,x.getId());
			});
    	}
    	return null;
    }
    
    @RequestMapping("getStartTaskUser")
    public Object getStartTaskUser(String pid) {
    	Process process = ProcessUtils.getProcess(pid);
    	process.getFlowElements().forEach(x->{
    		log.info("{},",x,x.getName());
    	});
    	return null;
    }
    
    /**
     * 根据流程id启动流程
     * @param pid
     */
    @RequestMapping("startProcess")
    public void startProcess(@RequestParam(required = false) String pid,
    		String startName,String value) {

    	//设置各个节点处理人id
        Map<String, Object> variables = new HashMap<String, Object>();
//        variables.put("id", "123");
//        variables.put("bid", "456");
//        variables.put("tid", "789");
//        variables.put(startName,value);
//        variables.put("dept", false);
        
        FlowData flowData = new FlowData();
        String startUser = value;
        flowData.setStartUser(startUser);
        flowData.setCurrentUser(startUser);
        flowData.setFirstNode(true);
        flowData.setFirstSubmit(true);
        flowData.setNextUser(startUser);
        flowData.setProcessDefinitionId(pid);
        variables.put(ProcessUtils.FLOW_DATA_KEY, flowData);
        
        ProcessInstance pi = runtimeService.startProcessInstanceById(pid,variables);
        
        String processId = pi.getId();
        log.info("流程创建成功，当前流程实例ID:{}",processId);

//        Task task=taskService.createTaskQuery().processInstanceId(processId).singleResult();
        //多任务实例 多子流程
        List<Task> list = taskService.createTaskQuery().processInstanceId(processId).list();
        
        System.out.println("任务数量:"+list.size());
        list.forEach(task -> {
            /**
		             * 模拟几级审批(正式环境要从配置或者数据库读取)，因为这里要清楚每个部门走几级审批逻辑
		             * 变量存放到每个任务节点的全局任务变量中
             */
            Random r = new Random();//创建随机种子，Random对象
            int rank = r.nextInt(3)+1;
            System.out.println("任务ID："+task.getId());
            System.out.println("指派人ID："+task.getAssignee());
            taskService.setVariable(task.getId(),"rank",rank);
        });
//        flowData.setProcessName(processName);
        flowData.setProcessDefinitionId(pid);
//        flowData.setProcessInstanceId(processId);
//        flowData.setFirstNodeId(task.getTaskDefinitionKey());
//        flowData.setCurrentNodeId(task.getTaskDefinitionKey());
//        flowData.setCurrentNodeName(task.getName());
//        flowData.setTaskId(task.getId());
//        flowData.setExecutionId(task.getExecutionId());
        //设置form表数据
//        variables.put("yesOrNo", value);
        
//        System.out.println("第一次执行前，任务名称："+task.getName());
//        variables.put(ProcessUtils.FLOW_DATA_KEY, flowData);
//        taskService.complete(task.getId(), variables);
//        taskService.complete(taskId);
        
        return ;
    }
    
    
    
	//进行审批操作
 	@RequestMapping("sumNode")
 	public Object sumNode(String taskId) {

 		Task curTask = taskService.createTaskQuery().taskId(taskId).singleResult();
 		String assignee = curTask.getAssignee();
 		log.info("当前处理人={},节点名称={},流程实例id={}",assignee,curTask.getName(),curTask.getProcessInstanceId());
 		//获取携带数据
 		Map<String, Object> var = taskService.getVariables(taskId);
 		log.info("携带数据={}",var);

 		log.info("开始提交数据");
 		 //设置form表数据
        Map<String, String> formProperties = new HashMap<>(); 
        BeanUtils.copyProperties(var, formProperties);
        formProperties.put("yesOrNo", taskId);
        formProperties.put("dept", "true");
        formService.saveFormData(taskId, formProperties);
        
        formService.submitTaskFormData(taskId, formProperties);
 		return null;
 	}
 	
 	@RequestMapping("goBackBytaskId")
 	public Object goBackBytaskId(@RequestParam("sTaskId") String sTaskId,@RequestParam("tTaskId") String tTaskId) {
 		Task curTask =  taskService.createTaskQuery().taskId(sTaskId).singleResult();
// 		curTask.get
 		String curUser = curTask.getAssignee();//当前处理人
 		
 		String executionId = curTask.getExecutionId();
 		
 		String processInstanceId = curTask.getProcessInstanceId();  

 		Execution execution = runtimeService.createExecutionQuery().executionId(executionId).singleResult();
 		
 		HistoricTaskInstance histTask = historyService.createHistoricTaskInstanceQuery()
 											.taskId(tTaskId).singleResult();
 		
 		HistoricActivityInstance  acIn = historyService.createHistoricActivityInstanceQuery()
 						.processInstanceId(processInstanceId).executionId(histTask.getExecutionId())
 						.taskAssignee(histTask.getAssignee()).activityName(histTask.getName())
 						.singleResult();
 		
 		String targetNodeId = acIn.getActivityId();
 		log.info("targetNodeId={}",targetNodeId);
// 		if(true) {
// 			return  null;
// 		}
 		String definitionId = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult().getProcessDefinitionId();  
// 		ProcessUtils.getFlowElement(targetNodeId, definitionId);
 		Map<String,Object> var = taskService.getVariables(sTaskId);
 		ProcessUtils.nodeJumpTo(sTaskId, targetNodeId, curUser, var, "驳回【" + curUser + "】环节");
 		log.info("nodeId/activitiId={}",execution.getActivityId());
// 		String definitionId = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult().getProcessDefinitionId();  
 		//多实例还是单实例
 		int taskType = ProcessUtils.getTaskType(execution.getActivityId(),definitionId);
 		if (taskType == ProcessUtils.USER_TASK_TYPE_NORMAL) {
				log.info("流程实例id={},目标节点ID={}",processInstanceId,execution.getActivityId());
//				String targetNodeId = execution.getActivityId();
				HistoricActivityInstance historicActivityInstance = historyService.createHistoricActivityInstanceQuery()
                    .processInstanceId(processInstanceId).activityId(targetNodeId).finished().singleResult();
//				
	            String assignee = historicActivityInstance.getAssignee();
	//            flowData.setNextUser(assignee);
	            FlowElement flowElement = ProcessUtils.getFlowElement(targetNodeId, processInstanceId);
	     		log.info("获取到目标节点{}",flowElement);
//	     		Map<String,Object> var = taskService.getVariables(sTaskId);
	     		String name;
	     		if(flowElement==null) {
	     			name = "ceshi ";
	     		}else {
	     			name = flowElement.getName();
	     		}
	     		ProcessUtils.nodeJumpTo(sTaskId, targetNodeId, curUser, var, "驳回【" + name + "】环节");
			}else {
				//开始进行多实例回退
//				throw new RuntimeException("目前不支持多任务实例驳回");
				log.info("流程实例id={},目标节点ID={}",processInstanceId,execution.getActivityId());
//				String targetNodeId = execution.getActivityId();
//				HistoricActivityInstance historicActivityInstance = historyService.createHistoricActivityInstanceQuery()
//                    .processInstanceId(processInstanceId).activityId(targetNodeId).finished().singleResult();
//	            String assignee = historicActivityInstance.getAssignee();
	//            flowData.setNextUser(assignee);
	            FlowElement flowElement = ProcessUtils.getFlowElement(targetNodeId, processInstanceId);
	     		log.info("获取到目标节点{}",flowElement);
//	     		Map<String,Object> var = taskService.getVariables(sTaskId);
	     		String name;
	     		if(flowElement==null) {
	     			name = "ceshi ";
	     		}else {
	     			name = flowElement.getName();
	     		}
	     		ProcessUtils.nodeJumpTo(sTaskId, targetNodeId, curUser, var, "驳回【" + name + "】环节");
				
			}
 		
 		 //获取流程发布Id信息   
        

 		//获取全部流程节点信息
        Process process = ProcessUtils.getProcess(definitionId);
        process.getFlowElements().forEach(x->{
        	log.info("id={},name={},class={},subProcess={}",x.getId(),x.getName(),x.getClass().toString(),x.getSubProcess());
        	if(x instanceof SubProcess) {
        		SubProcess sp = (SubProcess) x;
        		sp.getFlowElements().forEach(spItem->{
        			log.info("子流程id={},name={},class={},subProcess={}",
        					spItem.getId(),spItem.getName(),spItem.getClass().toString(),spItem.getSubProcess());
        		});
        	}
        });
        

//        //获取跳转任务节点处理人
//        HistoricActivityInstance historicActivityInstance = historyService.createHistoricActivityInstanceQuery()
//                .processInstanceId(processInstanceId).activityId(targetNodeId).finished().singleResult();
//			
//        String assignee = historicActivityInstance.getAssignee();
        

  

 		
 		log.info(curUser);
 		
 		
 		return null;
 		
 	}
 	
 	/**
 	 * 节点回退
 	 * @return
 	 */
 	@RequestMapping("goBack")
 	public Object goBack(String sourceTaskId,String targetNodeId) {
 		
 		final String currentUser = "zhuanfa";
 		
 		Map<String,Object> var = taskService.getVariables(sourceTaskId);
 		FlowData flowData = ProcessUtils.getFlowData(var);
 		String name = null;
 		//设置节点处理人员
 		if(targetNodeId.equals(flowData.getFirstNodeId())) {
 			flowData.setFirstNode(true);
 			flowData.setStartUser(flowData.getStartUser());
 		}else {
 			int taskType = ProcessUtils.getTaskType(targetNodeId, flowData.getProcessDefinitionId());
 			if (taskType == ProcessUtils.USER_TASK_TYPE_NORMAL) {
 				log.info("流程实例id={},目标节点ID={}",flowData.getProcessInstanceId(),targetNodeId);
// 				
 				HistoricActivityInstance historicActivityInstance = historyService.createHistoricActivityInstanceQuery()
                        .processInstanceId(flowData.getProcessInstanceId()).activityId(targetNodeId).finished().singleResult();
// 				
                String assignee = historicActivityInstance.getAssignee();
                flowData.setNextUser(assignee);
 			}else {
 				throw new RuntimeException("目前不支持多任务实例驳回");
 			}
 		}
 		FlowElement flowElement = ProcessUtils.getFlowElement(targetNodeId, flowData.getProcessDefinitionId());
 		log.info("获取到目标节点{}",flowElement);
 		
 		if(flowElement==null) {
 			name = "ceshi ";
 		}else {
 			name = flowElement.getName();
 		}
 		ProcessUtils.nodeJumpTo(sourceTaskId, targetNodeId, currentUser, var, "驳回【" + name + "】环节");

 		
 		
 		return null;
 	}
 	
 	
 	@RequestMapping("doTasks") 
 	public List<TaskRepresentation> doTasks(String id){
 		List<HistoricTaskInstance> list =  historyService.createHistoricTaskInstanceQuery()
 						.processDefinitionId("")
 						.taskAssignee(id)
 						.finished()
 						.list();
 		List<TaskRepresentation> dtos = new ArrayList<TaskRepresentation>();
 		list.forEach(x->{
 			dtos.add(new TaskRepresentation(x.getId(), x.getName()));
 		});
 		
 		return dtos;
 		
 	}
 	
 	
 	 //获取某人待办项
 	@RequestMapping(value="/tasks", method= RequestMethod.GET)
    public List<TaskRepresentation> getTasks(@RequestParam String id) {
 		
 	// 实际情况需判断是否是会签节点，即多人审批节点
 	// boolean isMultiInstanceNode = userTask.getLoopCharacteristics() != null;
 		
        List<Task> tasks = myService.getTasks(id);
        List<TaskRepresentation> dtos = new ArrayList<TaskRepresentation>();
        for (Task task : tasks) {
        	log.info("{}",task);
        	log.info("待办项{},所属流程id={},流程定义id=",task,task.getProcessInstanceId(),task.getProcessInstanceId());
            dtos.add(new TaskRepresentation(task.getId(), task.getName()));
        }
        return dtos;
    }
 	
 	//获取流程运行节点信息
 	@RequestMapping("getNodeInfo")
 	public Object getProcessInfo(String pId) {
 		List<HistoricActivityInstance>  list= historyService.createHistoricActivityInstanceQuery() // 创建历史活动实例查询
        .processInstanceId(pId) // 执行流程实例id
        .finished()
        .list();
 		
 		for(HistoricActivityInstance hai:list){
// 			hai.get
            System.out.println("活动ID:"+hai.getId());
            System.out.println("流程实例ID:"+hai.getProcessInstanceId());
            System.out.println("活动名称："+hai.getActivityName());
            System.out.println("办理人："+hai.getAssignee());
            System.out.println("开始时间："+hai.getStartTime());
            System.out.println("结束时间："+hai.getEndTime());
            System.out.println("=================================");
        }
 		return null;
 	}
 	
 	@RequestMapping("getAllNodeInfo")
 	public Object getAllNodeInfo(String pid) {
 		//流程定义id
 		String processDefinitionId = runtimeService.createProcessInstanceQuery().processInstanceId(pid).singleResult().getProcessDefinitionId();
 		BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
 		org.activiti.bpmn.model.Process process = bpmnModel.getProcesses().get(0);
 		//获取所有节点
 		Collection<FlowElement> flowElements = process.getFlowElements();
 		//只获取任务节点
// 		List<UserTask> UserTaskList = process.findFlowElementsOfType(UserTask.class);
 		
 		flowElements.forEach(x->{
 			log.info("节点信息={},节点名称={}",x.getClass().getName(),x.getName());
 		});
 		
 		return null;
 	}
    
   
    
 
    @RequestMapping("/firstDemo")
    public void firstDemo() {
 
        //根据bpmn文件部署流程
        Deployment deployment = repositoryService.createDeployment().addClasspathResource("processes/demo2.bpmn").deploy();
        //获取流程定义
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
        //启动流程定义，返回流程实例
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("id", "123");
        
        ProcessInstance pi = runtimeService.startProcessInstanceById(processDefinition.getId(),variables);
        String processId = pi.getId();
        System.out.println("流程创建成功，当前流程实例ID："+processId);
 
        
       
        Task task=taskService.createTaskQuery().processInstanceId(processId).singleResult();
        //设置form表数据
        Map<String, String> formProperties = new HashMap<>(); 
        formProperties.put("yesOrNo", "20150601");
        formService.saveFormData(task.getId(), formProperties);
        
        System.out.println("第一次执行前，任务名称："+task.getName());
        formService.submitTaskFormData(task.getId(), formProperties);
//        taskService.complete(task.getId());
 
        task = taskService.createTaskQuery().processInstanceId(processId).singleResult();
        //获取form数据
        TaskFormData startFormData = formService.getTaskFormData(task.getId());
        List<FormProperty> formValue = startFormData.getFormProperties();
        log.info("第一次获取表单数据长度={}",formValue.size());
        formValue.forEach(x->{
        	log.info("第一次获取表单数据={}",x.getName()+"-"+x.getValue()+"-"+x.getId());
        });
        
        System.out.println("第二次执行前，任务名称："+task);
//        formService.saveFormData(task.getId(), formProperties);
//        taskService.complete(task.getId());
        formService.submitTaskFormData(task.getId(), formProperties);
        //第二次审批获取form数据
       
        task = taskService.createTaskQuery().processInstanceId(processId).singleResult();
        startFormData = formService.getTaskFormData(task.getId());
        formValue = startFormData.getFormProperties();
        log.info("第二次获取表单数据长度={}",formValue.size());
        formValue.forEach(x->{
        	log.info("第二次获取表单数据={}",x.getName()+":"+x.getValue());
        });
        System.out.println("task为null，任务执行完毕："+task);
    }
=======
package com.example.activiti.controller;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo")
public class DemoController {
    @Autowired
    //Activiti 中每一个不同版本的业务流程的定义都需要使用一些定义文件，部署文件和支持数据 ( 例如 BPMN2.0 XML 文件，表单定义文件，流程定义图像文件等 )，
//    这些文件都存储在 Activiti 内建的 Repository 中。Repository Service 提供了对 repository 的存取服务。
    private RepositoryService repositoryService;
    @Autowired
    //在 Activiti 中，每当一个流程定义被启动一次之后，都会生成一个相应的流程对象实例。
//    Runtime Service 提供了启动流程、查询流程实例、设置获取流程实例变量等功能。此外它还提供了对流程部署，流程定义和流程实例的存取服务。
    private RuntimeService runtimeService;
    @Autowired
    //在 Activiti 中业务流程定义中的每一个执行节点被称为一个 Task，对流程中的数据存取，状态变更等操作均需要在 Task 中完成。
//    Task Service 提供了对用户 Task 和 Form 相关的操作。它提供了运行时任务查询、领取、完成、删除以及变量设置等功能
    private TaskService taskService;
    
    
    @Autowired
//    Activiti 中内置了用户以及组管理的功能，必须使用这些用户和组的信息才能获取到相应的 Task。
//    Identity Service 提供了对 Activiti 系统中的用户和组的管理功能。
    IdentityService identityService;
    
    @Autowired
    //通过使用 Form Service 可以存取启动和完成任务所需的表单数据并且根据需要来渲染表单
    FormService fromService;
    
    @Autowired
//    Management Service 提供了对 Activiti 流程引擎的管理和维护功能
//    ，这些功能不在工作流驱动的应用程序中使用，主要用于 Activiti 系统的日常维护。
    ManagementService managementService;
    
    @Autowired
//    History Service 用于获取正在运行或已经完成的流程实例的信息，与 Runtime Service 
//    中获取的流程信息不同，历史信息包含已经持久化存储的永久信息，并已经被针对查询优化。
    HistoryService historyService;
 
    @RequestMapping("/firstDemo")
    public void firstDemo() {
 
        //根据bpmn文件部署流程
        Deployment deployment = repositoryService.createDeployment().addClasspathResource("processes/demo2.bpmn").deploy();
        //获取流程定义
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
        //启动流程定义，返回流程实例
        ProcessInstance pi = runtimeService.startProcessInstanceById(processDefinition.getId());
        String processId = pi.getId();
        System.out.println("流程创建成功，当前流程实例ID："+processId);
 
        Task task=taskService.createTaskQuery().processInstanceId(processId).singleResult();
        System.out.println("第一次执行前，任务名称："+task.getName());
        taskService.complete(task.getId());
 
        task = taskService.createTaskQuery().processInstanceId(processId).singleResult();
        System.out.println("第二次执行前，任务名称："+task.getName());
        taskService.complete(task.getId());
 
        task = taskService.createTaskQuery().processInstanceId(processId).singleResult();
        System.out.println("task为null，任务执行完毕："+task);
    }
>>>>>>> 6b5f00b783b940a8a7d8952004aaa395719ffcb0
}