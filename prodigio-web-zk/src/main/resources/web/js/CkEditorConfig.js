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
CKEDITOR.editorConfig = function(config) {
	config.resize_enabled = false;
	config.toolbar = 'Complex';
	config.toolbar_Simple = [ [ 'Bold', 'Italic', '-', 'NumberedList',
			'BulletedList', '-', 'Link', 'Unlink', '-', 'About' ] ];
	config.toolbar_Complex = [
			[ 'Bold', 'Italic', 'Underline', 'Strike', 'Subscript',
					'Superscript', 'TextColor', 'BGColor', '-', 'Cut', 'Copy',
					'Paste', 'Link', 'Unlink', 'Image' ],
			[ 'Undo', 'Redo', '-', 'JustifyLeft', 'JustifyCenter',
					'JustifyRight', 'JustifyBlock' ],
			[ 'Table', 'Smiley', 'SpecialChar', 'PageBreak', 'Styles',
					'Format', 'Font', 'FontSize', 'Maximize' ] ];
};