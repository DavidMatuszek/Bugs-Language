package bugs;
import java.util.Random;

class Worker extends Thread {
    static Random rand = new Random();

    private int myWorkerNumber; // An ID, for printing purposes
    private int counter;        // A counter this worker must increment 
    private int increment;      // The amount to increment the counter
    private boolean blocked;    // If true, this worker cannot work
    private int howManyTimesToDoWork; // The number of tasks to be done
    private ThreadMaster tm;    // The synchronized methods are in the TM

    /** Constructor */
    public Worker(int workerNumber, int increment, ThreadMaster tm) {
        myWorkerNumber = workerNumber;
        counter = 0;
        this.increment = increment;
        this.tm = tm;
        howManyTimesToDoWork = rand.nextInt(11) + 10; // 10 to 20 times
        System.out.println("Worker " + myWorkerNumber + " will do " +
                           howManyTimesToDoWork + " loops.");
    }
    
    public int getWorkerNumber() { return myWorkerNumber; }
    
    public void setBlocked(boolean b) { blocked = b; }
    
    public boolean isBlocked() { return blocked; }

    int getCounter() { return counter; }

    /** Repeatedly: Get permission to work; work; signal completion */
    @Override
    public void run() {
        for (int i = 0; i < howManyTimesToDoWork; i++) {
            // Get permission to work
            tm.getWorkPermit(this);
            // Do the actual work, NOT inside a synchronized block
            pause();
            counter += increment;
            // Indicate that this work has been completed
            tm.completeCurrentTask(this);
        }
        tm.terminateWorker(this);
        // Thread dies upon exit
    }

    /** Pause for a random amount of time */
    private void pause() {
        try { sleep(rand.nextInt(100)); }
        catch (InterruptedException e) {}
    }
}