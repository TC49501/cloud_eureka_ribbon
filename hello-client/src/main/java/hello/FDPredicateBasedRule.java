package hello;

import com.google.common.base.Optional;
import com.netflix.loadbalancer.AbstractServerPredicate;
import com.netflix.loadbalancer.ClientConfigEnabledRoundRobinRule;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;

/**
 * Created by thiru on 3/16/18.
 */
public abstract class FDPredicateBasedRule extends FDClientConfigEnabledRoundRobinRule {
    public FDPredicateBasedRule() {
    }

    public abstract AbstractServerPredicate getPredicate();

    public Server choose(Object key) {
        ILoadBalancer lb = this.getLoadBalancer();
        Optional server = this.getPredicate().chooseRoundRobinAfterFiltering(lb.getAllServers(), key);
        return server.isPresent()?(Server)server.get():null;
    }
}
