package UDM.api;

import org.eclipse.jetty.alpn.ALPN;
import org.eclipse.jetty.alpn.server.ALPNServerConnectionFactory;
import org.eclipse.jetty.http2.HTTP2Cipher;
import org.eclipse.jetty.http2.server.HTTP2ServerConnectionFactory;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.glassfish.jersey.servlet.ServletContainer;

//Handles request coming from other network functions
public class UDMServer extends Thread implements Runnable {
    private int portNumber;

    UDMServer(int portNumber) {
        this.portNumber = portNumber;
    }

    @Override
    public void run() {
        try {
            createServer();
        } catch (Exception e) {
            System.out.println("Exception Occured in Server thread.");
        }
    }

    //Creates a Http2 servlet server using Jetty and Jersey
    private void createServer() throws Exception {
        Server server = new Server(); //jetty http servlet server
        System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        context.setContextPath("/");

        server.setHandler(context);
        ServletHolder jerseyServlet = context.addServlet(ServletContainer.class, "/*");

        jerseyServlet.setInitParameter("jersey.config.server.provider.packages", "UDM.resources");

        // HTTP Configuration
        HttpConfiguration http_config = new HttpConfiguration();
        http_config.setSecureScheme("https");
        http_config.setSecurePort(portNumber);

        // SSL Context Factory for HTTPS and HTTP/2
        SslContextFactory sslContextFactory = new SslContextFactory();
        sslContextFactory.setKeyStoreResource(Resource.newClassPathResource("keystore"));
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
        SslConnectionFactory ssl = new SslConnectionFactory(sslContextFactory, alpn.getProtocol());

        // HTTP/2 Connector
        ServerConnector http2Connector =
                new ServerConnector(server, ssl, alpn, h2, new HttpConnectionFactory(https_config));
        http2Connector.setPort(portNumber);
        server.addConnector(http2Connector);

        ALPN.debug = false;

        server.start();

        server.join();
    }
}