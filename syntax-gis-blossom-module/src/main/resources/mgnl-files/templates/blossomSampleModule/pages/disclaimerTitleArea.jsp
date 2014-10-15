<%--@elvariable id="components" type="java.util.Collection"--%>
<%@ include file="../includes/taglibs.jsp" %>

<div id="promos">
    <div id="logo" style="margin-top: 10px; margin-bottom: 20px; align-self:center; display: table;">
        <div style="display: table-row;">
            <div class="disclaimerTitle" style="display: table-cell;">
                ${content.title}
            </div>
            <div class="disclaimerLogo" style="display: table-cell;">
                <c:choose>
                    <c:when test="${not empty content.logo}">
                        Image: <img src="${cmsfn:link(content.logo)}" />
                    </c:when>
                </c:choose>
                <%--<img src="${cmsfn:link(content.logo)}" />--%>
            </div>
        </div>
    </div>
</div>
