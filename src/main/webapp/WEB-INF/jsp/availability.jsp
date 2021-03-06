<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="true"%>
<%@ page import="javax.persistence.EnumType"%>
<%@ page import="edu.iastate.models.Member"%>
<%@ page import="edu.iastate.models.Team"%>
<% Member member = (Member) session.getAttribute("member");%>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">

<title>Availability</title>

<!-- CSS -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap.min.css">
<link rel="stylesheet" href="//cdnjs.cloudflare.com/ajax/libs/jquery.bootstrapvalidator/0.5.3/css/bootstrapValidator.min.css" />
<style type="text/css" style="http://cdn.datatables.net/1.10.4/css/jquery.dataTables.min.css"></style>

<!-- Page specific CSS -->
<link rel="stylesheet" href="../../resources/css/availability.css"></link>
</head>
<body>
    <jsp:include page="header.jsp" />
    <div class="container-fluid">
        <div class="row">
            <div class="col-lg-9 col-md-8 col-sm-12 col-xs-12">
                <table id="availabilityTable" class="table table-striped table-bordered">
                    <thead>
                        <tr>
                            <th>Day</th>
                            <c:forEach items="${slots}" var="slot">
                                <th><c:out value="${slot.hour() }"></c:out> <c:out value="${slot.period() }"></c:out></th>
                            </c:forEach>
                        </tr>
                    </thead>

                    <tbody>
                        <c:forEach items="${days}" var="day">
                            <tr>
                                <td><c:out value="${day.getName()}"></c:out></td>

                                <c:forEach items="${day.getPeriods()}" var="period">
                                    <td>
                                        <c:choose>
                                            <c:when test="${isTeam}">
                                                <c:if test='${period.isAvailable() }'><span class="glyphicon glyphicon-ok"></span></c:if>
                                            </c:when>
                                            <c:otherwise>
                                                <input type="checkbox" class="checkbox" name="${day.getName()}" value="${period.getSlot()}" <c:if test='${period.isAvailable() }'>checked</c:if>>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </c:forEach>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
                <c:if test="${!isTeam}">
                    <label class="float-right"><input type="checkbox" id="selectall">Select all</label> <br> <br>
                    <button id="update" type="submit" class="btn btn-default float-right">Update</button>
                </c:if>
            </div>
            <jsp:include page="sideBar.jsp" />
        </div>
    </div>
</body>

<footer>
    <!-- jQuery library -->
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>

    <!-- Bootstrap JavaScript plug-ins -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/js/bootstrap.min.js"></script>

    <!-- Bootstrap validator -->
    <script type="text/javascript" src="//cdnjs.cloudflare.com/ajax/libs/jquery.bootstrapvalidator/0.5.2/js/bootstrapValidator.min.js"></script>

    <!-- DataTable -->
    <script type="text/javascript" src="http://cdn.datatables.net/1.10.4/js/jquery.dataTables.min.js"></script>

    <!-- Page specific JS -->
    <script type="text/javascript" src="../../resources/js/availability.js"></script>
    <script type="text/javascript">
        var createTeam = '<c:out value="${createTeam}"/>';
        var teamId = '<c:out value="${teamId}"/>';
    </script>
    <script src="../../resources/js/header.js"></script>
</footer>

</html>