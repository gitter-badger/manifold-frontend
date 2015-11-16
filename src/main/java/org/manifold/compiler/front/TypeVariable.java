package org.manifold.compiler.front;

import org.manifold.compiler.SchematicValueVisitor;
import org.manifold.compiler.TypeValue;

import java.util.HashMap;

public class TypeVariable extends TypeValue {

  private final VariableIdentifier id;
  public VariableIdentifier getID() {
    return id;
  }

  public TypeVariable(VariableIdentifier id, TypeValue superType) {
    super(superType, new HashMap<>());
    this.id = id;
  }

  @Override
  public String toString() {
    return getID().toString();
  }

  @Override
  public void accept(SchematicValueVisitor v) {
    throw new UnsupportedOperationException("attempted to visit type variable");
  }

  @Override
  public boolean equals(Object aThat) {
    if (this == aThat) {
      return true;
    } else if (!(aThat instanceof TypeVariable)) {
      return false;
    } else {
      TypeVariable that = (TypeVariable) aThat;
      return (this.getID().equals(that.getID()));
    }
  }

  @Override
  public int hashCode() {
    return getID().hashCode();
  }

}
