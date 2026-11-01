
import java.util.Scanner;
class DivideByZeroException extends Exception{

    DivideByZeroException(String msg) {
        super(msg);
    }
    
}

public class Calculator {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);

        boolean success=false;

        while(!success){
            try{
                System.out.println("Enter first number:");
                double a=Double.parseDouble(sc.nextLine());

                System.out.println("Enter second number:");
                double b=Double.parseDouble(sc.nextLine());

                System.out.println("Enter operator +,-,/,*:");
                char op=sc.nextLine().charAt(0);

                double result;

                if(op=='/'&& b==0){
                    throw new DivideByZeroException("Cannot divide by zero!");
                }

                switch(op){
                    case '+':
                        result=a+b;
                        break;
                    case '-':
                        result=a-b;
                        break;
                    case '*':
                        result= a*b;
                        break;
                    case '/':
                        result=a/b;
                        break;
                    default:
                        throw new Exception("Invalid operator!");
                }

                System.out.println("Result="+result);
                success=true;
            }
            catch(NumberFormatException e){
                System.out.println("Invalid number! please enter number only.");
            }
            catch(DivideByZeroException e){
                System.out.println(e.getMessage());
            }
            catch(Exception e){
                System.out.println(e.getMessage());
            }
            finally{
                System.out.println("Attempt completed!");
            }
        }
        sc.close();
    }
}
