package org.whdl.intermediate.expressions;

import org.whdl.intermediate.ExprEvalVisitor;
import org.whdl.intermediate.ExprTypeVisitor;
import org.whdl.intermediate.Expression;
import org.whdl.intermediate.Type;
import org.whdl.intermediate.types.PrimitiveType;

public class IntegerLiteral implements Expression {

  private Integer val;
  public IntegerLiteral(Integer val){
    this.val = val;
  }
  
  @Override
  public Type accept(ExprTypeVisitor v){
    return PrimitiveType.INTEGER;
  }

  @Override
  public Object accept(ExprEvalVisitor v){
    return val;
  }
  
}
