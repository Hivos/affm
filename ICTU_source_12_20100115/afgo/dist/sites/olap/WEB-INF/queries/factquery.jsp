<%@ page session="true" contentType="text/html; charset=ISO-8859-1" %>
<%@ taglib uri="http://www.tonbeller.com/jpivot" prefix="jp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<!--
    (c) 2007 ActFact B.V.
-->

<jp:mondrianQuery id="query01" jdbcDriver="oracle.jdbc.driver.OracleDriver" jdbcUrl="jdbc:oracle:thin:bci/compiere@sv001:1521:dev1" catalogUri="/WEB-INF/queries/factcube.xml">
	SELECT
  	{
  		[Measures].[Balance]
  	} ON COLUMNS,
 	{
 		(
 			[Product].[All Products],
 			[Account].[All Accounts]
 		)
 	} ON rows
	FROM Fact_Acct_Balance
</jp:mondrianQuery>

<c:set var="title01" scope="session">Accounting Cube</c:set>
