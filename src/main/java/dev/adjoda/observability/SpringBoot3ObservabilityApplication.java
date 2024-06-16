package dev.adjoda.observability;

import dev.adjoda.observability.post.JsonPlaceholderService;
import dev.adjoda.observability.post.Post;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.annotation.Observed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.List;

@SpringBootApplication
@Slf4j
public class SpringBoot3ObservabilityApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBoot3ObservabilityApplication.class, args);
	}

	@Bean
	JsonPlaceholderService jsonPlaceholderService() {
		RestClient restClient = RestClient.create("https://jsonplaceholder.typicode.com");
		HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClient)).build();
		return factory.createClient(JsonPlaceholderService.class);

	}
	
	@Bean
	@Observed(name = "posts.load-all-posts", contextualName = "post-service.load-all-posts")
	CommandLineRunner commandLineRunner(JsonPlaceholderService jsonPlaceholderService) {
		return args -> {
			jsonPlaceholderService.findAll();
		};
	}

}
