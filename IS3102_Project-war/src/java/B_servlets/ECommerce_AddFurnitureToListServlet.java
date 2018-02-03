package B_servlets;

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
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import HelperClasses.ShoppingCartLineItem;
import static java.lang.System.out;

@WebServlet(name = "ECommerce_AddFurnitureToListServlet", urlPatterns = {"/ECommerce_AddFurnitureToListServlet"})
public class ECommerce_AddFurnitureToListServlet extends HttpServlet {

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
            String SKU = request.getParameter("SKU");
            String id = request.getParameter("id");
            Double price = Double.parseDouble(request.getParameter("price"));
            String name = request.getParameter("name");
            String imageURL = request.getParameter("imageURL");
            int quantity = getQuantityAvailable(SKU);
            boolean itemIsInCart = false;
            
            HttpSession session = request.getSession();

            ArrayList<ShoppingCartLineItem> shoppingCart = (ArrayList<ShoppingCartLineItem>) session.getAttribute("shoppingCart");
            ShoppingCartLineItem newItemInCart = new ShoppingCartLineItem();
            
            //check if there's already a shopping cart
            if(shoppingCart == null) 
                shoppingCart = new ArrayList<>();
            else
            {
                for(ShoppingCartLineItem itemInCart : shoppingCart)
                {   
                    if(itemInCart.getSKU().equals(SKU))
                    {
                        itemIsInCart = true;
                        if(quantity > itemInCart.getQuantity())
                        {
                            itemInCart.setQuantity(itemInCart.getQuantity() + 1);
                            response.sendRedirect("/IS3102_Project-war/B/SG/shoppingCart.jsp?goodMsg=Furniture successfully added to your shopping cart");
                        }
                        else
                        {
                            response.sendRedirect("/IS3102_Project-war/B/SG/shoppingCart.jsp?errMsg=Item not added to cart, not enough quantity available.");
                        }
                    }
                }
            }
            
            //check if the item has yet to be added in cart and there's still stock left
            if(itemIsInCart == false && quantity > 0)
            {
                newItemInCart.setId(id);
                newItemInCart.setSKU(SKU);
                newItemInCart.setImageURL(imageURL);
                newItemInCart.setName(name);
                newItemInCart.setPrice(price);
                newItemInCart.setQuantity(1); 
                shoppingCart.add(newItemInCart);
                session.setAttribute("shoppingCart", shoppingCart);
                response.sendRedirect("/IS3102_Pr   oject-war/B/SG/shoppingCart.jsp?goodMsg=Furniture successfully added to your shopping cart");
            }
            else if(quantity == 0) //out of stock
            {
                response.sendRedirect("/IS3102_Project-war/B/SG/shoppingCart.jsp?errMsg=Item not added to cart, not enough quantity available.");
            }
        }
        catch (Exception ex) {
            out.println("\n\n " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public int getQuantityAvailable(String SKU)
    {
        try
        {
            System.out.println("getQuantity() SKU: " + SKU);
            Client client =ClientBuilder.newClient();
            WebTarget target = client
                    .target("http://localhost:8080/IS3102_WebService-Student/webresources/entity.countryentity")
                    .path("getQuantity")
                    .queryParam("SKU", SKU);
            
            Invocation.Builder builder = target.request(MediaType.APPLICATION_JSON);
            Response response = builder.get();
            System.out.println("status: " + response.getStatus());
            if(response.getStatus() != 200)
            {
                return 0;
            }
            String result = (String) response.readEntity(String.class);
            System.out.println("Result returned from ws: " + result);
            return Integer.parseInt(result);           
        } catch(Exception ex)
        {
            ex.printStackTrace();
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