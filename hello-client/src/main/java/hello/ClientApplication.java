package hello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.ribbon.RibbonClient;

/**
 * Ribbon client app to test client side load balancing.
 */
@SpringBootApplication
@RestController
@RibbonClient(name = "hello-service", configuration = RibbonConfiguration.class)
public class ClientApplication {
  private static Logger log = LoggerFactory.getLogger(ClientApplication.class);

  @LoadBalanced
  @Bean
  RestTemplate restTemplate(){
    return new RestTemplate();
  }

  @Autowired
  private LoadBalancerClient loadBalancer;

  /**
   * Expose REST endpoint
   * Make external Hello service call using client side load balancing with Ribbon
   * @param name
   * @return
   */
  @RequestMapping("/hello")
  public String hello(@RequestParam(value="name", defaultValue="Thiru") String name) {
    log.info("Access /hello");

    String greeting = restTemplate().getForObject("http://hello-service/greeting", String.class);
    return String.format("%s, %s!", greeting, name);
  }

  /**
   *
   * @param name
   * @return
   */
  @RequestMapping("/hello2")
  public String hello2(@RequestParam(value="name", defaultValue="Thiru") String name) {
    log.info("Access /hello2");

    ServiceInstance serviceInstance = loadBalancer.choose("hello-service");
    log.info(String.valueOf(serviceInstance.getUri()));

    String baseUrl = serviceInstance.getUri().toString();
    baseUrl = baseUrl+"/greeting";

    String greeting = new RestTemplate().getForObject(baseUrl, String.class);
    return String.format("%s, %s!", greeting, name);
  }

  /**
   *
   * @param args
   */
  public static void main(String[] args) {
    SpringApplication.run(ClientApplication.class, args);
  }
}

