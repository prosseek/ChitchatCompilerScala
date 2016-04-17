grammar Chitchat;

prog: (typedef)+
    ;

typedef: annotation TYPE id EXT  base_type NEWLINE
       ;

base_type: id '(' assigns ')';
assigns: (assign ','?)+;
assign: ID '=' INT ;
annotation: ('+'|'-');
id: ID | STRING;

EXT: 'extends';
TYPE: 'type';
GROUP: 'group';
RULE: 'rule';

INT: [0-9]+;
ID : [a-z][a-zA-Z0-9]* ;
STRING : '"' .*? '"' ;
NEWLINE : [\r\n]+ ;

WS: [ \t]+ -> skip;