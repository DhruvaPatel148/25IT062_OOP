import java.util.Scanner;

public class Driver3
 {
    public static void main(String[] args){
        Scanner sc=new Scanner(System.in);

        Employee[] e=new Employee[3];
        System.out.println();
        System.out.println("<-----------Employee Details---------->");
        System.out.println("Enter Name of full time employee:");
        String s=sc.next();
        System.out.println("Enter ID:");
        int id=sc.nextInt();
        System.out.println("Enter Salary:");
        double salary=sc.nextDouble();

        e[0]=new FullTime(s,id,salary);
        System.out.println();
        System.out.println("Enter name of part time employee:");
        String name=sc.next();
        System.out.println("Enter ID:");
        id=sc.nextInt();
        System.out.println("Enter Rate:");
        double r=sc.nextDouble();
        System.out.println("Enter hours of work:");
        int h=sc.nextInt();

        e[1]=new PartTime(name, id, r, h);

        System.out.println("Enter intern name:");
        name=sc.next();
        System.out.println("Enter ID:");
        id=sc.nextInt();
        System.out.println("Enter stipend:");
        double stipend=sc.nextDouble();

        e[2]=new Intern(name, id, stipend);

        double total=0;
        
        for(int i=0;i<e.length;i++){
            System.out.println("Name: "+e[i].name);
            System.out.println("ID: "+e[i].id);
            System.out.println("Salary:"+e[i].monthlySalary());

            if(e[i] instanceof Intern){
                System.out.println("Note: This Employee is an intern.");
            }
            
            System.out.println();

            total+=e[i].monthlySalary();

        }
        System.out.println("Total salary:"+total);

        sc.close();
    }
}
