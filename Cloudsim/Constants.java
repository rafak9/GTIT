package org.cloudbus.cloudsim.examples.power.util;

import org.cloudbus.cloudsim.power.models.PowerModel;
import org.cloudbus.cloudsim.power.models.PowerModelCubic;
import org.cloudbus.cloudsim.power.models.PowerModelLinear;
import org.cloudbus.cloudsim.power.models.PowerModelSpecPowerHpProLiantMl110G4Xeon3040;
import org.cloudbus.cloudsim.power.models.PowerModelSpecPowerHpProLiantMl110G5Xeon3075;
import org.cloudbus.cloudsim.power.models.PowerModelSpecPowerIbmX3250XeonX3470;
import org.cloudbus.cloudsim.power.models.PowerModelSpecPowerIbmX3250XeonX3480;
import org.cloudbus.cloudsim.power.models.PowerModelSqrt;
import org.cloudbus.cloudsim.power.models.PowerModelSquare;

/**
 * Some constants used for power-aware simulation examples.
 *
 * <p>If you are using any algorithms, policies or workload included in the power package, please cite
 * the following paper:
 * <br>
 * Anton Beloglazov, and Rajkumar Buyya, "Optimal Online Deterministic Algorithms and Adaptive
 * Heuristics for Energy and Performance Efficient Dynamic Consolidation of Virtual Machines in
 * Cloud Data Centers", Concurrency and Computation: Practice and Experience (CCPE), Volume 24,
 * Issue 13, Pages: 1397-1420, John Wiley & Sons, Ltd, New York, USA, 2012
 * </p>
 * @author Anton Beloglazov
 */
public final class Constants {
    public final static boolean ENABLE_OUTPUT = true;
    public final static boolean OUTPUT_CSV = false;
    public final static double SCHEDULING_INTERVAL =  300;
    public final static double SIMULATION_LIMIT = 3600;// 24 * 60 * 60;

    public final static int CLOUDLET_LENGTH = 2660 * (int) SIMULATION_LIMIT;
  
    public final static int CLOUDLET_PES = 1;
    public final static int VM_TYPES = 1;

    public final static int[] VM_MIPS = {2660};
    public final static int[] VM_PES = {1};

    public final static int[] VM_RAM = {4096};

    public final static int VM_BW = 100000; // 100 Mbit/s
    public final static int VM_SIZE = 10240; // 10000

    /**
     * Defines the number of host configurations statically given
     * in the arrays {@link #HOST_MIPS}, {@link #HOST_PES} and
     * {@link #HOST_RAM}. <p/>
     * <p>
     * The values of this constants currently define 2 Host Types:
     * <ul>
     * <li>HP ProLiant ML110 G4 (1 x [Xeon 3040 1860 MHz, 2 cores], 4GB)</li>
     * <li>HP ProLiant ML110 G5 (1 x [Xeon 3075 2660 MHz, 2 cores], 4GB)</li>
     * </ul>
     * <p>
     * We increase the memory size to enable over-subscription (x4)
     */
    public final static int HOST_TYPES = 1;

//    public final static int HOST_TYPES = 2;

    /**
     * The PEs capacity of each host defined by {@link #HOST_TYPES}.
     */
    public final static long[] HOST_MIPS = {2660, 2660};

    /**
     * The number of PEs of each host defined by {@link #HOST_TYPES}.
     */
//    public final static int[] HOST_PES = {2, 2};
    public final static int[] HOST_PES = {2};

    /**
     * The RAM capacity of each host defined by {@link #HOST_TYPES}.
     */
    public final static int[] HOST_RAM = {4096,4096};

    public final static long HOST_BW = 1000000; // 1 Gbit/s
    public final static int HOST_STORAGE = 1000000000; // 

    
  public final static PowerModel[] HOST_POWER = {
 
    //http://www.spec.org/power_ssj2008/results/res2011q1/power_ssj2008-20110124-00339.html
      new PowerModelSpecPowerHpProLiantMl110G5Xeon3075() //Linear Interpol
   // new PowerModelLinear(135, 0.6941) // Linear
   // new PowerModelSquare(135,0.6941) //Square
   // new PowerModelCubic(135,0.6941) //Cubic
   // new PowerModelSqrt(135, 0.6941) //Square Root
   // 0.6941 = 93.7 / 135    
  
};
    
    /**
     * A private constructor to avoid class instantiation.
     */
    private Constants(){}
}
