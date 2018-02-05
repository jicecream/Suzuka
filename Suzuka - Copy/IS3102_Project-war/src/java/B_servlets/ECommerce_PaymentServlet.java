package B_servlets;

import HelperClasses.Member;
import HelperClasses.ShoppingCartLineItem;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@WebServlet(name = "ECommerce_PaymentServlet", urlPatterns = {"/ECommerce_PaymentServlet"})
public class ECommerce_PaymentServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            
            HttpSession session = request.getSession();
            long countryID = (long) session.getAttribute("countryID");
           
            ArrayList<ShoppingCartLineItem> shoppingCart = new ArrayList<ShoppingCartLineItem>();

            shoppingCart = (ArrayList<ShoppingCartLineItem>)session.getAttribute("shoppingCart");

            String memberEmail = (String)session.getAttribute("memberEmail");
        Member member = (Member) session.getAttribute("member");
            Long storeID = 10001L;
             ArrayList<String> sku = new ArrayList<>();
            for(ShoppingCartLineItem cart : shoppingCart) {
             sku.add(cart.getSKU());
               //set the quantity of furniturelist
                    
            }
            
   
            double totalPrice = (double) session.getAttribute("totalPrice");
            int salesRecordId = createTransactionRecord(member, totalPrice);
            int lineItems = 0;
            if (salesRecordId != 0) {
                    for (ShoppingCartLineItem item: shoppingCart) {
                    if (createLineItemRecord(salesRecordId, Integer.parseInt(item.getId()), item.getQuantity(), item) == 1)
                        lineItems++;
            
                }
      
            } 
                shoppingCart.clear();
            
                response.sendRedirect("/IS3102_Project-war/B/SG/shoppingCart.jsp?");
        
        
        
        
    }
    }
    /**
     *
     * @param member
     * @param amountPaid
     * @return
     */
    public int createTransactionRecord(Member member, double amountPaid) {
        try {

            Client client = ClientBuilder.newClient();
            WebTarget target = client
                    .target("http://localhost:8080/IS3102_WebService-Student/webresources/commerce/")
                    .path("createECommerceTransactionRecord")
                       .queryParam("memberID", member.getId())
                    .queryParam("amountPaid", amountPaid);
            
            Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
            Response response = invocationBuilder.put(Entity.entity(member, MediaType.APPLICATION_JSON));
            System.out.println("status: " + response.getStatus());
            if (response.getStatus() != 200) {
                return 0;
            }
            String result = (String) response.readEntity(String.class);
            return Integer.parseInt(result);

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    
        public int createLineItemRecord( long itemEntityID,int quantity,long salesRecordID,  ShoppingCartLineItem item) {
        try {
            Client client = ClientBuilder.newClient();

            WebTarget target = client
                    .target("http://localhost:8080/IS3102_WebService-Student/webresources/commerce/")
                    .path("createECommerceLineItemRecord")
                    .queryParam("itemEntityID", itemEntityID)
                    .queryParam("quantity", quantity)
                    .queryParam("salesRecordID", salesRecordID);
                   
            Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);

            Response response = invocationBuilder.put(Entity.entity(item, MediaType.APPLICATION_JSON));
            System.out.println("status: " + response.getStatus());
            if (response.getStatus() != 200) {
                return 0;
            }

           int result = Integer.parseInt((String) response.readEntity(String.class));
            
            return result;

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
        
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}