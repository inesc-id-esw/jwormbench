package jwormbench.test;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.Formatter;

import org.junit.Test;

import junit.framework.Assert;
import jwormbench.core.AbstractStep;
import jwormbench.core.Direction;
import jwormbench.core.IWorld;
import jwormbench.core.IStep;
import jwormbench.core.IWorm;
import jwormbench.core.WormBench;
import jwormbench.core.WormThread;
import jwormbench.defaults.World;
import jwormbench.defaults.DefaultNodeFactory;
import jwormbench.defaults.DefaultCoordinateFactory;
import jwormbench.defaults.DefaultOperationFactory;
import jwormbench.defaults.DefaultWormFactory;
import jwormbench.factories.ICoordinateFactory;
import jwormbench.factories.IOperationFactory;
import jwormbench.factories.IStepFactory;
import jwormbench.factories.IWormFactory;
import jwormbench.setup.WorldFileLoader;
import jwormbench.setup.IStepSetup;
import jwormbench.setup.IWormsSetup;
import jwormbench.setup.StepsFileLoader;
import jwormbench.setup.WormsFileLoader;

public class BenchmarkTests {
  
  @Test
  public void testBenchWorm() throws InterruptedException{
    final String WORLD_CONFIG_FILE = "config/128.txt";
    final String WORM_CONFIG_FILE = "config/W-B[1.8]-H[1.8]-128.txt";
    final String OPERATIONS_CONFIG_FILE = "config/1000_10.txt";
   
    ICoordinateFactory cordFac =  new DefaultCoordinateFactory();
    IWormsSetup wormSetup = new WormsFileLoader(WORM_CONFIG_FILE, cordFac);
    IWorld world = new World(
        new WorldFileLoader(
            WORLD_CONFIG_FILE, new DefaultNodeFactory()));
    IWormFactory wormFac = new DefaultWormFactory(cordFac, world, wormSetup);
    
    final IOperationFactory opsFac = new DefaultOperationFactory(world);
    IStepFactory stepsFac = new IStepFactory() {      
      @Override
      public List<IStep> make() {
        List<IStep> steps = new LinkedList<IStep>();
        for (IStepSetup.OperationProperties opProps: new StepsFileLoader(OPERATIONS_CONFIG_FILE)) {
          Direction dir = Direction.values()[(int)(Math.random()*3)];
          // ignore direction given from file to make
          // the worm navigate on different directions for each test. 
          // steps.add(new AbstractStep(opProps.direction, opsFac.make(opProps.operationKind)) {
          steps.add(new AbstractStep(dir, opsFac.make(opProps.operationKind)) {
            public Object performStep(IWorm worm) {
              Object res = op.performOperation(worm);
              worm.move(direction);
              worm.updateWorldUnderWorm();
              return res;
            }
          });
        }
        return steps;
      }
    };
    Logger logger = Logger.getLogger("");
    logger.getHandlers()[0].setFormatter(new Formatter(){
      public String format(LogRecord record) {
        return record.getMessage();
      }
    });
    WormBench bench = new WormBench(
        world,
        wormFac, 
        stepsFac, 
        logger,
        1, // nr of threads 
        100, // nr of iterations
        0); // time out
    try{
     bench.getWormThread(1); 
    }catch(Exception e){
      Assert.assertEquals(e.getClass(), IndexOutOfBoundsException.class);
    }
    //
    // Get pre conditions
    //
    int initSum =  world.getSumOfAllNodes();
    //
    // Act - Run on main thread
    // Launch and run worker thread
    //
    bench.RunBenchmark("not sync");
    bench.LogExecutionTime();
    bench.LogConsistencyVerification();
    //
    // Assert post conditions
    //    
    WormThread wt = bench.getWormThread(0);    
    Assert.assertEquals(bench.getAccumulatedDiffOnWorld(),wt.getAccumulatedDiffOnWorld());
    Assert.assertEquals(initSum, world.getSumOfAllNodes() - bench.getAccumulatedDiffOnWorld());
  }
}
