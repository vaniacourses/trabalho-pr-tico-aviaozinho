package customer;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Date;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import customer.ChooseFlight;
import manager.ApproveSeats;
import models.Customer;
import models.Flight;
import models.Seat;

public class ChooseFlightTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private ServletContext servletContext;

    @Mock
    private RequestDispatcher requestDispatcher;

    @InjectMocks
    private ChooseFlight chooseFlightServlet;

    @Before
    public void setUp() throws Exception {
    	
        MockitoAnnotations.openMocks(this);
    	
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("customer")).thenReturn(new Customer("John Doe", "john.doe@example.com", null));
        when(request.getParameter("flight_name")).thenReturn("Flight 1");

        ArrayList<Flight> flights = new ArrayList<>();
        Flight flight1 = new Flight(true, 100, 50, 20, 170, "Flight 1", new ArrayList<Seat>(), 200, 30, "City A", "City B", null, null, 100, 50, 20);
        Flight flight2 = new Flight(false, 90, 40, 15, 145, "Flight 2", new ArrayList<Seat>(), 180, 25, "City C", "City D", null, null, 90, 40, 15);
        flights.add(flight1);
        flights.add(flight2);

        when(servletContext.getAttribute("flights")).thenReturn(flights);
        when(request.getServletContext()).thenReturn(servletContext);

        when(request.getRequestDispatcher("CurrentBooking.do")).thenReturn(requestDispatcher);

    }

    @Test
    public void testDoPost() throws ServletException, IOException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    	 Method doPostMethod = ChooseFlight.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
         doPostMethod.setAccessible(true);
         doPostMethod.invoke(chooseFlightServlet, request, response);


        verify(request).getRequestDispatcher("CurrentBooking.do");
        verify(requestDispatcher).forward(request, response);
    }
    
    @Test
    public void test_noFlights() throws ServletException, IOException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        ArrayList<Flight> emptyFlights = new ArrayList<>();

        when(servletContext.getAttribute("flights")).thenReturn(emptyFlights);

    	Method doPostMethod = ChooseFlight.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
         doPostMethod.setAccessible(true);
         doPostMethod.invoke(chooseFlightServlet, request, response);


        verify(request).getRequestDispatcher("CurrentBooking.do");
    }
    
    @Test
    public void test_FlightNotFound() throws ServletException, IOException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        when(request.getParameter("flight_name")).thenReturn("Flight 3");

    	Method doPostMethod = ChooseFlight.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
         doPostMethod.setAccessible(true);
         doPostMethod.invoke(chooseFlightServlet, request, response);


        verify(request).getRequestDispatcher("CurrentBooking.do");
    }
    
    // técnica funcional - classe de equivalência por condição lógica na entrada do servlet (servletContext)
    @Test
    public void test_classeEquivalencia_entrada() throws ServletException, IOException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    	Method doPostMethod = ChooseFlight.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
        doPostMethod.setAccessible(true);
        doPostMethod.invoke(chooseFlightServlet, request, response);
    	
    	verify(servletContext).getAttribute("flights");
    }
    
    // técnica funcional - classe de equivalência por condição lógica na saída do servlet (request)
    @Test
    public void test_classeEquivalencia_saída() throws ServletException, IOException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        when(request.getParameter("flight_name")).thenReturn("Flight 3");

    	Method doPostMethod = ChooseFlight.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
         doPostMethod.setAccessible(true);
         doPostMethod.invoke(chooseFlightServlet, request, response);


        verify(request).getRequestDispatcher("CurrentBooking.do");
    }
}