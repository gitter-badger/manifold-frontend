package org.manifold.compiler.front;

import org.manifold.compiler.TypeValue;
import org.manifold.compiler.Value;

public class VariableDeclarationVertex extends VariableReferenceVertex {
  private TypeValue type;
  private ExpressionVertex vType;

  public VariableDeclarationVertex(ExpressionGraph g, VariableIdentifier id, ExpressionVertex type) {
    super(g, id);
    this.vType = type;
  }

  @Override
  public TypeValue getType() {
    return type;
  }

  @Override
  public void verify() throws Exception {
    super.verify();
    this.elaborate();
  }

  @Override
  public void elaborate() throws Exception {
    super.elaborate();
    type = super.getType();
    vType.elaborate();
    Value value = vType.getValue();
    TypeValue declaredType = TypeAssertions.assertIsType(value);
    // check if assigned value is of the correct declaredType
    if (!type.isSubtypeOf(declaredType)) {
      throw new TypeMismatchException(declaredType, type);
    }
  }
}
