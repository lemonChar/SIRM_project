package com.ipads.bookadmin.service;

import com.ipads.bookadmin.entity.Admin;

public interface AdminService {
    /**
     * 实现管理员登录检查操作，调用IAdminDAO接口中的findLogin方法
     * @param vo 表示要操作的对象，包括aid，password
     * @return 成功返回true，并且将最后一次登录日期 传递到页面，失败返回false
     * @throws Exception
     */
    public boolean login(Admin vo) throws Exception;

    /**
     * 更新最后一次登录日期
     * @param aid
     * @return
     * @throws Exception
     */
    public boolean updateByLastDate(String aid) throws Exception;
}
