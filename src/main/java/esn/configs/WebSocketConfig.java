package esn.configs;

import esn.db.OrganizationDAO;
import esn.entities.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.List;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private OrganizationDAO orgDAO;
    private List<Organization> allOrgs;
    @Autowired
    public void setOrgDAO(OrganizationDAO orgDAO) {
        this.orgDAO = orgDAO;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        for (Organization org: allOrgs) {
            registry.addEndpoint("/" + org.getUrlName() + "/ws/netstatus").withSockJS();
            registry.addEndpoint("/" + org.getUrlName() + "/ws/genchatmessage").withSockJS();
        }

    }

    @Override
    public void configureMessageBroker(org.springframework.messaging.simp.config.MessageBrokerRegistry registry) {
        allOrgs = orgDAO.getAllOrgs();
        for (Organization org : allOrgs) {
            registry.enableSimpleBroker("/" + org.getUrlName() + "ws/contlist");
            registry.setApplicationDestinationPrefixes("/" + org.getUrlName() + "ws/app");
        }

    }


}
