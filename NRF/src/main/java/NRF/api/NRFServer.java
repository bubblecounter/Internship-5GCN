package NRF.api;

import org.eclipse.jetty.alpn.ALPN;
import org.eclipse.jetty.alpn.server.ALPNServerConnectionFactory;
import org.eclipse.jetty.http2.HTTP2Cipher;
import org.eclipse.jetty.http2.server.HTTP2ServerConnectionFactory;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.glassfish.jersey.servlet.ServletContainer;

import static org.eclipse.jetty.util.resource.Resource.newClassPathResource;

//Handles request coming from other network functions
public class NRFServer{
    private int portNumber;

    NRFServer(int portNumber) {
        this.portNumber = portNumber;
    }

    //Creates a Http2 servlet server using Jetty and Jersey
    public void createServer(){
        System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
        Server server = new Server(); //jetty http servlet server

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        context.setContextPath("/");

        server.setHandler(context);
        ServletHolder jerseyServlet = context.addServlet(ServletContainer.class, "/*");

        jerseyServlet.setInitParameter("jersey.config.server.provider.packages", "NRF.resources");

        // HTTP Configuration
        HttpConfiguration http_config = new HttpConfiguration();
        http_config.setSecureScheme("https");
        http_config.setSecurePort(portNumber);

        // SSL Context Factory for HTTPS and HTTP/2
        SslContextFactory sslContextFactory = new SslContextFactory();
        sslContextFactory.setKeyStoreResource(newClassPathResource("keystore"));
        sslContextFactory.setKeyStorePassword("OBF:1vny1zlo1x8e1vnw1vn61x8g1zlu1vn4");
        sslContextFactory.setKeyManagerPassword("OBF:1u2u1wml1z7s1z7a1wnl1u2g");
        sslContextFactory.setCipherComparator(HTTP2Cipher.COMPARATOR);

        // HTTPS Configuration
        HttpConfiguration https_config = new HttpConfiguration(http_config);
        https_config.addCustomizer(new SecureRequestCustomizer());

        // HTTP/2 Connection Factory
        HTTP2ServerConnectionFactory h2 = new HTTP2ServerConnectionFactory(https_config);
        NegotiatingServerConnectionFactory.checkProtocolNegotiationAvailable();
        ALPNServerConnectionFactory alpn = new ALPNServerConnectionFactory();
        alpn.setDefaultProtocol("h2");

        // SSL Connection Factory
        SslConnectionFactory ssl = new SslConnectionFactory(sslContextFactory,alpn.getProtocol());

        // HTTP/2 Connector
        ServerConnector http2Connector =
                new ServerConnector(server,ssl,alpn,h2,new HttpConnectionFactory(https_config));
        http2Connector.setPort(portNumber);
        server.addConnector(http2Connector);

        ALPN.debug=false;

        try {
            server.start();
            System.out.println("NRF ");
            System.out.println("NRF address: https://localhost:"+portNumber);
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
