import java.util.concurrent.Semaphore;


public class DinningPhilosoher {
	final static int  NUMBER_OF_PHILOSOPHER = 5;
	public static Semaphore[] fork;
	public static void main(String[] args) throws Exception{
		 System.out.println("Begin");
		 
	        for (int count = 0; count < NUMBER_OF_PHILOSOPHER; count++) {
	       
	            fork[count] = new Semaphore(1, true);
	        }
	 

	        Philosopher[] philosopher = new Philosopher[NUMBER_OF_PHILOSOPHER];
	        for (int philosopher_count = 0; philosopher_count < NUMBER_OF_PHILOSOPHER; philosopher_count++) {
	           
	            int Next_Philosopher = philosopher_count + 1;
	            if (Next_Philosopher == NUMBER_OF_PHILOSOPHER) Next_Philosopher = 0;
	 
	           
	            philosopher[philosopher_count] = new Philosopher(philosopher_count, fork[philosopher_count], fork[Next_Philosopher]); 
	        }
	 
	        
	        for (int philosopher_count = 0; philosopher_count < NUMBER_OF_PHILOSOPHER; philosopher_count++) {
	              philosopher[philosopher_count].start();
	        }
	 

	        for (int philosopher_count = 0; philosopher_count < NUMBER_OF_PHILOSOPHER; philosopher_count++) {
	          try {
	            philosopher[philosopher_count].join();
	          } catch(InterruptedException ex) { }
	        }
	 
	       
	        System.out.println("Done");
	    }

	}


