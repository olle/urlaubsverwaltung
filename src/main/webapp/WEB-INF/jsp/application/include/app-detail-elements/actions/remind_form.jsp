<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="joda" uri="http://www.joda.org/joda/time/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<spring:url var="formUrlPrefix" value="/web" />

<form:form method="PUT" id="remind" action="${formUrlPrefix}/application/${application.id}/remind" style="display:inline;"></form:form>

        