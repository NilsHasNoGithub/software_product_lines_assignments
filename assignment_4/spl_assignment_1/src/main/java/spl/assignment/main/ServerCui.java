package spl.assignment.main;

import java.util.List;

import spl.assignment.server.Server;

public class ServerCui implements ServerUi {
    private final Server server;
    private final Thread refreshThread;
    private int logCount;

    public ServerCui(Server server) {
        this.server = server;
        this.logCount = 0;

        Runnable refresher = new Runnable() {

            @Override
            public void run() {

                while(true) {
                    update();
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                
            }
        
        };

        refreshThread = new Thread(refresher);
        refreshThread.start();

    }

    private void update() {
        List<String> newLogs = this.server.getLogger().getLogs();
	    
	    int newLogCount = newLogs.size();
	    int toPrint = newLogCount - this.logCount;
	    
	    for (int i = 0; i < toPrint; i++) {
	    	System.out.println(newLogs.get(newLogCount - toPrint + i));
	    }
	    
	    this.logCount = newLogCount;
   }


    public void show() {
    }
}
