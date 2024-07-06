package customer;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Date;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.util.StringUtils;
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
    	when(request.getParameter("flight_name")).thenReturn("TESTE-789");

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
    
    
    
    // técnica funcional - classes de equivalência
    
    // o valor do parâmetro "flight_name", presente na request, deve seguir um determinado padrão
    // 1a regra do padrão - a string deve começar com a substring "PK-" 
    // 2a regra do padrão - após o hífen, deve haver um valor numérico, entre 000 e 999
    
    // ex.: "PK-001" (classe válida) 
    
    // Ou seja:
    // Condição 1 - Condição lógica (String deve começar com "PK-")
    // Condição 2 - Intervalo de valores (valor entre 000 e 999)
    // Condição 3 - Análise do valor limite (relacionado ao valor entre 000 e 999)
    
    // CONDIÇÃO 1 - CLASSES VÁLIDAS
    @Test
    public void condicao_1_valida() throws ServletException, IOException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    	// Classe válida
    	when(request.getParameter("flight_name")).thenReturn("PK-001");

    	Method doPostMethod = ChooseFlight.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
        doPostMethod.setAccessible(true);
        doPostMethod.invoke(chooseFlightServlet, request, response);
    	
        String flightName = request.getParameter("flight_name");

        assertTrue(flightName.startsWith("PK-"));
    }
    
    // CONDIÇÃO 1 - CLASSES INVÁLIDAS (atenção à diferença no assert)
    @Test
    public void condicao_1_invalida() throws ServletException, IOException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    	// Classe inválida
    	when(request.getParameter("flight_name")).thenReturn("T9-RM2");
    	
    	Method doPostMethod = ChooseFlight.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
        doPostMethod.setAccessible(true);
        doPostMethod.invoke(chooseFlightServlet, request, response);
    	
        String flightName = request.getParameter("flight_name");
       
        
        assertFalse(flightName.startsWith("PK-"));
    }
    
    // CONDIÇÃO 2 - CLASSES VÁLIDAS
    @Test
    public void condicao_2_valida() throws ServletException, IOException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    	// Classe válida
    	when(request.getParameter("flight_name")).thenReturn("PK-001");

    	Method doPostMethod = ChooseFlight.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
        doPostMethod.setAccessible(true);
        doPostMethod.invoke(chooseFlightServlet, request, response);
    	
        String flightName = request.getParameter("flight_name");
        
        // Verifica se após o hífen, existem valores númericos, até o fim da string
        Pattern pattern = Pattern.compile("-\\d+$"); 
        Matcher matcher = pattern.matcher(flightName);
        matcher.find();
        
        int valor = Integer.valueOf(matcher.group().substring(1));

        assertTrue(000 <= valor && valor <= 999);
    }
    
    // CONDIÇÃO 2 - CLASSES INVÁLIDAS
    @Test
    public void condicao_2_invalida_e_valorLimite() throws ServletException, IOException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    	// Classe inválida e acima do valor limite
    	when(request.getParameter("flight_name")).thenReturn("PK-1999");

    	Method doPostMethod = ChooseFlight.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
        doPostMethod.setAccessible(true);
        doPostMethod.invoke(chooseFlightServlet, request, response);
    	
        String flightName = request.getParameter("flight_name");
        
        // Verifica se após o hífen, existem valores númericos, até o fim da string
        Pattern pattern = Pattern.compile("-\\d+$");
        Matcher matcher = pattern.matcher(flightName);
        matcher.find();
        
        int valor = Integer.valueOf(matcher.group().substring(1));

        assertTrue(000 > valor || valor > 999);
    }
    
    // CONDIÇÃO 3 - VALOR IGUAL AO VALOR LIMITE
    @Test
    public void condicao_3_igualLimite() throws ServletException, IOException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    	// Igual ao valor limite
    	when(request.getParameter("flight_name")).thenReturn("PK-999");

    	Method doPostMethod = ChooseFlight.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
        doPostMethod.setAccessible(true);
        doPostMethod.invoke(chooseFlightServlet, request, response);
    	
        String flightName = request.getParameter("flight_name");
        
        // Verifica se após o hífen, existem dígitos númericos até o fim da string
        Pattern pattern = Pattern.compile("-\\d+$"); 
        Matcher matcher = pattern.matcher(flightName);
        matcher.find();
        
        int valor = Integer.valueOf(matcher.group().substring(1));

        assertTrue(valor == 999 || valor == 000);
    }
    
    // CONDIÇÃO 3 - VALOR LOGO ALÉM DOS LIMITES
    @Test
    public void condicao_3_alemLimite() throws ServletException, IOException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    	// Logo além ao valor limite
    	when(request.getParameter("flight_name")).thenReturn("PK-1000");

    	Method doPostMethod = ChooseFlight.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
        doPostMethod.setAccessible(true);
        doPostMethod.invoke(chooseFlightServlet, request, response);
    	
        String flightName = request.getParameter("flight_name");
        
        // Verifica se após o hífen, existem dígitos númericos até o fim da string
        Pattern pattern = Pattern.compile("-\\d+$"); 
        Matcher matcher = pattern.matcher(flightName);
        matcher.find();
        
        int valor = Integer.valueOf(matcher.group().substring(1));

        assertTrue(valor == 999+1 || valor == 000-1);
    }
     
}