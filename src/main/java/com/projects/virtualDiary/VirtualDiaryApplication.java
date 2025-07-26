package com.projects.virtualDiary;

//import com.projects.virtualDiary.Data.DBinitialData;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class VirtualDiaryApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(VirtualDiaryApplication.class, args);
	}

}
