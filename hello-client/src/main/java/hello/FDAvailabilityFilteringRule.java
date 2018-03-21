package hello;

import com.google.common.collect.Collections2;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.*;
import com.netflix.servo.annotations.DataSourceType;
import com.netflix.servo.annotations.Monitor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Created by thiru on 3/16/18.
 */
public class FDAvailabilityFilteringRule extends FDPredicateBasedRule {
    private AbstractServerPredicate predicate = CompositePredicate.withPredicate(new AvailabilityPredicate(this, (IClientConfig) null)).addFallbackPredicate(AbstractServerPredicate.alwaysTrue()).build();

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public FDAvailabilityFilteringRule() {
    }

    /**
     *
     * @param clientConfig
     */
    public void initWithNiwsConfig(IClientConfig clientConfig) {
        this.predicate = CompositePredicate.withPredicate(new AvailabilityPredicate(this, clientConfig)).addFallbackPredicate(AbstractServerPredicate.alwaysTrue()).build();
    }

    @Monitor(
            name = "AvailableServersCount",
            type = DataSourceType.GAUGE
    )
    /**
     *
     */
    public int getAvailableServersCount() {
        ILoadBalancer lb = this.getLoadBalancer();
        List servers = lb.getAllServers();
        return servers == null ? 0 : Collections2.filter(servers, this.predicate.getServerOnlyPredicate()).size();
    }

    /**
     *
     * @param key
     * @return
     */
    public Server choose(Object key) {
        ILoadBalancer lb = this.getLoadBalancer();
        List<Server> servers = lb.getAllServers();
        for (Server element : servers) {
            //if (isServerAlive(element))
                return element;
        }
        return null;

    }

    /**
     *
     * @param server
     * @return
     */
    private boolean isServerAlive(Server server) {
        String endpoint = "http://" + server.toString();
        ResponseEntity<String> response = restTemplate().getForEntity(endpoint, String.class);
        return response.getStatusCode().equals(HttpStatus.OK);
    }

    /**
     *
     * @return
     */
    public AbstractServerPredicate getPredicate() {
        return this.predicate;
    }
}
