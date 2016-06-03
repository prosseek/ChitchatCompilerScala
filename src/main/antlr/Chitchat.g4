grammar Chitchat;

prog: (typedef | correlation | situation | schema | valuedef | function | command )+ ;

// typedef
typedef: annotation TYPE id EXT  base_type ;
base_type: id '(' expressions ')' ;

// correlation
correlation: CORRELATION id '=' '(' expressions ')' ;

// situation
situation: SITUATION id '(' expressions ')' '=' expression ;

// schema
schema: annotation SCHEMA id '=' '(' expressions ')' ('|' '(' expressions ')' )* ;

// value
valuedef: VALUE id '=' block ;

// function
function: FUNCTION id params '=' block ;

// command
command: '{' (expression ';'?)+ '}' ;

///////////////////////////////////////
// EXPRESSION

ids: (id ','?)+;
expressions: (expression ','?)*;
expression: function_call | value | assignment | comparison ;
params: '(' ( id ','? )* ')' ;
args: '(' ( constant ','?)* ')' ;

comparison: '(' expression comparison_operator expression ')' ;
value: id | constant | list ;
function_call: id args ;
assignment: ID '=' expression ;
absolute: '|' expression '-' expression '|' ;

while_loop: WHILE '(' expression ')' block ;
if_else: IF '(' expression ')' block (ELSE block)? ;

block: '{' expressions '}';

/////////////////////////////////

id: ID | STRING;
annotation: ('+'|'-');
comparison_operator: '<'|'>'|'<='|'>=';
constant: INT | FLOAT | TRUE | FALSE | CHAR ;
unit: '_km' | '_m' | '_min' | '_sec' | '_hour' ;
unit_value: constant (unit)?;
list: '[' (constant ','?)+ ']' ;

////////////////////////////////////////
// TERMINAL

TYPE: 'type';
CORRELATION: 'correlation';
SITUATION: 'situation';
EXT: 'extends';
GROUP: 'group';
SCHEMA: 'schema';
DEFINE: 'define';
SUMMARY: 'summary';
FUNCTION: 'function';
WHILE: 'while';
IF: 'if';
ELSE: 'else';
VALUE: 'value';

CHAR: '\''[a-zA-Z]'\'';
TRUE: 'true';
FALSE: 'false'; 
INT: ('+'|'-')?[0-9]+;
FLOAT: ('+'|'-')?[0-9]+'.'[0-9]+;
ID : [_a-zA-Z][a-zA-Z0-9?]* ;
STRING : '"' .*? '"' ;

COMMENT: '//' ~( '\r' | '\n' )*  -> skip ;
WS: [ \t\r\n]+ -> skip;