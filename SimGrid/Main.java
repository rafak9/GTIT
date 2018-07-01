/* Copyright (c) 2016. The SimGrid Team. All rights reserved.               */

/* This program is free software; you can redistribute it and/or modify it
 * under the terms of the license (GNU LGPL) which comes with this package. */

package energy.vm;

import org.simgrid.msg.Host;
import org.simgrid.msg.HostNotFoundException;
import org.simgrid.msg.Msg;

class Main {
  private Main() {
    throw new IllegalAccessError("Utility class");
  }

  public static void main(String[] args) throws HostNotFoundException {
    Msg.energyInit();
    Msg.init(args);

    String strFileName = "C:\\Facul\\simgrid\\SimGrid-3.19.1\\examples\\java\\energy\\vm\\platform_files\\platform_1000.xml";    
    Msg.createEnvironment(strFileName);

    /* Create and start a runner for the experiment */
    new EnergyVMRunner(Host.all()[0],"energy VM runner",null).start();
    Msg.run();
  }
}
