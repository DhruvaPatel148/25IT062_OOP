public class Driver2 {
    public static void main(String[] args){
        Shape[] s={
            new Circle(5),
            new Rectangle(5,10),
            new Triangle(15,20),
        };

        double t=0;
        double largest=0;
        System.out.println();
        for(int i=0;i<s.length;i++){
            double a=s[i].area();
            System.out.println("Area: "+a);

            t+=a;

            if(a>largest){
                largest=a;
            }
        }

        System.out.println("Total Area: "+t);
        System.out.println("Largest Area: "+largest);
    }
}
