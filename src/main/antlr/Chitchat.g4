grammar Chitchat;

prog: (typedef)+
    ;

typedef: annotation TYPE id EXT  base_type ;
base_type: id '(' expressions ')';

expressions: (expression ','?)+;
expression: comparison | assignment | function_call | primary_expresion;
comparison: ID comparison_operator primary_expresion;
assignment: ID '=' primary_expresion ;
function_call: ID '(' (primary_expresion ','?)* ')';
annotation: ('+'|'-');
comparison_operator: '<'|'>'|'<='|'>=';

id: ID | STRING;
primary_expresion: ID | STRING | constant;
constant: INT | FLOAT ;

EXT: 'extends';
TYPE: 'type';
GROUP: 'group';
RULE: 'rule';

INT: ('+'|'-')?[0-9]+;
FLOAT: ('+'|'-')?[0-9]+'.'[0-9]+;
ID : [a-zA-Z][a-zA-Z0-9]* ;
STRING : '"' .*? '"' ;

COMMENT: '//' ~( '\r' | '\n' )*  -> skip ;
WS: [ \t\r\n]+ -> skip;