package org.manifold.compiler.front;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;

import org.manifold.compiler.TypeValue;
import org.manifold.compiler.UndefinedBehaviourError;

public class TypeChecker implements ExpressionVisitor {

  // type of last visited expression
  private TypeValue type;

  private Map<NamespaceIdentifier, Namespace> namespaces;
  private Namespace environment;
  private Scope currentScope;

  public TypeChecker(Map<NamespaceIdentifier, Namespace> namespaces,
      Namespace env) {
    this.namespaces = namespaces;
    this.environment = env;
  }

  public void check() throws VariableNotAssignedException {
    currentScope = environment.getPrivateScope();
    for (VariableIdentifier vi : currentScope.getSymbolIdentifiers()) {
      try {
        Variable v = currentScope.getVariable(vi);
        Expression rhs = v.getValueExpression();
        rhs.accept(this);
        v.setType(type);
      } catch (VariableNotDefinedException e) {
        throw new UndefinedBehaviourError(
            "inconsistency: variable identifier '" + vi.toString() +
            " present in scope but not actually defined");
      }
    }
  }

  public static void typecheck(
      Map<NamespaceIdentifier, Namespace> namespaces,
      Namespace env) throws VariableNotAssignedException {
    TypeChecker t = new TypeChecker(namespaces, env);
    t.check();
  }

  @Override
  public void visit(FunctionInvocationExpression functionInvocationExpression) {
    // TODO Auto-generated method stub

  }

  @Override
  public void visit(LiteralExpression literalExpression) {
    // this one is easy because all values know their type
    this.type = literalExpression.getValue(currentScope).getType();
  }

  @Override
  public void visit(VariableAssignmentExpression variableAssignmentExpression) {
    // TODO Auto-generated method stub

  }

  @Override
  public void visit(VariableReferenceExpression variableReferenceExpression) {
    VariableIdentifier vid = variableReferenceExpression.getIdentifier();
    // TODO check for private vs. public reference
    NamespaceIdentifier id = vid.getNamespaceIdentifier();
    Namespace ns = checkNotNull(namespaces.get(id));
    try {
      Variable v = ns.getPrivateScope().getVariable(vid);
      if (v.getType().equals(UnknownTypeValue.getInstance())) {
        // no type assigned yet; attempt to assign one
        // TODO(murphy) fix cyclic resolution
        v.getValueExpression().accept(this);
        // implicitly populates `type`
      } else {
        type = v.getType();
      }
    } catch (VariableNotDefinedException e) {
      throw new TypeError(e);
    } catch (VariableNotAssignedException e) {
      throw new TypeError(e);
    }
  }

  class TypeError extends Error {
    private static final long serialVersionUID = -6551145239250515985L;
    private final Throwable cause;
    @Override
    public Throwable getCause() {
      return cause;
    }
    public TypeError(Throwable cause) {
      this.cause = cause;
    }
    @Override
    public String getMessage() {
      return cause.getMessage();
    }
  }

}
