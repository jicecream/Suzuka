/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package B_servlets;

import HelperClasses.Member;
import java.io.IOException;
import java.io.PrintWriter;
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
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Janie
 */
@WebServlet(name = "ECommerce_GetMember", urlPatterns = {"/ECommerce_GetMember"})
public class ECommerce_GetMember extends HttpServlet {

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

        try {
            //request.getParameter(string)

            HttpSession session = request.getSession();
            Client client = ClientBuilder.newClient();
            WebTarget target = client
                    .target("http://localhost:8080/IS3102_WebService-Student/webresources/entity.memberentity")
                    .path("getMember")
                    .queryParam("email", request.getParameter("email"))
                    .queryParam("password", request.getParameter("email"));
            Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);

            Response cat = invocationBuilder.get();

            System.out.println("status: " + response.getStatus());

            System.out.println("status: " + cat.getStatus());

            Member m = cat.readEntity(new GenericType<Member>() {
            });

            session.setAttribute("member", m);

            response.sendRedirect("/IS3102_Project-war/B/SG/memberProfile.jsp");

//            //get response from web service
//            if (cat.getStatus() == Response.Status.OK.getStatusCode()) {
//                System.out.println("success");
//                //String c = cat.readEntity(String.class);
//                int c = cat.readEntity(new GenericType<Integer>() {
//                });
//                System.out.println("The value is " + c);
//
//                //redirect to user profile page
//                response.sendRedirect("./IS3102_Project-war/B/SG/memberProfile.jsp" + c);
//                
//            }

            /* TODO output your page here. You may use following sample code. */
            //   response.sendRedirect("http://localhost:8080/IS3102_Project-war/B/SG/memberProfile.jsp");
        } catch (Exception e) {
            e.printStackTrace();
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

        try (PrintWriter out = response.getWriter()) {
            out.println("doget method");

        }

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
