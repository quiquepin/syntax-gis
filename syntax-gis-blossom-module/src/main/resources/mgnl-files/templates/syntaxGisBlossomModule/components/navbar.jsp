<%@ include file="../includes/taglibs.jsp" %>
<div style="display: table;">
    <div style="display: table-row">
        <%--If there is no next page, we assume it's the last page of the navigation and disable both buttons--%>
        <c:catch var="exception">
            <c:if test="${not empty nextPage}">
                <div style="display: table-cell;" align="left">
                    <form name="previous" action="${previousPage}.html" method="get">
                        <input class="button" type="submit" value="${previousPageButton}">
                    </form>
                </div>

                <div style="display: table-cell;" align="right">
                    <form class="button" name="next" action="${nextPage}.html" method="get">
                        <input type="submit" value="${nextPageButton}">
                    </form>
                </div>
            </c:if>
        </c:catch>
    </div>
</div>