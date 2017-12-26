/*******************************************************************************
 * Minerium Meta Framework
 *
 * Licença: GNU Lesser General Public License (LGPL), version 3.
 *  
 * Copyright (C) (2013-2018) Prodemge. Todos os direitos reservados.
 *   
 * Este arquivo é parte do Minerium Meta Framework
 * O Minerium Meta Framework é um software livre; você pode redistribuí-lo e/ou 
 * modificá-lo dentro dos termos da GNU Lesser General Public License (LGPL), version 3.
 *
 * Este framework é distribuído na esperança de que possa ser  útil, 
 * mas SEM NENHUMA GARANTIA; sem uma garantia implícita de ADEQUAÇÃO
 * a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. 
 * Ver arquivo LICENSE.md no diretório raiz ou acessar <https://www.gnu.org/licenses/lgpl.html>
 *******************************************************************************/

function cep(compId) {
	mascara(compId, "99999-999");
}

function rg(compId) {
	mascara(compId, "aa-99999999");
}

function cpf(compId) {
	mascara(compId, "999.999.999/99");
}

function cnpj(compId) {
	mascara(compId, "99.999.999/9999-99");
}

function telefone(compId) {
	jQuery(compId)
    .mask("(99) 9999-9999?9")
    .focusout(function (event) {  
        var target, phone, element;  
        target = (event.currentTarget) ? event.currentTarget : event.srcElement;  
        phone = target.value.replace(/\D/g, '');
        element = $(target);  
        element.unmask();  
        if(phone.length > 10) {  
            element.mask("(99) 99999-999?9");  
        } else {  
            element.mask("(99) 9999-9999?9");  
        }  
    });
}


function dataHora(compId) {
	mascara(compId, "99/99/9999 99:99:99");
}

function monetario(compId) {
	$('#' + compId).priceFormat( {
		prefix : '',
		centsSeparator : ',',
		thousandsSeparator : '.'
	});
}

function monetario(compId, centLimit) {
	$('#' + compId).priceFormat( {
		prefix : '',
		centsSeparator : ',',
		thousandsSeparator : '.',
		centsLimit: centLimit
	});
}

function monetario(compId, centLimit, length) {
	$('#' + compId).priceFormat( {
		limit : length,
		prefix : '',
		centsSeparator : ',',
		thousandsSeparator : '.',
		centsLimit: centLimit
	});
}

function mascara(compId, mask) {
	compId = '#' + compId;
	jQuery(compId).mask(mask);
}

function onPaste(compId, mask) {
	compId = '#' + compId;
	$(compId).on('paste', function(e) {
	    e.preventDefault();
	});
}


function autotab(id){
	$(document).ready(function() {
		$('#'+id).autotab_filter(
			{format: 'all',uppercase: false});
			});
}

function upper(id){
	$(document).ready(function() {
		var lstr = document.getElementById(id)
        var caretPosition = getCaretPosition(lstr);
		lstr.value = lstr.value.toUpperCase();
        setCaretPosition(lstr, caretPosition);
	});
	
}

function getCaretPosition(ctrl) {
    var CaretPos = 0;    // IE Support
    if (document.selection) {
        //ctrl.focus();
        var Sel = document.selection.createRange();
        Sel.moveStart('character', -ctrl.value.length);
        CaretPos = Sel.text.length;
    }
    // Firefox support
    else if (ctrl.selectionStart || ctrl.selectionStart == '0') {
        CaretPos = ctrl.selectionStart;
    }
    
    return CaretPos;
}

function corrigeCursorBandBox(id){
	$(document).ready(function() {
		var lstr = document.getElementById(id)
		
        // Remember original caret position
       // var caretPosition =lstr.value.replace(/^\s+|\s+$/g, '').length;
        setCaretPosition(lstr,'0');
	});
	
}

function verificarBandboxLenght(e) {
	if (e.keyCode == '8' && $('#'+e.target.id).val() == 0) {
		zAu.send(new zk.Event(zk.Widget.$(jq(e.target.id, zk)[0]), 'onCtrlKey'))
	}
}

function setCaretPosition(ctrl, pos) {
    if (ctrl.setSelectionRange) {
        //ctrl.focus();
        ctrl.setSelectionRange(pos,pos);
    }
    else if (ctrl.createTextRange) {
        var range = ctrl.createTextRange();
        range.collapse(true);
        range.moveEnd('character', pos);
        range.moveStart('character', pos);
        range.select();
    }
}



/*Função Pai de Mascaras*/
function mascara2(o,f){
        v_obj=o;
        v_fun=f;
        setTimeout("execmascara()",1);
}

/*Função que Executa os objetos*/
function execmascara(){
        v_obj.value=v_fun(v_obj.value);
}

/*Função que Determina as expressões regulares dos objetos*/
function leech(v){
        v=v.replace(/o/gi,"0");
        v=v.replace(/i/gi,"1");
        v=v.replace(/z/gi,"2");
        v=v.replace(/e/gi,"3");
        v=v.replace(/a/gi,"4");
        v=v.replace(/s/gi,"5");
        v=v.replace(/t/gi,"7");
        return v;
}

/*Função que padroniza a conta Contabil*/
function ContaContabil(v){
        
		a=7;
		v=v.replace(/\D/g,"");                                
        v=v.replace(/(\d{1})(\d)/,"$1.$2");         
        v=v.replace(/(\d{1})(\d)/,"$1.$2");         
        v=v.replace(/(\d{1})(\d)/,"$1.$2");
		v=v.replace(/(\d{1})(\d)/,"$1.$2");
		v=v.replace(/(\d{1})(\d)/,"$1.$2");
		while (a<25){ 
		 v=v.replace(/(\d{2})(\d)/,"$1.$2");
		 a=a+2;
		}   
        return v;
}

/*Função que ADICIONA A FUNÇÃO ContaContabil aos eventos onkey*/
function InjetaJS(v){
        				
	//elem = document.getElementById('cconta2');					
	elem = document.getElementById(v);
	
	elem.onkeydown=function(){
		mascara2(this,ContaContabil);
	}
	elem.onkeyup=function(){
		mascara2(this,ContaContabil);
	}
	elem.onkeypress=function(){ 
		mascara2(this,ContaContabil);
     }   
}
