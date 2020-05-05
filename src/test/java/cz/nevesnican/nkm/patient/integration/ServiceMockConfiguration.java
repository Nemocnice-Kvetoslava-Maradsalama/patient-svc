package cz.nevesnican.nkm.patient.integration;

import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ServerList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.ribbon.StaticServerList;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

@ComponentScan(basePackages = "cz.nevesnican.nkm.patient")
public class ServiceMockConfiguration {
    @Autowired
    Environment env;

    @Bean
    public ServerList<Server> ribbonServerList() {
        return new StaticServerList<>(new Server("127.0.0.1", Integer.parseInt(this.env.getProperty("wiremock.server.port"))));
    }
}
