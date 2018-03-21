package hello;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.RoundRobinRule;
import com.netflix.loadbalancer.Server;

/**
 * Created by thiru on 3/16/18.
 */
public class FDClientConfigEnabledRoundRobinRule extends AbstractLoadBalancerRule {
    RoundRobinRule roundRobinRule = new RoundRobinRule();

    public FDClientConfigEnabledRoundRobinRule() {
    }

    public void initWithNiwsConfig(IClientConfig clientConfig) {
        this.roundRobinRule = new RoundRobinRule();
    }

    public void setLoadBalancer(ILoadBalancer lb) {
        super.setLoadBalancer(lb);
        this.roundRobinRule.setLoadBalancer(lb);
    }

    public Server choose(Object key) {
        if(this.roundRobinRule != null) {
            return this.roundRobinRule.choose(key);
        } else {
            throw new IllegalArgumentException("This class has not been initialized with the RoundRobinRule class");
        }
    }
}
