<%-- Per default a JSP generates a session. Make sure to disable this for performance reasons. --%>
<%@ page session="false" buffer="none"%>
<%-- Just the standard JSTL includes --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<div>
	<c:forEach var="entry" items="${paramValues}">
		<dl>
			<c:out value="${entry.key}" />
		</dl>
		<dd>
			<ul>
				<c:forEach var="value" items="${entry.value}">
					<li><c:out value="${value}" /></li>
				</c:forEach>
			</ul>
		</dd>
	</c:forEach>
</div>