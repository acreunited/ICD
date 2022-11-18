package serial_reflect;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;

import java.lang.reflect.Method; 
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Constructor; 

public class Employee implements java.io.Serializable {
	   private static final long serialVersionUID = 1L;
	   private String name="Jonh Doe";
	   private String address="Pleasant Ave, Summerville, NY";
	   private transient int SSN=11122333;  // final ????
	   private int number=101;
	   private LocalDate birthdate=LocalDate.ofEpochDay(0);	//LocalDate.of(1970, Month.JANUARY, 1);
	   private transient long age=birthdate.until(LocalDate.now(), ChronoUnit.YEARS);  // calculado!
	   
	   @SuppressWarnings("unused")
	   private void mailCheck() {
	      System.out.println("Mailing a check to " + name + ", " + address);
	   }
	   
	   public void print() {
		   System.out.println("Name: " + name);
		   System.out.println("Address: " + address);
		   System.out.println("SSN(transient): " + SSN);
		   System.out.println("Number(private): " + number);
		   System.out.println("Birthdate: " + birthdate);
		   System.out.println("Age: " + age);
	   }
	   
	   public void setName(String name) {
		   this.name=name;
	   }
	   
	   public void setAddress(String address) {
		   this.address=address;
	   }
	   
	   public void setSSN(int SSN) {
		   this.SSN=SSN;
	   } 
	   
	   public void setNumber(int number) {
		   this.number=number;
	   }
	   
	   public void setBirthdate(LocalDate birthdate) {
		   this.birthdate=birthdate;
	   }
	   
	    public static void main( String[] args ) throws IntrospectionException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException
	    {
	    	Employee emp= new Employee();
	        System.out.println("Initial values are: ");
	        emp.print();
	    	// Creating class object from the object using 
	        // getclass method 
	        Class<? extends Employee> cls = emp.getClass();   	
   
	        System.out.println("The name of class is '" + cls.getName()+"'"); 
	  
	        // Getting the constructor of the class through the 
	        // object of the class 
	        Constructor<? extends Employee> constructor = cls.getConstructor(); 
	        System.out.println("The name of constructor is '" + 
	                            constructor.getName()+"'"); 
	  
	        System.out.println("The property desriptors name are: ");
	        BeanInfo info = Introspector.getBeanInfo( cls );
	        for (PropertyDescriptor pd : info.getPropertyDescriptors() )
	            System.out.println( pd.getName() );
	        
	        System.out.println("The public methods of class are: "); 
	  
	        // Getting methods of the class through the object 
	        // of the class by using getMethods 
	        Method[] methods = cls.getMethods(); 

	        for (Method method:methods) 
	            System.out.println(method.getName()); 
	        
	        // creates object of the desired field by providing 
	        // the name of field as argument to the  
	        // getDeclaredField method 
	        Field field = cls.getDeclaredField("number"); 
	  
	        // allows the object to access the field irrespective 
	        // of the access specifier used with the field 
	        field.setAccessible(true); 
	  
	        // takes object and the new value to be assigned 
	        // to the field as arguments 
	        field.set(emp, 199); 
	        
	        // creates object of desired method by providing the 
	        // method name and parameter class as arguments to 
	        // the getDeclaredMethod 
	        Method methodcall1 = cls.getDeclaredMethod("print"); 
	  
	        System.out.println("Invokes the public method '"+methodcall1.getName()+"' at runtime");
	        // invokes the method at runtime 
	        methodcall1.invoke(emp); 
	  
	        // Creates object of the desired method by providing 
	        // the name of method as argument to the  
	        // getDeclaredMethod method 
	        Method methodcall2 = cls.getDeclaredMethod("mailCheck"); 
	  
	        // allows the object to access the method irrespective  
	        // of the access specifier used with the method 
	        methodcall2.setAccessible(true); 
	        System.out.println("Invokes the private method '"+methodcall2.getName()+"' at runtime");
	        // invokes the method at runtime 
	        methodcall2.invoke(emp);
	        
	    }
	}