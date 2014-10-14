<%@ include file="../includes/taglibs.jsp" %>
<div style="display: table;">
    <div style="display: table-row">

        <div style="display: table-cell;" align="left">
            <form name="previous" action="${previousPage}.html" method="get">
                <input  class="button" type="submit" value="Back">
            </form>
        </div>
        <c:catch var="exception">
            <c:if test="${not empty nextPage}">
                <div style="display: table-cell;" align="right">
                    <form class="button" name="next" action="${nextPage}.html" method="get">
                        <input type="submit" value="Next">
                    </form>
                </div>
            </c:if>
        </c:catch>
    </div>
</div>