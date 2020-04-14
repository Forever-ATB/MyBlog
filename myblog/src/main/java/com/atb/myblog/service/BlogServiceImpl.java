package com.atb.myblog.service;

import com.atb.myblog.exception.NotFoundException;
import com.atb.myblog.dao.BlogRepository;
import com.atb.myblog.po.Blog;
import com.atb.myblog.po.Type;
import com.atb.myblog.util.MarkdownUtils;
import com.atb.myblog.util.MyBeanUtils;
import com.atb.myblog.vo.BlogQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.*;


@Service
public class BlogServiceImpl implements BlogService {


    @Autowired
    private BlogRepository blogRepository;

    @Override
    public Blog getBlog(final Long id) {
        return blogRepository.findById(id).orElse(null);
    }

    @Transactional
    @Override
    public Blog getAndConvert(final Long id) {
        final Blog blog = blogRepository.findById(id).orElse(null);
        if (blog == null) {
            throw new NotFoundException("该博客不存在");
        }
        final Blog b = new Blog();
        BeanUtils.copyProperties(blog,b);
        final String content = b.getContent();
        b.setContent(MarkdownUtils.markdownToHtmlExtensions(content));

        blogRepository.updateViews(id);
        return b;
    }


    @Override
    public Page<Blog> listBlog(final Pageable pageable, final BlogQuery blog) {
        return blogRepository.findAll(new Specification<Blog>() {
            /**
			 *
			 */
			private static final long serialVersionUID = 1L;

			@Override
            public Predicate toPredicate(final Root<Blog> root, final CriteriaQuery<?> cq, final CriteriaBuilder cb) {
                final List<Predicate> predicates = new ArrayList<>();
                if (!"".equals(blog.getTitle()) && blog.getTitle() != null) {
                    predicates.add(cb.like(root.<String>get("title"), "%"+blog.getTitle()+"%"));
                }
                if (blog.getTypeId() != null) {
                    predicates.add(cb.equal(root.<Type>get("type").get("id"), blog.getTypeId()));
                }
                if (blog.isRecommend()) {
                    predicates.add(cb.equal(root.<Boolean>get("recommend"), blog.isRecommend()));
                }
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        },pageable);
    }

    @Override
    public Page<Blog> listBlog(final Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    @Override
    public Page<Blog> listBlog(final Long tagId, final Pageable pageable) {
        return blogRepository.findAll(new Specification<Blog>() {
            /**
			 *
			 */
			private static final long serialVersionUID = 1L;

			@Override
            public Predicate toPredicate(final Root<Blog> root, final CriteriaQuery<?> cq, final CriteriaBuilder cb) {
                Join join = root.join("tags");
                return cb.equal(join.get("id"),tagId);
            }
        },pageable);
    }

    @Override
    public Page<Blog> listBlog(final String query, final Pageable pageable) {
        return blogRepository.findByQuery(query,pageable);
    }

    @Override
    public List<Blog> listRecommendBlogTop(final Integer size) {
        final Sort sort =Sort.by(Sort.Direction.DESC,"updateTime");
        final Pageable pageable =PageRequest.of(0, size, sort);
        return blogRepository.findTop(pageable);
    }

    @Override
    public Map<String, List<Blog>> archiveBlog() {
        final List<String> years = blogRepository.findGroupYear();
        final Map<String, List<Blog>> map = new HashMap<>();
        for (final String year : years) {
            map.put(year, blogRepository.findByYear(year));
        }
        return map;
    }

    @Override
    public Long countBlog() {
        return blogRepository.count();
    }


    @Transactional
    @Override
    public Blog saveBlog(final Blog blog) {
        if (blog.getId() == null) {
            blog.setCreateTime(new Date());
            blog.setUpdateTime(new Date());
            blog.setViews(0);
        } else {
            blog.setUpdateTime(new Date());
        }
        return blogRepository.save(blog);
    }

    @Transactional
    @Override
    public Blog updateBlog(final Long id, final Blog blog) {
        final Blog b = blogRepository.findById(id).orElse(null);
        if (b == null) {
            throw new NotFoundException("该博客不存在");
        }
        BeanUtils.copyProperties(blog,b, MyBeanUtils.getNullPropertyNames(blog));
        b.setUpdateTime(new Date());
        return blogRepository.save(b);
    }

    @Transactional
    @Override
    public void deleteBlog(final Long id) {
        blogRepository.deleteById(id);
    }
}
