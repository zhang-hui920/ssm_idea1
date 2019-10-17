package com.zh.util;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


public class PageTag extends BodyTagSupport {

	private static final long serialVersionUID = 1L;
	private PageBean pageBean;
	public PageBean getPageBean() {
		return pageBean;
	}
	public void setPageBean(PageBean pageBean) {
		this.pageBean = pageBean;
	}

	@Override
	public int doStartTag() throws JspException {
		JspWriter out = pageContext.getOut();
		try {
			out.print(toHTML());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return super.doStartTag();
	}
	private String toHTML() {
		StringBuffer sb = new StringBuffer();
		/*
		 * 拼接向后台提交数据的form表单
		 * 	注意：拼接的form表单中的page参数是变化的，所以不需要保留上一次请求的值
		 */
		sb.append("<form id='pageBeanForm' action='"+pageBean.getUrl()+"' method='post'>");
		sb.append("<input type='hidden' name='page'>");
		Map<String, String[]> parameterMap = pageBean.getMap();
		if(parameterMap != null && parameterMap.size() > 0) {
			Set<Entry<String, String[]>> entrySet = parameterMap.entrySet();
			for (Entry<String, String[]> entry : entrySet) {
				if(!"page".equals(entry.getKey())) {
					String[] values = entry.getValue();
					for (String val : values) {
						sb.append("<input type='hidden' name='"+entry.getKey()+"' value='"+val+"'>");
					}
				}
			}
		}
		sb.append("</form>");
		
		/*
		 * 展示的分页条
		 */
		sb.append("<div style='text-align: right; font-size: 12px;'>");
		sb.append("每页"+pageBean.getRows()+"条，共"+pageBean.getTotal()+"条，第"+pageBean.getPage()+"页，共"+pageBean.getMaxPage()+"页&nbsp;&nbsp;<a href='javascript:gotoPage(1)'>首页</a>&nbsp;&nbsp;<a");
		sb.append(" href='javascript:gotoPage("+pageBean.getPreivousPage()+")'>上一页</a>&nbsp;&nbsp;<a");
		sb.append(" href='javascript:gotoPage("+pageBean.getNextPage()+")'>下一页</a>&nbsp;&nbsp;<a");
		sb.append(" href='javascript:gotoPage("+pageBean.getMaxPage()+")'>尾页</a>&nbsp;&nbsp;<input type='text'");
		sb.append(" id='skipPage'");
		sb.append(" style='text-align: center; font-size: 12px; width: 50px;'>&nbsp;&nbsp;<a");
		sb.append(" href='javascript:skipPage()'>Go</a>");
		sb.append("</div>");
		
		/*
		 * 给分页条添加与后台交互的js代码
		 */
		sb.append("<script type='text/javascript'>");
		sb.append("		function gotoPage(page) {");
		sb.append("			document.getElementById('pageBeanForm').page.value = page;");
		sb.append("			document.getElementById('pageBeanForm').submit();");
		sb.append("		}");
		sb.append("		function skipPage() {");
		sb.append("			var page = document.getElementById('skipPage').value;");
		sb.append("			if(!page || isNaN(page) || parseInt(page)<1 || parseInt(page)>"+pageBean.getMaxPage()+"){");
		sb.append("				alert('请输入1~N的数字');");
		sb.append("				return;");
		sb.append("			}");
		sb.append("			gotoPage(page);");
		sb.append("		}");
		sb.append("</script>");
		return sb.toString();
	}
}
