class Theater{
    int seatsLeft=5;
    boolean book(){
        if(seatsLeft>0){
            try{
                Thread.sleep(100);
            }
            catch(InterruptedException e){
                Thread.currentThread().interrupt();
            }

            seatsLeft--;
            return true;
        }

        return false;
    }
}
public class SeatRace {
    public static void main(String[] args) throws Exception{
        Theater t=new Theater();
        
        Thread[] threads=new Thread[10];

        for(int i=0;i<10;i++){
            threads[i] =new Thread(()->{
                if(t.book()){
                    System.out.println("\nSeat booked!");
                }
                else{
                    System.out.println("No seats available");
                }
            });
            threads[i].start();
        }

        for(int i=0;i<10;i++){
            threads[i].join();
        }

        System.out.println("\nSeats left:"+t.seatsLeft);
    }
}
