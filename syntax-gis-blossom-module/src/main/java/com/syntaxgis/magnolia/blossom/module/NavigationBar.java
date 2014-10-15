/**
 * This file Copyright (c) 2013-2014 Magnolia International
 * Ltd.  (http://www.magnolia-cms.com). All rights reserved.
 *
 *
 * This file is dual-licensed under both the Magnolia
 * Network Agreement and the GNU General Public License.
 * You may elect to use one or the other of these licenses.
 *
 * This file is distributed in the hope that it will be
 * useful, but AS-IS and WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE, TITLE, or NONINFRINGEMENT.
 * Redistribution, except as permitted by whichever of the GPL
 * or MNA you select, is prohibited.
 *
 * 1. For the GPL license (GPL), you can redistribute and/or
 * modify this file under the terms of the GNU General
 * Public License, Version 3, as published by the Free Software
 * Foundation.  You should have received a copy of the GNU
 * General Public License, Version 3 along with this program;
 * if not, write to the Free Software Foundation, Inc., 51
 * Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * 2. For the Magnolia Network Agreement (MNA), this file
 * and the accompanying materials are made available under the
 * terms of the MNA which accompanies this distribution, and
 * is available at http://www.magnolia-cms.com/mna.html
 *
 * Any modifications to this file must keep this entire header
 * intact.
 *
 */
package com.syntaxgis.magnolia.blossom.module;

import info.magnolia.context.MgnlContext;
import info.magnolia.jcr.util.PropertyUtil;
import info.magnolia.module.blossom.annotation.TabFactory;
import info.magnolia.module.blossom.annotation.Template;
import info.magnolia.module.blossom.annotation.TemplateDescription;
import info.magnolia.ui.form.config.TabBuilder;
import info.magnolia.ui.framework.config.UiConfig;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.servlet.http.HttpSession;

/**
 * Navigation bar.
 */
@Controller
@Template(title = "Navigation Bar", id = "syntaxGisBlossomModule:components/navbar")
@TemplateDescription("Adds a forced navigation bar")
public class NavigationBar {

    private static final String REQUEST_ORIGIN_PROPERTY = "requestOrigin";
    private static final String PREVIOUS_PAGE_PROPERTY = "previousPage";
    private static final String NEXT_PAGE_PROPERTY = "nextPage";
    private final String PREVIOUS_PAGE_BUTTON_PROPERTY = "previousPageButton";
    private final String NEXT_PAGE_BUTTON_PROPERTY = "nextPageButton";
    private final String IN_EDIT_PROPERTY = "IN_EDIT_PROPERTY";
    private final String IN_EDIT_LABEL = "In Edition";
    private final String PREVIOUS_PAGE_LABEL = "Previous Page";
    private final String NEXT_PAGE_LABEL = "Next Page";
    private final String ORIGIN_PAGE_LABEL = "Origin Page";
    private final String PREVIOUS_PAGE_BUTTON_LABEL_DEFAULT = "Back";
    private final String NEXT_PAGE_BUTTON_LABEL_DEFAULT = "Next";
    private final String PREVIOUS_PAGE_BUTTON_DESCRIPTION = "Label of the button to go to previous page in the navigation";
    private final String NEXT_PAGE_BUTTON_DESCRIPTION = "Label of the button to go to following page in the navigation";
    private final String PREVIOUS_PAGE_BUTTON_LABEL = "Previous Button label";
    private final String NEXT_PAGE_BUTTON_LABEL = "Next Button label";

    @RequestMapping("/navbar")
    public String render(Node page, ModelMap model, HttpSession session) throws RepositoryException {

        Node currentContentNode = MgnlContext.getAggregationState().getCurrentContentNode();
        Node containingPage = currentContentNode.getParent().getParent();
        String requestOrigin = (String) session.getAttribute(REQUEST_ORIGIN_PROPERTY);
        String expectedPrevious = PropertyUtil.getString(currentContentNode, PREVIOUS_PAGE_PROPERTY);
        String next = PropertyUtil.getString(currentContentNode, NEXT_PAGE_PROPERTY);
        String previousPageButtonLabel = PropertyUtil.getString(currentContentNode, PREVIOUS_PAGE_BUTTON_PROPERTY);
        String nextPageButtonLabel = PropertyUtil.getString(currentContentNode, NEXT_PAGE_BUTTON_PROPERTY);


        boolean inEdit = PropertyUtil.getBoolean(currentContentNode, IN_EDIT_PROPERTY, true);
        String path = containingPage.getPath();
        if (expectedPrevious != null && !inEdit && !expectedPrevious.equals(requestOrigin)) {
            //Redirect to Origin of Flow
            String originOfFlow = PropertyUtil.getString(currentContentNode, REQUEST_ORIGIN_PROPERTY) + ".html";
            session.removeAttribute(REQUEST_ORIGIN_PROPERTY);
            return "redirect:" + originOfFlow;
        }

        session.removeAttribute(REQUEST_ORIGIN_PROPERTY);
        session.setAttribute(REQUEST_ORIGIN_PROPERTY, path);
        model.put(PREVIOUS_PAGE_PROPERTY, expectedPrevious);
        model.put(NEXT_PAGE_PROPERTY, next);
        model.put(PREVIOUS_PAGE_BUTTON_PROPERTY, previousPageButtonLabel);
        model.put(NEXT_PAGE_BUTTON_PROPERTY, nextPageButtonLabel);

        return "components/navbar.jsp";
    }

    @TabFactory("Content")
    public void contentTab(UiConfig cfg, TabBuilder tab) {

        tab.fields(cfg.fields.checkbox(IN_EDIT_PROPERTY).label(IN_EDIT_LABEL).buttonLabel(IN_EDIT_LABEL).defaultValue("true"),
                cfg.fields.link(PREVIOUS_PAGE_PROPERTY).label(PREVIOUS_PAGE_LABEL).appName("pages"),
                cfg.fields.link(NEXT_PAGE_PROPERTY).label(NEXT_PAGE_LABEL).appName("pages"),
                cfg.fields.link(REQUEST_ORIGIN_PROPERTY).label(ORIGIN_PAGE_LABEL).appName("pages"),
                cfg.fields.text(PREVIOUS_PAGE_BUTTON_PROPERTY).label(PREVIOUS_PAGE_BUTTON_LABEL).required().defaultValue(
                        PREVIOUS_PAGE_BUTTON_LABEL_DEFAULT).description(PREVIOUS_PAGE_BUTTON_DESCRIPTION),
                cfg.fields.text(NEXT_PAGE_BUTTON_PROPERTY).label(NEXT_PAGE_BUTTON_LABEL).required().defaultValue(NEXT_PAGE_BUTTON_LABEL_DEFAULT).description(
                        NEXT_PAGE_BUTTON_DESCRIPTION)
        );
    }
}
