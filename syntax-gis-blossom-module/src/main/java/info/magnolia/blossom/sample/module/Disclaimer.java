/**
 * This file Copyright (c) 2010-2014 Magnolia International
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
package info.magnolia.blossom.sample.module;

import info.magnolia.context.MgnlContext;
import info.magnolia.dam.asset.config.DamConfig;
import info.magnolia.jcr.util.PropertyUtil;
import info.magnolia.module.blossom.annotation.Area;
import info.magnolia.module.blossom.annotation.AvailableComponentClasses;
import info.magnolia.module.blossom.annotation.Inherits;
import info.magnolia.module.blossom.annotation.Template;
import info.magnolia.module.blossom.annotation.TabFactory;
import info.magnolia.module.blossom.annotation.TernaryBoolean;
import info.magnolia.module.blossom.dialog.DialogCreationContext;
import info.magnolia.ui.form.config.TabBuilder;
import info.magnolia.ui.framework.config.UiConfig;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.Set;

/**
 * Template with two columns, a main content area and a right side column.
 */
@Controller
@SessionAttributes("requestOrigin")
@Template(title = "Disclaimer", id = "blossomSampleModule:pages/disclaimer")
public class Disclaimer {

    private static final String REQUEST_ORIGIN_PROPERTY = "requestOrigin";
    private static final String PREVIOUS_PAGE_PROPERTY = "previousPage";
    private static final String NEXT_PAGE_PROPERTY = "nextPage";
    private static final String LOGO_PROPERTY = "logo";
    private static final String TITLE_PROPERTY = "title";
    private static final String LOGO_LABEL = "Logo";
    private static final String TITLE_LABEL = "Title";
    private final String IN_EDIT_PROPERTY = "IN_EDIT_PROPERTY";
    private final String IN_EDIT_LABEL = "In Edition";
    private final String PREVIOUS_PAGE_LABEL = "Previous Page";
    private final String NEXT_PAGE_LABEL = "Next Page";
    private final String ORIGIN_PAGE_LABEL = "Origin Page";

    /**
     * Promos area, uses the {@link info.magnolia.blossom.sample.module.DisclaimerTitle} component category annotation to specify which components are available.
     */
    @Controller
    @Area(value = "TITLE_LABEL", maxComponents = 1, optional = TernaryBoolean.TRUE)
    @Inherits
    @AvailableComponentClasses({DisclaimerTitle.class})
    public static class DisclaimerTitleArea {

        @RequestMapping("/mainTemplate/title")
        public String render() {
            return "pages/disclaimerTitleArea.jsp";
        }

        @TabFactory("Content")
        public void contentTab(UiConfig cfg, TabBuilder tab, DamConfig damConfig) {

            tab.fields(
                    damConfig.fields.damUpload(LOGO_PROPERTY).label(LOGO_LABEL).binaryNodeName(LOGO_PROPERTY),
                    cfg.fields.text(TITLE_PROPERTY).label(TITLE_LABEL)
            );
        }
    }

    /**
     * Main area.
     */
    @Area("main")
     @Controller
//    @AvailableComponentClasses({TextComponent.class, BookComponent.class, TourComponent.class, CommentsComponent.class, ViewShoppingCartComponent.class, PurchaseComponent.class, ContactFormComponent.class, YoutubeComponent.class, TwoColumnComponent.class})
     public static class DisclaimerMainArea {

        @RequestMapping("/disclaimer/main")
        public String render() {
            return "pages/mainArea.jsp";
        }
    }

    @RequestMapping("/disclaimer")
    public String render(Node page, ModelMap model, HttpSession session) throws RepositoryException {

        Node currentContentNode = MgnlContext.getAggregationState().getCurrentContentNode();
    String requestOrigin = (String) session.getAttribute(REQUEST_ORIGIN_PROPERTY);
    String expectedPrevious = PropertyUtil.getString(currentContentNode, PREVIOUS_PAGE_PROPERTY);
    String next = PropertyUtil.getString(currentContentNode, NEXT_PAGE_PROPERTY);

    boolean inEdit = PropertyUtil.getBoolean(currentContentNode, IN_EDIT_PROPERTY, true);

    if (expectedPrevious != null && !inEdit && !expectedPrevious.equals(requestOrigin)) {
        //Redirect to Origin of Flow
        String originOfFlow = PropertyUtil.getString(currentContentNode, REQUEST_ORIGIN_PROPERTY) + ".html";
        return "redirect:" + originOfFlow;
    }

    String current = PropertyUtil.getString(currentContentNode, TITLE_PROPERTY);
    session.setAttribute(REQUEST_ORIGIN_PROPERTY, current);
    model.put(PREVIOUS_PAGE_PROPERTY, expectedPrevious);
    model.put(NEXT_PAGE_PROPERTY, next);

    return "pages/disclaimer.jsp";
}

    @TabFactory("Content")
    public void contentTab(UiConfig cfg, DialogCreationContext
            dialogCreationContext, TabBuilder tab, DamConfig damConfig) {

        Set<String> disclaimerSiblings = getNavigationItems(dialogCreationContext);

        tab.fields(
                damConfig.fields.damUpload(LOGO_PROPERTY).label(LOGO_LABEL).binaryNodeName(LOGO_PROPERTY),
                cfg.fields.text(TITLE_PROPERTY).label(TITLE_LABEL),
//                cfg.fields.checkbox(IN_EDIT_PROPERTY).label(IN_EDIT_LABEL),
//                cfg.fields.select(PREVIOUS_PAGE_PROPERTY).label(PREVIOUS_PAGE_LABEL).options(disclaimerSiblings),
//                cfg.fields.select(NEXT_PAGE_PROPERTY).label(NEXT_PAGE_LABEL).options(disclaimerSiblings),
//                cfg.fields.select(REQUEST_ORIGIN_PROPERTY).label(ORIGIN_PAGE_LABEL).options(disclaimerSiblings),
                cfg.fields.link("test").label("test").appName("pages")
        );
    }

    private Set<String> getNavigationItems(DialogCreationContext dialogCreationContext) {
        Set<String> disclaimerSiblings = new HashSet<String>();
        disclaimerSiblings.add("");
//        try {
//            Node currentNode = dialogCreationContext.getContentNode();
//            String currentNodeTemplate = NodeTypes.Renderable.getTemplate(currentNode);
//            Iterator<Node> siblings = NodeUtil.getSiblings(currentNode).iterator();
//            while (siblings.hasNext()) {
//                Node sibling = siblings.next();
//                if (NodeTypes.Renderable.getTemplate(sibling) != null && NodeTypes.Renderable.getTemplate(sibling).equals(currentNodeTemplate)) {
//                    String siblingTitle = sibling.getProperty(TITLE_PROPERTY).getString();
//                    disclaimerSiblings.add(siblingTitle);
//                }
//            }
//        } catch (RepositoryException e) {
//            e.printStackTrace();
//        }

        return disclaimerSiblings;
    }
}
