<%--
- form.jsp
-
- Copyright (c) 2019 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<acme:form>
	<acme:form-url code="sponsor.commercial-banner.form.label.picture" path="picture" />
	<acme:form-textbox code="sponsor.commercial-banner.form.label.slogan" path="slogan" />
	<acme:form-url code="sponsor.commercial-banner.form.label.target" path="target" />
	<acme:form-textbox code="sponsor.commercial-banner.form.label.titleHolder" path="titleHolder" readonly="${command != 'create'}"/>
	<acme:form-textbox code="sponsor.commercial-banner.form.label.creditCardNumber" path="creditCardNumber" readonly="${command != 'create'}"/>
	<acme:form-textbox code="sponsor.commercial-banner.form.label.month" path="month" readonly="${command != 'create'}"/>
	<acme:form-textbox code="sponsor.commercial-banner.form.label.year" path="year" readonly="${command != 'create'}"/>
	<acme:form-textbox code="sponsor.commercial-banner.form.label.cvc" path="cvc" readonly="${command != 'create'}"/>

	<acme:form-return code="sponsor.commercial-banner.form.button.return" />
	<acme:form-submit test="${command == 'show'}" 
		code="sponsor.commercial-banner.form.button.update" 
		action="/sponsor/commercial-banner/update"/>
	<acme:form-submit test="${command == 'show'}" 
		code="sponsor.commercial-banner.form.button.delete" 
		action="/sponsor/commercial-banner/delete"/>
	<acme:form-submit test="${command == 'create'}" 
		code="sponsor.commercial-banner.form.button.create" 
		action="/sponsor/commercial-banner/create"/>
	<acme:form-submit test="${command == 'update'}" 
		code="sponsor.commercial-banner.form.button.update" 
		action="/sponsor/commercial-banner/update"/>
	<acme:form-submit test="${command == 'delete'}" 
		code="sponsor.commercial-banner.form.button.delete" 
		action="/sponsor/commercial-banner/delete"/>
</acme:form>
