abstract class Employee {
    String name;
    int id;

    Employee(String name,int id){
        this.name=name;
        this.id=id;
    }

    abstract double monthlySalary();
}

class FullTime extends Employee{
    double salary;

    FullTime(String name,int id,double salary){
        super(name, id);
        this.salary=salary;
    }

    double monthlySalary(){
        return salary;
    }

}

class PartTime extends Employee{
    double rate;
    int hour;

    PartTime(String name,int id,double rate,int hour){
        super(name, id);
        this.rate=rate;
        this.hour=hour;
    }

    double monthlySalary(){
        return rate*hour;
    }
}

class Intern extends Employee{
    double stipend;

    Intern(String name,int id,double stipend){
        super(name, id);
        this.stipend=stipend;
    }

    double monthlySalary(){
        return stipend;
    }
}
