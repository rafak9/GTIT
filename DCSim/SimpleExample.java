package edu.uwo.csd.dcsim.examples;

import java.util.ArrayList;
import java.util.Collections;

import org.apache.log4j.Logger;

import edu.uwo.csd.dcsim.*;
import edu.uwo.csd.dcsim.application.Applications;
import edu.uwo.csd.dcsim.application.InteractiveApplication;
import edu.uwo.csd.dcsim.application.workload.TraceWorkload;
import edu.uwo.csd.dcsim.common.SimTime;
import edu.uwo.csd.dcsim.core.*;
import edu.uwo.csd.dcsim.examples.management.RelocationPolicy;
import edu.uwo.csd.dcsim.host.Host;
import edu.uwo.csd.dcsim.host.HostModels;
import edu.uwo.csd.dcsim.host.resourcemanager.DefaultResourceManagerFactory;
import edu.uwo.csd.dcsim.host.scheduler.DefaultResourceSchedulerFactory;
import edu.uwo.csd.dcsim.management.*;
import edu.uwo.csd.dcsim.management.capabilities.HostManager;
import edu.uwo.csd.dcsim.management.capabilities.HostPoolManager;
import edu.uwo.csd.dcsim.management.events.VmPlacementEvent;
import edu.uwo.csd.dcsim.management.policies.HostMonitoringPolicy;
import edu.uwo.csd.dcsim.management.policies.HostOperationsPolicy;
import edu.uwo.csd.dcsim.management.policies.HostStatusPolicy;
import edu.uwo.csd.dcsim.management.policies.DefaultVmPlacementPolicy;
import edu.uwo.csd.dcsim.vm.VmAllocationRequest;
import edu.uwo.csd.dcsim.application.Application;

/**
 * A basic example of how to setup and run a simulation.
 *
 * @author Michael Tighe
 *
 */
public class SimpleExample extends SimulationTask {

    private static Logger logger = Logger.getLogger(SimpleExample.class);
    private static int N_VMS = 1000;

    public static void main(String args[]) {
        //MUST initialize logging when starting simulations
        Simulation.initializeLogging();
        //create an instance of our task, with the name "simple", to run for 10 minutes
        SimulationTask task = new SimpleExample("simple", SimTime.minutes(60));
        //run the simulation
        task.run();
        //output metric values
        task.getMetrics().printDefault(logger);
    }

    public SimpleExample(String name, long duration) {
        super(name, duration);
    }

    private static ArrayList<Host> createHosts(Simulation simulation, 
            AutonomicManager dcAM, HostPoolManager hostPool) {
        ArrayList<Host> hosts = new ArrayList<Host>();

        for (int i = 0; i < N_VMS; ++i) {
            Host host;
            Host.Builder proLiantML110G5 = HostModels.ProLiantML110G5(simulation).
                            privCpu(2660).privBandwidth(1000000)
                    .resourceManagerFactory(new DefaultResourceManagerFactory())
                    .resourceSchedulerFactory(new DefaultResourceSchedulerFactory());

            host = proLiantML110G5.build();
            AutonomicManager hostAM = new AutonomicManager(simulation, 
                    new HostManager(host));
            hostAM.installPolicy(new HostOperationsPolicy());
            host.installAutonomicManager(hostAM);
            hostPool.addHost(host, hostAM);
            hosts.add(host);
        }
        return hosts;
    }

    public static ArrayList<VmAllocationRequest> createVmList(Simulation simulation, boolean allocAvg) {

        ArrayList<VmAllocationRequest> vmList = new ArrayList<VmAllocationRequest>(N_VMS);

        for (int i = 0; i < N_VMS; ++i) {
            String trace = "traces/clarknet";//TRACES[i % N_TRACES];
            long offset = 9000000;// (int)(simulation.getRandom().nextDouble() * OFFSET_MAX[i % N_TRACES]);
            int size = 2660;
            int cores = 1;
            int memory = 4096;
            Application application = createApplication(simulation, trace, offset, size, cores, memory);
            ArrayList<VmAllocationRequest> requests = application.createInitialVmRequests();

            if (allocAvg) {
                for (VmAllocationRequest request : requests) {
                    request.setCpu(2660);
                    request.setMemory(4096);
                }
            }
            vmList.addAll(requests);
        }
        return vmList;
    }

    private static Application createApplication(Simulation simulation, String fileName, long offset, int coreCapacity, int cores, int memory) {

        TraceWorkload workload = new TraceWorkload(simulation, fileName, 2660, offset); //scale to n replicas		
        int bandwidth = 12800; //100 Mb/s
        int storage = 1024; //1GB
        InteractiveApplication application = Applications.singleTaskInteractiveApplication(simulation, workload, cores, coreCapacity, memory, bandwidth, storage, 0.001f);
        workload.setScaleFactor(1);
        return application;

    }

    public static void placeVms(ArrayList<VmAllocationRequest> vmList, AutonomicManager dcAM, Simulation simulation) {

        VmPlacementEvent vmPlacementEvent = new VmPlacementEvent(dcAM, vmList);

        //ensure that all VMs are placed, or kill the simulation
        vmPlacementEvent.addCallbackListener(new EventCallbackListener() {

            @Override
            public void eventCallback(Event e) {
                VmPlacementEvent pe = (VmPlacementEvent) e;
                if (!pe.getFailedRequests().isEmpty()) {
                    throw new RuntimeException("Could not place all VMs " + pe.getFailedRequests().size());
                }
            }

        });

        simulation.sendEvent(vmPlacementEvent, 0);
    }

    @Override
    public void setup(Simulation simulation) {

        DataCentre dc = new DataCentre(simulation);
        simulation.addDatacentre(dc);

        HostPoolManager hostPool = new HostPoolManager();
        AutonomicManager dcAM = new AutonomicManager(simulation, hostPool);
        dcAM.installPolicy(new HostStatusPolicy(5));
        dcAM.installPolicy(new DefaultVmPlacementPolicy());
        dc.addHosts(createHosts(simulation, dcAM, hostPool));
        ArrayList<VmAllocationRequest> vmList = createVmList(simulation, true);
        placeVms(vmList, dcAM, simulation);

    }

}
