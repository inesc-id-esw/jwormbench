package jwormbench.core.operations;

import jwormbench.core.AbstractOperation;
import jwormbench.core.IWorld;
import jwormbench.core.ICoordinate;
import jwormbench.core.IWorm;
import jwormbench.core.OperationKind;

public class Average extends AbstractOperation<Integer>{
  public Average(IWorld world) {
    super(world, OperationKind.Average, false);
  }

  @Override
  public Integer performOperation(IWorm w) {
    int sum = 0;
    for (int i = 0; i < w.getHeadLength(); i++) {
      ICoordinate c = w.getHeadCoordinate(i);
      sum += world.getNode(c).getValue();
    }
    int average = sum / w.getHeadLength();
    return average;
  }
}
