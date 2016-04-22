grammar Chitchat;

prog: (typedef | correlation | situation)+ ;

// typedef
typedef: annotation TYPE id EXT  base_type ;
base_type: id '(' expressions ')' ;

// group
group: GROUP id '=' grouping ('|' grouping)? ;
grouping: '(' group_ids ')' ;
group_ids: ((ID | STRING | grouping) ','?)+ ;

// correlation
correlation: CORRELATION id '=' (grouping | from_group_name) ;
from_group_name: 'group.'  id ;

// situation
situation: SITUATION id '=' constraints ;

// rule
rule: RULE id '=' grouping ;

// define
define: DEFINE id '=' ;

// summary
summary: SUMMARY id '=' '{' summary_content '}';
summary_content: .* ;

// function
function: FUNCTION id '=' '{' commands '}';
commands: (command)+;
command: 'a' ;

constraints: absolute_constraint | range_constraint ;
absolute_constraint: '|' id '-' id '|' comparison_operator unit_value  ;
range_constraint: unit_value comparison_operator id comparison_operator unit_value ;


ids: (id ','?)+;
expressions: (expression ','?)+;
expression: comparison | assignment | function_call | primary_expresion;
comparison: ID comparison_operator primary_expresion;
assignment: ID '=' primary_expresion ;
function_call: ID '(' params ')';
params: (primary_expresion ','?)* ;
annotation: ('+'|'-');
comparison_operator: '<'|'>'|'<='|'>=';
unit_value: value (unit)?;

id: ID | STRING;
primary_expresion: ID | STRING | constant;
constant: INT | FLOAT | TRUE | FALSE | CHAR ;
value: INT | FLOAT ;
unit: 'km' | 'm' | 'min' | 'sec' | 'hour' ;

TYPE: 'type';
CORRELATION: 'correlation';
SITUATION: 'situation';

EXT: 'extends';
GROUP: 'group';
RULE: 'rule';
DEFINE: 'define';
SUMMARY: 'summary';
FUNCTION: 'function';

CHAR: '\''[a-zA-Z]'\'';
TRUE: 'true';
FALSE: 'false'; 
INT: ('+'|'-')?[0-9]+;
FLOAT: ('+'|'-')?[0-9]+'.'[0-9]+;
ID : [a-zA-Z][a-zA-Z0-9?]* ;
STRING : '"' .*? '"' ;

COMMENT: '//' ~( '\r' | '\n' )*  -> skip ;
WS: [ \t\r\n]+ -> skip;