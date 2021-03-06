package jwormbench.app.config;

import jwormbench.factories.INodeFactory;
import jwormbench.factories.IStepFactory;
 

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

/**
 * Depends on project jvstm-doublelayout-v3 - version of Multiprog12 - replicate via array. 
 * @author mcarvalho
 *
 */
public class JvstmDblLayoutSyncModule extends AbstractModule{
  @Override
  protected void configure() {
    bind(IStepFactory.class)
    .to(jwormbench.sync.jvstmdbl.JvstmStepFactory.class)
    .in(Singleton.class);
    bind(INodeFactory.class)
    .to(jwormbench.sync.jvstmdbl.NodeDoubleLayoutFactory.class)
    .in(Singleton.class);
  }
}
