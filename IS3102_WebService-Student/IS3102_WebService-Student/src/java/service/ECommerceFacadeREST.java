package service;

import JavaBean.TransactionRecordBean;
import Entity.LineItem;
import JavaBean.LineItemBean;
import Entity.TransactionRecord;
import JavaBean.LineItemTransactionRecord;
import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

@Path("commerce")
public class ECommerceFacadeREST {

    @Context
    private UriInfo context;

    public ECommerceFacadeREST() {
    }

    @GET
    @Produces("application/json")
    public String getJson() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    /**
     * PUT method for updating or creating an instance of ECommerce
     *
     * @param memberID
     * @param amountPaid
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Path("createECommerceTransactionRecord")
    @Consumes({"application/xml", "application/json"})
    @Produces({"application/xml", "application/json"})
    public Response createECommerceTransactionRecord(@QueryParam("memberID")long memberID, @QueryParam("amountPaid")double amountPaid) {
        try {
            TransactionRecordBean TransactionRecordBean = new TransactionRecordBean();  //Instantiate sales record utility class object to create sales record
            TransactionRecord transactionRecord = TransactionRecordBean.createTransRecord(memberID, amountPaid);
            
            UriBuilder builder = context.getAbsolutePathBuilder();
            builder.path(Long.toString(transactionRecord.getTransactionId()));
            
            return Response.created(builder.build()).build();
        } catch (Exception ex) {
            ex.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
    
    @PUT
    @Path("createECommerceLineItemRecord")
    @Consumes({"application/xml", "application/json"})
    @Produces({"application/xml", "application/json"})
    public Response createECommerceLineItemRecord(@QueryParam("transactionRecordID")long transactionRecordID, @QueryParam("itemEntityID")long itemEntityID, 
            @QueryParam("quantity")int quantity) {
        try {
            LineItem lineItem = new LineItem();
            LineItemBean LineItemBean = new LineItemBean();       //Instantiate lineItem utility object to create line item record in database
            lineItem = LineItemBean.createLineItemRecord(quantity, itemEntityID);
            
            if (lineItem == null)
                throw new SQLException("Create LineItem Record Failed");
            
            LineItemTransactionRecord LineItemTransactionRecord = new LineItemTransactionRecord();
            int result = LineItemTransactionRecord.createSalesRecordLineItem(transactionRecordID, lineItem.getId());     
            //Create transactionRecord_lineitem association record
            
            if (result == 0)
                throw new SQLException("Create transactionRecord_LineItem Record Failed");
            
            LineItemBean.setItemQuantity(quantity, itemEntityID);      //Update stock quantity in database
            
            UriBuilder builder = context.getAbsolutePathBuilder();
            builder.path(Integer.toString(result));
            
            return Response.created(builder.build()).build();
        } catch (Exception ex) {
            ex.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}