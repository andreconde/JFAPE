package br.com.jfap.bci.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class CustomServletContextListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		/*try {
			System.out.println("Loading custom context...");
			ApplicationContext context = new ApplicationContext(8000);
			context.onLoad(event.getServletContext());
			System.out.println("Custom context has been loaded...");
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		
	}
	

}
