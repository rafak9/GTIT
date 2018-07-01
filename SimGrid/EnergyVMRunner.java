/* Copyright (c) 2016. The SimGrid Team. All rights reserved.               */

/* This program is free software; you can redistribute it and/or modify it
 * under the terms of the license (GNU LGPL) which comes with this package. */

package energy.vm;

//http://browser.geekbench.com/geekbench2/468886
//https://www.cnet.com/products/hp-proliant-ml110-g5-xeon-x3330-2-66-ghz-monitor-none-series/specs/


import org.simgrid.msg.Host;
import org.simgrid.msg.HostFailureException;
import org.simgrid.msg.HostNotFoundException;
import org.simgrid.msg.Msg;
import org.simgrid.msg.Process;
import org.simgrid.msg.Task;
import org.simgrid.msg.TaskCancelledException;
import org.simgrid.msg.VM;

/* This class is a process in charge of running the test. It creates and starts the VMs, and fork processes within VMs */
public class EnergyVMRunner extends Process {

  public class DummyProcess extends Process {
    public  DummyProcess (Host host, String name) {
      super(host, name); 
    }

    @Override
    public void main(String[] strings) {
      Task  task = new Task(this.getHost().getName()+"-task", 1.32E9 , 0);    //300E6     
     
      try {
        task.execute();
      } catch (HostFailureException | TaskCancelledException e) {
        Msg.error(e.getMessage());
        e.printStackTrace();
      } 
      Msg.info("This worker is done."); 
    }
  }

  EnergyVMRunner(Host host, String name, String[] args) throws HostNotFoundException {
    super(host, name, args);
  }

  @Override
  public void main(String[] strings) throws HostNotFoundException, HostFailureException {
	  int nVM = 1000;
	  Host hosts[] = new Host[nVM];
	  VM vmList[] = new VM[nVM];	  
	  for(int n=0; n< nVM; n++)
	  {
		  	String strHostName = "MyHost" + n;
		  	hosts[n] = Host.getByName(strHostName);
		  	String vmName = "vmHost" + n;
		  	vmList[n] = new VM(hosts[n],vmName);
		  	vmList[n].start();
		  	String pName = "task" + n;
   			new DummyProcess (vmList[n], pName).start(); 
	  }
	  waitFor(3600);
   	  
	  for(int j=0; j<nVM; j++)
	  {
        	vmList[j].destroy();
	  }	  
  }
}
