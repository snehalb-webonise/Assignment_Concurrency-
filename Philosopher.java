import java.util.Random;
import java.util.concurrent.Semaphore;


public class Philosopher extends Thread{
	
	private static final Random RANDOM = new Random();
	final static int SLEEP_TIME =4000;
	final static int  NUMBER_OF_PHILOSOPHER = 5;
    private static int event=0;
    private int Number_of_fork = NUMBER_OF_PHILOSOPHER;
    private int philosopher_id;                 
    private Semaphore left_fork;      
    private Semaphore right_fork;
    private int Total_meals = 5;     
    public static Semaphore[] fork = new Semaphore[NUMBER_OF_PHILOSOPHER];
 
    public Philosopher(int id, Semaphore forkleft, Semaphore forkright)
    {
        philosopher_id = id;
        left_fork = forkleft;
        right_fork = forkright;
    }
 
 
    private void postMsg(String string) {
        System.out.printf(" %d. fork left: %d. Philosopher %d %s\n",
                         ++event, NumberofFork(), philosopher_id, string);
    }
    
    private void pause()
    {
        try
        {
            sleep(RANDOM.nextInt(SLEEP_TIME));
        } catch (InterruptedException e){}
    }

    private void think()
    {
        postMsg("is thinking");
        pause();
    }
    
    private synchronized void takefork()
    {
    	Number_of_fork--;
    } 
    
    private synchronized void putfork()
    {
    	Number_of_fork++;
    }
    
     
    
    private synchronized int NumberofFork()
    {
        return Number_of_fork;
    }
    
    
 
    
    private void eat()
    {
        if (NumberofFork() < 2){
            postMsg("is waiting for chopsticks");
        } else {
            postMsg("is hungry and is trying to pick up two chopsticks");
        }
        pause();
        try {
            
        	takefork();
            left_fork = fork[NumberofFork() - 1];
            left_fork.acquire();
 
            takefork();
            right_fork = fork[NumberofFork() - 1];
            if (!right_fork.tryAcquire()) {
                
                postMsg("was not able to get a second chopstick");
                return;
            };
 
            int current_meal = --Total_meals;
            postMsg(" is eating meal #" + (5 - current_meal));
            pause();
 
            postMsg("puts down his chopsticks");
            putfork();
            right_fork.release();
 
        } catch (InterruptedException e) {
           
            postMsg("was interrupted while waiting for his fork");
        }
        finally { 
        	putfork();
            left_fork.release();
        }
    }
 
   
    public void run()
    {
        while (Total_meals > 0)
        {
            think();
            eat();
        }
    }
}
