<%@ page session="true" contentType="text/html; charset=ISO-8859-1" %>
<%@ taglib uri="http://www.tonbeller.com/jpivot" prefix="jp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<!--
    (c) 2007-2008 ActFact B.V.
-->

<jp:mondrianQuery id="query01" jdbcDriver="oracle.jdbc.driver.OracleDriver" jdbcUrl="jdbc:oracle:thin:afgo/compiere@sv001:1521:dev1" catalogUri="/WEB-INF/queries/afgocube.xml">
	SELECT
  	{
  		([Period].[All Periods], [Measures].[Funding]),
  		([Period].[All Periods], [Measures].[Invoiced Funding]),
  		([Period].[All Periods], [Measures].[Invoiced Receivables]),
  		([Period].[All Periods], [Measures].[Budget]),
  		([Period].[All Periods], [Measures].[Prognosis]),
  		([Period].[All Periods], [Measures].[Cost Distribution]),
  		([Period].[All Periods], [Measures].[Commitment]),
  		([Period].[All Periods], [Measures].[Formalized Commitment]),
  		([Period].[All Periods], [Measures].[Internal Commitment]),
  		([Period].[All Periods], [Measures].[Cost Allocation]),
  		([Period].[All Periods], [Measures].[Invoiced Commitment]),
  		([Period].[All Periods], [Measures].[Invoiced Payables])
  	} ON COLUMNS,
 	{
 		(
 			[Allocation].[All Programs],
 			[Service Type].[All Service Types],
 			[Cost Type].[All Cost Types],
 			[Product].[All Products]
 		)
 	} ON rows
	FROM Program
</jp:mondrianQuery>

<c:set var="title01" scope="session">Funding Cube</c:set>
