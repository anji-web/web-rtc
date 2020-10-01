package com.example.webrtc.service;

import com.example.webrtc.entity.UserCredentials;
import com.example.webrtc.entity.UserRegister;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class KeycloakService {

    @Value("${keycloak.auth-server-url}")
    private String url;

    @Value("${keycloak.realm}")
    private String REALM;

    @Value("${keycloak.resource}")
    private String clientId;

    @Value("${keycloak.credentials.secret}")
    private String clientSecret;

    private static final Logger logger = LoggerFactory.getLogger(KeycloakService.class);


    private UsersResource getUserResource(){
        Keycloak keycloak = KeycloakBuilder.builder().serverUrl(url).realm("master").username("secretcode").password("2827180315")
                .clientId("admin-cli").resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).build()).build();
        RealmResource realmResource = keycloak.realm(REALM);
        UsersResource usersResource = realmResource.users();
        return usersResource;
    }

    private RealmResource getRealmResource(){
        Keycloak keycloak = KeycloakBuilder.builder().serverUrl(url).realm("master").username("secretcode").password("2827180315")
                .clientId("admin-cli").resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).build()).build();
        RealmResource realmResource = keycloak.realm(REALM);
        return realmResource;
    }

    public int createUserResource(UserRegister userRegister){
        int statusId = 0;
        try {

            UsersResource usersResource = getUserResource();
            UserRepresentation userRepresentation = new UserRepresentation();
            userRepresentation.setUsername(userRegister.getUsername());
            userRepresentation.setEmail(userRegister.getEmail());
            userRepresentation.setFirstName(userRegister.getFirstName());
            userRepresentation.setLastName(userRegister.getLastName());
            userRepresentation.setEnabled(true);

            Response result = usersResource.create(userRepresentation);
            statusId = result.getStatus();

            if (statusId == 201){
                String UID = result.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
                logger.info("UID Path : " + UID);

//            define password

                CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
                credentialRepresentation.setTemporary(false);
                credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
                credentialRepresentation.setValue(userRegister.getPassword());

//            set password
                usersResource.get(UID).resetPassword(credentialRepresentation);

//            set roles

                RealmResource realmResource = getRealmResource();
                RoleRepresentation savedRole = realmResource.roles().get("bank-user").toRepresentation();
                realmResource.users().get(UID).roles().realmLevel().add(Arrays.asList(savedRole));

                logger.info("Username " + userRegister.getUsername() + " created in keycloak successfully");
            }else if (statusId == 409){
                logger.info("Username " + userRegister.getUsername() + " already present in  keycloak");
            }else {
                logger.info("Username " + userRegister.getUsername() + " cannot created in  keycloak");
            }

        }catch (RuntimeException e){
            logger.error("Create user Error : " + e.getMessage());
        }

    return statusId;

    }

    public String getToken(UserCredentials userCredentials)throws Exception{
        String response = null;
        try {

            String username = userCredentials.getUsername();
            List<NameValuePair> urlsParam = new ArrayList<>();
            urlsParam.add(new BasicNameValuePair("grant_type", "password"));
            urlsParam.add(new BasicNameValuePair("client_id", clientId));
            urlsParam.add(new BasicNameValuePair("username", username));
            urlsParam.add(new BasicNameValuePair("password", userCredentials.getPassword()));
            urlsParam.add(new BasicNameValuePair("client_secret", clientSecret));

            response = sendPost(urlsParam);
        }catch (RuntimeException e){
            e.printStackTrace();
            logger.error("Get token error : " + e.getMessage());
        }
        return response;
    }




    private String sendPost(List<NameValuePair> urlsParam) throws Exception {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(url + "/realms/" + REALM + "/protocol/openid-connect/token");
        post.setEntity(new UrlEncodedFormEntity(urlsParam));

        HttpResponse response = client.execute(post);

        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";

        while ((line = reader.readLine()) != null){
            result.append(line);
        }

        return result.toString();

    }




}
