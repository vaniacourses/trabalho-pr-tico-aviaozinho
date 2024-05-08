package customer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import models.Customer;
import models.Flight;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 *
 * @author MuhammadHarris
 */
public class ChooseFlight extends HttpServlet {


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ArrayList<Flight> flights = (ArrayList<Flight>) (getServletContext().getAttribute("flights"));

        Flight f = null;
        
        for (int i = 0; i < flights.size(); i++)
        {
            if (flights.get(i).getFlightName().equals(request.getParameter("flight_name")))
            {
                f = flights.get(i);
                break;
            }            
        }
        
        f.setCustomer((Customer)(request.getSession().getAttribute("customer")));
        
        request.getRequestDispatcher("CurrentBooking.do").forward(request, response);
    }

}
