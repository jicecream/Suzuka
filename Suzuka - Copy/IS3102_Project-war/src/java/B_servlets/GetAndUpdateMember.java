/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author Isaac
 */
@Path("webservice")
public class GetAndUpdateMember {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of TestGit
     */
    public GetAndUpdateMember() {
    }

    /**
     * Retrieves representation of an instance of services.TestGit
     * @param email
     * @param password
     * @return an instance of java.lang.String
     */
    @GET
    @Path("VerifyUser")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response VerifyUser(@QueryParam("email") String email,  
                                      @QueryParam("password") String password) {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            String connURL = "jdbc:mysql://localhost/islandfurniture-it07Zz?user=root&password=12345";
            ResultSet rs;
            boolean result;
            try (Connection conn = DriverManager.getConnection(connURL)) {
                String sqlStr = "select * from login where userid=? and password=?";
                PreparedStatement pstmt = conn.prepareStatement(sqlStr);
                pstmt.setString(1, email);
                pstmt.setString(2, password);
                rs = pstmt.executeQuery();
                result = rs.next();
            }
            
            rs.close();            
             return Response
                    .status(200)
                    .entity("" + result)
                    .build();

        }
        catch(ClassNotFoundException | SQLException e)
        {
            System.out.print("encountered error.." + e.toString());
            return null;
        }      
    }

    /**
     * PUT method for updating or creating an instance of TestGit
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    public void putJson(String content) {
    }
}