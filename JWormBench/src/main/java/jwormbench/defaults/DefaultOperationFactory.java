package jwormbench.defaults;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Inject;

import jwormbench.core.operations.*;
import jwormbench.core.IWorld;
import jwormbench.core.IOperation;
import jwormbench.core.OperationKind;
import jwormbench.factories.IOperationFactory;

public class DefaultOperationFactory implements IOperationFactory {
  private final Map<OperationKind , IOperation<?>> operations;

  @Inject
  public DefaultOperationFactory(IWorld world){
    operations = new HashMap<OperationKind, IOperation<?>>();
    IOperation<?> op = new Sum(world);
    operations.put(op.getKind(), op);
    op = new Average(world);
    operations.put(op.getKind(), op);
    op = new Median(world);
    operations.put(op.getKind(), op);
    op = new Minimum(world);
    operations.put(op.getKind(), op);
    op = new Maximum(world);
    operations.put(op.getKind(), op);
    op = new ReplaceMaxWithAverage(world, this);
    operations.put(op.getKind(), op);
    op = new ReplaceMinWithAverage(world, this);
    operations.put(op.getKind(), op);
    op = new ReplaceMedianWithAverage(world, this);
    operations.put(op.getKind(), op);
    op = new ReplaceMaxAndMin(world, this);
    operations.put(op.getKind(), op);
    op = new ReplaceMedianWithMin(world, this);
    operations.put(op.getKind(), op);
    op = new ReplaceMedianWithMax(world, this);
    operations.put(op.getKind(), op);
    op = new Sort(world, this);
    operations.put(op.getKind(), op);
    op = new Transpose(world, this);
    operations.put(op.getKind(), op);
  }
  /* (non-Javadoc)
   * @see jwormbench.defaults.IOperationFactory#make(jwormbench.defaults.OperationKind)
   */
  @SuppressWarnings("unchecked")
  public <T> IOperation<T> make(OperationKind opKind){
    IOperation<T> op = (IOperation<T>) operations.get(opKind);
    if(op == null)
      throw new UnsupportedOperationException("Operation not implementes for " + opKind);
    return op;
  }
}
