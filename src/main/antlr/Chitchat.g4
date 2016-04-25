grammar Chitchat;

prog: (typedef | correlation | situation)+ ;

// typedef
typedef: annotation TYPE id EXT  base_type ;
base_type: id '(' expressions ')' ;

// correlation
correlation: CORRELATION id '=' (grouping | from_group_name) ;
from_group_name: 'schema.'  id ;

// situation
situation: SITUATION function_call '=' constraints ;

// schema
schema: annotation SCHEMA id '=' grouping ;

// group
grouping: '(' group_ids ')' ;
group_ids: ((ID | STRING | grouping) ','?)+ ;

// define
define: DEFINE id '=' ;

// summary
summary: SUMMARY id '=' '{' summary_content '}';
summary_content: .*? ;

// value
value: function_call '=' '{' (assignment)+ '}' ;

// function
function: FUNCTION function_call '=' '{' commands '}';
commands: (command)+;
command: 'hello_world' ;

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
unit_value: const_value (unit)?;

id: ID | STRING;
primary_expresion: ID | STRING | constant | list ;
constant: INT | FLOAT | TRUE | FALSE | CHAR ;
const_value: INT | FLOAT ;
unit: '_km' | '_m' | '_min' | '_sec' | '_hour' ;
list: '[' (const_value ','?)+ ']' ;
TYPE: 'type';
CORRELATION: 'correlation';
SITUATION: 'situation';

EXT: 'extends';
GROUP: 'group';
SCHEMA: 'schema';
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