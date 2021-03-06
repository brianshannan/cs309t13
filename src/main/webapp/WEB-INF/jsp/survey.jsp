<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page session="true" %>
<%@ page import="javax.persistence.EnumType" %>
<%@ page import="edu.iastate.models.Member" %>
<% Member member = (Member) session.getAttribute("member"); %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>Survey</title>

    <!-- CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap.min.css">
    <link rel="stylesheet" href="//cdnjs.cloudflare.com/ajax/libs/jquery.bootstrapvalidator/0.5.3/css/bootstrapValidator.min.css"/>
</head>
<body>
    <jsp:include page="header.jsp"/>
    <div class="container-fluid">
        <div class="row">
            <div class="col-lg-9 col-md-8 col-sm-12 col-xs-12">

                <!-- Page specific html -->
                <div class="container-fluid" id="surveyContainer">
                    <div id="surveyBox" class="mainbox col-md-8 col-md-offset-2 col-sm-10 col-msm-offset-1 col-xs-10 col-xs-offset-1">
                        <div class="panel panel-primary">
                            <div class="panel-heading">
                                <h3 class="panel-title">Player Survey</h3>
                            </div> <!-- panel heading -->
                            <div class="panel-body">
                                <form role="form" id="surveyForm" action="<c:out value="/survey/${tournamentId}/submit?isTeamLeader=${isTeamLeader}"/>" method="POST">
                                    <div class="panel-body">
                                        <% if (member.getSex() == null) { %>
                                            <div class="form-group">
                                                <label for="sex">Sex:</label>
                                                <div class="radio">
                                                    <label>
                                                        <input type="radio" name="sex" value="m" /> Male
                                                    </label>
                                                </div>
                                                <div class="radio">
                                                    <label>
                                                        <input type="radio" name="sex" value="f" /> Female
                                                    </label>
                                                </div>
                                            </div>
                                        <% } %>
            
                                        <% if (member.getHeight() == -1) { %>
                                            <div class="form-group">
                                                <label for="height">Height:</label>
                                                <input id="height" name="height" type="text" placeholder="height in inches" class="form-control input-md">
                                            </div>
                                        <% } %>
            
                                        <% if (member.getWeight() == -1) { %>
                                            <div class="form-group">
                                                <label for="weight">Weight:</label>
                                                <input id="weight" name="weight" type="text" placeholder="weight in pounds" class="form-control input-md">
                                            </div>
                                        <% } %>
            
                                        <div class="form-group">
                                            <label for="compYears">Competitive years played in this sport:</label>
                                            <select id="compYears" name="compYears" class="form-control">
                                              <option value=""> Select...</option>
                                              <option value="1">0</option>
                                              <option value="2">1</option>
                                              <option value="3">2</option>
                                              <option value="4">3</option>
                                              <option value="5">More than 3</option>
                                            </select>
                                        </div>
            
                                        <div class="form-group">
                                            <label for="intsPlayed">Number of times playing in this intramural:</label>
                                            <select id="intsPlayed" name="intsPlayed" class="form-control">
                                                <option value=""> Select...</option>
                                                <option value="1">0</option>
                                                <option value="2">1</option>
                                                <option value="3">2</option>
                                                <option value="4">3</option>
                                                <option value="5">More than 3</option>
                                            </select>
                                        </div>
            
                                        <div class="form-group">
                                            <label for="compLvl">How competitive do you want to be:</label>
                                            <select id="compLvl" name="compLvl" class="form-control">
                                                <option value=""> Select...</option>
                                                <option value="1">1 - not competitive</option>
                                                <option value="2">2</option>
                                                <option value="3">3 - neutral</option>
                                                <option value="4">4</option>
                                                <option value="5">5 - very competitive</option>
                                            </select>
                                        </div>
            
                                        <div class="form-group">
                                            <label for="isClubPlayer">University club experience in this sport:</label>
                                            <div class="radio">
                                                <label>
                                                    <input type="radio" name="isClubPlayer" id="isClubPlayer-0" value="false"> No
                                                </label>
                                            </div>
            
                                            <div class="radio">
                                                <label>
                                                    <input type="radio" name="isClubPlayer" id="isClubPlayer-1" value="true"> Yes
                                                </label>
                                            </div>
                                        </div>
            
                                        <div class="btn-group">
                                            <input type="submit" name="submitSurvey" class="btn btn-primary" value="Submit!"/>
                                        </div>
                                    </div>
                                </form>
                            </div> <!-- panel body -->
                        </div> <!-- panel -->
                    </div> <!-- survey box -->
                </div> <!-- container -->

            </div>
            <jsp:include page="sideBar.jsp"/>
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

    <!-- Page specific JS -->
    <script src="../../resources/js/survey.js"></script>
    <script src="../../resources/js/header.js"></script>
</footer>
</html>
