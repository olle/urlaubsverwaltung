<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="joda" uri="http://www.joda.org/joda/time/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<table class="list-table bordered-table selectable-table">
    <tbody>
    <c:forEach items="${applications}" var="app" varStatus="loopStatus">
        <c:choose>
            <c:when test="${app.status.state == 'state.cancelled' || app.status.state == 'state.rejected'}">
                <c:set var="CSS_CLASS" value="inactive"/>
            </c:when>
            <c:otherwise>
                <c:set var="CSS_CLASS" value="active"/>
            </c:otherwise>
        </c:choose>
        <tr class="${CSS_CLASS}" onclick="navigate('${formUrlPrefix}/application/${app.id}');">
            <td class="visible-print">
                <span>
                    <spring:message code="${app.status.state}"/>
                </span>
            </td>
            <td class="is-centered state ${app.status} hidden-print">
                <span>
                     <c:choose>
                         <c:when test="${app.status == 'WAITING'}">
                             <i class="fa fa-question"></i>
                         </c:when>
                         <c:when test="${app.status == 'ALLOWED'}">
                             <i class="fa fa-check"></i>
                         </c:when>
                         <c:when test="${app.status == 'REJECTED'}">
                             <i class="fa fa-ban"></i>
                         </c:when>
                         <c:when test="${app.status == 'CANCELLED'}">
                             <i class="fa fa-trash"></i>
                         </c:when>
                         <c:otherwise>
                             &nbsp;
                         </c:otherwise>
                     </c:choose>
                </span>
            </td>
            <td>
                <h4 class="visible-print">
                    <spring:message code="${app.vacationType.vacationTypeName}"/>
                </h4>
                <a class="hidden-print vacation ${app.vacationType}" href="${formUrlPrefix}/application/${app.id}">
                    <h4><spring:message code="${app.vacationType.vacationTypeName}"/></h4>
                </a>

                <p>
                    <c:choose>
                        <c:when test="${app.startDate == app.endDate}">
                            <uv:date date="${app.startDate}"/>, <spring:message code="${app.howLong.dayLength}"/>
                        </c:when>
                        <c:otherwise>
                            <uv:date date="${app.startDate}"/> - <uv:date date="${app.endDate}"/>
                        </c:otherwise>
                    </c:choose>
                </p>
            </td>
            <td class="is-centered">
                <span class="days-${loopStatus.index}">
                    <%--is filled by javascript--%>
                    <script type="text/javascript">

                        $(document).ready(function () {

                            var dayLength = '<c:out value="${app.howLong}" />';
                            var personId = '<c:out value="${app.person.id}" />';

                            var startDate = "<joda:format pattern='yyyy/MM/dd' value='${app.startDate}' />";
                            var endDate = "<joda:format pattern='yyyy/MM/dd' value='${app.endDate}' />";

                            var from = new Date(startDate);
                            var to = new Date(endDate);

                            sendGetDaysRequest("<spring:url value='/api' />", from, to, dayLength, personId, ".days-${loopStatus.index}");

                        });

                    </script>
                </span>
                <span>Tage</span>
            </td>
            <td class="is-centered hidden-xs hidden-print">
                <i class="fa fa-clock-o"></i>
                <span>
                    <c:choose>
                        <c:when test="${app.status == 'WAITING'}">
                            <spring:message code="progress.applied" /> <uv:date date="${app.applicationDate}" />
                        </c:when>
                        <c:when test="${app.status == 'ALLOWED'}">
                            <spring:message code="progress.allowed" /> <uv:date date="${app.editedDate}" />
                        </c:when>
                        <c:when test="${app.status == 'REJECTED'}">
                            <spring:message code="progress.rejected" /> <uv:date date="${app.editedDate}" />
                        </c:when>
                        <c:when test="${app.status == 'CANCELLED'}">
                            <spring:message code="progress.cancelled" /> <uv:date date="${app.cancelDate}" />
                        </c:when>
                        <c:otherwise>
                            &nbsp;
                        </c:otherwise>
                    </c:choose>
                </span>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>

