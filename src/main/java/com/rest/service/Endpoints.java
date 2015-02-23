package com.rest.service;
 
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.login.LoginException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.rest.service.mongodb.UserRepository;
import com.rest.service.mongodb.domain.ComponentStatus;
import com.rest.service.mongodb.domain.ServiceAuth;
import com.rest.service.mongodb.domain.User;
import com.rest.service.security.Authenticator;
import com.rest.service.security.UserCredential;
import com.rest.service.util.CommandUtil;
import com.rest.service.util.DataUtil;

/**
 * Root resource 
 */
@Path("/")
public class Endpoints {
 
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String ping() {
        return "Hey, This is REST API !";
    }

    /**
     * Method handling the endpoint that authenticates a user based on a login/password passwind in JSON
     * payload and verify against the mongodb data
     *
     * @return String that will be returned as a text/plain with the auth_token.
     */    
    @POST  
    @Path("login")  
    @Consumes(MediaType.APPLICATION_JSON)  
    public Response login(@Context HttpHeaders httpHeaders, UserCredential userInfo) {
        String serviceKey = httpHeaders.getHeaderString( "service_key" );
        String userName = userInfo.getName();
        String password = userInfo.getPassword();
        String token = null;
     
        try {     
            Authenticator authenticator = Authenticator.getInstance();     
            token = authenticator.login(serviceKey, userName, password);
        } catch (LoginException e) {
            return getNoCacheResponseBuilder( Response.Status.INTERNAL_SERVER_ERROR ).build();
        }

        if(token == null)
            return getNoCacheResponseBuilder( Response.Status.UNAUTHORIZED).build();
     
        return getNoCacheResponseBuilder( Response.Status.OK ).entity( token ).build(); 
    }  
 

    @GET 
    @Path("allusers/field/{field}/value/{value}/group/{group}")
    @Produces(MediaType.APPLICATION_JSON)
    //  http://<IP address>:8080/restapi/v1/allusers/field/profession/value/Engineer/group/city
    public List<User> getAllUsers(@Context HttpHeaders httpHeaders, @PathParam("field") String field, @PathParam("value") String value, @PathParam("group") String group) {
        String serviceKey = httpHeaders.getHeaderString( "service_key" );
        String authToken = httpHeaders.getHeaderString( "auth_token");

        List<User> users = null;
        try {

            if(!Authenticator.getInstance().isAuthTokenValid(serviceKey, authToken)) 
                return users;
     
            UserRepository userRepository = DataUtil.getUserRepository();
            users = userRepository.getUsers(field, value, group);
        }
        catch (Throwable t) {
            throw new WebApplicationException(t.getMessage());
        }
        return users;
    }

 
    @GET  
    @Path("filelist")  
    @Produces(MediaType.APPLICATION_JSON) 
    public List<String> getFileList(@Context HttpHeaders httpHeaders, @QueryParam("directory") String directory) {  
        String serviceKey = httpHeaders.getHeaderString( "service_key" );
        String authToken = httpHeaders.getHeaderString( "auth_token");

        List<String> files = new ArrayList<String>();
        try {
            if(!Authenticator.getInstance().isAuthTokenValid(serviceKey, authToken)) 
                return files;
     
            File dirObj = new File(directory);
            if (dirObj.isDirectory() == true) {        
                File[] fileList = dirObj.listFiles();
                for (File file : fileList) {
                    files.add(file.getName());
                }
            }
        }
        catch (Throwable t) {
            throw new WebApplicationException(t.getMessage());         
        }
     
        return files;  
    }  

    @GET  
    @Path("componentsstatus")  
    @Produces(MediaType.APPLICATION_JSON) 
    // http://<IP address>:8080/testWebApp/rest/v1/status
    public List<ComponentStatus> getComponentsStatus(@Context HttpHeaders httpHeaders) { 
        String serviceKey = httpHeaders.getHeaderString( "service_key" );
        String authToken = httpHeaders.getHeaderString( "auth_token");

        List<ComponentStatus> components = new ArrayList<ComponentStatus>();    
        try {
            if(!Authenticator.getInstance().isAuthTokenValid(serviceKey, authToken)) 
                return components;
     
            String status = CommandUtil.executeCommand("/sbin/service mongod status");

            components.add(new ComponentStatus("Mongo",status));
        }
        catch (Throwable t) {
            throw new WebApplicationException(t.getMessage());                  
        }
     
        return components;
    }  
 
 
    private Response.ResponseBuilder getNoCacheResponseBuilder( Response.Status status ) {
        CacheControl cc = new CacheControl();
        cc.setNoCache( true );
        cc.setMaxAge( -1 );
        cc.setMustRevalidate( true );
        return Response.status( status ).cacheControl( cc );
    }

}