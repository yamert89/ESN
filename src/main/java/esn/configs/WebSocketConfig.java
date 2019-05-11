package esn.configs;

import esn.db.OrganizationDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private OrganizationDAO orgDAO;
    private String[] urls;

    @Autowired
    public void setOrgDAO(OrganizationDAO orgDAO) {
        this.orgDAO = orgDAO;
    }

    @Override
    public void configureMessageBroker(org.springframework.messaging.simp.config.MessageBrokerRegistry registry) {
        urls = orgDAO.getAllOrgs().stream().map(el ->  "/allusers" + el.getId())
                .peek(System.out::println).toArray(String[]::new);
        String[] urls2 = new String[urls.length + 2];
        for (int i = 0; i < urls.length; i++){
            urls2[i] = urls[i];
        }
        urls2[urls2.length - 2] = "/message";
        urls2[urls2.length - 1] = "/user";
        registry.enableSimpleBroker(urls2); //mes to client
        registry.setApplicationDestinationPrefixes("/app");
        //registry.setApplicationDestinationPrefixes("/" + org.getUrlName() + "/app");

        System.out.println(" configureMessageBroker");

    }


    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/messages").withSockJS();
        /*urls = orgDAO.getAllOrgs().stream().map(el ->  "/netstatus" + el.getId())
                .peek(System.out::println).toArray(String[]::new);
        registry.addEndpoint(urls);*/

        System.out.println("registerStompEndpoints");

    }



    /*@Bean
    public DefaultHandshakeHandler handshakeHandler() {

        WebSocketPolicy policy = new WebSocketPolicy(WebSocketBehavior.SERVER);
        policy.setInputBufferSize(8192);
        policy.setIdleTimeout(600000);

        return new DefaultHandshakeHandler(
                new JettyRequestUpgradeStrategy(new WebSocketServerFactory(policy)));
    }*/




}
