package br.com.jfap.bci.classreloading;

import java.lang.annotation.Annotation;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.reflect.Method;
import java.security.ProtectionDomain;
import org.apache.log4j.Logger;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.LoaderClassPath;
import javassist.NotFoundException;
import br.com.jfap.bci.annotations.RewriteConstructor;
import br.com.jfap.bci.annotations.RewriteMethod;
import br.com.jfap.bci.annotations.TargetClass;

/**
 * 
 * @author Andre Conde
 * Responsible for managing bytecode injection in classes annotated with @TargetClass
 *
 */
public class Injector implements java.lang.instrument.ClassFileTransformer {
	private static Logger log = Logger.getLogger(Injector.class);
	private ClassPool pool = ClassPool.getDefault();
	private String workClassCanonicalName;
	protected CtClass workClass;
	private br.com.jfap.bci.pojo.Method method;
	protected br.com.jfap.bci.pojo.Constructor constructor;

	public Injector() {
		try {
			setWorkClassCanonicalName();
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}

	/**
	 * @return the Canonical Name of the Class.
	 */
	public String getCanonicalName() {
		return this.workClassCanonicalName;
	}

	/**
	 * 
	 * @param methodName The name of the method that must be modified via byte code injection 
	 * @return The method Object specified by the methodName param
	 * @throws Exception
	 */
	private br.com.jfap.bci.pojo.Method getMethod(String methodName) throws Exception {
		return new br.com.jfap.bci.pojo.Method(workClass.getDeclaredMethod(methodName));
	}

	private br.com.jfap.bci.pojo.Constructor getConstructor(CtClass[] params) throws Exception {
		return new br.com.jfap.bci.pojo.Constructor(workClass.getDeclaredConstructor(params));
	}

	public br.com.jfap.bci.pojo.Method getMethod() {
		return this.method;
	}

	/**
	 * Sets the class that must be modified via byte code injection
	 * @throws Exception
	 */
	private void setWorkClassCanonicalName() throws Exception {
		log.debug("Setting target Class");

		if ( this.getClass().isAnnotationPresent(TargetClass.class) ) {
			TargetClass tc = this.getClass().getAnnotation(TargetClass.class);
			this.workClassCanonicalName = tc.canonicalName();
		}
	}


	/**
	 * Gets the class from the pool.
	 * @throws NotFoundException
	 */
	private void setWorkClass() throws NotFoundException {
		log.debug("Debugging Class Pool...");
		log.debug("ClassLoaderPool: " + pool.getClassLoader().getClass().getCanonicalName());
		log.debug("Getting this class from the pool: " + workClassCanonicalName);
		pool.insertClassPath(new LoaderClassPath(Thread.currentThread().getContextClassLoader()));
		this.workClass = pool.get(workClassCanonicalName);
	}

	private void injectMethods() throws Exception {
		Method[] methods = this.getClass().getMethods();

		for ( Method annotatedMethod : methods ) {
			Annotation[] methodAnnotations = annotatedMethod.getAnnotations();
			for ( Annotation a : methodAnnotations ) {
				if ( a instanceof RewriteMethod ) {
					this.method = getMethod(((RewriteMethod) a).methodName());
					log.debug("invoking Method: " + annotatedMethod.getName() + " TargetMethodName: " + this.method.getName());
					annotatedMethod.invoke(this);
					log.debug("Class: " + getCanonicalName() + " Method: " + method.getName() + " has been reloaded");
					break;
				}
			}
		}
	}

	private CtClass[] convertClassToCtClassArray(Class<? extends Object>[] classArray) throws Exception {
		CtClass[] ctClassArray = new CtClass[classArray.length];

		for ( int i = 0; i < classArray.length; i++) {
			ctClassArray[i] = pool.get(classArray[i].getCanonicalName());
		}

		return ctClassArray;
	}

	private void injectConstructors() throws Exception {
		Method[] methods = this.getClass().getMethods();

		for ( Method annotatedMethod : methods ) {
			Annotation[] methodAnnotations = annotatedMethod.getAnnotations();
			for ( Annotation a : methodAnnotations ) {
				if ( a instanceof RewriteConstructor ) {
					this.constructor =  getConstructor(convertClassToCtClassArray(((RewriteConstructor) a).params()));
					log.debug("invoking Constructor: " + annotatedMethod.getName() + " TargetConstructorName: " + this.constructor.getName());
					annotatedMethod.invoke(this);
					log.debug("Class: " + getCanonicalName() + " Constructor: " + constructor.getName() + " has been reloaded");
					break;
				}
			}
		}

	}


	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
		byte[] byteCode = classfileBuffer;

		try {
			if ( className.equalsIgnoreCase(this.workClassCanonicalName.replaceAll("/", ".")) ) {
				log.debug("className: " + className);
				log.debug("this.clazz: " + this.workClassCanonicalName);
				setWorkClass();
				injectMethods();
				injectConstructors();
				byteCode = this.workClass.toBytecode();
				this.workClass.detach();
			}
		} catch ( Throwable t ) {
			t.printStackTrace();
			throw new RuntimeException("Erro ao iniciar jfap agent");
		}

		return byteCode;
	}

}
