class Counter{
    int count=0;

    void increment(){
        count++;
    }
}

public class CounterRace {
    public static void main(String[] args)throws Exception{
        Counter c=new Counter();

        Thread[] threads=new Thread[10];

        for(int i=0;i<10;i++){
            threads[i]=new Thread(()->{
                for(int j=0;j<100000;j++){
                    c.increment();
                }
            });

            threads[i].start();
        }

        for(int i=0;i<10;i++){
            threads[i].join();
        }

        System.out.println("\nExpected: 100000");
        System.out.println("Actual: "+c.count);

    }
}
