grammar Umlang;

/*@parser::header {*/
/*package com.github.dylon.umlang;*/
/*}*/

program
	: ( statement )* EOF
	;

statement
	: initialization
	| declaration
	| assignment
	;

initialization
	: Identifier ':' Identifier '=' expression ';'
	;

declaration
	: Identifier ':' Identifier ';'
	;

assignment
	: Identifier '=' expression
	;

expression
	: '(' expression ')'         // Nested expression
	| expression '*' expression  // Multiplication
	| expression '/' expression  // Division
	| expression '+' expression  // Addition
	| expression '-' expression  // Subtraction
	| Identifier
	| literal
	;

literal
	: real
	| integer
	;

real
	: Integer '.' Integer
	| '.' Integer
	| Integer '.'
	;

integer
	: Integer
	;

WhiteSpace
	: [ \t\r\n\f]+ -> skip
	;

Integer
	: [0-9]+
	;

Identifier
	: [a-zA-Z_$][a-zA-Z_$0-9-]*
	;

