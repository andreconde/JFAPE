package br.com.jfap.bci.agent;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.util.Set;
import org.reflections.Reflections;
import br.com.jfap.bci.classreloading.Injector;

public class Agent {

	public static void premain(String agentArgs, Instrumentation inst) throws InstantiationException, IllegalAccessException {
		Reflections reflections = new Reflections("");
		Set<Class<? extends Injector>> classes = reflections.getSubTypesOf(Injector.class);
		
		for ( Class<? extends Injector> clazz : classes ) {
			System.out.println("Registering transformer: " + clazz.getCanonicalName());
			inst.addTransformer((ClassFileTransformer) clazz.newInstance());
		}	
		
	}

}
