package top.teek.uac.system.controller.monitor;

import top.teek.core.http.HttpResult;
import top.teek.core.http.Response;
import top.teek.mp.base.PageQuery;
import top.teek.mp.base.TablePage;
import top.teek.security.domain.LoginUser;
import top.teek.uac.core.helper.UacHelper;
import top.teek.utils.StringUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * @author Teeker
 * @date 2024/4/9 0:24
 * @note
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/monitor/online")
public class SysUserOnlineController {

    @GetMapping("/listPage")
    @Operation(summary = "在线用户列表查询", description = "通过条件查询在线用户列表（支持分页）")
    @PreAuthorize("hasAuthority('system:onlineUser:list')")
    public Response<TablePage<LoginUser>> listPage(String username, PageQuery pageQuery) {
        List<LoginUser> allLoginUser = UacHelper.getAllLoginUser();
        Page<LoginUser> page = pageQuery.buildPage();

        int fromIndex = (int) ((page.getCurrent() - 1) * page.getSize());
        int toIndex = (int) Math.min(fromIndex + page.getCurrent(), allLoginUser.size());

        if (StringUtil.hasText(username)) {
            allLoginUser = allLoginUser.stream().filter(userOnline -> username.equals(userOnline.getUsername())).toList();
        }

        // 使用 subList 方法进行分页
        if (!allLoginUser.isEmpty()) {
            page.setRecords(allLoginUser.subList(fromIndex, toIndex));
        }

        return HttpResult.ok(TablePage.build(page));
    }

    @DeleteMapping("/{username}")
    @Operation(summary = "在线用户强制下线", description = "强制下线用户")
    @PreAuthorize("hasAuthority('system:onlineUser:forceLogout')")
    public Response<String> forceLogout(@PathVariable String username) {
        String loginUsername = UacHelper.getUsername();
        if (Objects.nonNull(loginUsername) && loginUsername.equals(username)) {
            return HttpResult.failMessage("无法操作自己");
        }
        UacHelper.logout(username);
        return HttpResult.failMessage("强制下线成功");
    }
} 
