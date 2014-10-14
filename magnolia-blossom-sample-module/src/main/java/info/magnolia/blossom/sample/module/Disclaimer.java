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

    /**
     * Promos area, uses the {@link info.magnolia.blossom.sample.module.DisclaimerTitle} component category annotation to specify which components are available.
     */
    @Controller
    @Area(value = "title", maxComponents = 1, optional = TernaryBoolean.TRUE)
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
                    damConfig.fields.damUpload("logo").label("logo").binaryNodeName("logo"),
                    cfg.fields.text("title").label("Title")
            );
        }
    }

    /**
     * Main area.
     */
    @Area("main")
    @Controller
    @AvailableComponentClasses({TextComponent.class, BookComponent.class, TourComponent.class, CommentsComponent.class, ViewShoppingCartComponent.class, PurchaseComponent.class, ContactFormComponent.class, YoutubeComponent.class, TwoColumnComponent.class})
    public static class DisclaimerMainArea {

        @RequestMapping("/disclaimer/main")
        public String render() {
            return "pages/mainArea.jsp";
        }
    }

    @RequestMapping("/disclaimer")
    public String render(Node page, ModelMap model, HttpSession session) throws RepositoryException {

        Node currentContentNode = MgnlContext.getAggregationState().getCurrentContentNode();
        String requestOrigin = (String) session.getAttribute("requestOrigin");
        String expectedPrevious = PropertyUtil.getString(currentContentNode, "previousPage");

        if(expectedPrevious!=null && !expectedPrevious.equals(requestOrigin)) {
            //Redirect to Origin of Flow
            String originOfFlow = PropertyUtil.getString(currentContentNode, "originPage") + ".html";
            //String originOfFlow = "test1.html";
            return "redirect:" + originOfFlow;
        }

        String current = PropertyUtil.getString(currentContentNode, "title");
        try {
            Node logo = currentContentNode.getNode("logo");
        }
        catch (Exception ex){

        }
        session.setAttribute("requestOrigin", current);
        //model.put("previousPage", expectedPrevious);
        //model.put("nextPage", next);

        return "pages/disclaimer.jsp";
    }

    @TabFactory("Content")
    public void contentTab(UiConfig cfg,DialogCreationContext
            dialogCreationContext, TabBuilder tab, DamConfig damConfig) {

        Set<String> disclaimerSiblings = getNavigationItems(dialogCreationContext);

        tab.fields(
                damConfig.fields.damUpload("logo").label("logo").binaryNodeName("logo"),
                cfg.fields.text("title").label("Title"),
                cfg.fields.text("previousPage").label("Previous Page"),
                cfg.fields.text("nextPage").label("Next Page"),
                //cfg.fields.select("previousPage").label("Previous Page").options(disclaimerSiblings),
                //cfg.fields.select("nextPage").label("Next Page").options(disclaimerSiblings),
                cfg.fields.select("originPage").label("Origin Page").options(disclaimerSiblings)
        );
    }

    private Set<String> getNavigationItems(DialogCreationContext dialogCreationContext) {
        Set<String> disclaimerSiblings = new HashSet<String>();
        disclaimerSiblings.add("");
//        try {
//            Node currentNode = dialogCreationContext.getContentNode();
//            String currentNodeTemplate = NodeTypes.Renderable.getTemplate(currentNode);
//            Iterator<Node> siblings = NodeUtil.getSiblings(currentNode).iterator();
//            while(siblings.hasNext())
//            {
//                Node sibling = siblings.next();
//                if(NodeTypes.Renderable.getTemplate(sibling).equals(currentNodeTemplate)){
//                    String title = sibling.getProperty("title").getString();
//                    disclaimerSiblings.add(title);
//                }
//            }
//        } catch (RepositoryException e) {
//            e.printStackTrace();
//        }

        return disclaimerSiblings;
    }
}
