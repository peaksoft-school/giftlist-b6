package kg.peaksoft.giftlistb6;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@SpringBootApplication
public class GiftlistB6Application {

	public static void main(String[] args) {
		SpringApplication.run(GiftlistB6Application.class, args);
		System.out.println("Welcome colleagues, project name is Giftlist!");
	}

	@GetMapping("/")
	public String greetingPage(){
		return "welcome";
	}
}
