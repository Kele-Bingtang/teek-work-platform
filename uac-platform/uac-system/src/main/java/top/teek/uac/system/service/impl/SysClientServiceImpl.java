package top.teek.uac.system.service.impl;

import cn.hutool.crypto.SecureUtil;
import top.teek.mp.base.PageQuery;
import top.teek.mp.base.TablePage;
import top.teek.uac.system.mapper.SysClientMapper;
import top.teek.uac.system.model.dto.SysClientDTO;
import top.teek.uac.system.model.po.SysClient;
import top.teek.uac.system.model.vo.SysClientVO;
import top.teek.uac.system.model.vo.extra.ClientTreeVO;
import top.teek.uac.system.service.SysClientService;
import top.teek.utils.IdsUtil;
import top.teek.utils.ListUtil;
import top.teek.utils.MapstructUtil;
import top.teek.utils.StringUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * @author Teeker
 * @date 2023-59-13 22:59:42
 * @note 针对表【t_sys_client(客户端授权表)】的数据库操作Service实现
 */
@Service
public class SysClientServiceImpl extends ServiceImpl<SysClientMapper, SysClient> implements SysClientService {

    @Override
    public SysClient checkClientIdThenGet(String clientId) {
        return baseMapper.selectOne(Wrappers.<SysClient>lambdaQuery().eq(SysClient::getClientId, clientId));
    }

    @Override
    public List<SysClientVO> listAll(SysClientDTO sysClientDTO) {
        LambdaQueryWrapper<SysClient> wrapper = buildQueryWrapper(sysClientDTO);
        List<SysClient> sysAppList = baseMapper.selectList(wrapper);
        return MapstructUtil.convert(sysAppList, SysClientVO.class);
    }

    @Override
    public TablePage<SysClientVO> listPage(SysClientDTO sysClientDTO, PageQuery pageQuery) {
        LambdaQueryWrapper<SysClient> wrapper = buildQueryWrapper(sysClientDTO);
        Page<SysClient> sysClientPage = baseMapper.selectPage(pageQuery.buildPage(), wrapper);

        TablePage<SysClientVO> tablePage = TablePage.build(sysClientPage, SysClientVO.class);
        tablePage.getList().forEach(r -> r.setGrantTypeList(List.of(r.getGrantTypes().split(","))));
        return tablePage;
    }

    private LambdaQueryWrapper<SysClient> buildQueryWrapper(SysClientDTO sysClientDTO) {
        return Wrappers.<SysClient>lambdaQuery()
                .like(StringUtil.hasText(sysClientDTO.getClientId()), SysClient::getClientId, sysClientDTO.getClientId())
                .eq(StringUtil.hasText(sysClientDTO.getClientKey()), SysClient::getClientKey, sysClientDTO.getClientKey())
                .eq(StringUtil.hasText(sysClientDTO.getClientName()), SysClient::getClientName, sysClientDTO.getClientName())
                .like(StringUtil.hasText(sysClientDTO.getClientSecret()), SysClient::getClientSecret, sysClientDTO.getClientSecret())
                .in(ListUtil.isNotEmpty(sysClientDTO.getGrantTypeList()), SysClient::getGrantTypes, sysClientDTO.getGrantTypeList())
                .eq(Objects.nonNull(sysClientDTO.getStatus()), SysClient::getStatus, sysClientDTO.getStatus());
    }

    @Override
    public List<ClientTreeVO> listTreeList() {
        // TODO 是否分页
        List<SysClient> sysClientList = baseMapper.selectList(Wrappers.<SysClient>lambdaQuery()
                .select(SysClient::getClientId, SysClient::getClientName)
                .orderByAsc(SysClient::getCreateTime));

        return MapstructUtil.convert(sysClientList, ClientTreeVO.class);
    }

    @Override
    public boolean insertClient(SysClientDTO sysClientDTO) {
        SysClient sysClient = MapstructUtil.convert(sysClientDTO, SysClient.class);
        // 如果没有设置 clientSecret，则自动生成
        if (Objects.isNull(sysClient.getClientSecret())) {
            sysClient.setClientSecret(IdsUtil.simpleUUID());
        }
        String clientSecret = sysClient.getClientSecret();
        String clientKey = sysClient.getClientKey();
        // 生成 appId
        String appId = SecureUtil.md5(clientSecret + clientKey);
        sysClient.setClientId(appId);
        sysClient.setGrantTypes(String.join(",", sysClientDTO.getGrantTypeList()));
        return baseMapper.insert(sysClient) > 0;
    }

    @Override
    public boolean updateClient(SysClientDTO sysClientDTO) {
        SysClient sysClient = MapstructUtil.convert(sysClientDTO, SysClient.class);
        sysClient.setGrantTypes(String.join(",", sysClientDTO.getGrantTypeList()));
        return baseMapper.updateById(sysClient) > 0;
    }

    @Override
    public boolean updateStatus(Long id, Integer status) {
        return baseMapper.update(null, Wrappers.<SysClient>lambdaUpdate()
                .eq(SysClient::getId, id)
                .set(SysClient::getStatus, status)) > 0;
    }

    @Override
    public boolean removeBatch(Collection<Long> ids) {
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public boolean checkClientKeyUnique(SysClientDTO sysClientDTO) {
        return baseMapper.exists(Wrappers.<SysClient>lambdaQuery()
                .eq(SysClient::getClientKey, sysClientDTO.getClientKey())
                .ne(Objects.nonNull(sysClientDTO.getId()), SysClient::getId, sysClientDTO.getId()));
    }

    @Override
    public boolean checkClientSecretUnique(SysClientDTO sysClientDTO) {
        return baseMapper.exists(Wrappers.<SysClient>lambdaQuery()
                .eq(SysClient::getClientSecret, sysClientDTO.getClientSecret())
                .ne(Objects.nonNull(sysClientDTO.getId()), SysClient::getId, sysClientDTO.getId()));
    }
}




