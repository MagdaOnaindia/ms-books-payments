package com.msbookspayments.microservice.facade;

import com.msbookspayments.microservice.facade.model.Book;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@Slf4j
public class BooksFacade {

	private final String getBookUrl;
	private final WebClient.Builder webClientBuilder;

	@Autowired
	public BooksFacade(
			@Qualifier("loadBalancedWebClientBuilder") WebClient.Builder webClientBuilder,
			@Value("${getBook.url}") String getBookUrl
	) {
		this.webClientBuilder = webClientBuilder;
		this.getBookUrl = getBookUrl;
	}

	  public Book getBook(String id) {

	    try {
	      String url = String.format(getBookUrl, id);
	      log.info("Getting book with ID {}. Request to {}", id, url);
	      return webClientBuilder.build()
	              .get()
	              .uri(url)
	              .retrieve()
	              .bodyToMono(Book.class)
	              .block();
	    } catch (HttpClientErrorException e) {
	      log.error("Client Error: {}, Book with ID {}", e.getStatusCode(), id);
	      return null;
	    } catch (HttpServerErrorException e) {
	      log.error("Server Error: {}, Book with ID {}", e.getStatusCode(), id);
	      return null;
	    } catch (Exception e) {
	      log.error("Error: {}, Book with ID {}", e.getMessage(), id);
	      return null;
	    }
	  }


}
