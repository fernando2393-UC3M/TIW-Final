package main;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet Filter implementation class LogInFilter
 */
@WebFilter(urlPatterns={"/*"})
public class LogInFilter implements Filter {
	
	//private FilterConfig fConfig;
	
	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		//this.fConfig = fConfig;
	}
	
    /**
     * Default constructor. 
     */
    public LogInFilter() {}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
		
        HttpSession session = req.getSession(false);

        String loginURI = req.getContextPath() + "/login";
        String logoutURI = req.getContextPath() + "/logout";
        String indexURI = req.getContextPath() + "/index";
        String registerURI = req.getContextPath() + "/register";
        String resultadosURI = req.getContextPath() + "/resultados";
        String alojamientoURI = req.getContextPath() + "/alojamiento";
        String mainURI = req.getContextPath() + "/";
        
        boolean loggedIn = session != null && session.getAttribute("user") != null;
        boolean isLoginRequest = req.getRequestURI().equals(loginURI);
        boolean isLogoutRequest = req.getRequestURI().equals(logoutURI);
        boolean isRegisterRequest = req.getRequestURI().equals(registerURI);
        boolean isResultadosRequest = req.getRequestURI().equals(resultadosURI);
        boolean isAlojamientoRequest = req.getRequestURI().equals(alojamientoURI);
        boolean isIndexRequest = req.getRequestURI().equals(indexURI) || 
        						 req.getRequestURI().equals(mainURI);
        boolean isStaticResource = req.getRequestURI().startsWith(req.getContextPath() + "/css/")   ||
        						   req.getRequestURI().startsWith(req.getContextPath() + "/fonts/") ||
        						   req.getRequestURI().startsWith(req.getContextPath() + "/images/")|| 
        						   req.getRequestURI().startsWith(req.getContextPath() + "/js/");

        if (loggedIn || isLoginRequest || isLogoutRequest || isIndexRequest || isAlojamientoRequest || isResultadosRequest || isRegisterRequest || isStaticResource) {
        	// pass the request along the filter chain
            chain.doFilter(request, response);
        } else {
        	// go to the home page
			res.sendRedirect(req.getContextPath());
        }
        return;

	}

	
}
