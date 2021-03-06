<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="joda" uri="http://www.joda.org/joda/time/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<script type="text/javascript">
    $(document).ready(function () {
        <c:if test="${action == 'allow'}">
        $("#allow").show();
        </c:if>
    });
</script>

<spring:url var="formUrlPrefix" value="/web"/>

<form:form id="confirm" cssClass="form action-form confirm alert alert-success" method="PUT"
           action="${formUrlPrefix}/application/${application.id}/allow" modelAttribute="comment">

    <div class="form-group">
        <div class="control-label">
            <b><spring:message code='allow.confirm'/></b>
        </div>
    </div>

    <div class="form-group">
        <div class="control-label">
            <spring:message code='comment'/>, <spring:message code="optional"/>: (<span
                id="text-confirm"></span><spring:message code="max.chars"/>)
        </div>
        <form:textarea rows="1" cssClass="form-control" cssErrorClass="form-control error" path="reason"
                       onkeyup="count(this.value, 'text-confirm');"
                       onkeydown="maxChars(this,200); count(this.value, 'text-confirm');"/>
    </div>

    <div class="form-group is-sticky">
        <button type="submit" class="btn btn-success btn-large halves">
            <i class="fa fa-check"></i>&nbsp;<spring:message code='app.state.ok.short'/>
        </button>
        <button type="button" class="btn btn-default btn-large halves" onclick="$('#confirm').hide();">
            <i class="fa fa-remove"></i>&nbsp;<spring:message code='cancel'/>
        </button>
    </div>

</form:form>
