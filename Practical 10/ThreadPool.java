import java.util.concurrent.*;

public class ThreadPool{
    public static void main(String[] args) throws Exception{
        ExecutorService pool=Executors.newFixedThreadPool(3);

        for(int i=0;i<10;i++){

            int id=i+1;

            pool.submit(()->{
                System.out.println("Task "+id+" running on:"+Thread.currentThread().getName());

                try{
                    Thread.sleep(1000);
                }catch(InterruptedException e){
                    Thread.currentThread().interrupt();
                }
            });
        }
        pool.shutdown();
        pool.awaitTermination(10,TimeUnit.SECONDS);
        System.out.println("Tasks completed");
    }
}