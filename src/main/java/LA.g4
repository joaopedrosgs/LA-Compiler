﻿grammar LA;
@lexer::members {void erroLexico(String msg) { throw new ParseCancellationException(msg); }}

fragment ALGARISMO : '0'..'9';
fragment LETRA: [a-zA-Z];

IDENT: (LETRA|'_') ('_'|ALGARISMO|LETRA)*;

CADEIA : '"' ~('\n' | '\r' | '"')* '"';

NUM_INT : ALGARISMO+;

NUM_REAL : ALGARISMO+ '.' ALGARISMO+;

// Comentarios e espacos em branco são ignorados

COMENTARIO: '{' ~('}'|'\n'|'\r')* '}' -> skip;

// Ignorar espacos em branco
WS:   (' ') -> skip;

// Ignorar fim de linha
ENDL:  ([\n] | [\t] | [\r]) -> skip;

programa : declaracoes 'algoritmo' corpo 'fim_algoritmo' ;

declaracoes : decl_local_global*;

decl_local_global : decl_local | decl_global;

decl_local : 'declare' variavel 
    | 'constante' id1=IDENT ':' tipo_basico '=' valor_constante
    | 'tipo' id2=IDENT ':' tipo;

variavel : id=identificador (',' outrosIds+=identificador)* ':' tipo;

identificador : id=IDENT ('.' outrosIds+=IDENT)* dimensao;

dimensao : ('[' exp_aritmetica ']')*;

tipo : registro | tipo_estendido;

tipo_basico : 'literal' | 'inteiro' | 'real' | 'logico';

tipo_basico_ident : tipo_basico | IDENT;

tipo_estendido : '^'? tipo_basico_ident;

valor_constante : CADEIA | NUM_INT | NUM_REAL | 'verdadeiro' | 'falso';

registro : 'registro' variavel* 'fim_registro';

decl_global : 'procedimento' ident1=IDENT '(' (params1=parametros)? ')' (decl1+=decl_local)* (c1+=cmd)* 'fim_procedimento'
    | 'funcao' ident2=IDENT '(' (params2=parametros)? ')' ':' tipo_estendido (decl2+=decl_local)* (c2+=cmd)* 'fim_funcao';

parametro : 'var'? id1=identificador (',' id2+=identificador)* ':' tipo_estendido;

parametros : param1=parametro (',' param2+=parametro)*;

corpo : decl_local* cmd*;

cmd : cmdLeia | cmdEscreva | cmdSe | cmdCaso | cmdPara | cmdEnquanto | cmdFaca
    | cmdAtribuicao | cmdChamada | cmdRetorne;

cmdLeia : 'leia' '(' '^'? id1=identificador (',' '^'? id2+=identificador)* ')';

cmdEscreva : 'escreva' '(' exp1=expressao (',' exp2+=expressao)* ')';

cmdSe : 'se' e1=expressao 'entao' (c1+=cmd)* ('senao' c2+=cmd*)? 'fim_se';

cmdCaso : 'caso' exp_aritmetica 'seja' selecao ('senao' cmd*)? 'fim_caso';

cmdPara : 'para' IDENT '<-' ea1=exp_aritmetica 'ate' ea2=exp_aritmetica 'faca' cmd* 'fim_para';

cmdEnquanto : 'enquanto' expressao 'faca' cmd* 'fim_enquanto';

cmdFaca : 'faca' cmd* 'ate' expressao;

cmdAtribuicao : (ponteiro='^')? identificador '<-' expressao;

cmdChamada : id1=IDENT '(' exp=expressao (',' outrasExp+=expressao)* ')';

cmdRetorne : 'retorne' expressao;

selecao : item_selecao*;

item_selecao : constantes ':' cmd*;

constantes : ni1=numero_intervalo (',' ni2+=numero_intervalo)*;

numero_intervalo : opu1=op_unario? ni1=NUM_INT ('..' (opu2=op_unario)? ni2=NUM_INT)?;

op_unario : '-';

exp_aritmetica : t1=termo (op1 outrosTermos+=termo)*;

termo : f1=fator (op2 outrosFatores+=fator)*;

fator : p1=parcela (op3 outrasParcelas+=parcela)*;

op1 : '+' | '-';

op2 : '*' | '/';

op3 : '%';

parcela : op_unario? parcela_unario | parcela_nao_unario;

parcela_unario : '^'? identificador
    | IDENT '(' exp1=expressao (',' exp2+=expressao)* ')'
    | NUM_INT
    | NUM_REAL
    | '(' exp3=expressao ')'
    ;

parcela_nao_unario : '&' identificador | CADEIA;

exp_relacional : e1=exp_aritmetica (op_relacional e2=exp_aritmetica)?;

op_relacional : '=' | '<>' | '>=' | '<=' | '>'| '<';

expressao : t1=termo_logico (op_logico1 t2+=termo_logico)*; 

termo_logico : f1=fator_logico (op_logico2 f2+=fator_logico)*;

fator_logico : (nao='nao')? parcela_logica;

parcela_logica : 'verdadeiro' | 'falso' | exp_relacional;

op_logico1 : 'ou';

op_logico2 : 'e';

// Regra especial para detectar erros léxicos no parser, onde é mais fácil obter informações sobre a parte da entrada que causou o erro

COMENTARIO_NAO_FECHADO
:   '{' .*?
    {erroLexico("Linha " + (getLine()+1) + ": comentario nao fechado");}
;

ERRO_LEXICO
:	.
	{erroLexico("Linha " + getLine() + ": " + getText() + " - simbolo nao identificado");}
;
