import org.junit.Assert;
import org.junit.Test;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

import java.security.PublicKey;


public class TestAction extends JUnitActionBase {
    //@Test
    public void test() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
         request.setServletPath("/user/hyperMedia");
//        request.addParameter("id", "1002");
//        request.addParameter("date", "2010-12-30");
        request.setMethod("GET");
        // 执行URI对应的action
        final ModelAndView mav = this.excuteAction(request, response);
    }


//    @Test
//    public void test2() throws Exception{
//        Link link = new Link("http://localhost:8080");
//
//        PersonResource pr = new PersonResource();
//        pr.firstname = "sllx";
//        pr.lastname = "dbyy";
//        pr.add(link);
//        System.out.println(pr);
//    }
//
//    class PersonResource extends ResourceSupport{
//        String firstname;
//        String lastname;
//    }
}