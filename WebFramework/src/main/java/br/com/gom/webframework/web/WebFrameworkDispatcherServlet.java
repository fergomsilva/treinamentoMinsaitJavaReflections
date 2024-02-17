package br.com.gom.webframework.web;

import java.io.IOException;

import br.com.gom.webframework.util.WebFrameworkLogger;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class WebFrameworkDispatcherServlet extends HttpServlet{

    @Override
    protected void service(final HttpServletRequest req, final HttpServletResponse resp) 
    throws ServletException, IOException{
        // ignorar o favIcon
        if( req.getRequestURL().toString().endsWith( "favicon.ico" ) )
            return;

        final String url = req.getRequestURL().toString();
        final String httpMethod = req.getMethod().toUpperCase();
        WebFrameworkLogger.log( "", ( "URL: " + url + " (" + httpMethod + ")" ) );
    }

}