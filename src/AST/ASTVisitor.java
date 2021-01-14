package AST;


public interface ASTVisitor {
//    Object visit(ASTNode node);

    Object visit(Program node);
    Object visit(Variable node);
    Object visit(Function node);
    Object visit(ClassDef node);
    Object visit(PrimitiveType node);
    Object visit(ClassType node);
    Object visit(ArrayType node);
    Object visit(BlockStmt node);
    Object visit(VarDefStmt node);
    Object visit(IfStmt node);
    Object visit(WhileStmt node);
    Object visit(BreakStmt node);
    Object visit(ContinueStmt node);
    Object visit(ReturnStmt node);
    Object visit(ExprStmt node);
    Object visit(ForStmt node);
    Object visit(PostFixExpr node);
    Object visit(NewExpr node);
    Object visit(MemberExpr node);
    Object visit(FuncCallExpr node);
    Object visit(SubScriptExpr node);
    Object visit(PreFixExpr node);
    Object visit(BinaryExpr node);
    Object visit(ThisExpr node);
    Object visit(BoolLiteral node);
    Object visit(IntLiteral node);
    Object visit(StringLiteral node);
    Object visit(NullExpr node);
    Object visit(IdentifierExpr node);
    Object visit(VariableList node);
}
