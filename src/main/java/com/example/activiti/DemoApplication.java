package com.example.activiti;

<<<<<<< HEAD
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.springframework.boot.CommandLineRunner;
=======
>>>>>>> 6b5f00b783b940a8a7d8952004aaa395719ffcb0
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
<<<<<<< HEAD
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;

import lombok.extern.slf4j.Slf4j;
@Slf4j
=======

>>>>>>> 6b5f00b783b940a8a7d8952004aaa395719ffcb0
@SpringBootApplication(exclude ={
        org.activiti.spring.boot.SecurityAutoConfiguration.class,
        SecurityAutoConfiguration.class,
        ManagementWebSecurityAutoConfiguration.class
})
<<<<<<< HEAD
@EntityScan("com.example.activiti.entity")
=======
>>>>>>> 6b5f00b783b940a8a7d8952004aaa395719ffcb0
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
<<<<<<< HEAD
	
	@Bean
	public CommandLineRunner init(RepositoryService repositoryService,
									RuntimeService runtimeService,
									TaskService taskService) {
		return new CommandLineRunner() {
			
			@Override
			public void run(String... args) throws Exception {
				
				log.info("Number of process definitions=",repositoryService.createProcessDefinitionQuery().count());
				log.info("Number of Task =",taskService.createTaskQuery().count());
				log.info("Number of tasks after process start: {}" , taskService.createTaskQuery().count());
			}
		};
	}
=======
>>>>>>> 6b5f00b783b940a8a7d8952004aaa395719ffcb0

}
