import static org.mockito.Mockito.*;
import org.junit.Test;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletContext;

public class ApproveSeatsTest {

    @Test
    public void testDoPost() throws ServletException, IOException{
        // Criar os objetos de contexto e requisição/resposta simulados
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        ServletContext servletContext = mock(ServletContext.class);

        // Preparar dados simulados
        ArrayList<Flight> flights = new ArrayList<>();
        Flight flight = new Flight("AA123", 20, 20, 20, 20);
        flights.add(flight);

        when(request.getParameter("flight_name")).thenReturn("AA123");
        when(servletContext.getAttribute("flights")).thenReturn(flights);

        // Criar Instância do servçet e configurar o contexto
        ApproveSeats servlet = new ApproveSeats();
        servlet.getServletContext().setAttribute("flights", flights);

        // Executar o método que está sendo testado
        servlet.doPost(request, response);

        // Verificar se os assentos antigos foram zerados
        assertEquals(0. flight.getOldESeats());
        assertEquals(0. flight.getOldESeats());
        assertEquals(0. flight.getOldESeats());
        assertEquals(0. flight.getOldESeats());

        // Verificar se o redirecionamento ocorreu
        verify(response.sendRedirect("ApproveSeats.jsp"));
    }
}