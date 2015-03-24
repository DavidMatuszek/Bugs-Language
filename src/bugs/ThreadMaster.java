package bugs;
import java.util.ArrayList;
import java.util.Random;

public class ThreadMaster {
    private int numberOfWorkers = 3;
    private ArrayList<Worker> workers = new ArrayList<Worker>();
    
    // Control the amount of output produced
    private boolean verbose = false;

    public static void main(String[] args) {
        new ThreadMaster().run();
    }

    /** Creates and coordinates all Workers */
    private void run() {
        // Create an ArrayList of workers, all initially blocked
        int increment = 1;
        for (int i = 0; i < numberOfWorkers; i++) {
            Worker worker = new Worker(i, increment, this);
            workers.add(worker);
            worker.setBlocked(true);
            increment *= 10;
        }
        // Start worker threads (still blocked)
        for (int i = 0; i < numberOfWorkers; i++) {
            workers.get(i).start();
        }
        // When all worker threads are blocked (waiting), unblock them
        // If no worker threads remain, exit loop
        while (workers.size() > 0) {
            unblockAllWorkers();
        }
        System.out.println("Master thread dies.");
    }

    /** Called by a Worker to try to get permission to work */
    synchronized void getWorkPermit(Worker worker) {
        int workerNumber = worker.getWorkerNumber();
        verbosePrint("    Worker " + workerNumber + 
                     " is trying to get a work permit.");
        while (worker.isBlocked()) {
            try {
                verbosePrint("    Worker " + workerNumber +
                             " is waiting.");
                wait();
            }
            catch (InterruptedException e) {
                verbosePrint("    Worker " + workerNumber +
                             " has been interrupted.");
            }
        }
        verbosePrint("Worker " + workerNumber + " got a work permit.");
    }
    
    /** Called by a Worker to indicate completion of work */
    synchronized void completeCurrentTask(Worker worker) {
        worker.setBlocked(true);
        verbosePrint("  Worker " + worker.getWorkerNumber() +
                     " has done work and is now blocked.");
        notifyAll();
    }

    /** Called by this TaskManager to allow all Workers to work */
    synchronized void unblockAllWorkers() {
        verbosePrint("    Master is trying to reset all.");
        while (countBlockedWorkers() < workers.size()) {
            try {
                verbosePrint("    Master is waiting for all workers" +
                             " to be blocked.");
                wait();
            }
            catch (InterruptedException e) {
                verbosePrint("    Master has been interrupted.");
            }
        }
        printResultsSoFar();
        for (Worker worker : workers) {
            worker.setBlocked(false);
        };
        verbosePrint("\nMaster has unblocked all workers.");
        notifyAll();  
    }
    
    /** Counts the number of currently blocked Workers; since this is
     *  called from a synchronized method, it is effectively synchronized */
    private int countBlockedWorkers() {
        int count = 0;
        for (Worker worker : workers) {
            if (worker.isBlocked()) {
                count++;
            }
        }
        return count;
    }
    
    /** Called by a Worker to die; synchronized because it modifies the
     * ArrayList of workers, which is used by other synchronized methods. */
    synchronized void terminateWorker(Worker worker) {
        workers.remove(worker);
        System.out.println("* Worker " + worker.getWorkerNumber() +
                           " has terminated.");
    }

    private void printResultsSoFar() {
        for (Worker worker : workers) {
            System.out.print("Worker " + worker.getWorkerNumber() + 
                             " -> " + worker.getCounter() + "    ");
        }
        System.out.println();
    }
    
    private void verbosePrint(String s) {
        if (verbose) {
            System.out.println(s);
        }
    }
}