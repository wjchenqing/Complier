grammar Mx;

@header {
package Parser;
}

program
    :   (programUnit)*
    ;

programUnit
    :   funcDef
    |   classDef
    |   varDef
    |   ';'
    ;

funcDef
    :   (VOID | type) IDENTIFIER '(' paramList? ')' block
    ;

classDef
    :   CLASS IDENTIFIER '{' (varDef | funcDef | constructorDef)* '}' ';'
    ;

varDef
    :   type varDefList ';'
    ;

varDefList
    :   varDefUnit (',' varDefUnit)*
    ;

varDefUnit
    :   IDENTIFIER ('=' expr)?
    ;

constructorDef
    :   IDENTIFIER '(' paramList? ')' block
    ;

type:   type '[' ']'
    |   nonArrayType
    ;

primitiveType
    :   BOOL
    |   INT
    |   STRING
    ;

nonArrayType
    :   primitiveType
    |   IDENTIFIER
    ;

paramList
    :   param (',' param)*
    ;

param
    :   type IDENTIFIER
    ;

block
    : '{' statement* '}'
    ;

statement
    :   block                                               #blockStmt
    |   varDef                                              #varDefStmt
    |   IF '(' expr ')' statement (ELSE statement)?         #ifStmt
    |   FOR '(' init=expr? ';'
                cond=expr? ';'
                step=expr? ')' statement                    #forStmt
    |   WHILE '(' expr ')' statement                        #whileStmt
    |   BREAK ';'                                           #breakStmt
    |   CONTINUE ';'                                        #continueStmt
    |   RETURN expr? ';'                                    #returnStmt
    |   expr ';'                                            #exprStmt
    |   ';'                                                 #emptyStmt
    ;

expr:   expr op=('++' | '--')                               #postfixExpr
    |   <assoc=right> NEW newInstance                           #newExpr
    |   expr '.' IDENTIFIER                                 #memberExpr
    |   expr '(' exprList? ')'                              #funcCallExpr
    |   expr '[' expr ']'                                   #subscriptExpr
    |   <assoc=right> op=('++' | '--') expr                 #prefixExpr
    |   <assoc=right> op=( '+' | '-' ) expr                 #prefixExpr
    |   <assoc=right> op=( '!' | '~' ) expr                 #prefixExpr
    |   opd1=expr op=('*' | '/' | '%') opd2=expr            #binaryExpr
    |   opd1=expr op=('+' | '-') opd2=expr                  #binaryExpr
    |   opd1=expr op=('<<' | '>>') opd2=expr                #binaryExpr
    |   opd1=expr op=('<' | '>' | '<=' | '>=') opd2=expr    #binaryExpr
    |   opd1=expr op=('==' | '!=') opd2=expr                #binaryExpr
    |   opd1=expr op='&' opd2=expr                          #binaryExpr
    |   opd1=expr op='|' opd2=expr                          #binaryExpr
    |   opd1=expr op='^' opd2=expr                          #binaryExpr
    |   opd1=expr op='&&' opd2=expr                         #binaryExpr
    |   opd1=expr op='||' opd2=expr                         #binaryExpr
    |   <assoc=right> opd1=expr op='=' opd2=expr            #binaryExpr
    |   '(' expr ')'                                        #subExpr
    |   THIS                                                #thisExpr
    |   constant                                            #constExpr
    |   IDENTIFIER                                          #idExpr
    ;

exprList
    :   expr (',' expr)*
    ;

newInstance
    :   nonArrayType ('[' expr ']')*('[' ']')+('[' expr ']')+   #wrongNew
    |   nonArrayType ('[' expr ']')+('[' ']')*                  #arrayNew
    |   nonArrayType '(' ')'                                    #classNew
    |   nonArrayType                                            #classNew
    ;

constant
    :   BoolLiteral
    |   IntLiteral
    |   StringLiteral
    |   NULL
    ;

BoolLiteral: TRUE | FALSE;
IntLiteral: '0' | [1-9][0-9]*;
StringLiteral: '"' (ESC|.)*? '"';
ESC: '\\"' | '\\n' | '\\\\';

INT:    'int';
BOOL:   'bool';
STRING: 'string';
NULL:   'null';
VOID:   'void';
TRUE:   'true';
FALSE:  'false';
IF:     'if';
ELSE:   'else';
FOR:    'for';
WHILE:  'while';
BREAK:  'break';
CONTINUE:'continue';
RETURN: 'return';
NEW:    'new';
CLASS:  'class';
THIS:   'this';

IDENTIFIER: [a-zA-Z][a-zA-Z_0-9]*;

Whitespace
    :   [ \t\n\r]+  -> skip
    ;

Newline
    :   (   '\r' '\n'?
        |   '\n'
        )   -> skip
    ;

LineComment
    :   '//' ~[\r\n]*   -> skip
    ;

BlockComment
    :   '/*' .*? '*/'   -> skip
    ;
