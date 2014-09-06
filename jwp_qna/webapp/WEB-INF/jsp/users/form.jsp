<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
<!-- spring custom form tag 적용 -->
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>SLiPP :: 회원가입</title>

<%@ include file="../commons/_header.jspf"%>

</head>
<body>
    <%@ include file="../commons/_top.jspf"%>

	<div class="container">
		<div class="row">
			<div class="span12">
				<section id="typography">
				<div class="page-header">
					<h1>회원가입</h1>
				</div>
				
				<!-- spring custom form tag -->
				<!--
					model에 user라는 이름으로 연결된 속성값을 가져옴
					user 객체가 전달되면 User 객체가 가진 setter/getter method를 사용하여
					각각의 속성들과 자동적으로 mapping을 해줌
				 -->
				
				<!--
					pageScope / set tag를 이용하여 변수를 지정
					model은 requestScope
					session은 sessionScope	
				-->
				<c:choose>
				<c:when test="${ empty user.userId }" >
					<c:set var="method" value="post" />
				</c:when>
				<c:otherwise>
					<c:set var="method" value="put" />
					<!--
						http 규격상 get과 post만 사용하여 정보 전달.
						put일지라도 내부적으로 hidden input tag를 생성하여 put이라는 정보를 함께 넘김	
					-->
				</c:otherwise>
				</c:choose>
				
				<form:form modelAttribute="user" cssClass="form-horizontal" action="/users" method="${ method }">
					<div class="control-group">
						<label class="control-label" for="userId">사용자 아이디</label>
						<div class="controls">
						
						<!-- 
							전달받는 데이터는 user객체에서 userId가 존재하는 경우 수정페이지
							아닌 경우 회원가입 페이지
						 -->
							
							<c:choose>
							<c:when test="${ empty user.userId }" >
								<form:input path="userId" />
								<!-- <input type="text" name="userId" value="" /> -->
								<!-- error message인 경우 form:errors라는 tag를 이용하여 적용 가능 -->
								<form:errors path="userId" cssClass="error" />
							</c:when>
							
							<c:otherwise>
								${ user.userId }
								<form:hidden path="userId" />
								<!-- hidden 처리를 하는 이유는 submit되었을 때 userId값이 제대로 mapping되기 위함 -->
							</c:otherwise>
							</c:choose>
						</div>
					</div>
					<div class="control-group">
						<label class="control-label" for="password">비밀번호</label>
						<div class="controls">
							<form:password path="password" />
							<!-- <input type="password" id="password" name="password" placeholder="" /> -->
							<form:errors path="password" cssClass="error" />
						</div>
					</div>
					<div class="control-group">
						<label class="control-label" for="name">이름</label>
						<div class="controls">
							<form:input path="name" />
							<!-- <input type="text" id="name" name="name" value="" placeholder="" /> -->
							<form:errors path="name" cssClass="error" />
						</div>
					</div>
					<div class="control-group">
						<label class="control-label" for="email">이메일</label>
						<div class="controls">
							<form:input path="email" />
							<!-- <input type="text" id="email" name="email" value="" /> -->
							<form:errors path="email" cssClass="error" />
						</div>
					</div>
					<div class="control-group">
						<div class="controls">
							<button type="submit" class="jbtn btn-primary">회원가입</button>
						</div>
					</div>
				</form:form>
				
				<%-- <form class="form-horizontal" action="/users" method="post">
					
				</form> --%>

			</div>
		</div>
	</div>
</body>
</html>