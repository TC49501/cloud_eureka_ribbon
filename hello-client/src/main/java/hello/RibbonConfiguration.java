package hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.IPing;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.PingUrl;
import com.netflix.loadbalancer.AvailabilityFilteringRule;

/**
 * Ribbon custom Configuration
 */
public class RibbonConfiguration {

  @Autowired
  IClientConfig ribbonClientConfig;

  /**
   *
   * @param config
   * @return
   */
  @Bean
  public IPing ribbonPing(IClientConfig config) {
    return new PingUrl();
  }

  /**
   *
   * @param config
   * @return
   */
  @Bean
  public IRule ribbonRule(IClientConfig config) {
    return new AvailabilityFilteringRule();
  }

}

