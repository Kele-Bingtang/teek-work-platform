package top.teek.ag.system.service.impl;

import top.teek.ag.core.helper.AgHelper;
import top.teek.ag.system.mapper.CategoryMapper;
import top.teek.ag.system.model.dto.CategoryDTO;
import top.teek.ag.system.model.po.Category;
import top.teek.ag.system.model.vo.CategoryVO;
import top.teek.ag.system.permission.PermissionHelper;
import top.teek.ag.system.service.CategoryService;
import top.teek.ag.system.service.ProjectMemberService;
import top.teek.ag.system.service.ServiceInfoService;
import top.teek.mp.base.PageQuery;
import top.teek.mp.base.TablePage;
import top.teek.utils.MapstructUtil;
import top.teek.utils.StringUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * @author Teeker
 * @date 2024-06-22 00:15:48
 * @note 针对表「t_category(目录表)」的数据库操作Service实现
 */
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    private final ProjectMemberService projectMemberService;
    private final ServiceInfoService serviceInfoService;

    @Override
    public List<CategoryVO> listAll(CategoryDTO categoryDTO) {
        LambdaQueryWrapper<Category> wrapper = buildQueryWrapper(categoryDTO);
        List<Category> categoryList = baseMapper.selectList(wrapper);
        return MapstructUtil.convert(categoryList, CategoryVO.class);
    }

    @Override
    public TablePage<CategoryVO> listPage(CategoryDTO categoryDTO, PageQuery pageQuery) {
        LambdaQueryWrapper<Category> wrapper = buildQueryWrapper(categoryDTO);
        Page<Category> categoryPage = baseMapper.selectPage(pageQuery.buildPage(), wrapper);
        TablePage<CategoryVO> categoryVOTablePage = TablePage.build(categoryPage, CategoryVO.class);

        // 主目录不允许删除
        categoryVOTablePage.getList().forEach(categoryVO -> categoryVO.setDisableRemove(categoryVO.getIsMain() == 1));
        return categoryVOTablePage;
    }

    private LambdaQueryWrapper<Category> buildQueryWrapper(CategoryDTO categoryDTO) {
        return Wrappers.<Category>lambdaQuery()
                .eq(StringUtil.hasText(categoryDTO.getTeamId()), Category::getTeamId, categoryDTO.getTeamId())
                .eq(StringUtil.hasText(categoryDTO.getCategoryCode()), Category::getCategoryCode, categoryDTO.getCategoryCode())
                .eq(StringUtil.hasText(categoryDTO.getCategoryName()), Category::getCategoryName, categoryDTO.getCategoryName())
                .eq(StringUtil.hasText(categoryDTO.getProjectId()), Category::getProjectId, categoryDTO.getProjectId())
                .orderByAsc(Category::getOrderNum);
    }

    @Override
    public boolean addCategory(CategoryDTO categoryDTO) {
        Category category = MapstructUtil.convert(categoryDTO, Category.class);
        return baseMapper.insert(category) > 0;
    }

    @Override
    public boolean editCategory(CategoryDTO categoryDTO) {
        Category category = MapstructUtil.convert(categoryDTO, Category.class);
        return baseMapper.updateById(category) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeCategory(String categoryId) {
        Category category = baseMapper.selectOne(Wrappers.<Category>lambdaQuery()
                .eq(Category::getCategoryId, categoryId));

        PermissionHelper.checkProjectOperator(AgHelper.getUserId(), category.getProjectId(), "1h");

        // 删除所有接口
        serviceInfoService.removeAllServiceInfo(categoryId);

        return baseMapper.delete(Wrappers.<Category>lambdaQuery()
                .eq(Category::getCategoryId, categoryId)) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeAllCategory(String projectId) {
        // 删除项目下所有接口
        serviceInfoService.removeAllServiceInfoByProjectId(projectId);

        return baseMapper.delete(Wrappers.<Category>lambdaQuery()
                .eq(Category::getProjectId, projectId)) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeAllCategoryByTeamId(String teamId) {
        // 删除团队下所有接口
        serviceInfoService.removeAllServiceInfoByTeamId(teamId);

        return baseMapper.delete(Wrappers.<Category>lambdaQuery()
                .eq(Category::getTeamId, teamId)) > 0;
    }

    @Override
    public boolean checkCategoryCodeUnique(CategoryDTO categoryDTO) {
        return baseMapper.exists(Wrappers.<Category>lambdaQuery()
                .eq(Category::getCategoryCode, categoryDTO.getCategoryCode())
                .eq(Category::getProjectId, categoryDTO.getProjectId())
                .ne(Objects.nonNull(categoryDTO.getId()), Category::getId, categoryDTO.getId()));
    }

    
}




