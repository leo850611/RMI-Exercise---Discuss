//*******************************************************************
//*  Network Programming - Unit 5 Remote Method Invocation          *
//*  Program Name: CalculatorRMIClient                              *
//*  The program is a RMI client.                                   *
//*  Usage: java CalculatorRMIClient op num1 num2,                  *
//*         op = add, sub, mul, div                                 *
//*  2014.02.26                                                     *
//*******************************************************************
import java.io.*;
import java.rmi.*;
import java.util.Scanner;

class CalculatorRMIClient
{
	public static void main(String args[])
	{
	    ArithmeticInterface		o = null;		
	    int flag = 0;
		int select = 0;
		int number = 0;
        String id = null;
        String username = null;
        String password = null;
        String password2 = null;
        String subject = null;
        String contents = null;
	    //System.setSecurityManager(new RMISecurityManager());
	    // Connect to RMIServer
	    try {
	    	o = (ArithmeticInterface) Naming.lookup("rmi://127.0.0.1/arithmetic");
	    	System.out.println("RMI server connected");
	    }
	    catch(Exception e) {
	    	System.out.println("Server lookup exception: " + e.getMessage());
	    }
	    
	    try {
	    	while(flag != 1){
				System.out.println("\n1. Register"); //register 
                System.out.println("2. Login"); //login 
				System.out.println("3. New discussion"); //create
				System.out.println("4. List discussion name"); //subject
				System.out.println("5. Responses discussion"); //reply
				System.out.println("6. Show content of discussion and its responses");//discussion
				System.out.println("7. Delete discussion"); //delete
				System.out.println("8. Quit");
				System.out.println("---------------------------------------------------");
				System.out.println("select a number ->");
				Scanner scanner = new Scanner(System.in);
				select = scanner.nextInt();
				
				switch(select){
					case 1:
						System.out.println("Enter username:");
                        username = scanner.next();
                        System.out.println("Enter password:");
                        password = scanner.next();
                        System.out.println("Enter password again:");
                        password2 = scanner.next();
                        if(password.equals(password2)){
                            System.out.println(o.register(username,password));
                        }
                        else{
                            System.out.println("The password is inconsistent, please try again.");
                        }
						break;

                    case 2:
                        System.out.println("Enter username:");
                        username = scanner.next();
                        System.out.println("Enter password:");
                        password = scanner.next();
                        if(o.login(username,password)== 1){
                            System.out.println("Login successful!\n");
                            id = username;
                        }   
                        else{
                            System.out.println("Err: Username or password wrong.\n");
                        }                        
                        break;

					case 3:
						if(id != null){
                            subject = "null";
                            System.out.println("Enter subject Name:");
                            scanner = new Scanner(System.in);
                            subject = scanner.nextLine();

                            System.out.println("Enter contents:");
                            scanner = new Scanner(System.in);
                            contents = scanner.nextLine();
                            if(o.create(id,subject,contents)== 1){
                                System.out.println("Create discussion successful!\n");
                            }
                            else{
                                System.out.println("The same subject name, please try again.");
                            }
                            
                        }
                        else{
                            System.out.println("Err: No login.\n");
                        }
						break;
					
					case 4:
                        System.out.println(o.subject());
						break;
					
					case 5:
						if(id != null){
							System.out.println(o.subject());
                            System.out.println("Enter discussion number:");
							number = scanner.nextInt();
							System.out.println("Enter reply contents:");
                            scanner = new Scanner(System.in);
                            contents = scanner.nextLine();
                            if(o.reply(number,username,contents) == 1){
								System.out.println("Responses discussion successful!\n");
							}
							else{
								System.out.println("The wrong number, please try again.\n");
							}
                        }
                        else{
                            System.out.println("Err: No login.\n");
                        }
						break;
					
					case 6:
						if(id != null){
                            System.out.println(o.subject());
                            System.out.println("Enter discussion number:");
							number = scanner.nextInt();
                            System.out.println();
							System.out.println(o.discussion(number));
                        }
                        else{
                            System.out.println("Err: No login.\n");
                        }
						break;
					
					case 7:
                        if(id != null){
                            System.out.println(o.subject());
                            System.out.println("Enter delete discussion number:");
							number = scanner.nextInt();
                            if(o.delete(number,username) == 1){
								System.out.println("Delete discussion successful!\n");
							}
							else{
								System.out.println("The wrong number or user error, please try again.");
							}
                        }
                        else{
                            System.out.println("Err: No login.\n");
                        }
						break;
					
					case 8:
						flag = 1;
						System.out.println("BYE~");
                        break;
					default:
						System.out.println("Error select number!");
				}

			}
        
	    }
        catch(Exception e)
        {
        	System.out.println("ArithmeticServer exception: " + e.getMessage());
        	e.printStackTrace();
        }
	}
}