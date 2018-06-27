package com.framework.swing.ui;

import java.awt.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.SwingWorker;

import org.apache.log4j.Logger;

import com.framework.bom.ServiceLocator;
import com.framework.config.repositories.ActionConfigRepository;
import com.framework.config.repositories.Repositories;
import com.framework.config.repositories.ActionConfigRepository.ActionPlugin;
import com.framework.config.repositories.ActionConfigRepository.Service;
import com.framework.config.repositories.ActionConfigRepository.Variable;
import com.framework.reflect.ReflectionInvoker;
import com.framework.swing.components.Button;
import com.framework.swing.components.IComponent;
import com.framework.swing.components.Panel;
import com.framework.util.StringUtil;

/**
 * 
 * @author mahendra
 * 
 * @date 27 Apr 2011
 */
public class EventController {
	private static final Logger LOG = Logger.getLogger(EventController.class);

	public EventController() {

	}

	/**
	 * Process the action and renders the next page
	 * 
	 * @param curPage
	 * @param actionCommand
	 * @param samePage
	 */
	public void processAction(Object curPage, final String actionCmd, final Object actionSource) {
		try {
			final Page page = (Page) curPage;
			page.getMainFrame().startProgressBar();
			final ActionConfigRepository actionConfig = Repositories.getInstance().getActionConfigRepository();

			final Vector errors = new Vector();
			page.getPageProps().put("pageIndex", new Integer(0));
			SwingWorker sw = new SwingWorker() {
				@Override
				protected Object doInBackground() throws Exception {
					// MessageFactory.showProgressMessage((Component) page,
					// "Progress...");
					populatePageContext(page.getPageProps(), page.getComponents(), errors, hasValidate(actionSource));
					if (errors.isEmpty()) {
						ActionPlugin executePlugin = actionConfig.getAction(actionCmd).getPlugin("execute");
						if (executePlugin.getServices() != null) {
							try {

								for (Service s : executePlugin.getServices()) {
									processActionServices(s, page.getPageProps());
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
					return null;
				}

				@Override
				protected void done() {
					try {
						ActionPlugin actionPlugin = actionConfig.getAction(actionCmd).getPlugin("render");
						if (!errors.isEmpty()) {

						} else {
							// Render next page
							renderNextPage(page, actionPlugin.getResult("success").getValue(), page.getPageProps());
						}
						// MessageFactory.showMessage((Component) page,
						// "Done...", "");
					} catch (Exception ex) {
						LOG.error("", ex);
						// MessageFactory.showMessage((Component) page,
						// ex.getMessage(), "");
					}
					page.getMainFrame().stopProgressBar();
				}

			};
			sw.execute();
		} catch (Exception ex) {
			LOG.error("", ex);
		}
	}

	public void processEvent(final Map<String, Object> fields, final Service service) {
		SwingWorker sw = new SwingWorker() {
			@Override
			protected Object doInBackground() throws Exception {
				processActionServices(service, fields);
				return null;
			}

			@Override
			protected void done() {
				super.done();
			}
		};
		sw.execute();
	}

	/**
	 * 
	 * @param e
	 * @param page
	 * @param action
	 * @throws Exception
	 */
	private Page renderNextPage(Page page, String nextPage, Map<String, Object> actionProps) throws Exception {
		Page newPage = null;
		if (!StringUtil.isEmpty(nextPage)) {
			boolean dialog = page instanceof DialogPage;
			if (nextPage.indexOf(".") != -1) {
				// Expecting layout page is created and these actions for nodes
				// of it.
				newPage = SwingConfig.getSwingConfig().getPage(nextPage.substring(0, nextPage.indexOf(".")));
				Component[] comps = newPage instanceof PanelPage ? ((PanelPage) newPage).getComponents()
						: ((DialogPage) newPage).getContentPane().getComponents();
				// showing new tree if the page is panelpage otherwise
				// display only perticular tree node
				displayTreePage(nextPage, comps, actionProps);
			} else {
				newPage = SwingConfig.getSwingConfig().renderPage(nextPage, actionProps);
				newPage.getPageProps().putAll(actionProps);
				newPage.getMainFrame().stopProgressBar();
				Component[] comps = newPage instanceof DialogPage ? ((DialogPage) newPage).getContentPane()
						.getComponents() : ((PanelPage) newPage).getComponents();
				CompDataHelper.init(newPage, comps);
				// Action is on same page or next page is dialog then leave the
				// current page as it ease
				page.setVisible(page == newPage || newPage instanceof DialogPage);
			}
			
		} else {
			if (page instanceof PanelPage) {
				PanelPage panelPage = (PanelPage) page;
				CompDataHelper.init(page, panelPage.getComponents());
				panelPage.repaint();
			} else {
				DialogPage dialogPage = (DialogPage) page;
				CompDataHelper.init(page, dialogPage.getContentPane().getComponents());
				dialogPage.repaint();
			}
		}
		return newPage;
	}

	/**
	 * Displays tree page
	 * 
	 * @param treeLevel
	 * @param comps
	 * @param newTree
	 * @param data
	 * @throws Exception
	 */
	private void displayTreePage(String treeLevel, Component[] comps, Map<String, Object> data) throws Exception {
		for (Component comp : comps) {
			if (comp instanceof TreePane) {
				TreePane treePane = (TreePane) comp;
				// Get tree last node to be displayed
				String screen = treeLevel.substring(treeLevel.lastIndexOf(".") + 1);
				Page page = SwingConfig.getSwingConfig().getPage(screen);				
				if (page instanceof PanelPage) {
					PanelPage panelPage = (PanelPage) page;
					//Get the tree data
					Map treeData = treePane.getTreeData(panelPage.getPageId());
					//Add all action service result to tree data
					treeData.putAll(data);
					CompDataHelper.renderData(panelPage, treeData, panelPage.getComponents(), false, new HashMap());
					panelPage.setVisible(true);
					treePane.setRightComponent(panelPage);
					treePane.setDivider();
				}
			} else if (comp instanceof JPanel) {
				displayTreePage(treeLevel, ((JPanel) comp).getComponents(), data);
			}
		}
	}

	/**
	 * Validate and populate action class property with swing component
	 * 
	 * @param fields
	 * @param comps
	 * @param propertyMap
	 * @param validate
	 *            TODO
	 * @throws Exception
	 */
	public void populatePageContext(Object fields, Component[] comps, Vector errors, boolean validate) throws Exception {
		for (int i = 0; comps != null && i < comps.length; i++) {
			if (comps[i] instanceof IComponent && !StringUtil.isEmpty(comps[i].getName())) {
				IComponent icomp = (IComponent) comps[i];
				Object compVal = icomp.readData(errors, true);
				ReflectionInvoker.setProperty(fields, comps[i].getName(), compVal, true);
			} else if (comps[i] instanceof Panel) {
				Panel panel = (Panel) comps[i];
				populatePageContext(fields, panel.getComponents(), errors, validate);
			}
		}
	}

	private boolean hasValidate(Object actionSource) {
		boolean validate = false;
		if (actionSource instanceof Button) {
			validate = ((Button) actionSource).isValidate();
		}
		return validate;
	}

	public void processPlugin(String actionService, Map<String, Object> actionProps) {
		ActionConfigRepository actionConfig = Repositories.getInstance().getActionConfigRepository();
		ActionPlugin actionPlugin = actionConfig.getAction(actionService).getPlugin("execute");
		if (actionPlugin.getServices() != null) {
			try {
				for (Service s : actionPlugin.getServices()) {
					processActionServices(s, actionProps);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	protected void processActionServices(Service service, Map<String, Object> actionProps) throws Exception {
		Map sessionData = SwingConfig.getSwingConfig().getSession();
		// Expecting single service
		Variable outArg = null;
		List paramNameList = new ArrayList();
		List paramValueList = new ArrayList();
		for (int i = 0; service.getVariables() != null && i < service.getVariables().size(); i++) {
			Variable arg = service.getVariables().get(i);
			// Expecting there will be only one out arguement
			if (arg.isOut()) {
				outArg = arg;
				continue;
			}
			// Find the service variable value
			Object paramValue = null;
			if (StringUtil.isNotEmpty(arg.getValue())) {
				paramValue = arg.getValue();
			} else if (StringUtil.isNotEmpty(arg.getProperty())) {
				paramValue = ReflectionInvoker.getProperty(actionProps, arg.getProperty(), false);
				if (paramValue == null) {
					paramValue = ReflectionInvoker.getProperty(sessionData, arg.getProperty(), false);
				}
			} else if (StringUtil.isNotEmpty(arg.getType())) {
				paramValue = ReflectionInvoker.newInstance(arg.getType());
			}
			//If arguement is part of another arguement set it.
			if(arg.getName().indexOf(".") != -1){
				String firstPart = arg.getName().substring(0,arg.getName().indexOf("."));
				String secondPart = arg.getName().substring(arg.getName().indexOf(".")+1);
				if(!paramNameList.contains(firstPart)){
					paramValueList.add(paramValue);
					paramNameList.add(arg.getName());
				}else{
					int index = paramNameList.indexOf(firstPart);
					ReflectionInvoker.setProperty(paramValueList.get(index), secondPart, paramValue, true);
				}
			}else{
				paramValueList.add(paramValue);
				paramNameList.add(arg.getName());
			}
			if (arg.getScope() != null && "session".equals(arg.getScope())) {
				sessionData.put(arg.getName(), paramValue);
			}
		}
		Object serviceBean = ServiceLocator.getInstance().getBean(service.getName());
		Object ret = ReflectionInvoker.invokeMethod(serviceBean, service.getMethod(), paramValueList.toArray());
		if (outArg != null) {
			String result = outArg.getName().startsWith("$") ? outArg.getName().substring(1) : outArg.getName();
			ReflectionInvoker.setProperty(actionProps, result, ret, true);
			if (outArg.getScope() != null && "session".equals(outArg.getScope())) {
				sessionData.put(result, ret);
			}
		}
	}

	protected void populateServiceArg(Map<String, Object> inputs, Object object) {
		for (String param : inputs.keySet()) {
			try {
				Object value = inputs.get(param);
				if (value instanceof String[]) {
					value = ((String[]) value).length > 1 ? value : ((String[]) value)[0];
				}
				if (param.startsWith("$")) {
					param = param.substring(1);
				}
				// param = (String) evaluateExpr(request, param);
				ReflectionInvoker.setProperty(object, param, value, false);
			} catch (Exception e) {
			}
		}
	}

}
