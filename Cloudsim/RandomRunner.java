package org.cloudbus.cloudsim.examples.power.util;

import org.cloudbus.cloudsim.brokers.DatacenterBroker;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.cloudlets.CloudletSimple;
import org.cloudbus.cloudsim.util.Log;
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModel;

import java.util.ArrayList;
import java.util.List;
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModelFull;

/**
 * The example runner for the random workload.
 * <p>
 * If you are using any algorithms, policies or workload included in the power package please cite
 * the following paper:
 * <p>
 * Anton Beloglazov, and Rajkumar Buyya, "Optimal Online Deterministic Algorithms and Adaptive
 * Heuristics for Energy and Performance Efficient Dynamic Consolidation of Virtual Machines in
 * Cloud Data Centers", Concurrency and Computation: Practice and Experience (CCPE), Volume 24,
 * Issue 13, Pages: 1397-1420, John Wiley & Sons, Ltd, New York, USA, 2012
 *
 * @author Anton Beloglazov
 * @since Jan 5, 2012
 */
public final class RandomRunner extends RunnerAbstract {
    public final static int NUMBER_OF_VMS = 1000;
    public final static int NUMBER_OF_HOSTS = 1000;
    public final static long CLOUDLET_UTILIZATION_SEED = 1;

    /**
     * @param enableOutput
     * @param outputToFile
     * @param inputFolder
     * @param outputFolder
     * @param workload
     * @param vmAllocationPolicy
     * @param vmSelectionPolicy
     * @param safetyParameterOrUtilizationThreshold a double value to be passed to the specific
     *                                              PowerVmSelectionPolicy being created, which the meaning depends
     *                                              on that policy.
     */
    public RandomRunner(
        boolean enableOutput,
        boolean outputToFile,
        String inputFolder,
        String outputFolder,
        String workload,
        String vmAllocationPolicy,
        String vmSelectionPolicy,
        double safetyParameterOrUtilizationThreshold) {
        super(
            enableOutput,
            outputToFile,
            inputFolder,
            outputFolder,
            workload,
            vmAllocationPolicy,
            vmSelectionPolicy,
            safetyParameterOrUtilizationThreshold);
    }

    /**
     * Creates the cloudlet list.
     *
     * @param broker          the broker
     * @param cloudletsNumber the cloudlets number
     * @return the list< cloudlet>
     */
    public static List<Cloudlet> createCloudletList(DatacenterBroker broker, int cloudletsNumber) {
        List<Cloudlet> list = new ArrayList<>(cloudletsNumber);
        UtilizationModel utilization = new  UtilizationModelFull();
        for (int i = 0; i < cloudletsNumber; i++) {
                
            
            Cloudlet cloudlet= new CloudletSimple(i, Constants.CLOUDLET_LENGTH, Constants.CLOUDLET_PES)
                    .setUtilizationModelCpu(utilization)
                    .setUtilizationModelRam(utilization)
                    .setUtilizationModelBw(utilization)
                    .setFileSize(5000000)
                    .setOutputSize(30000000)
                    .setUtilizationModel(utilization);            
            list.add(cloudlet);
        }

        return list;
    }

    @Override
    protected void init(final String inputFolder) {
        try {
            super.init(inputFolder);

            broker = Helper.createBroker(getSimulation());
            cloudletList = createCloudletList(broker, NUMBER_OF_VMS);
            vmList = Helper.createVmList(broker, cloudletList.size());
            hostList = Helper.createHostList(NUMBER_OF_HOSTS);
        } catch (Exception e) {
            e.printStackTrace();
            Log.printLine("The simulation has been terminated due to an unexpected error");
            System.exit(0);
        }
    }

}
