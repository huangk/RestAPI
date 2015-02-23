package com.rest.service.security;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.security.GeneralSecurityException;
import javax.security.auth.login.LoginException;

import com.rest.service.mongodb.UserRepository;
import com.rest.service.mongodb.domain.ServiceAuth;
import com.rest.service.mongodb.domain.User;
import com.rest.service.util.DataUtil;

public final class Authenticator {
    private static Authenticator authenticator = null;

    // An authentication token storage which stores <service_key, auth_token>.

    private final Map<String, String> authorizationTokensStorage = new HashMap<String, String>();

    private Authenticator() {
    }


    public static Authenticator getInstance() {

        if ( authenticator == null ) {
            authenticator = new Authenticator();
        }

        return authenticator;
    }
    

    public String login( String serviceKey, String username, String password ) throws LoginException {
        String authToken = null;
        
        try {
            UserRepository userRepository = DataUtil.getUserRepository();        
            ServiceAuth serviceAuth = userRepository.getServiceAuth(username, serviceKey);
        
            if(serviceAuth != null) {
                User user=userRepository.getUser(username, password);
        
                if(user != null) {
                    authToken = UUID.randomUUID().toString();
                    authorizationTokensStorage.put( authToken, username );
                }
            }
        }
        catch (Throwable t) {
            throw new LoginException(t.getMessage());
        }
        return authToken;
    }

    
    /**
     * The method that pre-validates if the client which invokes the REST API is
     * from a authorized and authenticated source.
     *
     * @param serviceKey The service key
     * @param authToken The authorization token generated after login
     * @return TRUE for acceptance and FALSE for denied.
     */

    public boolean isAuthTokenValid( String serviceKey, String authToken ) {
        UserRepository userRepository = DataUtil.getUserRepository();        
        ServiceAuth serviceAuth = userRepository.getServiceAuth(serviceKey);
                
        if ( serviceAuth != null ) {
            String usernameMatch1 = serviceAuth.getUser();
            if ( authorizationTokensStorage.containsKey( authToken ) ) {
                String usernameMatch2 = authorizationTokensStorage.get( authToken );

                if ( usernameMatch1.equals( usernameMatch2 ) ) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * This method checks is the service key is valid
     *
     * @param serviceKey
     * @return TRUE if service key matches the pre-generated ones in service key
     * storage. FALSE for otherwise.
     */

    public boolean isServiceKeyValid( String serviceKey ) {
        boolean isValid = false;
        UserRepository userRepository = DataUtil.getUserRepository();        
        ServiceAuth serviceAuth = userRepository.getServiceAuth(serviceKey);
        
        if(serviceAuth != null)
            isValid = true;
        
        return isValid;
    }

    public void logout( String serviceKey, String authToken ) throws GeneralSecurityException {
        UserRepository userRepository = DataUtil.getUserRepository();        
        ServiceAuth serviceAuth = userRepository.getServiceAuth(serviceKey);
                
        if ( serviceAuth != null ) {
            String usernameMatch1 = serviceAuth.getUser();
            if ( authorizationTokensStorage.containsKey( authToken ) ) {
                String usernameMatch2 = authorizationTokensStorage.get( authToken );

                if ( usernameMatch1.equals( usernameMatch2 ) ) {
                    /**
                     * When a client logs out, the authentication token will be
                     * remove and will be made invalid.
                     */

                    authorizationTokensStorage.remove( authToken );
                    return;
                }

            }
        }

        throw new GeneralSecurityException( "Invalid service key and authorization token match." );
    }

}

