<%--@elvariable id="content" type="info.magnolia.jcr.util.ContentMap"--%>
<%--@elvariable id="navigation" type="java.util.Map<java.lang.String, java.lang.String>"--%>
<%@ include file="../includes/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="sv" lang="sv">
<head>
    <title>${content.title}</title>
    <style type="text/css">
        body {
            font-family: "Lucida Sans Unicode", "Lucida Grande", Verdana, Arial, Helvetica, sans-serif;
            font-size: 13px;
            background-color: #DDDDDD;
        }
        a {
            color: #4040ff
        }
        a:visited {
            color: #4040ff;
        }
        #container {
            margin-left: 50px;
            width: 875px;
        }
        #logo {
            font-family: Georgia, 'Times New Roman', Times, serif;
            font-size: 46px;
            padding: 50px 0px 8px 10px;
            background-color: #ffffff;
        }
        #menu ul {
            background: #313131;
            background: -moz-linear-gradient(center top, #5D5Da0, #313160);
            background: -webkit-gradient(linear, left top, left bottom, from(#5D5Da0), to(#313160));
            height: 30px;
            margin: 0 0 10px 0;
            padding: 0 0 0 4px;
            font-size: 14px;
            list-style: none;
        }
        #menu ul li {
            margin-top: 0;
            float: left;
            display: inline
        }
        #menu ul li a {
            margin-top: 4px;
            padding: 5px 8px;
            color: #fff;
            text-decoration: none;
            display: block;
            outline: 0 none;
        }
        #menu ul li a:hover {
            background: #fafafa;
            color: #4b4b4b
        }
        #content {
            background-color: white;
            padding: 15px;
            margin-bottom: 20px;
            -moz-border-radius-bottomleft: 5px;
            -moz-border-radius-bottomright: 5px;
        }
        #main {
            float: left;
            width: 625px;
            background-color: #ffffff;
        }
        #promos {
            float: left;
            width: 200px;
            background-color: #ffffff;
            margin-left: 20px;
        }
        #copyright {
            padding: 20px 15px 0 15px;
            clear: both;
            text-align: center;
            color: #737373;
        }
    </style>
    <cms:init />
</head>
<body>
<div id="container">
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
    <div id="content">
        <cms:area name="main" />
</br>
        <div style="display: table;">
            <div style="display: table-row">
                <div style="display: table-cell;" align="left">
                    <form name="previous" action="${previousPage}.html" method="get">
                        <input type="submit" value="Back">
                    </form>
                </div>
                <div style="display: table-cell;" align="right">
                    <form name="next" action="${nextPage}.html" method="get">
                        <input type="submit" value="Next">
                    </form>
                </div>
            </div>
        </div>

        <div id="copyright">Product only for testing</div>
    </div>
</div>
</body>
</html>
