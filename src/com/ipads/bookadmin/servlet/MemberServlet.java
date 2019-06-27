package com.ipads.bookadmin.servlet;

import com.ipads.bookadmin.entity.Member;
import com.ipads.bookadmin.factory.ServiceFactory;
import com.ipads.bookadmin.utility.ValidateUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "MemberServlet",urlPatterns = "/pages/back/MemberServlet/*")
public class MemberServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = "/pages/errors.jsp";//定义错误页面
        String status = request.getRequestURI().substring(request.getRequestURI().lastIndexOf("/") + 1);
        //表示截取的最后一个字符串
        if (status != null) {
            if ("insert".equals(status)) {
               path = this.insert(request);
            }
        }
        System.out.println(status);
        request.getRequestDispatcher(path).forward(request, response);
    }

    public String insert(HttpServletRequest request){
        String url ="";
        String msg ="";
        //接收数据
        String mid = request.getParameter("mid");
        String name = request.getParameter("name");
        Integer age = Integer.parseInt(request.getParameter("age"));
        Integer sex = Integer.parseInt(request.getParameter("sex"));
        String phone = request.getParameter("phone");
        //验证数据是否为空
        System.out.println(mid);
        if(ValidateUtils.validateEmpty(mid)&&ValidateUtils.validateEmpty(name)&& ValidateUtils.validateEmpty(phone)){
            Member vo = new Member();
            vo.setMid(mid);
            vo.setName(name);
            vo.setSex(sex);
            vo.setAge(age);
            vo.setPhone(phone);
                try {
                    if (ServiceFactory.getIMemberServiceInstance().insert(vo)){
                        url = "/pages/back/member/member_insert.jsp";
                        msg = "用户数据增加成功!";
                    }else{
                        url = "/pages/back/member/member_insert.jsp";
                        msg = "用户数据增加失败!";
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


        else{
            url = "/pages/back/member/member_insert.jsp";
            msg = "数据不能为空";
        }
        request.setAttribute("url",url);
        request.setAttribute("msg",msg);
        return "/pages/forward.jsp";



    }


}

