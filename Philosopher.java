import java.util.concurrent.Semaphore;
import java.util.Random;


public class Philosopher extends Thread
{
    
    private static final Random rand = new Random();
    private static int event=0;
    final static int N = 5; 
    public static Semaphore[] fork = new Semaphore[N];
    private int oneOnTop = N;
 
   
    private int philosopher_id;                 
    private Semaphore left_fork;      
    private Semaphore right_fork;
    private int meals = 5;         
 
    public Philosopher(int i, Semaphore fork1, Semaphore fork2)
    {
        philosopher_id = i;
        left_fork = fork1;
        right_fork = fork2;
    }
 
 
    private void postMsg(String str) {
        System.out.printf(" %d. Chopsitcks left: %d. Philosopher %d %s\n",
                         ++event, getTopOne(), philosopher_id, str);
    }
 
    
    private void pause()
    {
        try
        {
            sleep(rand.nextInt(4000));
        } catch (InterruptedException e){}
    }

    private void think()
    {
        postMsg("is thinking");
        pause();
    }
    
    private synchronized void takeOne()
    {
        oneOnTop--;
    } 
    
    private synchronized void putBack()
    {
        oneOnTop++;
    }
    
     
    
    private synchronized int getTopOne()
    {
        return oneOnTop;
    }
    
    
 
    
    private void eat()
    {
        if (getTopOne() < 2){
            postMsg("is waiting for chopsticks");
        } else {
            postMsg("is hungry and is trying to pick up two chopsticks");
        }
        pause();
        try {
            
            takeOne();
            left_fork = fork[getTopOne() - 1];
            left_fork.acquire();
 
            takeOne();
            right_fork = fork[getTopOne() - 1];
            if (!right_fork.tryAcquire()) {
                
                postMsg(">>>> was not able to get a second chopstick");
                return;
            };
 
           
            postMsg(" is eating meal #" + (5 - --meals));
            pause();
 
            postMsg("puts down his chopsticks");
            putBack();
            right_fork.release();
 
        } catch (InterruptedException e) {
           
            postMsg("was interrupted while waiting for his fork");
        }
        finally { 
            putBack();
            left_fork.release();
        }
    }
 
   
    public void run()
    {
        while (meals > 0)
        {
            think();
            eat();
        }
    }
 

    public static void main(String[] args)
    {
        System.out.println("Begin");
 
        for (int f = 0; f < N; f++) {
       
            fork[f] = new Semaphore(1, true);
        }
 

        Philosopher[] philosopher = new Philosopher[N];
        for (int me = 0; me < N; me++) {
           
            int myneighbor = me + 1;
            if (myneighbor == N) myneighbor = 0;
 
           
            philosopher[me] = new Philosopher(me, fork[me], fork[myneighbor]); 
        }
 
        
        for (int i = 0; i < N; i++) {
              philosopher[i].start();
        }
 

        for (int i = 0; i < N; i++) {
          try {
            philosopher[i].join();
          } catch(InterruptedException ex) { }
        }
 
       
        System.out.println("Done");
    }
}
