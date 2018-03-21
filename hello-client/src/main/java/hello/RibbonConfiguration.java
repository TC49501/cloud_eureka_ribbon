package hello;

import com.netflix.loadbalancer.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import com.netflix.client.config.IClientConfig;

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
    return new FDAvailabilityFilteringRule();
  }

}

