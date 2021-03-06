package jwormbench.defaults;

import java.lang.reflect.Field;

import com.google.inject.Inject;

import jwormbench.core.IWorld;
import jwormbench.core.INode;
import jwormbench.core.ICoordinate;
import jwormbench.setup.IWorlSetup;

/**
 * Abstracts the BenchWorld - the underlying data structure that
 * represents the matrix so that it can be changed. 
 * 
 * @author F. Miguel Carvalho mcarvalho[@]cc.isel.pt 
 */
public class World implements IWorld {
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // ---------------------- FIELDS --------------------- 
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  private final INode[][] world;
  final int rowsNum;
  final int columnsNum;
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // -------------------   CONSTRUCTOR ----------------- 
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  @Inject
  public World(IWorlSetup bwSetup) {
    this.world = bwSetup.loadWorld();
    rowsNum= 0; columnsNum = 0;
    init();
  }
  
  public void init(){
      try {
        Field fRowsNum = World.class.getDeclaredField("rowsNum");
        fRowsNum.setAccessible(true);
        Field fColumnsNum = World.class.getDeclaredField("columnsNum");
        fColumnsNum.setAccessible(true);
        fRowsNum.set(this, world.length);
        fColumnsNum.set(this, world[0].length);
    }
    catch (NoSuchFieldException e) {throw new RuntimeException(e);}
    catch (SecurityException e) {throw new RuntimeException(e);} 
    catch (IllegalArgumentException e) {throw new RuntimeException(e);}
    catch (IllegalAccessException e) {throw new RuntimeException(e);}
  }
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // -------------------  PROPERTIES   ----------------- 
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  /**
   * @see jwormbench.core.IWorld#getRowsNum()
   */
  public int getRowsNum() {
    return rowsNum;
  }
  /**
   * @see jwormbench.core.IWorld#getColumnsNum()
   */
  public int getColumnsNum() {
    return columnsNum;
  }
  /**
   * @see jwormbench.core.IWorld#getNode(int, int)
   */
  public INode getNode(int x, int y){
    x = x % rowsNum;
    y = y % columnsNum;
    if (x < 0) x = x + rowsNum;
    if (y < 0) y = y + columnsNum;
    return world[x][y];
  }
  /**
   * @see jwormbench.core.IWorld#getNode(jwormbench.core.ICoordinate)
   */
  public INode getNode(ICoordinate c){
    return getNode(c.getX(), c.getY());
  }
  
  @Override
  public int getSumOfAllNodes() {
    int total = 0;
    for (int i = 0; i < getRowsNum(); i++) {
      for (int j = 0; j < getColumnsNum(); j++) {
        total += world[i][j].getValue();
      }
    }
    return total;
  }
}

