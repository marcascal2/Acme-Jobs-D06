<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<acme:list>
	<acme:list-column code="authenticated.job.duty.list.label.title" path="title" width="20%" />
	<acme:list-column code="authenticated.job.duty.list.label.description" path="description" width="60%" />
	<acme:list-column code="authenticated.job.duty.list.label.percentage" path="percentageTimeForWeek" width="20%" />
</acme:list>

