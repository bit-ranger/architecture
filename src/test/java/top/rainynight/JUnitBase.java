package top.rainynight;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.BeforeClass;
import org.springframework.context.ApplicationContext;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

public class JUnitBase {
    protected static XmlWebApplicationContext context;
    protected static HandlerMapping handlerMapping;
    protected static HandlerAdapter handlerAdapter;

    /**
     * 读取spring3 MVC配置文件
     */
    @BeforeClass
    public static void setUp() {
        if(context == null){
            context = new XmlWebApplicationContext();

            String[] configs = {
                    "file:site/main/resources/spring.xml",
                   // "file:site/main/resources/user/spring-dao.xml",
                    //"file:site/main/resources/user/spring-service.xml",
                    "file:site/main/resources/blog/spring-dao.xml",
                    "file:site/main/resources/blog/spring-service.xml"
            };
            context.setConfigLocations(configs);
        }
        if (handlerMapping == null) {
            MockServletContext msc = new MockServletContext();
            context.setServletContext(msc);			context.refresh();
            msc.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, context);
            handlerMapping = (HandlerMapping) context
                    .getBean(RequestMappingHandlerMapping.class);
            handlerAdapter = (HandlerAdapter) context.getBean(context.getBeanNamesForType(RequestMappingHandlerAdapter.class)[0]);
        }
    }

    /**
     * 执行request对象请求的action
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ModelAndView excuteAction(HttpServletRequest request, HttpServletResponse response)throws Exception {
        HandlerExecutionChain chain = handlerMapping.getHandler(request);
        final ModelAndView model = handlerAdapter.handle(request, response, chain.getHandler());
        return model;
    }

}