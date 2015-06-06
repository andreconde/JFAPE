# JFAPE

The goal of this framework is to ease the byte code injection process at run time. Sometimes, it's necessary to change the byte code of a method, for example, at run time for development purposes.

So, in this initial release, jfape uses javassist under the covers and ClassFileTransformer interface to provide an easy way to change a method or a constructor of a class at run time.

A simple example about how to use it:

- Put the jar of jfape framework in your project classpath. ( You can download it or build it from source )

- Suppose you have the class below and want to change the body of test1 method at run time:

public class TestClass {

	public void test1() {
		System.out.println("Original Method Body");
	}
	
	public static void main(String[] args) {
		TestClass test = new TestClass();
		test.test1();
	}
	
}

- Create a class annotated with @TargetClass and a method inside it annotated with @RewriteMethod as the example below:

@TargetClass(canonicalName="TestClass") //Canonical name of the class that you want to modify
public class TestInjector extends Injector {

	@RewriteMethod(methodName="test1") // Name of the method you want to modify, in this example, test1
	public void rewriteTestMethod() throws Exception {
		String src = "System.out.println(\"Modified Method Body\");";
		getMethod().setBody("{" + src + "}");
	}
	
} 

After doing that, all you have to do is run your main class with -javaagent parameter as follows:
java -javaagent:/tmp/jfapAgent.jar TestClass

When you run the command line above you will get the "Modified Method Body" printed on the screen. If you take off the "-javaagent:/tmp/jfapAgent.jar" from the command line, it would print "Original Method Body".

