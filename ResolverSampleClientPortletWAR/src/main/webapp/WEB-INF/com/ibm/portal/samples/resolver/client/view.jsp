<%-- Per default a JSP generates a session. Make sure to disable this for performance reasons. --%>
<%@ page session="false" buffer="none"%>
<%-- Just the standard JSTL includes --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<form method="POST" action="${fn:escapeXml(bean.actionURL)}">

	<input type="hidden" name="_charset_">

	<!-- select the language -->
	<select name="${bean.keyLanguage}">
		<c:forEach var="lang" items="${bean.languages}">
			<option value="${lang.key}"
				<c:if test="${lang.selected}">selected</c:if>><c:out
					value="${lang.title}" /></option>
		</c:forEach>
	</select>

	<!-- select some extra string -->
	<input type="text" name="${bean.keyInfo}"
		value="${fn:escapeXml(bean.info)}">

	<!-- submit button -->
	<button type="submit" name="${bean.keySubmit}">
		<c:out value="${bean.resourceSubmit.value}" />
	</button>
</form>