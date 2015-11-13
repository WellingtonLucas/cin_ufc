<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%><html>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<div class="container">
	<div class="row">
		<h2>Melhores apostadores</h2>
       	<c:forEach var="aluno" items="${jogo.alunos }" varStatus="idAlu">
            <div class="items col-xs-12 col-sm-10 col-md-10 col-lg-10 clearfix">
               <div class="info-block block-info clearfix">
                    <div class="square-box pull-left">
                        <span class="glyphicon glyphicon-user glyphicon-lg"></span>
                    </div>
                    <h5>Nome</h5>
                    <h4>${aluno.nome } ${aluno.sobreNome }</h4>
                    <p>Posição <span>${idAlu.count}</span></p>
                </div>
            </div>
    	</c:forEach>
	</div>
</div>