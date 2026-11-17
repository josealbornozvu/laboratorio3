package com.udea.lab3demomcp;

import com.udea.lab3demomcp.service.TareaToolsService;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.util.ReflectionUtils;

@SpringBootApplication
public class Lab3demomcpApplication {

	public static void main(String[] args) {
		SpringApplication.run(Lab3demomcpApplication.class, args);
	}
	/**  Registra los metodos anotados con @Tool del bean TareaTools
	 como herramienta MCP disponible para elciente (Claude Desktop)

	 */

	@Bean
	public ToolCallbackProvider tareaTools(TareaToolsService tools){
		return MethodToolCallbackProvider.builder()
				.toolObjects(tools)
				.build();
	}

}
