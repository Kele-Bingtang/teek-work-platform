package top.teek.auth.handler;

import top.teek.core.http.HttpResult;
import top.teek.uac.system.mapper.SysUserMapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.catalina.users.GenericUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Teeker
 * @date 2023/4/2 22:09
 * @note 退出系统成功处理类
 */
public class ExitSuccessHandler implements LogoutSuccessHandler {

    @Resource
    private SysUserMapper sysUserMapper;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 获取当登录用户信息
        User user = (User) authentication.getPrincipal();

        UpdateWrapper<GenericUser> updateWrapper = new UpdateWrapper<>();
        // 修改用户状态为 离线
        updateWrapper.set("username", user.getUsername()).eq("status", 0);
        // TODO
        // sysUserMapper.update(null, updateWrapper);

        // 获取输出流
        response.setCharacterEncoding("UTF-8");
        // 设置客户端的响应的内容类型
        response.setContentType("application/json;charset=UTF-8");
        
        PrintWriter writer = response.getWriter();
        writer.print(new ObjectMapper().writeValueAsString(HttpResult.ok("退出成功")));
        writer.flush();
        writer.close();

    }
}
